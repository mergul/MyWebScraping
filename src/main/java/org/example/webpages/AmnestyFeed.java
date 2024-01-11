package org.example.webpages;

import org.example.base.Content;
import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AmnestyFeed extends PageObject {
    private static final Logger log = LoggerFactory.getLogger(AmnestyFeed.class);
    private static final String PAGE_URL="https://www.amnesty.org/en/search/kurdish";//"https://www.amnesty.org/en/search/?q=kurdish&ref=&year=2020&lang=en&sort=date";

    @FindBy(how = How.XPATH, using = "//*[@id=\"ccc-notify-accept\"]")
    @CacheLookup
    protected WebElement submit;

    @FindBy(how = How.XPATH, using = "//section[@aria-label='Search results']/article")
    @CacheLookup
    protected List<WebElement> articles;

    public AmnestyFeed(WebDriver driver){
        super(driver);
    }

    @Override
    protected void onLoad() {
        domIsReady("amnesty page dom is ready");
        waitFor(articles, "amnesty page");
        waitFor(submit, "submit cookies");
    }

    public List<Content> getArticles() throws InterruptedException {
        Thread.sleep(2L);
        submit.click();
        List<Content> contentList=new ArrayList<>();
        for (int i=0; i<3; i++) {
            WebElement article = articles.get(i);
            WebElement hez = article.findElement(By.className("post-title")).findElement(By.tagName("a"));
            String he = hez.getText();
            String be = article.findElement(By.className("post-excerpt")).getText();
            contentList.add(new Content(he, be, new ArrayList<>()));
            log.info("başlık ::=> " + he);
            log.info("konu ::=> " + be);
        }
        return contentList;
    }
    public static AmnestyFeed go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new AmnestyFeed(driver);
    }

    public List<Content> getDetails() throws InterruptedException, IOException {
        List<Content> contentList=new ArrayList<>();
        Actions actions=new Actions(driver);
        for (int i=0; i<3; i++) {
            WebElement article = articles.get(i);
            WebElement hez = article.findElement(By.className("post-title")).findElement(By.tagName("a"));
            log.info("getDetails -> "+hez.getAttribute("href"));
            actions.moveToElement(hez).click().perform();
            Thread.sleep(2000L);
            AmnestyDetails amnestyDetails= AmnestyDetails.go(driver);
            contentList.add(amnestyDetails.checkHtml());
            amnestyDetails.backCall();
            Thread.sleep(2000L);
        }

        return contentList;
    }
}
