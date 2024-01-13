package org.example.webpages;

import org.example.base.Content;
import org.example.base.PageObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class GNewsDetails extends PageObject {
    private static final Logger log = LoggerFactory.getLogger(GNewsDetails.class);

//    @FindBy(how = How.XPATH, using = "//body")
//    @CacheLookup
//    protected List<WebElement> details;

    protected GNewsDetails(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        //domIsReady("amnesty page dom is ready");
       // waitFor(details, "GNewsDetails");
    }
    public static GNewsDetails go(WebDriver driver) { return new GNewsDetails(driver); }

    public Content checkHtml() throws InterruptedException {
        Wait<WebDriver> wait =
                new FluentWait<>(driver)
                        .withTimeout(Duration.ofSeconds(15))
                        .pollingEvery(Duration.ofMillis(500))
                        .ignoring(ElementNotInteractableException.class)
                        .ignoring(StaleElementReferenceException.class);
        StringBuilder icepick= new StringBuilder();
        String title="title";
        if(driver.getCurrentUrl().contains("youtube.com"))
            return new Content("youtube", "youtube", Collections.emptyList());
//        Thread.sleep(5L);
//        if (!details.isEmpty()) {
//            WebElement bod=details.get(0);
//            log.info("details.get(0) -> " + bod.getTagName());
          //  waitFor(By.tagName("p"), "body p");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//body//p")));
        List<WebElement> detailz = driver.findElements(By.xpath("//body//p"));
//        List<WebElement> detailz = isElementPresent(details, By.xpath(".//article//p"))?details.findElements(By.xpath(".//article//p")):
//        isElementPresent(details, By.xpath(".//*[contains(@class, 'article')]//p"))?details.findElements(By.xpath(".//*[contains(@class, 'article')]//p")):
//        isElementPresent(details, By.xpath(".//*[contains(@class, 'article-content')]//p"))?details.findElements(By.xpath(".//*[contains(@class, 'article-content')]//p")):
//        isElementPresent(details, By.xpath(".//*[contains(@class, 'article__content')]//p"))?details.findElements(By.xpath(".//*[contains(@class, 'article__content')]//p")):
//        isElementPresent(details, By.xpath(".//main//p"))?details.findElements(By.xpath(".//main//p")):details.findElements(By.xpath(".//p"));
      //  title = !detailz.isEmpty() ? detailz.get(0).getText() : "title";
        String txt;
        for (WebElement article : detailz) {
           try {
               txt = article.getText();
               icepick.append(txt);
           } catch(StaleElementReferenceException ex){
               log.error("StaleElementReferenceException -> ", ex.getCause());
            }
        }
//        }
        return new Content(title, icepick.toString(), Collections.emptyList());
    }
}
