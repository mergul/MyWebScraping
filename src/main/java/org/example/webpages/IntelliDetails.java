package org.example.webpages;

import org.example.base.Content;
import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.Collections;
import java.util.List;

public class IntelliDetails extends PageObject {

    @FindBy(how = How.XPATH, using = "//div[contains(@class, 'mainContent')]//h1[@class='subtitle']")
    protected WebElement title;

    @FindBy(how = How.XPATH, using = "//div[contains(@class, 'mainContent')]//h1[@class='subtitle']/..")
    protected WebElement imageParent;

    @FindBy(how = How.XPATH, using = "//div[contains(@class, 'mainContent')]//div[@class='restBody ft18']/div[@class='pt10 mt10 searchHighlight']")
    protected WebElement articleDetails;

    protected IntelliDetails(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(articleDetails, "Intelligent News Page Details");
    }
    public static IntelliDetails go(WebDriver driver, String PAGE_URL) {
        driver.get(PAGE_URL);
        return new IntelliDetails(driver);
    }
    public Content checkHtml() {
        StringBuilder icepick= new StringBuilder();
        List<WebElement> articleDetailz=articleDetails.findElements(By.tagName("p"));
        for (WebElement article: articleDetailz) {
            icepick.append(article.getText());
        }
        WebElement image=imageParent.findElement(By.tagName("img"));
        return new Content(title.getText(), icepick.toString(), image!=null?Collections.singletonList(image.getAttribute("src")):Collections.emptyList());
    }
}
