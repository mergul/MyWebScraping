package org.example.webpages;

import org.apache.commons.io.FileUtils;
import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LemondePage extends PageObject {
    private static final String PAGE_URL = "https://www.lemonde.fr/les-decodeurs/article/2020/05/05/coronavirus-age-mortalite-departements-pays-suivez-l-evolution-de-l-epidemie-en-cartes-et-graphiques_6038751_4355770.html";

    @FindBy(how = How.XPATH, using = "//div[@id='d_repartition_france']/..")
    @CacheLookup
    protected WebElement franceSvg;

    @FindBy(how = How.XPATH, using = "//div[@id='d_repartition_countries']/..")
    @CacheLookup
    protected WebElement europeSvg;

    public LemondePage(WebDriver driver) {
        super(driver);
    }

    public String getSvgHtml() {
        transformSimpleSvg(franceSvg.findElements(By.tagName("rect")));
        transformSimpleSvg(europeSvg.findElements(By.tagName("rect")));
        return "<section class='myHtml'> " + franceSvg.getAttribute("innerHTML") + "</section>" +
                "<section class='myHtml'> " + europeSvg.getAttribute("innerHTML") + "</section>";
    }

    public void transformSimpleSvg(List<WebElement> rects) {
        for (WebElement rect : rects) {
            ((JavascriptExecutor) driver).executeScript("[...arguments[0].attributes].map(val => {\n" +
                    " if (val.nodeName.startsWith('data-')) {" +
                    " var title=document.createElement('title');" +
                    " var template = document.createElement('template');" +
                    " template.innerHTML=arguments[0].dataset[val.nodeName.split('-')[1]].trim().replace(/\\&nbsp;/g, '');" +
                    " title.innerHTML=template.content.textContent;" +
                    " arguments[0].append(title);" +
                    " }" +
                    " })", rect);
        }
    }

    @Override
    protected void onLoad() {
        waitFor(franceSvg, "Username input");
        waitFor(europeSvg, "Username input");
    }

    public static LemondePage go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new LemondePage(driver);
    }

    public void downloadTableHtml() {
        try {
            FileUtils.writeStringToFile(new File("/home/mesut/Documents/myfrance.html"), getSvgHtml(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/*
    @FindBy(how = How.XPATH, using = "//div[@id='tableau_monde']/..")
    protected WebElement worldSvg;

    waitFor(worldSvg, "Username input");

    transformTableau();

    public void transformTableau() {
        WebElement tableau = worldSvg.findElement(By.xpath("//div[@class='divTableBody']"));
        List<WebElement> rects = tableau.findElements(By.xpath("div"));
        for (WebElement rect : rects) {
            if (rect.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", rect);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rect.click();
            }
        }
    }
   public String transformMany(){
        transformTableau();
        List<WebElement> miRects = worldSvg.findElements(By.xpath("//div[@class='divTableBody']/div[contains(@class, 'contenantgraphemonde unpays')]"));
        System.out.println("country counts: --> "+miRects.size());
        for (WebElement rec : miRects) {
            System.out.println("country name: --> "+rec.getAttribute("id"));
            transformSimpleSvg(rec.findElements(By.className("transparente")));
        }
        return "<section class='myHtml'> " + worldSvg.getAttribute("innerHTML") + "</section>";

    }
*/
