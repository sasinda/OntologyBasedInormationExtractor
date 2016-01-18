package sigmacr.informationretriver.structure.handlers;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FileAssistent {

    public String createDateFolder(String path){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String date_= dateFormat.format(date);
        boolean created =(new File(path+"/"+date_)).mkdir();
        return  path+"/"+date_;

    }

    public  String createFilePath(String savePath,String url){
        String productName= url.split("/")[3];
        boolean success = (new File(savePath+"/"+productName)).mkdir();


        String filePath=createDateFolder(savePath+"/"+productName);
        addMetaInfoFile(filePath,url);
        return filePath;
    }

    public String createSearchFolder(String fileLocation, String searchName){
        boolean success = (new File(fileLocation+"/"+searchName)).mkdir();
        return  fileLocation+"/"+searchName;
    }


    public Boolean addMetaInfoFile(String filePath, String url){

        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            //get current date time with Date()
            Date date = new Date();
            String date_= dateFormat.format(date);
            String time =timeFormat.format(date);
            String productName= url.split("/")[3];

            String metadata= "url :"+url+"\n"+"product name :"+productName+"\n"+"accessed date :"+date_+"\n"+"accessed time :"+time+"\n";



            File file = new File(filePath+"/metadata.meta");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(metadata);
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void callScraper(String productUrl, String fileSaveLocation) {
        String[] commands = {"nodejs", "../src/main/java/com/Spider.js",productUrl,fileSaveLocation};
        Runtime rt = Runtime.getRuntime();
        Process proc = null;


        try {

            proc = rt.exec(commands);
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);

            String line = null;
            System.out.println("<OUTPUT>");

            while ((line = br.readLine()) != null)
                System.out.println(line);

            System.out.println("</OUTPUT>");
            int exitVal = proc.waitFor();
            System.out.println("Process exitValue: " + exitVal);
        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
