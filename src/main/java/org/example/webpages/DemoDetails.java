package org.example.webpages;

import org.example.base.Content;
import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.Collections;

public class DemoDetails extends PageObject {

    @FindBy(how = How.XPATH, using = "//*[@id='story_text']/div[@class='story_summary']/div[@class='text']")
    @CacheLookup
    protected WebElement details;
    @FindBy(how = How.XPATH, using = "//*[@id='story_content']/h1")
    @CacheLookup
    protected WebElement title;
    @FindBy(how = How.XPATH, using = "//*[@id='story_video']/div[@class='jw-preview jw-reset']")
    @CacheLookup
    protected WebElement imageParent;

    protected DemoDetails(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(details, "Democracy Now Page Details");
    }
    public static DemoDetails go(WebDriver driver, String PAGE_URL) {
        driver.get(PAGE_URL);
        return new DemoDetails(driver);
    }
    public Content checkHtml() {
        String image=imageParent.getCssValue("background-image");
        return new Content(title.getText(), details.getText(), Collections.singletonList(image.substring(5, image.length() - 2)));
    }
}
