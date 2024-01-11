package org.example.webpages;

import org.example.base.Content;
import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

public class GoogleNews extends PageObject {
    private static final Logger log = LoggerFactory.getLogger(GoogleNews.class);
    private static final String PAGE_URL="https://news.google.com";

    @FindBy(how = How.XPATH, using = "//main/div[2]//section//article")
    @CacheLookup
    protected List<WebElement> articles;

    protected GoogleNews(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        domIsReady("amnesty page dom is ready");
        waitFor(articles, "amnesty page");
    }
    public static GoogleNews go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new GoogleNews(driver);
    }
    public List<Content> getArticles() {
        List<Content> contentList=new ArrayList<>();
        int len=Math.min(articles.size(), 10);
        for (int i=0; i<len; i++) {
            WebElement article = articles.get(i);
            List<WebElement> hez = article.findElements(By.tagName("a"));
            String he = hez.get(1).getText();
            contentList.add(new Content(he, he, new ArrayList<>()));
            log.info("başlık ::=> " + he);
            log.info("konu ::=> " + he);
        }
        return contentList;
    }
    public List<Content> getDetails() throws InterruptedException, IOException {
        List<Content> contentList=new ArrayList<>();
        Actions actions=new Actions(driver);
        String originalWindow = driver.getWindowHandle();
        Wait<WebDriver> wait =
                new FluentWait<>(driver)
                        .withTimeout(Duration.ofSeconds(2))
                        .pollingEvery(Duration.ofMillis(300))
                        .ignoring(ElementNotInteractableException.class);
        for (int i=0; i<10; i++) {
            WebElement article = articles.get(i);
            List<WebElement> hez = article.findElements(By.tagName("a"));
            log.info("getDetails -> "+hez.get(1).getAttribute("href"));
            actions.moveToElement(hez.get(1)).click().perform();
            wait.until(numberOfWindowsToBe(2));
            Thread.sleep(2000L);
            for (String windowHandle : driver.getWindowHandles()) {
                if(!originalWindow.contentEquals(windowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
           // wait.until(titleIs("Selenium documentation"));
            GNewsDetails gNewsDetails= GNewsDetails.go(driver);
            contentList.add(gNewsDetails.checkHtml());
            Thread.sleep(2000L);
            driver.close();
            driver.switchTo().window(originalWindow);
        }

        return contentList;
    }
}
