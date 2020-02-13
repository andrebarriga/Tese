import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadWrite_File {
    public void WriteFile(String filename, String[] StringToWrite) throws IOException {
        //Create a file outside the jar with the given filename
        String path = getPath();
        File file = new File(path + filename);

        if (!file.exists()){
            file.createNewFile();
        }

        OutputStream fos = new FileOutputStream(file ,false);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for(int i = 0; i < StringToWrite.length; i++) {
            bw.write(StringToWrite[i]);
            bw.newLine();
        }

        bw.flush();
        bw.close();

    }

    public String[] ReadFile(String filename) throws IOException {
        //If the file was updated and created a file with the same name outside the jar read from this one.
        //If this file does not exist search for the filename in the resources

        String path = getPath()+"/"+filename;
        File file = new File(path);
        BufferedReader reader;
        int sizeFile = 0;

        if (file.exists()){
            reader = new BufferedReader(new FileReader(file));
            sizeFile = (int) Files.lines(Paths.get(path)).count();
        }
        else{
            InputStream in = getClass().getClassLoader().getResourceAsStream(filename);
            reader = new BufferedReader(new InputStreamReader(in));

            while(reader.readLine() != null){
                sizeFile++;
            }

            in = getClass().getClassLoader().getResourceAsStream(filename);
            reader = new BufferedReader(new InputStreamReader(in));
        }

        String line = reader.readLine();
        String[] lines = new String[sizeFile];
        int i = 0;

        while(i < lines.length){
            lines[i] = line;
            line = reader.readLine();
            i++;
        }

        return lines;
    }

    private String getPath(){
        String path = "";
        try {
            path = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return path;
    }
}
