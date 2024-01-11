package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YoutubePage  extends PageObject {
    @FindBy(how = How.XPATH, using = "//*[@id='contents']/ytd-video-renderer")
    @CacheLookup
    protected List<WebElement> videoDiv;
    protected YoutubePage(WebDriver driver) {
        super(driver);
    }
    public YoutubeEmbedPage getEmbeddedVideo(){
        Map<String,String> map= new HashMap<>();
        WebElement vid=videoDiv.get(0);
        WebElement embedAnchor=vid.findElement(By.tagName("a"));
        //String href=embedAnchor.getAttribute("href");
        Actions actions = new Actions(driver);
        actions.moveToElement(embedAnchor).click().perform();
        return YoutubeEmbedPage.press(driver);
//        String embed=vid.findElement(By.tagName("a")).getAttribute("href");//.split("=")[1];
//        return "<iframe id='youtube' width='560' height='315' src='https://www.youtube.com/embed/"+embed+"?hl=en&cc_lang_pref=en&cc_load_policy=1' frameborder='0' allow='accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture; web-share' allowfullscreen> </iframe>";
    }
    @Override
    protected void onLoad() {
        waitFor(videoDiv, "Youtube Page");
    }
    public static YoutubePage go(WebDriver driver, String PAGE_URL) {
        String url="https://www.youtube.com/results?search_query="
                + PAGE_URL.replace(" ", "+");
        driver.get(url);
        return new YoutubePage(driver);
    }
}
