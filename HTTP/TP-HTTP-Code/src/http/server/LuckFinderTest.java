package http.server;
import java.lang.ProcessBuilder;
import java.util.List;
import java.io.*;
//import java.io.fabric8.common.util.Processes;
public class LuckFinderTest {
    public static void main(String[] args) throws Exception {
    }
    
    public String launch(String sign, String nomFichier){
        try{
            String pathString = System.getProperty("user.name");
            String scriptPathString = System.getProperty("user.dir");
            ProcessBuilder processBuilder = new ProcessBuilder("C:/Users/" + pathString + "/AppData/Local/Programs/Python/Python39/python.exe", scriptPathString + "/" + nomFichier, "-sign", sign);
            processBuilder.redirectErrorStream(true);
            Process p = processBuilder.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //List<String> results = readProcessOutput(p.getInputStream());
            return "<h1> " + in.readLine() + " </h1>";
        }
        catch (Exception e){
            System.err.println("Error in Luck Finder module");
            return "<h1> +  Error in Luck Finder module +  </h1>";
        }
    }
}
