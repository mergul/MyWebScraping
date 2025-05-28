package org.example.webpagestests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.base.Content;
import org.example.base.WebEventListener;
import org.example.webpages.AmnestyFeed;
import org.example.webpages.GTranslate;
import org.example.webpages.GooPicsPage;
import org.example.webpages.TranslatedPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public class CloudflareSeleniumTest {
    private WebDriver driver;
    private WebEventListener eventListener;

//    @Before
//    public void setup() {
//        // 1) Setup ChromeDriver via WebDriverManager
//        WebDriverManager.chromedriver().setup();
//
//        // 2) Configure ChromeOptions to look less automated
//        ChromeOptions options = new ChromeOptions();
//        options.setExperimentalOption("useAutomationExtension", false);
//        options.addArguments("--disable-blink-features=AutomationControlled");
//        options.addArguments("start-maximized");
//        // (optional) set a realistic desktop user-agent
//        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
//                + "AppleWebKit/537.36 (KHTML, like Gecko) "
//                + "Chrome/112.0.0.0 Safari/537.36");
//        // Add these to your existing ChromeOptions
//        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--disable-gpu");
//        options.addArguments("--remote-debugging-port=9222");
//
//// More realistic browser fingerprint
//        options.addArguments("--disable-features=VizDisplayCompositor");
//        options.setExperimentalOption("excludeSwitches",
//                Arrays.asList("enable-automation", "enable-logging"));
//        // Translation preferences
//        Map<String, Object> prefs = new HashMap<>();
//        Map<String, Object> langs = new HashMap<>();
//        langs.put("tr", "en");
//        langs.put("fr", "en");
//        langs.put("de", "en");
//        prefs.put("translate_whitelists", langs);
//        prefs.put("translate", ImmutableMap.of("enabled", true));
//        // prefs.put("intl.accept_languages", "tr");
//        options.setExperimentalOption("prefs", prefs);
//
//        // Logging preferences
//        options.addArguments("--enable-logging");
//
//        // Instantiate WebDriver
//        driver = new ChromeDriver(options);
//
//        // Browser window and timeouts
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//    }
@Before
public void setup() {
    WebDriverManager.chromedriver().setup();

    ChromeOptions options = new ChromeOptions();

    // Method 1: Properly quote the path with spaces
    String profilePath = "/home/mesut/.config/google-chrome/Default";
    options.addArguments("--user-data-dir=\"" + profilePath + "\"");

    // Alternative Method 2: Use escaped path
    // options.addArguments("--user-data-dir=/home/mesut/.config/google-chrome/Profile\\ 1");

    // Alternative Method 3: Use File.separator and proper escaping
    // String profilePath = "/home/mesut/.config/google-chrome" + File.separator + "Profile 1";
    // options.addArguments("--user-data-dir=" + profilePath);

    // Enhanced stealth options
    options.setExperimentalOption("excludeSwitches",
            Arrays.asList("enable-automation", "enable-logging"));
    options.setExperimentalOption("useAutomationExtension", false);
    options.addArguments("--disable-blink-features=AutomationControlled");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-gpu");
    options.addArguments("--disable-features=VizDisplayCompositor");

    // Window and display settings
    options.addArguments("--window-size=1920,1080");
    options.addArguments("--start-maximized");

    // Important: Specify profile directory (subfolder within user-data-dir)
    // Chrome creates multiple profiles as "Profile 1", "Profile 2", etc.
    // But the actual profile folder name might be different
    options.addArguments("--profile-directory=Default");

    // Enhanced preferences
    Map<String, Object> prefs = new HashMap<>();
    prefs.put("profile.default_content_setting_values.notifications", 2);
    prefs.put("profile.default_content_settings.popups", 0);
    prefs.put("profile.managed_default_content_settings.images", 1);
    options.setExperimentalOption("prefs", prefs);

    try {
        driver = new ChromeDriver(options);

        // Remove webdriver property
        ((JavascriptExecutor) driver).executeScript(
                "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
        );

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        System.out.println("Chrome started with profile: " + profilePath);

    } catch (Exception e) {
        System.out.println("Failed to start with profile, trying without...");
        System.out.println("Error: " + e.getMessage());

        // Fallback: Start without profile
        startWithoutProfile();
    }
}

    private void startWithoutProfile() {
        ChromeOptions options = new ChromeOptions();

        // All the stealth options without profile
        options.setExperimentalOption("excludeSwitches",
                Arrays.asList("enable-automation", "enable-logging"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");

        // Use a more realistic user agent
        options.addArguments("user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);

        // Remove webdriver property
        ((JavascriptExecutor) driver).executeScript(
                "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
        );

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        System.out.println("Chrome started without profile");
    }

    // Method to verify and find the correct profile path
    public static void findChromeProfiles() {
        String baseDir = "/home/mesut/.config/google-chrome";
        File chromeDir = new File(baseDir);

        System.out.println("Looking for Chrome profiles in: " + baseDir);

        if (chromeDir.exists() && chromeDir.isDirectory()) {
            File[] profiles = chromeDir.listFiles((dir, name) ->
                    name.startsWith("Profile") || name.equals("Default"));

            if (profiles != null) {
                System.out.println("Found profiles:");
                for (File profile : profiles) {
                    System.out.println("  - " + profile.getName() + " (Full path: " + profile.getAbsolutePath() + ")");

                    // Check if profile has essential Chrome files
                    File prefsFile = new File(profile, "Preferences");
                    File historyFile = new File(profile, "History");
                    System.out.println("    Has Preferences: " + prefsFile.exists());
                    System.out.println("    Has History: " + historyFile.exists());
                }
            } else {
                System.out.println("No profiles found");
            }
        } else {
            System.out.println("Chrome directory not found: " + baseDir);
        }
    }

    // Alternative setup method using Path class for better handling
    @Before
    public void setupWithPath() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Use Java Path for better handling of spaces and special characters
        Path profilePath = Paths.get("/home/mesut/.config/google-chrome/Default");

        if (Files.exists(profilePath)) {
            System.out.println("Profile exists: " + profilePath.toString());
            options.addArguments("--user-data-dir=" + profilePath.toString());
            options.addArguments("--profile-directory=Default");
        } else {
            System.out.println("Profile not found, checking alternatives...");

            // Try to find the correct profile
            Path baseDir = Paths.get("/home/mesut/.config/google-chrome");
            try {
                Files.list(baseDir)
                        .filter(path -> path.getFileName().toString().startsWith("Default"))
                        .forEach(path -> System.out.println("Found profile: " + path.getFileName()));
            } catch (Exception e) {
                System.out.println("Could not list profiles: " + e.getMessage());
            }
        }

        // Rest of the configuration...
        options.setExperimentalOption("excludeSwitches",
                Arrays.asList("enable-automation", "enable-logging"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new ChromeDriver(options);

        System.out.println("Driver started successfully");
    }

    // Test method to verify profile is working
    @Test
    public void testProfileLoading() {
        // Navigate to a page that shows browser info
        driver.get("chrome://version/");

        try {
            Thread.sleep(2000);

            // Get profile path from Chrome's version page
            String pageSource = driver.getPageSource();
            System.out.println("Chrome version page loaded");

            if (pageSource.contains("Profile Path")) {
                System.out.println("✓ Profile information found in version page");
            } else {
                System.out.println("⚠ Profile information not found");
            }

        } catch (Exception e) {
            System.out.println("Error checking profile: " + e.getMessage());
        }

        // Test navigation to see if cookies/history are preserved
        driver.get("https://mail.google.com");
        System.out.println("Navigation test completed");
    }
    @Test
    public void testAdvancedCloudflareBypass() throws InterruptedException {
        testProfileLoading();
        Thread.sleep(15000);
        String targetUrl = "https://amnesty.org/en/search";

        try {
            // Method 1: Direct navigation with extended wait
            System.out.println("=== Method 1: Direct Navigation ===");
            if (attemptDirectNavigation(targetUrl)) {
                System.out.println("✓ Direct navigation successful!");
                return;
            }

            // Method 2: Refresh and wait approach
            System.out.println("=== Method 2: Refresh Approach ===");
            if (attemptRefreshMethod(targetUrl)) {
                System.out.println("✓ Refresh method successful!");
                return;
            }

            // Method 3: JavaScript-based bypass
            System.out.println("=== Method 3: JavaScript Bypass ===");
            if (attemptJavaScriptBypass()) {
                System.out.println("✓ JavaScript bypass successful!");
                return;
            }

            // Method 4: New window approach
            System.out.println("=== Method 4: New Window Approach ===");
            if (attemptNewWindowMethod(targetUrl)) {
                System.out.println("✓ New window approach successful!");
                return;
            }

            System.out.println("❌ All bypass methods failed");

        } catch (Exception e) {
            System.out.println("Test execution failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("\n=== Final Status ===");
            System.out.println("URL: " + driver.getCurrentUrl());
            System.out.println("Title: " + driver.getTitle());
            analyzePageState();
        }
    }

    private boolean attemptDirectNavigation(String url) {
        try {
            System.out.println("Navigating to: " + url);
            driver.get(url);

            // Wait longer with detailed progress
            return waitForBypassWithProgress(180); // 3 minutes

        } catch (Exception e) {
            System.out.println("Direct navigation failed: " + e.getMessage());
            return false;
        }
    }

    private boolean attemptRefreshMethod(String url) {
        try {
            // Sometimes a refresh helps
            for (int i = 0; i < 3; i++) {
                System.out.println("Refresh attempt " + (i + 1));
                driver.navigate().refresh();
                Thread.sleep(5000);

                if (waitForBypassWithProgress(60)) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            System.out.println("Refresh method failed: " + e.getMessage());
            return false;
        }
    }

    private boolean attemptJavaScriptBypass() {
        try {
            System.out.println("Attempting JavaScript-based interactions...");

            // Simulate human-like behavior
            ((JavascriptExecutor) driver).executeScript(
                    "window.scrollTo(0, 100);" +
                            "setTimeout(() => window.scrollTo(0, 0), 1000);"
            );

            Thread.sleep(2000);

            // Try to trigger any hidden elements or events
            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll('*').forEach(el => {" +
                            "  if (el.onclick || el.addEventListener) {" +
                            "    try { el.click(); } catch(e) {}" +
                            "  }" +
                            "});"
            );

            Thread.sleep(3000);

            // Check if challenge element exists and try to interact
            Boolean challengeExists = (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "return document.querySelector('cf-challenge') !== null ||" +
                            "       document.querySelector('[data-cf-challenge]') !== null ||" +
                            "       document.querySelector('.cf-challenge') !== null"
            );

            if (challengeExists) {
                System.out.println("Challenge element found, attempting interaction...");
                ((JavascriptExecutor) driver).executeScript(
                        "const challenges = [" +
                                "  document.querySelector('cf-challenge')," +
                                "  document.querySelector('[data-cf-challenge]')," +
                                "  document.querySelector('.cf-challenge')" +
                                "].filter(el => el);" +
                                "challenges.forEach(el => {" +
                                "  try { el.click(); } catch(e) {}" +
                                "  try { el.focus(); } catch(e) {}" +
                                "});"
                );
            }

            return waitForBypassWithProgress(90);

        } catch (Exception e) {
            System.out.println("JavaScript bypass failed: " + e.getMessage());
            return false;
        }
    }

    private boolean attemptNewWindowMethod(String url) {
        try {
            System.out.println("Opening new window...");

            // Open new window
            ((JavascriptExecutor) driver).executeScript("window.open('');");

            // Switch to new window
            Set<String> windows = driver.getWindowHandles();
            for (String window : windows) {
                driver.switchTo().window(window);
                if (!driver.getCurrentUrl().equals(url)) {
                    break;
                }
            }

            // Navigate in new window
            driver.get(url);

            return waitForBypassWithProgress(120);

        } catch (Exception e) {
            System.out.println("New window method failed: " + e.getMessage());
            return false;
        }
    }

    private boolean waitForBypassWithProgress(int timeoutSeconds) {
        System.out.println("Waiting for bypass completion (timeout: " + timeoutSeconds + "s)...");

        long startTime = System.currentTimeMillis();
        long timeoutMs = timeoutSeconds * 1000L;

        String lastTitle = "";
        int sameCount = 0;

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            try {
                String currentTitle = driver.getTitle();
                String currentUrl = driver.getCurrentUrl();

                // Check if bypass is complete
                if (isBypassComplete(currentTitle, currentUrl)) {
                    System.out.println("✓ Bypass completed successfully!");
                    Thread.sleep(2000); // Stabilization wait
                    return true;
                }

                // Log progress every 10 seconds or when title changes
                if (!currentTitle.equals(lastTitle)) {
                    System.out.println("Progress: Title = '" + currentTitle + "', URL = " + currentUrl);
                    lastTitle = currentTitle;
                    sameCount = 0;
                } else {
                    sameCount++;
                    if (sameCount % 10 == 0) { // Every 10 seconds
                        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                        System.out.println("Still waiting... (" + elapsed + "s elapsed)");
                    }
                }

                Thread.sleep(1000);

            } catch (Exception e) {
                System.out.println("Error during wait: " + e.getMessage());
                return false;
            }
        }

        System.out.println("❌ Timeout reached");
        return false;
    }

    private boolean isBypassComplete(String title, String url) {
        // More comprehensive bypass detection
        try {
            boolean titleOk = !title.contains("Just a moment") &&
                    !title.contains("Please wait") &&
                    !title.contains("Checking your browser") &&
                    !title.equals("");

            boolean urlOk = !url.contains("/cdn-cgi/") &&
                    !url.contains("__cf_chl_jschl_tk__") &&
                    !url.contains("cf_challenge");

            if (titleOk && urlOk) {
                // Additional verification - check page source
                String pageSource = driver.getPageSource();
                boolean noCloudflareIndicators = !pageSource.contains("cf-challenge") &&
                        !pageSource.contains("DDoS protection") &&
                        !pageSource.contains("ray ID") &&
                        !pageSource.contains("__cf_chl_rt_tk");

                boolean hasContent = pageSource.contains("amnesty") ||
                        pageSource.contains("search") ||
                        pageSource.length() > 10000;

                return noCloudflareIndicators && hasContent;
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    private void analyzePageState() {
        try {
            System.out.println("\n=== Page Analysis ===");

            String pageSource = driver.getPageSource();
            System.out.println("Page source length: " + pageSource.length());

            // Check for Cloudflare indicators
            boolean hasCfChallenge = pageSource.contains("cf-challenge");
            boolean hasDdosProtection = pageSource.contains("DDoS protection");
            boolean hasRayId = pageSource.contains("ray ID");
            boolean hasJustMoment = pageSource.contains("Just a moment");

            System.out.println("Cloudflare indicators:");
            System.out.println("  - cf-challenge: " + hasCfChallenge);
            System.out.println("  - DDoS protection: " + hasDdosProtection);
            System.out.println("  - Ray ID: " + hasRayId);
            System.out.println("  - 'Just a moment': " + hasJustMoment);

            // Check for actual content
            boolean hasAmnestyContent = pageSource.contains("amnesty");
            boolean hasSearchContent = pageSource.contains("search");
            System.out.println("Content indicators:");
            System.out.println("  - Amnesty content: " + hasAmnestyContent);
            System.out.println("  - Search content: " + hasSearchContent);

            // Check JavaScript execution
            Object jsTest = ((JavascriptExecutor) driver).executeScript("return 'JS_WORKING'");
            System.out.println("JavaScript execution: " + (jsTest != null ? "Working" : "Failed"));

        } catch (Exception e) {
            System.out.println("Page analysis failed: " + e.getMessage());
        }
    }
    @Test
    public void testOriCloudflareBypass() {
        // Navigate to CF-protected page
        String url = "https://amnesty.org/en/search";
        driver.get(url);

        // Wait utility
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Locate the Cloudflare iframe inside its shadow DOM using JavaScript
        WebElement cfIframe = (WebElement) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript(
                        "const host = document.querySelector('cf-challenge');" +
                                "const iframe = host && host.shadowRoot && host.shadowRoot.querySelector('iframe');" +
                                "return iframe;"
                );

        // Switch into the CF challenge iframe
        driver.switchTo().frame(cfIframe);

        // Wait until the JS challenge completes (URL no longer contains chk_js)
        wait.until(d -> !d.getCurrentUrl().contains("/cdn-cgi/l/chk_js"));

        // Switch back to main document
        driver.switchTo().defaultContent();

        // Assert or log final page
        System.out.println("Bypass complete: " + driver.getCurrentUrl());
    }

    @Test
    public void testWithManualCaptchaSolve() {
        // 1) Navigate to the page containing the Google reCAPTCHA
        String url = "https://example.com/page-with-recaptcha";
        driver.get(url);

        // 2) Wait for the reCAPTCHA widget to render
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        // (adjust selector if needed)
        wait.until(d -> ((JavascriptExecutor)d)
                .executeScript("return document.querySelector('.g-recaptcha') != null"));

        // 3) Pause and prompt user to solve CAPTCHA
        System.out.println("=== CAPTCHA DETECTED ===");
        System.out.println("Please solve the CAPTCHA in the browser window,");
        System.out.println("then come back here and press ENTER to continue...");
        new Scanner(System.in).nextLine();

        // 4) After user presses ENTER, you can continue your assertions
        //    e.g. wait for some element on the post-CAPTCHA page:
        wait.until(d -> ((JavascriptExecutor)d)
                .executeScript("return document.querySelector('#protected-content') != null"));

        System.out.println("CAPTCHA solved. Page title is: " + driver.getTitle());
    }
    @Test
    public void applyAmnestyUpload() throws InterruptedException, IOException {
        try {
            AmnestyFeed amnesty = AmnestyFeed.go(driver);
            List<Content> articles = amnesty.getArticles();
            List<Content> details = amnesty.getDetails();
            Map<Integer, List<String>> listMap = new HashMap<Integer, List<String>>();
            for (int i = 0; i < 3; i++) {
                Content article = articles.get(i);
                Content detail = details.get(i);
                String con = detail.getInner();
                String cont = article.getInner();
                StringBuilder translated = new StringBuilder();
                String txt = article.getTitle() + " <> " + cont + " <> " + con;
                String[] stooges = txt.split("(?<=\\G.{4999})");
                for (String myTxt : stooges) {
                    TranslatedPage translatedPage = GTranslate.go(driver, "en", "tr", myTxt);
                    translated.append(translatedPage.getTextOf());
                    Thread.sleep(2L);
                }
                article.setTranslated(String.valueOf(translated));
                GooPicsPage picsPage = GooPicsPage.go(driver, article.getTitle());
                List<String> allHref = picsPage.findImagesBase64(article.getTitle().substring(0, Math.min(article.getTitle().length(), 20)).replace("/", "").trim());
                List<String> externalUrls = new ArrayList<>(Collections.unmodifiableList(picsPage.findMyRawImages()));
                listMap.put(i, externalUrls);
                article.getObjects().addAll(Collections.unmodifiableList(allHref));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
