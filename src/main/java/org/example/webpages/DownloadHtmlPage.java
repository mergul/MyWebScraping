package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class DownloadHtmlPage extends PageObject {
   // private static final String PAGE_URL="file:////tmp/micovid.html";
   // private static final String PAGE_URL="file:////tmp/mifrance.html";

    @FindBy(how = How.XPATH, using = "//section[@class='myHtml']/..")
    protected WebElement summary;

    public DownloadHtmlPage(WebDriver driver) {
        super(driver);
    }

    public static DownloadHtmlPage go(WebDriver driver, String PAGE_URL) {
        driver.get(PAGE_URL);
        return new DownloadHtmlPage(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(summary, "table read");
    }

    public void transformTableau(){
//        List<WebElement> mirects=summary.findElements(By.xpath("//div[@class='divTableBody']/div[contains(@class, 'contenantgraphemonde unpays')]"));
        List<WebElement> rects=summary.findElements(By.xpath("//div[@class='divTableBody']/div"));
        for (WebElement rect : rects) {
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();",rect);
            System.out.println(rect.getAttribute("class"));
        }
    }
    public String getContentItems() {
        return summary.getAttribute("innerHTML");
    }
}
