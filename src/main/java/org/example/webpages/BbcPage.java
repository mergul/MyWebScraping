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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BbcPage extends PageObject {
    private static final String PAGE_URL="https://www.bbc.com/turkce";

    @FindBy(how = How.XPATH, using = "//main//section[@aria-labelledby='Manşet-haber']//ul/li")
    @CacheLookup
    protected List<WebElement> articles;

    protected BbcPage(WebDriver driver) {
        super(driver);
    }

    public static BbcPage go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new BbcPage(driver);
    }
    @Override
    protected void onLoad() {
        waitFor(articles, "Indy Page");
        domIsReady("Indy Page dom is ready");
    }
    public List<Content> getArticles() {
        List<Content> contentList=new ArrayList<>();
        for (WebElement article : articles) {
            Actions actions = new Actions(driver);
            actions.moveToElement(article).perform();
            imgIsReady(article.findElement(By.tagName("img")), "Indy img is visible");
            String imgSrc = article.findElement(By.tagName("img")).getAttribute("src");
            String link = article.findElement(By.tagName("a")).getAttribute("href");
            String title = article.findElement(By.tagName("a")).getText();
            String time = article.findElement(By.tagName("time")).getText();
            String context = article.findElement(By.tagName("p")).getText();
//            String[] be = article.getText().split("\n");
            contentList.add(new Content(title, context, Arrays.asList(link, imgSrc)));
            System.out.println("title ::=> " + title);
            System.out.println("context ::=> " + context +" time : "+ time +" link : "+ link);
        }
        return contentList;
    }
}
