package org.example.base;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class PageObject {
    protected WebDriver driver;

    protected PageObject(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
        onLoad();
    }

    /**
     * Called when the page is presumably about to load.
     * The implementation for a page should therefore first <b>wait</b> for elements the page contains,
     * and then <b>assert</b> that they have the correct content.
     */
    protected abstract void onLoad();

    /**
     * Wait for the specified element to be visible.
     *
     * @param locator The element locator
     * @param message The message to display on failure.
     */
    protected void waitFor(By locator, String message) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .withMessage(message)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitFor(WebElement element, String message) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .withMessage(message)
                .until(ExpectedConditions.elementToBeClickable(element));
    }
    protected void waitFor(List<WebElement> elements, String message) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .withMessage(message)
                .until(ExpectedConditions.visibilityOfAllElements(elements));
    }
    protected void domIsReady(String message){
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .withMessage(message)
                .until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").toString().equals("complete"));
    }
    protected void imgIsReady(WebElement element, String message){
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .withMessage(message)
                .until(webDriver -> element.getAttribute("src").startsWith("http"));
    }
    public boolean isElementPresent(WebElement element, By by) {
        return  !element.findElements(by).isEmpty();
    }
}
