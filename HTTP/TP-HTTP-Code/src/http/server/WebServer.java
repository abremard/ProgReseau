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

import http.server.LuckFinder;
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
      // create the main server socket
      s = new ServerSocket(80);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    for (;;) {
      try {
        // wait for a connection
        Socket remote = s.accept();
        // remote is now the connected socket
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(
            remote.getInputStream()));
        PrintWriter out = new PrintWriter(remote.getOutputStream());
        
        // read the data sent. We basically ignore it,
        // stop reading once a blank line is hit. This
        // blank line signals the end of the client HTTP
        // headers.
        String str = ".";
        RequestHeader reqHead = new RequestHeader();
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

            String pathString = System.getProperty("user.dir");
            pathString = pathString.concat(reqHead.getRequestUrl());
            Path path = Paths.get(pathString);

            if (reqHead.getFileAccess()) {
              switch (reqHead.getFileExtension()) {
                case "jpg":
                  try {
                    requestHandler(out, 200, "OK", "image/jpeg");                                                       
                    BufferedImage originalImage = ImageIO.read(new File(pathString));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write( originalImage, "jpg", baos );
                    baos.flush();
                    byte[] imageInByte = baos.toByteArray();
                    baos.close();
                    remote.getOutputStream().write(imageInByte);                    
                  } catch (IIOException e) {
                    requestHandler(out, 404, "NOT FOUND", "image/jpeg");                                                       
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  break;
                case "png":
                  try {
                    BufferedImage originalPngImage = ImageIO.read(new File(pathString));
                    requestHandler(out, 200, "OK", "image/png");                                    
                    ByteArrayOutputStream pngBaos = new ByteArrayOutputStream();
                    ImageIO.write( originalPngImage, "png", pngBaos );
                    pngBaos.flush();
                    byte[] pngImageInByte = pngBaos.toByteArray();
                    pngBaos.close();
                    remote.getOutputStream().write(pngImageInByte);                    
                  } catch (IIOException e) {
                    requestHandler(out, 404, "NOT FOUND", "image/png");                                    
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  break;
                case "ico":
                  out.println("HTTP/1.0 200 OK");
                  out.println("Content-Type: image/x-icon");
                  out.println("Server: Bot");
                break;
                case "wav":
                  try {
                    ByteArrayOutputStream wavBaos = new ByteArrayOutputStream();
                    BufferedInputStream wavBis = new BufferedInputStream(new FileInputStream(new File(pathString)));
                    int read;
                    byte[] buff = new byte[1024];
                    while ((read = wavBis.read(buff)) > 0)
                    {
                      wavBaos.write(buff, 0, read);
                    }
                    wavBaos.flush();
                    byte[] audioBytes = wavBaos.toByteArray();
                    wavBaos.close();
                    remote.getOutputStream().write(audioBytes);
                    requestHandler(out, 200, "OK", "audio/wav");                                    
                  } catch (IIOException e) {
                    requestHandler(out, 404, "NOT FOUND", "audio/wav");                                    
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  break;
                case "mp3":
                  try {
                    ByteArrayOutputStream mp3Baos = new ByteArrayOutputStream();
                    BufferedInputStream mp3Bis = new BufferedInputStream(new FileInputStream(new File(pathString)));
                    int read;
                    byte[] buff = new byte[1024];
                    while ((read = mp3Bis.read(buff)) > 0)
                    {
                      mp3Baos.write(buff, 0, read);
                    }
                    mp3Baos.flush();
                    byte[] audioBytes = mp3Baos.toByteArray();
                    mp3Baos.close();
                    remote.getOutputStream().write(audioBytes);
                    requestHandler(out, 200, "OK", "audio/mpeg");                                    
                  } catch (IIOException e) {
                    requestHandler(out, 404, "NOT FOUND", "audio/mpeg");                                    
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  break;
                case "mp4":
                  try {
                    ByteArrayOutputStream mp4Baos = new ByteArrayOutputStream();
                    BufferedInputStream mp4Bis = new BufferedInputStream(new FileInputStream(new File(pathString)));
                    int read;
                    byte[] buff = new byte[1024];
                    while ((read = mp4Bis.read(buff)) > 0)
                    {
                      mp4Baos.write(buff, 0, read);
                    }
                    mp4Baos.flush();
                    byte[] audioBytes = mp4Baos.toByteArray();
                    mp4Baos.close();
                    remote.getOutputStream().write(audioBytes);
                    requestHandler(out, 200, "OK", "video/mpeg");                                    
                  } catch (IIOException e) {
                    requestHandler(out, 404, "NOT FOUND", "video/mpeg");                                    
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  break;
                case "pdf":
                  try {
                    ByteArrayOutputStream pdfBaos = new ByteArrayOutputStream();
                    BufferedInputStream pdfBis = new BufferedInputStream(new FileInputStream(new File(pathString)));
                    int read;
                    byte[] buff = new byte[1024];
                    while ((read = pdfBis.read(buff)) > 0)
                    {
                      pdfBaos.write(buff, 0, read);
                    }
                    pdfBaos.flush();
                    byte[] audioBytes = pdfBaos.toByteArray();
                    pdfBaos.close();
                    remote.getOutputStream().write(audioBytes);
                    requestHandler(out, 200, "OK", "application/pdf");                                    
                  } catch (IIOException e) {
                    requestHandler(out, 404, "NOT FOUND", "application/pdf");                                    
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  break;
                case "html":
                  try (Stream<String> lines = Files.lines(path)) {
                    requestHandler(out, 200, "OK", "text/html");                  
                    String content = lines.collect(Collectors.joining(System.lineSeparator()));
                    out.println(content);
                    out.println();
                  } catch (NoSuchFileException e) {
                    requestHandler(out, 404, "NOT FOUND", "text/html");
                  } catch (IOException e) {
                      e.printStackTrace();
                  }                  
                  break;
              
                default:
                  break;
              }

            } else {
              requestHandler(out, 400, "BAD SYNTAX", "text/html");
            }
            break;
        
          case "POST":
            File file = new File("files/test.txt");
            file.createNewFile();
            byte[] buffer = new byte[256];
            InputStream inputStream = remote.getInputStream();
            BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(file));
            while (inputStream.available() > 0) {
              int read = inputStream.read(buffer);
              fileOutput.write(buffer, 0, read);;
            }
            fileOutput.flush();
            fileOutput.close();

            break;

          case "HEAD":

            break;

          
          case "PUT":

            break;

          
          case "DELETE":

            break;

          default:
            break;
        }
        out.flush();
        remote.close();
      } catch (Exception e) {
        try {
          Socket remote = s.accept();
          BufferedReader in = new BufferedReader(new InputStreamReader(
              remote.getInputStream()));
          PrintWriter out = new PrintWriter(remote.getOutputStream());
          requestHandler(out, 500, "INTERNAL SERVER ERROR", " ");
          e.printStackTrace();          
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  private void requestHandler(PrintWriter out, int status, String statusMessage, String contentType, byte[] bodyObject) {
    out.println("HTTP/1.0 "+Integer.toString(status)+" "+statusMessage);
    out.println("Content-Type: "+contentType);
    out.println("Server: Bot");
    switch (status) {
      case 200:
        out.println("");
        out.println("<h2>Welcome to the Ultra Mini-WebServer</h2>");
        out.println(bodyObject);
        break;
      case 400:
        System.out.println("Error 400 : Bad request!");
        out.println("");
        out.println("<h1>Error 400</h1><h2>The request could not be understood by the server due to malformed syntax. Please verify the request syntax before retrying...</h2>");
        break;
      case 404:
        System.out.println("Error 404 : Ressource not found!");
        out.println("");
        out.println("<h1>Error 404</h1><h2>The requested resource could not be found but may be available again in the future. Please try again later...</h2>");        
        break;
      case 500:
        out.println("");
        out.println("<h1>Error 500</h1><h2>The server encountered an unexpected condition which prevented it from fulfilling the request. Please try again later...</h2>");
        break;
    
      default:
        break;
    }
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
