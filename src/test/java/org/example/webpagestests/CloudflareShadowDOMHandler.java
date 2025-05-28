package org.example.webpagestests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class CloudflareShadowDOMHandler {

    public static void handleCloudflareChallenge(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Increased wait time

        try {
            // Step 1: Detect the presence of the Cloudflare iframe.
            // Cloudflare often uses an iframe with a specific ID or class.
            // You might need to find the iframe that contains the challenge.
            // Example: Assuming Cloudflare iframe has id "cf-challenge-iframe"
           // WebElement cloudflareIframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//iframe[contains(@src, 'cloudflare.com/dist/')] | //iframe[contains(@id, 'cf-challenge-iframe')] | //iframe[contains(@title, 'Cloudflare challenge')]")));
            // Locate the Cloudflare iframe inside its shadow DOM using JavaScript
            WebElement cfIframe = (WebElement) ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript(
                            "const host = document.querySelector('cf-challenge');" +
                                    "const iframe = host && host.shadowRoot && host.shadowRoot.querySelector('iframe');" +
                                    "return iframe;"
                    );

            // Step 2: Switch to the Cloudflare iframe.
            driver.switchTo().frame(cfIframe);
            System.out.println("Switched to Cloudflare iframe.");

            // Step 3: Wait for the Shadow DOM host element to appear within the iframe.
            // This is the element that "hosts" the Shadow DOM.
            // You need to inspect the Cloudflare challenge to find the correct selector for this host.
            // Common selectors could be by ID, class, or data-attribute.
            // Example: Assuming the Shadow DOM host has an ID like "challenge-root" or a specific tag name.
            WebElement shadowHost = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div#challenge-root, #root-element-of-challenge, custom-challenge-component")));
            System.out.println("Found Shadow DOM host element.");

            // Step 4: Get the Shadow DOM root.
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement shadowRoot = (WebElement) js.executeScript("return arguments[0].shadowRoot", shadowHost);
            System.out.println("Accessed Shadow DOM root.");

            // Step 5: Locate the checkbox within the Shadow DOM.
            // This is the tricky part. The checkbox might be a custom element,
            // or an actual input within the Shadow DOM. You need to identify its selector.
            // Example: If the checkbox is a simple input type="checkbox" inside the shadow DOM
            // or a div/span that acts as a clickable area.
            // You might need to explore nested shadow DOMs if they exist.
            WebElement checkbox = wait.until(ExpectedConditions.visibilityOf((WebElement) js.executeScript(
                    "return arguments[0].querySelector('input[type=\"checkbox\"], div.mark-checkbox, span.custom-checkbox-button')", shadowRoot
            )));
            System.out.println("Found checkbox element within Shadow DOM.");

            // Step 6: Click the checkbox.
            // Use JavaScript click if direct Selenium click fails, which is common with Shadow DOM.
            js.executeScript("arguments[0].click();", checkbox);
            System.out.println("Clicked the Cloudflare challenge checkbox.");

            // Step 7: Wait for the challenge to resolve and switch back to the default content.
            // You might need to wait for a success message, a redirection, or the iframe to disappear.
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("cf-challenge-iframe"))); // Or similar
            driver.switchTo().defaultContent();
            System.out.println("Challenge resolved. Switched back to default content.");

        } catch (Exception e) {
            System.err.println("Failed to handle Cloudflare challenge: " + e.getMessage());
            // Optionally, switch back to default content even on failure to avoid issues
            try {
                driver.switchTo().defaultContent();
            } catch (Exception ex) {
                System.err.println("Could not switch back to default content: " + ex.getMessage());
            }
            throw e; // Re-throw the exception to indicate failure
        }
    }

    // You would call this method from your @Test method in DevTest.java
    // For example:
     @Test
     public void applyAmnestyUpload() throws InterruptedException, IOException {
         ChromeDriver driver = UndetectedChromeSetup.createUndetectedChrome();
         driver.get("https://amnesty.org/en/search");
         CloudflareShadowDOMHandler.handleCloudflareChallenge(driver); // Call this here
         // ... rest of your test ...
     }
}