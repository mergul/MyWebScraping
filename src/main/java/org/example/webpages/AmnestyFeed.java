package org.example.webpages;

import org.example.base.Content;
import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.ArrayList;
import java.util.List;

public class AmnestyFeed extends PageObject {
    private static final String PAGE_URL="https://www.amnesty.org/en/search/?q=kurdish&ref=&year=2020&lang=en&sort=date";

    @FindBy(how = How.XPATH, using = "//article[@class='search-item--list']")
    protected List<WebElement> articles;

    public AmnestyFeed(WebDriver driver){
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(articles, "amnesty page");
        domIsReady("amnesty page dom is ready");
    }

    public List<Content> getArticles() {
        List<Content> contentList=new ArrayList<>();
        for (WebElement article : articles) {
            String he = article.findElement(By.className("search-item__title--bare")).getText();
            String be = article.findElement(By.className("search-item__brief")).getText();
            contentList.add(new Content(he, be, new ArrayList<>()));
            System.out.println("başlık ::=> " + he);
            System.out.println("konu ::=> " + be);
        }
        return contentList;
    }
    public static AmnestyFeed go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new AmnestyFeed(driver);
    }
}
