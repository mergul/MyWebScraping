package org.example.webpagestests;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class UndetectedChromeSetup {
    public static ChromeDriver createUndetectedChrome() {
        ChromeOptions options = new ChromeOptions();

        // Essential for bypassing Cloudflare detection
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        // Stealth options
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Real user agent
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        ChromeDriver driver = new ChromeDriver(options);

        // Remove webdriver property
        driver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

        // Add human-like behavior
        driver.executeScript("Object.defineProperty(navigator, 'plugins', {get: () => [1, 2, 3, 4, 5]})");
        driver.executeScript("Object.defineProperty(navigator, 'languages', {get: () => ['en-US', 'en']})");

        return driver;
    }
}