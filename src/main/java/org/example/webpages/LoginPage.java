package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoginPage extends PageObject {
    private static final String PAGE_URL = "http://localhost:4200/login";

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(how = How.XPATH, using = "//form//input[@type='email']")
    @CacheLookup
    WebElement emailTextBox;

    @FindBy(how = How.XPATH, using = "//form//input[@type='password']")
    @CacheLookup
    WebElement passwordTextBox;

    @FindBy(how = How.XPATH, using = "//form//button[@type='submit']")
    @CacheLookup
    WebElement signinButton;

    public ProfilePage loginMe(String uid, String pass) {
        setEmail(uid);
        setPassword(pass);
        clickOnLoginButton();
        return ProfilePage.go(driver);
    }

    // This method is to set Email in the email text box
    public void setEmail(String strEmail) {
        emailTextBox.clear();
        emailTextBox.sendKeys(strEmail);
    }

    // This method is to set Password in the password text box
    public void setPassword(String strPassword) {
        passwordTextBox.clear();
        passwordTextBox.sendKeys(strPassword);
    }

    // This method is to click on Login Button
    public void clickOnLoginButton() {
        signinButton.click();
    }

    @Override
    protected void onLoad() {
        waitFor(emailTextBox, "Username input");
    }

    public static LoginPage go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new LoginPage(driver);
    }
}
