package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProfilePagex extends PageObject {
    @FindBy(how = How.XPATH, using = "//*[@id='uploadx']")
    @CacheLookup
    protected WebElement uploadButton;

    @FindBy(how = How.XPATH, using = "//*[@id=\"ltoolr\"]/div[1]/button[3]/img")
    @CacheLookup
    WebElement profileImg;

    @FindBy(how = How.XPATH, using = "//app-profile-center/div/app-news-list/div/ul")
    @CacheLookup
    WebElement profileNews;

    @FindBy(how = How.XPATH, using = "//app-profile-center/div/app-news-list/div/ul/li")
    @CacheLookup
    List<WebElement> profileNewsList;


    protected ProfilePagex(WebDriver driver) {
        super(driver);
    }
    public UploadPage goUpload(){
        uploadButton.click();
        return UploadPage.go(driver);
    }
    public void isUploaded(){
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .withMessage("message")
                .until(webDriver -> !profileNews.getText().equals(""));

    }
    public static ProfilePagex go(WebDriver driver) {
        return new ProfilePagex(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(uploadButton, "upload again");
        waitFor(profileImg, "entering profile succeeded!");
        waitFor(profileNews, "entering contents succeeded!");
    }
}
