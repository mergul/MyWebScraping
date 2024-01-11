package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.time.Duration;

public class SignPage extends PageObject {
    private static final String PAGE_URL = "https://busra.nur/secure/sign";//"http://localhost:4200/secure/sign";

    @FindBy(how = How.XPATH, using = "//app-sign/div/ul/li[@rel='signin']")
    @CacheLookup
    protected WebElement signButton;

    protected SignPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(signButton, "Username input");
    }
    public LoginPage login() {
        clickOnSignButton(driver);
        return LoginPage.press(driver);
    }
    public void clickOnSignButton(WebDriver driver) {
        signButton.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }
    public static SignPage go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new SignPage(driver);
    }
}
