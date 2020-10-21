package http.server;
import java.lang.ProcessBuilder;
import java.io.*;

/**
 * LuckFinderTest executes a Python script in a OS process and retrieves the first line from stdout
 * 
 * @author Iyad TOUT, Alexandre BREMARD
 * @version 1.0
 */
public class LuckFinderTest {
    public static void main(String[] args) throws Exception {
    }
    
  /**
   * Execute the python file given as a parameter with the user's sign as the other parameter.
   * 
   * @param sign the user's horoscope sign
   * @param nomFichier the python file path
   * @return prediction message as HTML h1 header
   */
    public String launch(String sign, String nomFichier){
        try{
            String pathString = System.getProperty("user.name");
            String scriptPathString = System.getProperty("user.dir");
            ProcessBuilder processBuilder = new ProcessBuilder("C:/Users/" + pathString + "/AppData/Local/Programs/Python/Python39/python.exe", scriptPathString + "/" + nomFichier, "-sign", sign);
            processBuilder.redirectErrorStream(true);
            Process p = processBuilder.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            return "<h1> " + in.readLine() + " </h1>";
        }
        catch (Exception e){
            System.err.println("Error in Luck Finder module");
            return "<h1> +  Error in Luck Finder module +  </h1>";
        }
    }
}
