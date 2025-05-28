package org.example.webpages;

import org.example.base.Content;
import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AmnestyFeed extends PageObject {
    private static final Logger log = LoggerFactory.getLogger(AmnestyFeed.class);
    private static final String PAGE_URL="https://www.amnesty.org/en/search/kurdish";//"https://www.amnesty.org/en/search/?q=kurdish&ref=&year=2020&lang=en&sort=date";

    @FindBy(how = How.XPATH, using = "//*[@id=\"ccc-notify-accept\"]")
    @CacheLookup
    protected WebElement submit;

    @FindBy(how = How.XPATH, using = "//section[@aria-label='Search results']/article")
    @CacheLookup
    protected List<WebElement> articles;

    public AmnestyFeed(WebDriver driver){
        super(driver);
    }

    @Override
    protected void onLoad() {
        handleCloudflareChallenge();
        domIsReady("amnesty page dom is ready");
      //  waitFor(submit, "submit cookies")
        waitFor(articles, "amnesty page");
    }

    public List<Content> getArticles() throws InterruptedException {
        Thread.sleep(2L);
        submit.click();
        List<Content> contentList=new ArrayList<>();
        for (int i=0; i<3; i++) {
            WebElement article = articles.get(i);
            WebElement hez = article.findElement(By.className("post-title")).findElement(By.tagName("a"));
            String he = hez.getText();
            String be = article.findElement(By.className("post-excerpt")).getText();
            contentList.add(new Content(he, be, new ArrayList<>()));
            log.info("başlık ::=> " + he);
            log.info("konu ::=> " + be);
        }
        return contentList;
    }
    public static AmnestyFeed go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new AmnestyFeed(driver);
    }

    public List<Content> getDetails() throws InterruptedException, IOException {
        List<Content> contentList=new ArrayList<>();
        Actions actions=new Actions(driver);
        for (int i=0; i<3; i++) {
            WebElement article = articles.get(i);
            WebElement hez = article.findElement(By.className("post-title")).findElement(By.tagName("a"));
            log.info("getDetails -> "+hez.getAttribute("href"));
            actions.moveToElement(hez).click().perform();
            Thread.sleep(2000L);
            AmnestyDetails amnestyDetails= AmnestyDetails.go(driver);
            contentList.add(amnestyDetails.checkHtml());
            amnestyDetails.backCall();
            Thread.sleep(2000L);
        }

        return contentList;
    }
    // New method to handle Cloudflare challenge
    private void handleCloudflareChallenge() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45)); // Increased wait time for challenge

        try {
            // Wait for the Cloudflare iframe to be present
            // The @FindBy cloudflareIframe might not be immediately available if it's dynamic
            // Use a more robust locator for presence check.
          //  WebElement cfIframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//iframe[contains(@src, 'cloudflare.com/dist/') or @title='Widget containing a Cloudflare security challenge']"))); //
// Locate the Cloudflare iframe inside its shadow DOM using JavaScript
            WebElement cfIframe = (WebElement) ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript(
                            "const host = document.querySelector('cf-challenge');" +
                                    "const iframe = host && host.shadowRoot && host.shadowRoot.querySelector('iframe');" +
                                    "return iframe;"
                    );

            System.out.println("Cloudflare iframe detected. Switching to iframe...");
            driver.switchTo().frame(cfIframe); // Switch to the iframe
            log.info("Switched to Cloudflare iframe.");

            // Wait for the Shadow DOM host element to appear within the iframe.
            // This is a common pattern for Cloudflare's "I am human" challenges.
            // You MUST inspect the Cloudflare challenge on Amnesty.org to find the correct selector.
            // Common selectors could be by ID like 'challenge-stage', 'root', or a specific custom tag.
//            WebElement shadowHost = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input#challenge-response, div#challenge-root, #root-element-of-challenge, [id^='cf-chl-widget']")));
//            log.info("Found Shadow DOM host element: " + shadowHost.getTagName() + " " + shadowHost.getAttribute("id"));
            // Wait until the JS challenge completes (URL no longer contains chk_js)
            wait.until(d -> !d.getCurrentUrl().contains("/cdn-cgi/l/chk_js"));

            // Switch back to main document
          //  driver.switchTo().defaultContent();

        //    JavascriptExecutor js = (JavascriptExecutor) driver;

            // Get the Shadow DOM root
           // WebElement shadowRoot = (WebElement) js.executeScript("return arguments[0].shadowRoot", shadowHost);
        //    log.info("Accessed Shadow DOM root.");

            // Wait for the specific element that acts as the "checkbox" within the Shadow DOM
            // This is highly dependent on Cloudflare's current implementation.
            // Common patterns: an input with type="checkbox", a div/span with a specific class, or a button.
            // YOU NEED TO VERIFY THIS SELECTOR by inspecting the Shadow DOM content in your browser.
//            WebElement checkbox = wait.until(ExpectedConditions.visibilityOf((WebElement) js.executeScript(
//                    "return arguments[0].querySelector('input[type=\"checkbox\"], div.mark-checkbox, #cf-spinner-please-wait, span.ctp-checkbox-label, div.button-submit')", shadowRoot
//            )));
//            log.info("Found checkbox/challenge element within Shadow DOM. Attempting to click.");

            // Click the element using JavaScript for robustness
//            js.executeScript("arguments[0].click();", checkbox);
//            log.info("Clicked the Cloudflare challenge element.");

            // Wait for the challenge to resolve. This might involve waiting for the iframe to disappear,
            // or for a specific element on the main page to become visible.
//            wait.until(ExpectedConditions.or(
//                    ExpectedConditions.invisibilityOfElementLocated(By.xpath("//iframe[contains(@src, 'cloudflare.com/dist/') or @title='Widget containing a Cloudflare security challenge']")), //
//                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section[aria-label='Search results']")) // A reliable element on the main Amnesty page
//            ));
            log.info("Cloudflare challenge resolved or disappeared.");

        } catch (Exception e) {
            log.warn("Cloudflare challenge not detected or failed to handle: " + e.getMessage());
            // It's crucial to switch back to default content even if handling fails,
            // otherwise, subsequent actions will fail if they expect to be on the main page.
        } finally {
            // Always switch back to the default content, even if the challenge was not present
            // or if an exception occurred.
            driver.switchTo().defaultContent();
            log.info("Switched back to default content.");
        }
    }
}
