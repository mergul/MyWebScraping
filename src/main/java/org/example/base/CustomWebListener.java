package org.example.base;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class CustomWebListener implements WebDriverListener {
    private static final Logger log = LoggerFactory.getLogger(WebEventListener.class);

    @Override
    public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args) {
        log.info("beforeAnyWebDriverCall");
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        log.info("after get");
    }

    @Override
    public void beforeTo(WebDriver.Navigation navigation, @NotNull String url) {
        log.info("Before navigating to: '" + url + "'");
    }
    public void beforeFindElement(WebDriver driver, @NotNull By locator) {
        log.info("Trying to find Element By : " + locator.toString());
    }

    public void afterFindElement(WebDriver driver, @NotNull By locator, @NotNull WebElement result) {
        log.info("Found Element By : " + locator.toString());
    }
    public void beforeFindElements(WebDriver driver, @NotNull By locator) {
        log.info("Trying to find Elements By : " + locator.toString());
    }

    public void afterFindElements(WebDriver driver, @NotNull By locator, @NotNull List<WebElement> result) {
        log.info("Found Elements By : " + locator.toString() + " list size -> "+ result.size());
    }

    @Override
    public void afterTo(WebDriver.Navigation navigation, @NotNull String url) {
        log.info("Navigated to:'" + url + "'");
    }

    @Override
    public void beforeClick(@NotNull WebElement element) {
        log.info("Trying to click on: " + element.toString());
    }
    @Override
    public void afterClick(@NotNull WebElement element) {
        log.info("Clicked on: " + element.toString());
    }

    @Override
    public  void beforeSendKeys(@NotNull WebElement element, CharSequence... keysToSend) {
        log.info("Value of the:" + Arrays.toString(keysToSend) + " before any changes made");
    }

    @Override
    public  void afterSendKeys(@NotNull WebElement element, CharSequence... keysToSend) {
        log.info("Element value changed to: " + Arrays.toString(keysToSend));
    }

    @Override
    public void beforeExecuteAsyncScript(WebDriver driver, @NotNull String script, Object[] args) {
        log.info("beforeExecuteAsyncScript -> " + script);
    }

    @Override
    public void afterExecuteAsyncScript(WebDriver driver, @NotNull String script, Object[] args, Object result) {
        log.info("afterExecuteAsyncScript -> " + script);
    }

    public void onError(Object target, Method method, Object[] args, InvocationTargetException error) {
        log.error("Error while calling method: " + method.getName() + " - " + error.getMessage());
    }
    @Override
    public void beforeGetText(WebElement element){
        log.info("beforeGetText");
    }
    @Override
    public void afterGetText(WebElement element, String result){
        log.info("afterGetText -> " + result);
    }
}