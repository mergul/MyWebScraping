package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GTranslate extends PageObject {
    private static final Logger log = LoggerFactory.getLogger(GTranslate.class);
    private static final String PAGE_URL = "https://translate.google.com";

    @FindBy(css = "textarea[role='combobox']")
    @CacheLookup
    private WebElement inputArea;

    protected GTranslate(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(inputArea, "translate page");
    }
    public static TranslatedPage go(WebDriver driver, String sl, String tl, String txt) {
        String url=PAGE_URL+"/?sl="+sl+"&tl="+tl+"&text="+txt+ "\n" +"&op=translate";
        log.info("TranslatedPage -> "+url);
        driver.get(url);
        return new TranslatedPage(driver);
    }
}
