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



    public static void main(String[] args) {
        Parser parser = new Parser();
        Document doc = null;
        try {
            doc = Jsoup.connect(ConfigManager.getURL()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(parser.getPostId(2, doc));
        System.out.println(parser.getPostText(2, doc));
        parser.getPostImages(2,doc).stream().forEach(s-> System.out.println(s));
        System.out.println(parser.getLinkWithMoreText(2, doc));
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
    return result;
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


    public void getTextPostFormIndex(int index, Document document) {
        Elements elements = document.select("div._post.post.page_block.all.own");
        Element wallText = elements.get(index).getElementsByClass("wall_post_text").get(0);
        System.out.println(wallText.text());
    }





    public String getVideoPostFormIndex(int index, Document document) {
        String result = null;
        Elements elements = document.select("div._post.post.page_block.all.own");
        Element wallVideo = null;
        try {
            wallVideo = elements.get(index).getElementsByClass("_post_content").get(0)
                    .getElementsByClass("post_content").get(0)
                    .getElementsByClass("post_info").get(0)
                    .getElementsByClass("wall_text").get(0)
                    .getElementsByClass("post_video_desc")
                    .get(0);
        } catch (IndexOutOfBoundsException e) {
            return " ";
        }
        result = parsingWihRegex(wallVideo.toString(), "href=\"\\/video(.*?)\\\"", 6, 1);
        //  System.out.println("https://vk.com"+result);
        return "https://vk.com" + result;
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
