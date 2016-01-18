import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Sasinda
 * Date: 5/7/13
 * Time: 6:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
        URL japeURL= null;
        try {
            japeURL = new URL("file:/E:/sermester%207/FYP/FY%20Project%20libs%20and%20etc/gate-7.1-build4485-BIN/plugins/Pronoun_Annotator/resources/org_city_pronoun_annotator.jape");
            System.out.println(japeURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        File f = new File(japeURL.getPath());
        String path = f.getParent() + "/";

        System.out.println(path);
        // copy japeURL file to temporary file
        File p = new File(path);
        System.out.println(p);
        try {
            File tempJapeMain = File.createTempFile("pr_", ".jape",p);
            tempJapeMain.deleteOnExit();
            FileWriter fw = new FileWriter(tempJapeMain);
            System.out.println("created" +tempJapeMain);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
