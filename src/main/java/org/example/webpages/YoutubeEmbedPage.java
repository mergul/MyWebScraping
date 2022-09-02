package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YoutubeEmbedPage extends PageObject {

    @FindBy(how = How.XPATH, using = "//*[@id='contents']/ytd-video-renderer")
    protected List<WebElement> videoDiv;

    protected YoutubeEmbedPage(WebDriver driver) {
        super(driver);
    }
    public String getEmbeddedVideos(){
        Map<String,String> map= new HashMap<>();
//        for (int i = 0; i < videoDiv.size()-1; i++) {
//            map.put(videoDiv.get(i).findElement(By.xpath("//*[@id='video-title']/yt-formatted-string")).getText(), videoDiv.get(i).findElement(By.xpath("//a[@id='thumbnail']")).getAttribute("href").split("=")[1]);
//            System.out.println(videoDiv.get(i).findElement(By.xpath("//a[@id='thumbnail']")).getAttribute("href").split("=")[1]);
//        }
        return "<iframe id='youtube' width='560' height='315' src='https://www.youtube.com/embed/"+videoDiv.get(0).findElement(By.xpath("//a[@id='thumbnail']")).getAttribute("href").split("=")[1]+"?hl=en&cc_lang_pref=en&cc_load_policy=1' frameborder='0' allow='accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture' allowfullscreen> </iframe>";
    }
    @Override
    protected void onLoad() {
        waitFor(videoDiv, "Youtube Page");
    }
    public static YoutubeEmbedPage go(WebDriver driver, String PAGE_URL) {
        String url="https://www.youtube.com/results?search_query="
                + PAGE_URL.replace(" ", "+");
        driver.get(url);
        return new YoutubeEmbedPage(driver);
    }
}
