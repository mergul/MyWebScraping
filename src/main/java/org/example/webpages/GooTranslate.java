package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class GooTranslate extends PageObject {
    private static final String PAGE_URL = "https://translate.google.com";

    @FindBy(css = "textarea[id='source']")
    @CacheLookup
    private WebElement inputArea;

    @FindBy(css = "div[class='sl-more tlid-open-source-language-list']")
    @CacheLookup
    private WebElement fromDropdownBtn;

    @FindBy(xpath = "//div[@class='language-list-unfiltered-langs-sl_list']/div[@class='language_list_section']/div[contains(@class, 'language_list_item_wrapper')]")
    @CacheLookup
    private List<WebElement> sourceLanguages;

    @FindBy(css = "div[class='tl-more tlid-open-target-language-list']")
    @CacheLookup
    private WebElement toDropdownBtn;

    @FindBy(xpath = "//div[@class='language-list-unfiltered-langs-tl_list']/div[@class='language_list_section']/div[contains(@class, 'language_list_item_wrapper')]")
    @CacheLookup
    private List<WebElement> targetLanguages;

    protected GooTranslate(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(inputArea, "translate page");
    }
    public static GooTranslate go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new GooTranslate(driver);
    }
    public void selectFrom(String sourceLang){
        fromDropdownBtn.click();

        WebElement sLang = null;
        for(WebElement e : sourceLanguages){
            if(e.getText().equals(sourceLang)){
                sLang = e;
                break;
            }
        }
        sLang.click();
    }

    public void selectTo(String targetLang){
        toDropdownBtn.click();

        WebElement tLang = null;
        for(WebElement e : targetLanguages){
            if(e.getText().equals(targetLang)){
                tLang = e;
                break;
            }
        }
        tLang.click();
    }
    public TranslatedPage translateText(String textToTranslate){
        selectFrom("English");
        selectTo("Turkish");
        inputArea.sendKeys(textToTranslate);

        return new TranslatedPage(driver);
    }
}
