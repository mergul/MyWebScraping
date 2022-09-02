package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class ExtensionPage extends PageObject {
    private static final String PAGE_URL = "chrome-extension://aapbdbdomjkkjkaonfhkkikfgjllcleb/popup.html";

    @FindBy(how = How.XPATH, using = "//a[@id='translate-page']")
    @CacheLookup
    private WebElement translateButton;

    protected ExtensionPage(WebDriver driver) {
        super(driver);
    }

    public static ExtensionPage go(EventFiringWebDriver driver) {
   //     driver.get(PAGE_URL);
        return new ExtensionPage(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(translateButton, "Extension page");
    }

    public void checkButton() {
        System.out.println("Button is reached");
        translateButton.click();
    }
}

/*
    private static Robot robot;

    try {
            robot= new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
//        Map<String, Object> prefs = new HashMap<>();
//        Map<String, Object> langs = new HashMap<>();
//        langs.put("en", "tr");
//        prefs.put("translate", "{'enabled' : True}");
//        prefs.put("enable-translate-new-ux", 1);
//        prefs.put("translate_whitelists", langs);
//        prefs.put("intl.accept_languages", "tr");
//        prefs.put("profile.default_content_setting_values.notifications", 1);
//        options.setExperimentalOption("prefs", prefs);
//        options.addArguments("--lang=tr");

//        options.addExtensions(new File("/home/mesut/.config/google-chrome/Default/Extensions/aapbdbdomjkkjkaonfhkkikfgjllcleb/2.0.9_0.crx"));


 @Test
    public void applyExtension() {
        ExtensionPage extensionPage=ExtensionPage.go(eventFiringWebDriver);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        extensionPage.checkButton();
    }
  public void openExtension() {
        robotKeyPress(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_Q);
        try {
            ExtensionPage extensionPage=ExtensionPage.go(eventFiringWebDriver);
            Thread.sleep(1000);
            extensionPage.checkButton();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            robotKeyRelease(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_Q);
        }
    }

    private void robotKeyPress(int... keys) {
        for (int k : keys) {
            robot.keyPress(k);
        }
    }
    private void robotKeyRelease(int... keys) {
        for (int k : keys) {
            robot.keyRelease(k);
        }
    }
*/
