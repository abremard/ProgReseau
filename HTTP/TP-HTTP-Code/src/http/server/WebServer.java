///A Simple Web Server (WebServer.java)

package http.server;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.IIOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.imageio.ImageIO;

import java.nio.charset.StandardCharsets;

// import http.server.LuckFinder;
import http.server.RequestHeader;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {

  private boolean debugMode = true;
  /**
   * WebServer constructor.
   */
  protected void start() {
    ServerSocket s;

    System.out.println("Webserver starting up on port 80");
    System.out.println("(press ctrl-c to exit)");
    try {
      s = new ServerSocket(80);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    for (;;) {
      BufferedOutputStream out = null;
      Socket remote = null;
      RequestHeader reqHead = new RequestHeader();
      try {
        remote = s.accept();
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
        BufferedInputStream inputStream = new BufferedInputStream(remote.getInputStream());
        String str = ".";
        str = in.readLine();
        while (str != null && !str.equals(""))
        {
          System.out.println(str);
          reqHead.parseRequest(str);
          str = in.readLine();
        }

        if (debugMode) {
          reqHead.printObject();  
        }
        String requestType = reqHead.getRequestType();
        switch (requestType) {
          case "GET":
            try {
              String pathString = System.getProperty("user.dir");
              pathString = pathString.concat(reqHead.getRequestUrl());
              File ressource = new File(pathString);
              BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(ressource));
              int readSize;
              byte[] buff = new byte[1024];
              out = new BufferedOutputStream(remote.getOutputStream());
              String headerString = requestBuilder(200, "OK", reqHead, ressource.length());
              out.write(headerString.getBytes());
              while ((readSize = fileStream.read(buff)) > 0)
              {
                out.write(buff, 0, readSize);
              }
              fileStream.close();
              out.flush();
            } catch (NoSuchFileException e) {
              String headerString = requestBuilder(404, "RESSOURCE NOT FOUND", reqHead, 0);
              out.write(headerString.getBytes());                                    
            } catch (Exception e) {
              String headerString = requestBuilder(500, "INTERNAL SERVER ERROR", reqHead, 0);
              out.write(headerString.getBytes());   
              e.printStackTrace();
            }
            break;
        
          case "POST":
            File file = new File("files/test.mp3");
            file.createNewFile();
            byte[] buffer = new byte[256];
            BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(file));
            while (inputStream.available() > 0) {
              int read = inputStream.read(buffer);
              fileOutput.write(buffer, 0, read);
            }
            fileOutput.flush();
            fileOutput.close();

            break;

          case "HEAD":

            break;

          
          case "PUT":

            break;

          
          // case "DELETE":
          // String deletePathString = System.getProperty("user.dir");
          // deletePathString = pathString.concat(reqHead.getRequestUrl());
          // Path deletePath = Paths.get(deletePathString);
          // try{
          //   boolean result = deleteIfExists(deletePath);
          //   if(result){
          //     bodyObjectText = "<h1> The file has been successfully deleted </h1>";
          //     byte[] bodyObject = bodyObjectText.getBytes();
          //     requestHandler(out, 200, "OK", "text/html", bodyObject);
          //   }
          //   else{
          //     bodyObjectText = "<h1> The requested file was not found </h1>";
          //     byte[] bodyObject = bodyObjectText.getBytes();
          //     requestHandler(out, 404, "NOT FOUND", "text/html", bodyObject);
          //   }
          // }
          // catch (Exception e) {
          //   bodyObjectText = "<h1> An unexpected error has occured </h1>";
          //   byte[] bodyObject = bodyObjectText.getBytes();
          //   requestHandler(out, 500, "INTERNAL SERVER ERROR", "text/html", bodyObject);
          //   e.printStackTrace();
          // }

          //   break;

          default:
            break;
        }
        remote.close();
      } catch (Exception e) {  
        e.printStackTrace();
      }
    }
  }

  private String requestBuilder(int statusCode, String statusMessage, RequestHeader reqHead, long length) {
    String responseHeader = "";
    responseHeader = responseHeader.concat("HTTP/1.0 "+Integer.toString(statusCode)+" "+statusMessage+"\r\n");
    switch (reqHead.getFileExtension()) {
      case "html":
        responseHeader = responseHeader.concat("Content-Type: text/html\r\n");
        break;
      case "png":
        responseHeader = responseHeader.concat("Content-Type: image/png\r\n");
        break;
      case "jpg":
        responseHeader = responseHeader.concat("Content-Type: image/jpg\r\n");
        break;
      case "mp3":
        responseHeader = responseHeader.concat("Content-Type: audio/mp3\r\n");
        break;
      case "mp4":
        responseHeader = responseHeader.concat("Content-Type: video/mp4\r\n");
        break;
      case "avi":
        responseHeader = responseHeader.concat("Content-Type: video/x-msvideo\r\n");
        break;
      case "pdf":
        responseHeader = responseHeader.concat("Content-Type: application/pdf\r\n");
        break;
      case "odt":
        responseHeader = responseHeader.concat("Content-Type: application/vnd.oasis.opendocument.text\r\n");
        break;
      default:
        break;
    }
    switch (statusCode) {
      case 400:
        System.out.println("Error 400 : Bad request!");
        responseHeader = responseHeader.concat("<h1>Error 400</h1><h2>The request could not be understood by the server due to malformed syntax. Please verify the request syntax before retrying...</h2>\r\n\r\n");
        break;
      case 404:
        System.out.println("Error 404 : Ressource not found!");
        responseHeader = responseHeader.concat("<h1>Error 404</h1><h2>The requested resource could not be found but may be available again in the future. Please try again later...</h2>\r\n\r\n");        
        break;
      case 500:
        System.out.println("Error 500 : Internal Server Error!");
        responseHeader = responseHeader.concat("<h1>Error 500</h1><h2>The server encountered an unexpected condition which prevented it from fulfilling the request. Please try again later...</h2>\r\n\r\n");
        break;
      default:
        responseHeader = responseHeader.concat("Content-Length: "+length+"\r\n");
        responseHeader = responseHeader.concat("Server: Bot\r\n\r\n");
        break;
    }
    return responseHeader;
  }

  /**
   * Start the application.
   * 
   * @param args
   *            Command line parameters are not used.
   */
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
