package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class WikiPicsPage extends PageObject {

    @FindBy(how = How.XPATH, using = "//*[@id='mw-content-text']//a/img")
    @CacheLookup
    protected List<WebElement> pics;

    protected WikiPicsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(pics, "Wikimedia pics");
        domIsReady("Wikimedia pics");
    }

    public static WikiPicsPage go(WebDriver driver, String PAGE_URL) {
        driver.get(PAGE_URL);
        return new WikiPicsPage(driver);
    }
    public String getImage(){
        StringBuilder miPics= new StringBuilder();
        for (WebElement img: pics) {
            miPics.append("<img src=\"").append(img.getAttribute("src")).append("\" width=\"").append(img.getAttribute("width")).append("\" height=\"").append(img.getAttribute("height")).append("\" />");
        }
        return miPics.toString();
    }
}
