

package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GooPicDetail extends PageObject {
    private static final Logger log = LoggerFactory.getLogger(org.example.webpages.GooPicsPage.class);

    @FindBy(how = How.XPATH, using = "//*[@id=\"islsp\"]//*[@id='Sva75c']//c-wiz//a[@role='link']/img")
    @CacheLookup
    protected List<WebElement> imgRefs;

    public GooPicDetail(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
       // waitFor(imgRefs, "search page");
       // domIsReady("GooPicDetail");
    }

    public static GooPicDetail go(WebDriver driver) {
        return new GooPicDetail(driver);
    }

    public List<String> getImgRefs() {
        List<String> images = new ArrayList<>();
        for (WebElement image: imgRefs) {
            String srcImg = image.getAttribute("src");
            if (!srcImg.startsWith("data:"))
                images.add(srcImg);
        }
        return images;
    }
    public void backCall() throws InterruptedException {
        driver.navigate().back();
    }
}
