package http.server;

import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import http.server.LuckFinderTest;
import http.server.RequestHeader;

/**
 * WebServer is a simplified HTTP Server that handles basic GET, POST, PUT, HEAD, DELETE requests
 * 
 * @author Iyad TOUT, Alexandre BREMARD
 * @version 1.0
 */
public class WebServer {

  private boolean debugMode = false;
  private String authorizedDirectory = "files";

  /**
   * Run HTTP server on given port number. The server always waits for a new connection, it receives the request, processes it,
   * sends a response and then closes the connection.
   * Any request that tries to access another directory than the authorized one will result in a 403 forbidden error (except for favicon.ico).
   * Any request type that is not supported by the server will result in a 501 not implemented error.
   * Any request that asks for a file that does not exist will result in a 404 not found error.
   * Any other server-related error will result in a 500 internal server error.  
   * @param portNo port number used for connection
   */
  protected void start(int portNo) {
    ServerSocket s;

    System.out.println("Webserver starting up on port "+portNo);
    System.out.println("(press ctrl-c to exit)");
    try {
      s = new ServerSocket(portNo);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }
    System.out.println("Waiting for connection");
    for (;;) {
      try {
        // receives a connection on port 80
        Socket remote = s.accept();
        // opens input stream for client socket to receive binary input data
        BufferedInputStream in = new BufferedInputStream(remote.getInputStream());
        // opens output stream for client socket to send binary output data
        BufferedOutputStream out = new BufferedOutputStream(remote.getOutputStream());
        // request header object that contains the request type, url, file extension...
        RequestHeader reqHead = new RequestHeader();
        /**
         * Algorithm that reads the input stream and parses request's description.
         * Reads character by character and assembles them into lines and calls RequestHeader.parseRequest to build the RequestHeader object.
         * The header ends with an empty line
         */
        int current = '\0';
        int previous = '\0';
        boolean newLine = false;
        String line = new String();
        current = in.read();
        while(current != -1 && !(previous == '\r' && current == '\n' && newLine)) {
            if(previous == '\r' && current == '\n') {
              newLine = true;
              reqHead.parseRequest(line);
              line = "";
            } else if(!(previous == '\n' && current == '\r')) {
              newLine = false;
            }
            previous = current;
            line += (char) current;
            current = in.read();
        }
        // utility function to debug RequestHeader object
        if (debugMode) {
          reqHead.printObject();  
        }
        // file can be accessed only in two cases: 1) belongs to the authorized directory 2) path is strictly /favicon.ico
        boolean authorizedAccess = ((reqHead.getRequestUrl().substring(1).startsWith(authorizedDirectory)) || (reqHead.getRequestUrl().equals("/favicon.ico")));
        String requestType = reqHead.getRequestType();
        String headerString;
        // if access is authorized, proceed to request handling, if not send a 403 forbidden error to client
        if (authorizedAccess) {
          switch (requestType) {
          /**
           * The GET method requests a representation of the specified resource. Requests using GET should only retrieve data
           * Opens the file requested on server using a buffered input stream and write content to output stream
           * If file is not found, returns a 404 not found error. Any other errors encountered are considered server-side
           */
            case "GET":
              if (reqHead.getFileExtension().startsWith("py")) {
                String param = reqHead.getFileExtension().split("=")[1];
                String scriptName = reqHead.getRequestUrl().split("\\?")[0];
                LuckFinderTest myLuckFinder = new LuckFinderTest();
                String result = myLuckFinder.launch(param, scriptName);
                headerString = responseBuilder(200, "OK", reqHead);
                out.write(headerString.getBytes());
                out.write(result.getBytes());
              } else {
                  try {
                    File file = new File(reqHead.getRequestUrl().substring(1));
                    BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file));
                    int readSize;
                    byte[] buff = new byte[1024];
                    headerString = responseBuilder(200, "OK", reqHead);
                    out.write(headerString.getBytes());
                    while ((readSize = fileStream.read(buff)) > 0)
                    {
                      out.write(buff, 0, readSize);
                    }
                    fileStream.close();
                  } catch (FileNotFoundException e) {
                    headerString = responseBuilder(404, "NOT FOUND", reqHead);
                    out.write(headerString.getBytes());                                    
                  } catch (Exception e) {
                    headerString = responseBuilder(500, "INTERNAL SERVER ERROR", reqHead);
                    out.write(headerString.getBytes());   
                    e.printStackTrace();
                  }
                }
              out.flush();
              break;
            /**
             * The PUT method replaces all current representations of the target resource with the request payload
             * Collects file content from input stream and write to a file output stream
             * By definition, PUT requests ignore initial file content and overwrites the file
             */        
            case "PUT":
              try {
                File file = new File(reqHead.getRequestUrl().substring(1));
                file.createNewFile();
                byte[] buffer = new byte[1024];
                BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(file));
                while (in.available() > 0) {
                  int read = in.read(buffer);
                  fileOutput.write(buffer, 0, read);
                }
                fileOutput.flush();
                fileOutput.close();
                headerString = responseBuilder(201, "CREATED", reqHead);
              } catch (Exception e) {
                headerString = responseBuilder(500, "INTERNAL SERVER ERROR", reqHead);
                e.printStackTrace();
              }
              out.write(headerString.getBytes());             
              out.flush();
              break;
            /**
             * The HEAD method asks for a response identical to that of a GET request, but without the response body
             */ 
            case "HEAD":
              try {
                File file = new File(reqHead.getRequestUrl().substring(1));
                if (file.exists() && file.isFile()) {
                  headerString = responseBuilder(200, "OK", reqHead);
                } else {
                  headerString = responseBuilder(404, "NOT FOUND", reqHead);
                }
              } catch (Exception e) {
                headerString = responseBuilder(500, "INTERNAL SERVER ERROR", reqHead);
                e.printStackTrace();
              }
              out.write(headerString.getBytes());   
              out.flush();
              break;
            /**
             * The POST method is used to submit an entity to the specified resource, often causing a change in state or side effects on the server
             * Collects file content from input stream and write to a file output stream
             * The difference between POST and PUT is that POST appends the data to an existing file or creates it with the data, whereas PUT just overwrites the file,
             * hence the distinction between 201 created and 200 ok
             */  
            case "POST":
              try {
                File file = new File(reqHead.getRequestUrl().substring(1));
                boolean fileExists = file.exists();
                boolean fileCreated = file.createNewFile();
                byte[] buffer = new byte[1024];
                BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(file, fileExists));
                while (in.available() > 0) {
                  int read = in.read(buffer);
                  fileOutput.write(buffer, 0, read);
                }
                fileOutput.flush();
                fileOutput.close();
                if (fileCreated) {
                  headerString = responseBuilder(201, "CREATED", reqHead);
                } else {
                  headerString = responseBuilder(200, "OK", reqHead);
                }
              } catch (Exception e) {
                headerString = responseBuilder(500, "INTERNAL SERVER ERROR", reqHead);
                e.printStackTrace();
              }
              out.write(headerString.getBytes());             
              out.flush();
              break;
            /**
             * The DELETE method deletes the specified resource
             * Only filename contained in the request url is used to locate the file to be deleted
             * Upon a successful delete operation, returns 204 no content
             */  
            case "DELETE":
              try{
                File file = new File(reqHead.getRequestUrl().substring(1));
                if (!file.exists() && file.isFile()) {
                  headerString = responseBuilder(404, "NOT FOUND", reqHead);
                } else {                
                  headerString = responseBuilder(204, "NO CONTENT", reqHead);
                }
              }
              catch (Exception e) {
                headerString = responseBuilder(500, "INTERNAL SERVER ERROR", reqHead);
                e.printStackTrace();
              }
              out.write(headerString.getBytes());             
              out.flush();
              break;
            /**
             * CONNECT, OPTIONS, TRACE, PATCH... are not implemented on this HTTP server, hence the 501 not implemented error code
             */  
            default:
              headerString = responseBuilder(501, "NOT IMPLEMENTED", reqHead);
              out.write(headerString.getBytes());
              out.flush();
              break;
        }
        remote.close();
        } else {
          headerString = responseBuilder(403, "FORBIDDEN", reqHead);
          out.write(headerString.getBytes());
          out.flush();
        }
      } catch (Exception e) {  
        e.printStackTrace();
      }  
    }
  }

  /**
   * Builds a response header as a String object that will then be sent back to client
   * First line describes the HTTP version, status code and status message
   * Second line describes the content type sent back to client : text/html, image/png...
   * Third line describes the source server
   * 
   * @param statusCode
   * @param statusMessage
   * @param reqHead
   * @return
   */
  private String responseBuilder(int statusCode, String statusMessage, RequestHeader reqHead) {
    String responseHeader = "";
    responseHeader = responseHeader.concat("HTTP/1.0 "+Integer.toString(statusCode)+" "+statusMessage+"\r\n");
    switch (statusCode) {
      case 400:
        System.out.println("Error 400 : Bad request!");
        responseHeader = responseHeader.concat("Content-Type: text/html\r\n");
        responseHeader = responseHeader.concat("\r\n<h1>Error 400</h1><h2>The request could not be understood by the server due to malformed syntax. Please verify the request syntax before retrying...</h2>");
        break;
      case 403:
        System.out.println("Error 403 : Forbidden Access Error!");
        responseHeader = responseHeader.concat("Content-Type: text/html\r\n");
        responseHeader = responseHeader.concat("\r\n<h1>Error 403</h1><h2>The user is not authorized to perform the operation or the resource is unavailable for some reason...</h2>");        
        break;
      case 404:
        System.out.println("Error 404 : Ressource Not Found Error!");
        responseHeader = responseHeader.concat("Content-Type: text/html\r\n");
        responseHeader = responseHeader.concat("\r\n<h1>Error 404</h1><h2>The requested resource could not be found but may be available again in the future. Please try again later...</h2>");        
        break;
      case 500:
        System.out.println("Error 500 : Internal Server Error!");
        responseHeader = responseHeader.concat("Content-Type: text/html\r\n");
        responseHeader = responseHeader.concat("\r\n<h1>Error 500</h1><h2>The server encountered an unexpected condition which prevented it from fulfilling the request. Please try again later...</h2>");
        break;
      case 501:
        System.out.println("Error 501 : Not Implemented Error!");
        responseHeader = responseHeader.concat("Content-Type: text/html\r\n");
        responseHeader = responseHeader.concat("\r\n<h1>Error 500</h1><h2>The server encountered an unexpected condition which prevented it from fulfilling the request. Please try again later...</h2>");
        break;
      default:
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
            responseHeader = responseHeader.concat("Content-Type: text/html\r\n");
            break;
        }
        break;
    }
    responseHeader = responseHeader.concat("Server: Bot\r\n\r\n");
    System.out.println(reqHead.getRequestType()+" "+reqHead.getRequestUrl()+"\r\n"+responseHeader);      
    return responseHeader;
  }

  /**
   * Start the application.
   * 
   * @param args command line parameters are not used.
   */
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start(80);
  }
}
