import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Аглиуллины on 03.09.2017.
 */
public class Parser {
    private String bufferFilePath = "buffer.dat";// "/home/ratmir/buffer.dat";
    public Integer likesCount = 0;
    public Integer repostCount = 0;

    public String getPostId(int index, Document document) {
        String result = null;

        Element element = document.select("div.feed.h-mod").get(index);
        result=element.attr("data-seen-params");
        return result;
    }

//https://ok.ru/udivitelnyymir
//https://ok.ru/1tv
    //https://ok.ru/paradpob


    public static void main(String[] args) {
        Parser parser = new Parser();
        Document doc = null;
        try {
            doc = Jsoup.connect(ConfigManager.getURL()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(parser.getPostId(1, doc));
        System.out.println(parser.getPostText(1, doc));
        parser.getGifsImages(1,doc).stream().forEach(s-> System.out.println(s));
        System.out.println(parser.getLinkWithMoreText(1, doc));
        System.out.println(parser.getVideoPostFromIndex(1, doc));
    }

public String getLinkWithMoreText(int index, Document document){
    Element element = document.select("div.media-status").get(index);
    Elements in = element.getElementsByClass("media_more");
    String result=null;
    try {
        Elements hrefs = in.get(0).getElementsByTag("a");
        result=hrefs.get(0).attr("href");
    }
    catch (IndexOutOfBoundsException e)
    {
        result = " ";
    }
    return "https://ok.ru"+result;
}

    public String getPostText(int index, Document document) { //IndexOutOfBoundsException сделать чтобы читал полный текст
        Element element = document.select("div.media-status").get(index);
        Elements in = element.getElementsByClass("media-text_cnt");
       String result = null;
       try {
           result = in.get(0).text();
       }
       catch (IndexOutOfBoundsException e)
       {
           result = " ";
       }
        return result;

    }


    public List<String> getGifsImages(int index, Document document) {
        String result = null;
        List<String> imageList = new ArrayList<>();
        Elements elements = document.select("div.media-status");
        try {
            Element postContent = elements.get(index).getElementsByClass("gif").get(0);
               Elements in = postContent.getElementsByTag("div");
           // imageList.add(postContent.toString());
            for (Element el : in
                    ) {

                imageList.add(el.attr("data-mp4src").substring(2));

            }
        }
        catch (IndexOutOfBoundsException e){
            imageList.add(" ");
        }
        return imageList;
    }



    public List<String> getPostImages(int index, Document document) {
        String result = null;
        List<String> imageList = new ArrayList<>();
        Elements elements = document.select("div.media-status");
        try{
        Element postContent = elements.get(index).getElementsByClass("image-hover").get(0);
        Elements in = postContent.getElementsByTag("img");

        for (Element el : in
                ) {

            imageList.add(el.attr("src").substring(2));

        }}
        catch (IndexOutOfBoundsException e){
            imageList.add(" ");
        }
        return imageList;
    }


    public String getVideoPostFromIndex(int index, Document document) {
        String result = null;
        String wallVideo=null;
        List<String> imageList = new ArrayList<>();
        Elements elements = document.select("div.media-status");
        try{
            Element postContent = elements.get(index).getElementsByClass("media-video").get(0);
          Elements in = postContent.getElementsByTag("div");
            wallVideo =in.attr("data-l");

        } catch (IndexOutOfBoundsException e) {
            return " ";
       }
        result = parsingWihRegex(wallVideo.toString(), "ti,(.*?),t", 3, 2);
        return "https://ok.ru/video/" + result;
    }

    public String parsingWihRegex(String input, String regex, int start, int end) {
        String result = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(input);
        if (m.find()) {
            result = m.group(0).substring(start, m.group(0).length() - end);


        }
        return result;
    }



    public void writeStringInFile(String in) throws IOException {

        BufferedWriter myfile = new BufferedWriter(new FileWriter(bufferFilePath));
        myfile.write(in);
        myfile.close();
    }

    public String getStringFromFile() throws IOException {
        BufferedReader myfile = new BufferedReader(new FileReader(bufferFilePath));
        return myfile.readLine();
    }


}
