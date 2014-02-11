package jode;
import java.io.*;

public enum Module {
    INSTANCE;

    public String readFromFile(InputStream inputStream) {
        StringBuilder fileData = new StringBuilder();
        String currentLine = "";
        try {

            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, "UTF-8")
            );


            while ((currentLine = bufferedReader.readLine()) != null) {
                fileData.append(currentLine).append("\n");
            }

        } catch (UnsupportedEncodingException ignored) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData.toString();
    }

    public String readFile(String path) {
        try {
            //System.out.println(path);
            return readFromFile(new FileInputStream(path));


        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
            System.exit(-1);
            return null;
        }
    }
}