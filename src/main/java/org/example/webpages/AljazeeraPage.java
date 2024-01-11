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

public class AljazeeraPage extends PageObject {
    private static final String PAGE_URL="https://www.aljazeera.com/tag/israel-palestine-conflict/";
   // "/html/body/div[1]/div/div[3]/div/main/div/ul/li[1]"
    @FindBy(how = How.XPATH, using = "//main[@id='featured-news-container']//ul[@class='featured-articles-list']/li[@class='featured-articles-list__item']")
    @CacheLookup
    protected List<WebElement> articles;

    protected AljazeeraPage(WebDriver driver) {
        super(driver);
    }

    public static AljazeeraPage go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new AljazeeraPage(driver);
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
            String header = article.findElement(By.tagName("a")).findElement(By.tagName("span")).getText();
            String paraf = article.findElement(By.tagName("p")).getText();
            List<String> allMediaList=new ArrayList<>();
            allMediaList.add(imgSrc);
            contentList.add(new Content(header, paraf, allMediaList));
            System.out.println("başlık ::=> " + header);
            System.out.println("konu ::=> " + paraf);
        }
        return contentList;
    }
}
