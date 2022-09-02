package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class SubtitlePage extends PageObject {

    @FindBy(how = How.XPATH, using = "//button[@data-title='[SRT] Turkish']")
    protected WebElement srtButton;

    @FindBy(how = How.XPATH, using = "//div[@id=\"ds-information\"]/div[contains(@class, \"title\")]/a")
    protected WebElement title;

    protected SubtitlePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(srtButton, "SubPage button");
    }
    public static SubtitlePage go(WebDriver driver, String PAGE_URL) {
        String url="subtitle.to/" + PAGE_URL;
        driver.get(url);
        return new SubtitlePage(driver);
    }
    public String findCaptions(){
        srtButton.click();
        return "[Turkish] "+ title.getText() + " [DownSub.com].srt";
    }
}
