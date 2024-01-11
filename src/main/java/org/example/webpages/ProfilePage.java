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

public class ProfilePage extends PageObject {
    @FindBy(how = How.XPATH, using = "//app-header/header/div[2]/button[2]")
    @CacheLookup
    protected WebElement uploadButton;

    @FindBy(how = How.XPATH, using = "//app-header/header/div[2]/div/a/img")
    //@FindBy(how = How.XPATH, using = "//app-root/div/app-logged-nav/mat-toolbar/div/button[4]")
    @CacheLookup
    WebElement profileImg;

    @FindBy(how = How.XPATH, using = "//app-user/main/section[4]/article/app-news-list/div/ul")
    @CacheLookup
    WebElement profileNews;

    @FindBy(how = How.XPATH, using = "//app-user/main/section[4]/article/app-news-list/div/ul/li")
    @CacheLookup
    List<WebElement> profileNewsList;

    protected ProfilePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(uploadButton, "upload again");
        waitFor(profileImg, "entering profile succeeded!");
//        waitFor(profileNews, "entering contents succeeded!");
    }
    public UploadPage goUpload() throws InterruptedException {
        Thread.sleep(5000L);
        uploadButton.click();
        return UploadPage.go(driver);
    }
    public void isUploaded(){
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .withMessage("message")
                .until(webDriver -> !profileNews.getText().equals(""));

    }
    public static ProfilePage go(WebDriver driver) {
        return new ProfilePage(driver);
    }
}
