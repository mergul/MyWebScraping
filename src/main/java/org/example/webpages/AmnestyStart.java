//package org.example.webpages;
//
//import org.example.base.PageObject;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.CacheLookup;
//import org.openqa.selenium.support.FindBy;
//import org.openqa.selenium.support.How;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.Duration;
//
//public class AmnestyStart  extends PageObject {
//    private static final Logger log = LoggerFactory.getLogger(AmnestyStart.class);
//    private static final String PAGE_URL="https://www.amnesty.org/en";//"https://www.amnesty.org/en/search/?q=kurdish&ref=&year=2020&lang=en&sort=date";
//
//    @FindBy(how = How.XPATH, using = "//*[@id=\"menu-item-185127\"]/a")
//    @CacheLookup
//    protected WebElement search;
//
//    @FindBy(how = How.XPATH, using = "//*[@id=\"ccc-notify-accept\"]")
//    @CacheLookup
//    protected WebElement submit;
//
//    protected AmnestyStart(WebDriver driver) {
//        super(driver);
//    }
//    public AmnestyFeed clickSearch(WebDriver myDriver) {
//        submit.click();
//        clickOnSearchButton(driver);
//        return AmnestyFeed.press(myDriver);
//    }
//    public void clickOnSearchButton(WebDriver driver) {
//        Actions actions = new Actions(driver);
//        actions.moveToElement(search).click().perform();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//    }
//    @Override
//    protected void onLoad() {
//        domIsReady("amnesty page dom is ready");
//        waitFor(submit, "submit cookies");
//    }
//    public static AmnestyStart go(WebDriver driver) {
//        driver.get(PAGE_URL);
//        return new AmnestyStart(driver);
//    }
//}
