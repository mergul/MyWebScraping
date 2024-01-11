package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class TranslatedPage extends PageObject {

    @FindBy(css = "span[lang='tr'] > span > span")
    @CacheLookup
    protected List<WebElement> translation;

    protected TranslatedPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(translation, "translated page");
    }
    public static TranslatedPage go(WebDriver driver) {
        return new TranslatedPage(driver);
    }
    public void printOut(){
        System.out.println("Translation:" +"\n");
        for(WebElement t : translation){
            System.out.print(t.getText() +"\n");
        }
    }
    public String getTextOf(){
        StringBuilder stringBuilder=new StringBuilder();
        for(WebElement t : translation){
            stringBuilder.append(t.getText());
        }
        return stringBuilder.toString();
    }
}
