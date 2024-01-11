package org.example.webpages;

import org.example.base.Content;
import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class GNewsDetails extends PageObject {
    private static final Logger log = LoggerFactory.getLogger(GNewsDetails.class);

    @FindBy(how = How.XPATH, using = "//body")
    @CacheLookup
    protected List<WebElement> details;

    protected GNewsDetails(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        //domIsReady("amnesty page dom is ready");
    }
    public static GNewsDetails go(WebDriver driver) { return new GNewsDetails(driver); }

    public Content checkHtml() {
        StringBuilder icepick= new StringBuilder();
        String title="title";
        if(driver.getCurrentUrl().contains("youtube.com"))
            return new Content("youtube", "youtube", Collections.emptyList());
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        if (!details.isEmpty()&&isElementPresent(details.get(0), By.tagName("p"))) {
            List<WebElement> detailz = details.get(0).findElements(By.tagName("p"));
//        List<WebElement> detailz = isElementPresent(details, By.xpath(".//article//p"))?details.findElements(By.xpath(".//article//p")):
//        isElementPresent(details, By.xpath(".//*[contains(@class, 'article')]//p"))?details.findElements(By.xpath(".//*[contains(@class, 'article')]//p")):
//        isElementPresent(details, By.xpath(".//*[contains(@class, 'article-content')]//p"))?details.findElements(By.xpath(".//*[contains(@class, 'article-content')]//p")):
//        isElementPresent(details, By.xpath(".//*[contains(@class, 'article__content')]//p"))?details.findElements(By.xpath(".//*[contains(@class, 'article__content')]//p")):
//        isElementPresent(details, By.xpath(".//main//p"))?details.findElements(By.xpath(".//main//p")):details.findElements(By.xpath(".//p"));
            title = !detailz.isEmpty() ? detailz.get(0).getText() : "title";
            for (WebElement article : detailz) {
                icepick.append(article.getText());
            }
        }
        return new Content(title, icepick.toString(), Collections.emptyList());
    }
}
