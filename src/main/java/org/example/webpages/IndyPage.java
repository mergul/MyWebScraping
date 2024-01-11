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

public class IndyPage extends PageObject {
    private static final String PAGE_URL="https://www.indyturk.com/";

    @FindBy(how = How.XPATH, using = "//article[.//a[@href='/haber']]")
    @CacheLookup
    protected List<WebElement> articles;

    protected IndyPage(WebDriver driver) {
        super(driver);
    }

    public static IndyPage go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new IndyPage(driver);
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
            String he = article.findElement(By.tagName("img")).getAttribute("src");
            String[] be = article.getText().split("\n");
            contentList.add(new Content(he, be[be.length-1], new ArrayList<>()));
            System.out.println("başlık ::=> " + he);
            System.out.println("konu ::=> " + be[be.length-1]);
        }
        return contentList;
    }
}
