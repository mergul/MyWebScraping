package org.example.webpages;

import org.example.base.Content;
import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.ArrayList;
import java.util.List;

public class IntelliNewsPage extends PageObject {
    private static final String PAGE_URL = "https://www.intellinews.com/search/?search_for=turkey";

    @FindBy(how = How.XPATH, using = "//div[@class='newsBlock']/div[contains(@class, 'newsArticle mt10')]")
    @CacheLookup
    protected List<WebElement> articles;

    protected IntelliNewsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(articles, "Intelligent News Page articles");
    }
    public static IntelliNewsPage go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new IntelliNewsPage(driver);
    }
    public List<Content> checkHtml() {
        List<Content> content=new ArrayList<>();
        for (WebElement article: articles) {
            String title=article.findElement(By.tagName("h2")).getText();
            String icerik=article.findElement(By.tagName("p")).getText();
            if (article.findElement(By.tagName("h2")).getText().contains("Turkey")||article.findElement(By.tagName("p")).getText().contains("Turkey")){
                List<String> anchors=new ArrayList<>();
                anchors.add(article.findElement(By.tagName("a")).getAttribute("href"));
                content.add(new Content(title, icerik, anchors));
                System.out.println("Main Page --> "+icerik);
            }
        }
        return content;
    }
}
