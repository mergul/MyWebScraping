package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoginPage extends PageObject {
    private static final String PAGE_URL = "https://busra.nur/login";//"http://localhost:4200/login";

    @FindBy(how = How.XPATH, using = "//form//input[@type='email']")
    @CacheLookup
    WebElement emailTextBox;

    @FindBy(how = How.XPATH, using = "//form//input[@type='password']")
    @CacheLookup
    WebElement passwordTextBox;

    @FindBy(how = How.XPATH, using = "//form//button[@type='submit']")
    @CacheLookup
    WebElement signinButton;

    @FindBy(how = How.XPATH, using = "//app-sign/div/article/section/button[@id='kaydet']")
    @CacheLookup
    WebElement socialInButton;

    @FindBy(how = How.XPATH, using = "//c-wiz/div/div[2]/div/div[1]/div/form/span/section/div/div/div[1]/div/div[1]/div/div[1]/input")
    @CacheLookup
    WebElement socialInput;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

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
        return press(driver);
    }
    public static LoginPage press(WebDriver driver) {
        return new LoginPage(driver);
    }

    public ProfilePagex loginMex(String uid, String pass) {
        setEmail(uid);
        setPassword(pass);
        clickOnLoginButton();
        return ProfilePagex.go(driver);
    }
    public ProfilePage loginMeSocial(String uid, String pass) throws InterruptedException {
        clickOnSocialButton();
        Thread.sleep(5000L);
        String handle = driver.getWindowHandles().toArray()[1].toString();
        driver.switchTo().window(handle);
        socialInput.sendKeys(uid);
        socialInput.sendKeys(Keys.RETURN);
        return ProfilePage.go(driver);
    }

    private void clickOnSocialButton() {
        Actions actions=new Actions(driver);
        actions.moveToElement(socialInButton).click().perform();
    }
//    private void setBoard(String value) {
//        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
//    }
}
