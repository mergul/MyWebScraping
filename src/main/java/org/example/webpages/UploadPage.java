package org.example.webpages;

import org.example.base.PageObject;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.time.Duration;
import java.util.List;

public class UploadPage extends PageObject {
    private static final Logger log = LoggerFactory.getLogger(UploadPage.class);

    @FindBy(how = How.XPATH, using = "//app-files-thumbnails/div/img[@id='images']")
    @CacheLookup
    protected WebElement thumbnail;

    @FindBy(how = How.XPATH, using = "//input[@id='file-input']")
    @CacheLookup
    protected WebElement fileInput;

    @FindBy(how = How.XPATH, using = "//button[@type='submit']")
    @CacheLookup
    protected WebElement submitButton;

    @FindBy(how = How.XPATH, using = "//*[@id='news_topic']")
    @CacheLookup
    protected WebElement title;

    @FindBy(how = How.XPATH, using = "//*[@id='news_description']")
    @CacheLookup
    protected WebElement summary;

    public UploadPage(WebDriver driver){
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(fileInput, "upload page");
        //waitFor(submitButton, "submit button");
    }

    public ProfilePage uploadContent(String inTitle, String inSummary, List<String> externalUrls, @NotNull List<String> base64img, String embeddedVideo, List<String> objects){

        for (String url: externalUrls) {
            url="<img src='"+url+"'/img>";
            inTitle=inTitle.concat(url);
        }
        for (String obj: objects) {
            inTitle=inTitle.concat(obj);
        }
//        embeddedVideo = embeddedVideo.replace("<iframe","<i").replace("</iframe>","");
//        embeddedVideo=embeddedVideo.replace("title=\"YouTube video player\"","");
//        embeddedVideo=embeddedVideo.replace("frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen","");
//        setSumma(inTitle+embeddedVideo.replace("https://www.youtube.com/embed",""));
        setSumma(inTitle+embeddedVideo);
        summary.sendKeys(inSummary);
        title.sendKeys(Keys.CONTROL , "v");
       // ((JavascriptExecutor)driver).executeScript("arguments[0].setAttribute('value', arguments[1])", summary, "<p>"+inSumma+"</p>"+table+"<youtube>"+svg+"</youtube>");
        String lll= String.join(System.lineSeparator(), base64img);
        if(!lll.isEmpty()){
            fileInput.sendKeys(lll);
            waitFor(thumbnail, "file input succeeded!");
        }
//        fileInput.sendKeys(file);
//        waitFor(thumbnail, "file input succeeded!");
//        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", submitButton);
        Actions actions = new Actions(driver);
        actions.moveToElement(submitButton).pause(Duration.ofSeconds(2L)).click().perform();
        return ProfilePage.go(driver);
    }

    private void setSumma(String value) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
    }

    public static UploadPage go(WebDriver driver) {
        return new UploadPage(driver);
    }

    public void uploadBase(List<String> allHref) throws InterruptedException {
        String lll= String.join(System.lineSeparator(), allHref);
        fileInput.sendKeys(lll);
        waitFor(thumbnail, "file input succeeded!");
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
//        for (String base64: allHref) {
//            fileInput.sendKeys(base64);
//            waitFor(thumbnail, "file input succeeded!");
//            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
//            Thread.sleep(5000L);
//        }
    }
}
