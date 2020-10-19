import java.lang.ProcessBuilder;
import java.util.List;
import java.io.*;
//import java.io.fabric8.common.util.Processes;
public class LuckFinderTest {
    public static void main(String[] args) throws Exception {
    }
    
    public launch(String sign){
        String pathString = System.getProperty("user.name");
        ProcessBuilder processBuilder = new ProcessBuilder("C:/Users/" + pathString + "/AppData/Local/Programs/Python/Python39/python.exe", "LuckFinder.py", "-sign", sign);
        processBuilder.redirectErrorStream(true);
        Process p = processBuilder.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        //List<String> results = readProcessOutput(p.getInputStream());
        return "<h1> " + in.readLine() + " </h1>";

    }
}
