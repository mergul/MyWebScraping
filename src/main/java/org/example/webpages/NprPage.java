package org.example.webpages;

import org.example.base.Content;
import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NprPage extends PageObject {
    private static final String PAGE_URL="https://www.npr.org";

    @FindBy(how = How.XPATH, using = "//div[@id='contentWrap']/div[contains(@class,'stories-wrap-featured')]")
    protected WebElement content;

    protected NprPage(WebDriver driver) {
        super(driver);
    }
    public Content getNews() {
        List<WebElement> articles=content.findElements(By.xpath(".//article[(.//img)]"));
        Map<String, Content> map= new HashMap<>();
        for (WebElement article: articles) {
            String[] strings=article.getText().split("\\r?\\n");
            System.out.println(article.findElement(By.tagName("h3")).getText() + " <--icerik--> " + strings[strings.length-1]);
            Content miCon = new Content(article.findElement(By.tagName("h3")).getText(), strings[strings.length-1], Collections.singletonList(article.findElement(By.tagName("img")).getAttribute("src")));
            map.put(article.findElement(By.tagName("h3")).getText(), miCon);
        }
        return map.values().iterator().next();
    }
    @Override
    protected void onLoad() {
        waitFor(content, "NPR page");
    }
    public static NprPage go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new NprPage(driver);
    }
}
