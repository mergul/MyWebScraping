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
import java.util.List;

public class DemoNow extends PageObject {
    private static final String PAGE_URL="https://www.democracynow.org";

    @FindBy(how = How.XPATH, using = "//*[@id='highlighted_stories']/div[@class='row']/div")
    @CacheLookup
    protected List<WebElement> articles;

    protected DemoNow(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(articles, "democracy now page");
    }
    public static DemoNow go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new DemoNow(driver);
    }
    public List<Content> getArticles() {
        List<Content> contentList=new ArrayList<>();
        for (WebElement article : articles) {
            Actions actions = new Actions(driver);
            actions.moveToElement(article).perform();
            imgIsReady(article.findElement(By.tagName("img")), "demnow img is visible");
            String he = article.findElement(By.tagName("img")).getAttribute("src");
            String be = article.findElement(By.tagName("h3")).getText();
            contentList.add(new Content(he, be, new ArrayList<>()));
            System.out.println("başlık ::=> " + he);
            System.out.println("konu ::=> " + be);
        }
        return contentList;
    }
}
