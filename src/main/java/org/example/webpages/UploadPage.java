package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class UploadPage extends PageObject {

    @FindBy(how = How.XPATH, using = "//app-files-thumbnails/div/img")
    protected WebElement thumbnail;

    @FindBy(how = How.XPATH, using = "//input[@id='file-input']")
    protected WebElement fileInput;

    @FindBy(how = How.XPATH, using = "//button[@type='submit']")
    protected WebElement submitButton;

    @FindBy(how = How.XPATH, using = "//*[@id='listing-dialog']/div/div/div[2]/form/div[1]/label/div/div[1]/textarea")
    protected WebElement title;

    @FindBy(how = How.XPATH, using = "//*[@id='listing-dialog']/div/div/div[2]/form/div[2]/label/div/div/textarea")
    protected WebElement summary;

    public UploadPage(WebDriver driver){
        super(driver);
    }

    @Override
    protected void onLoad() {
       // waitFor(fileInput, "upload page");
    }

    public ProfilePage uploadContent(String inTitle, String inSumma, String table, List<String> imgs, String svg, String file){
        this.title.sendKeys(inTitle);
        summary.sendKeys(inSumma);
        StringBuilder image= new StringBuilder();
        for (String ima: imgs){
            image.append(ima);
        }
        ((JavascriptExecutor)driver).executeScript("arguments[0].setAttribute('value', arguments[1])", summary, "<p>"+inSumma+"</p>"+table+svg+image.toString());
        fileInput.sendKeys(file);
        waitFor(thumbnail, "file input succeeded!");
//        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", submitButton);
        Actions actions = new Actions(driver);
        actions.moveToElement(submitButton).click().perform();
        return ProfilePage.go(driver);
    }
    public static UploadPage go(WebDriver driver) {
        return new UploadPage(driver);
    }
}
