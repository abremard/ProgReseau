package http.server;

/**
 * Request header object that contains header attributes
 * 
 * @author Iyad TOUT, Alexandre BREMARD
 * @version 1.0
 */

public class RequestHeader {
    
    private String requestType;
    private String requestUrl;
    private String httpVersion;
    private String host;
    private String fileExtension;
    // private String connection;
    // private String cacheControl;
    // private String upgradeInsecureRequests;
    // private String userAgent;
    // private String accept;

    private boolean firstLine;

    RequestHeader() {
        this.firstLine = true;
    }

    /**
     * Receives a String line containing key and value, then parses to the object's attributes
     * @param line
     */
    public void parseRequest(String line) {
        if (firstLine) {
            String stringArray[] = line.split(" ");
            this.requestType = stringArray[0];
            this.requestUrl = stringArray[1];
            try {
                this.fileExtension = stringArray[1].split("[.]")[1];
            } catch (Exception e) {
                // no file extension
            }
            this.httpVersion = stringArray[2];
            this.firstLine = false;
        }
        else {
            String stringArray[] = line.split(": ");
            String key = stringArray[0];
            String value = stringArray[1];
            switch (key) {
                case "Host":
                    this.host = value;
                    break;
            
                default:
                    break;
            }
        }
    }

    /** 
     * prints each attribute, used for debugging purposes
     */
    public void printObject() {
        System.out.println("requestType : "+requestType);
        System.out.println("requestUrl : "+requestUrl);
        System.out.println("fileExtension : "+fileExtension);
        System.out.println("httpVersion : "+httpVersion);
        System.out.println("host : "+host);
    }

    public String getRequestType() {
        return requestType;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHost() {
        return host;
    }

}
