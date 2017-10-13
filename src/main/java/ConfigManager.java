import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Аглиуллины on 01.10.2017.
 */
public class ConfigManager {
   // private static Properties properties;
    private static Properties Init(){
        Properties properties=null;///home/ratmir/OK1tvBot/config.properties
        File configFile = new File("/home/ratmir/OK1tvBot/config.properties");

        try {
            FileReader reader = new FileReader(configFile);
            properties = new Properties();
            properties.load(reader);
           reader.close();
        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            // I/O error
        }
        return properties;
    }

    public static String getURL(){
        return Init().getProperty("url");
    }

    public static String getChannelName(){
        return Init().getProperty("channelName");
    }
    public static String getBotUserName(){
        return Init().getProperty("botUserName");
    }
    public static String getBotToken(){
        return Init().getProperty("botToken");
    }
    public static Integer getTimeOut(){
        Integer val =  Integer.valueOf(Init().getProperty("timeout"));
        return val*1000;}


//    public static void main(String[] args) {
//        System.out.println(getURL()+" "+getChannelName()+" "+getBotUserName()+" "+getBotToken());
//    }
}
