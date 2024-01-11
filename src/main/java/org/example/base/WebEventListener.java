package org.example.base;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class WebEventListener extends AbstractWebDriverEventListener {
    private static final Logger log = LoggerFactory.getLogger(WebEventListener.class);

    public void beforeNavigateTo(String url, WebDriver driver) {
        log.info("Before navigating to: '" + url + "'");
    }

    public void afterNavigateTo(String url, WebDriver driver) {
        log.info("Navigated to:'" + url + "'");
    }

    public void beforeClickOn(@NotNull WebElement element, WebDriver driver) {
        log.info("Trying to click on: " + element.toString());
    }

    public void afterClickOn(@NotNull WebElement element, WebDriver driver) {
        log.info("Clicked on: " + element.toString());
    }

    public void beforeNavigateBack(WebDriver driver) {
        log.info("Navigating back to previous page");
    }

    public void afterNavigateBack(WebDriver driver) {
        log.info("Navigated back to previous page");
    }

    public void beforeNavigateForward(WebDriver driver) {
        log.info("Navigating forward to next page");
    }

    public void afterNavigateForward(WebDriver driver) {
        log.info("Navigated forward to next page");
    }

    public void onException(Throwable error, WebDriver driver) {
        log.info("Exception occured: " + error);
        try {
            TestUtil.takeScreenshotAtEndOfTest(driver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void beforeFindBy(@NotNull By by, WebElement element, WebDriver driver) {
        log.info("Trying to find Element By : " + by.toString());
    }

    public void afterFindBy(@NotNull By by, WebElement element, WebDriver driver) {
        log.info("Found Element By : " + by.toString());
    }

    /*
     * non overridden methods of WebListener class
     */
    public void beforeScript(String script, WebDriver driver) {
    }

    public void afterScript(String script, WebDriver driver) {
    }

    public void beforeAlertAccept(WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void afterAlertAccept(WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void afterAlertDismiss(WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void beforeAlertDismiss(WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void beforeNavigateRefresh(WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void afterNavigateRefresh(WebDriver driver) {
        // TODO Auto-generated method stub

    }

    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        log.info("Value of the:" + element.toString() + " before any changes made");
    }

    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        log.info("Element value changed to: " + element.toString());
    }

    public <X> void afterGetScreenshotAs(OutputType<X> arg0, @NotNull X arg1) {
        log.info("Trying to afterGetScreenshotAs: " + arg1.toString());
    }

    public void afterGetText(WebElement arg0, WebDriver arg1, String arg2) {
        // TODO Auto-generated method stub

    }

    public void afterSwitchToWindow(String arg0, WebDriver arg1) {
        // TODO Auto-generated method stub

    }

    public <X> void beforeGetScreenshotAs(@NotNull OutputType<X> arg0) {
        log.info("Trying to beforeGetScreenshotAs: " + arg0.toString());

    }

    public void beforeGetText(WebElement arg0, WebDriver arg1) {
        // TODO Auto-generated method stub

    }

    public void beforeSwitchToWindow(String arg0, WebDriver arg1) {
        // TODO Auto-generated method stub

    }

}
