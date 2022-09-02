package org.example.webpages;
import org.apache.commons.io.FileUtils;
import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WikiCovidPage extends PageObject {
    private static final String PAGE_URL="https://en.wikipedia.org/api/rest_v1/page/html/COVID-19_pandemic_in_Turkey?redirect=true&origin=*";

    @FindBy(how = How.XPATH, using = "//section[h2[@id='Timeline']]/div")
    protected List<WebElement> tableRs;

    public WikiCovidPage(WebDriver driver){
        super(driver);
    }
    public String getTableHtml() {
        StringBuilder result= new StringBuilder();
        for (WebElement findElement : tableRs) {
            int eleHeight = findElement.getSize().getHeight();
            if (eleHeight > 100) {
                result.append("<section class='myHtml'> ").append(findElement.getAttribute("innerHTML")).append("</section>");
            }
        }
        return result.toString();
    }

    @Override
    protected void onLoad() {
        waitFor(tableRs, "Username input");
    }
    public static WikiCovidPage go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new WikiCovidPage(driver);
    }
    public void downloadTableHtml() {
        for (WebElement findElement : tableRs) {
            int eleHeight = findElement.getSize().getHeight();
            if (eleHeight > 100) {
                try {
                    FileUtils.writeStringToFile(new File("/tmp/micovid.html"),"<section class='micovid'> " + findElement.getAttribute("innerHTML") + "</section>", StandardCharsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
