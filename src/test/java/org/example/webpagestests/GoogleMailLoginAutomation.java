package org.example.webpagestests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class GoogleMailLoginAutomation {

    public static void main(String[] args) {
        // --- Configuration ---
        // Start with Google's main sign-in page, which will redirect to Gmail upon success
        String GOOGLE_LOGIN_URL = "https://accounts.google.com/signin/v2/identifier";
        String YOUR_EMAIL = "your_email@gmail.com"; // <<<< REPLACE WITH YOUR GMAIL EMAIL
        String YOUR_PASSWORD = "your_strong_password"; // <<<< REPLACE WITH YOUR GMAIL PASSWORD

        // --- Selenium Setup ---
        // Automatically set up ChromeDriver based on your Chrome browser version
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // Maximize browser window
        // options.addArguments("--headless"); // Uncomment to run in headless mode (no visible browser UI)

        WebDriver driver = new ChromeDriver(options);

        // Define a WebDriverWait instance with a timeout
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Increased timeout for Google

        try {
            driver.get(GOOGLE_LOGIN_URL);
            System.out.println("Navigated to Google Sign-in: " + GOOGLE_LOGIN_URL);

            // --- Step 1: Wait for and locate the email/identifier input field ---
            System.out.println("Waiting for email/identifier input field...");
            WebElement emailField = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.id("identifierId"))
            );
            System.out.println("Email input field found.");
            emailField.sendKeys(YOUR_EMAIL);
            System.out.println("Entered email.");

            // --- Step 2: Wait for and locate the "Next" button for email and click it ---
            System.out.println("Waiting for 'Next' button (email step)...");
            WebElement emailNextButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("identifierNext"))
            );
            System.out.println("Email 'Next' button found and clickable.");
            emailNextButton.click();
            System.out.println("Clicked email 'Next' button.");

            // --- Step 3: Wait for and locate the password input field ---
            System.out.println("Waiting for password input field...");
            WebElement passwordField = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.name("password"))
            );
            System.out.println("Password input field found.");
            passwordField.sendKeys(YOUR_PASSWORD);
            System.out.println("Entered password."); // Avoid printing password for security

            // --- Step 4: Wait for and locate the "Next" button for password and click it ---
            System.out.println("Waiting for 'Next' button (password step)...");
            WebElement passwordNextButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("passwordNext"))
            );
            System.out.println("Password 'Next' button found and clickable.");
            passwordNextButton.click();
            System.out.println("Clicked password 'Next' button.");

            // --- Step 5: Optional - Wait for successful login (e.g., redirection to Gmail inbox) ---
            System.out.println("Waiting for redirection to Gmail inbox...");
            // You can wait for an element that is unique to the Gmail inbox,
            // or simply wait for the URL to change to mail.google.com
            wait.until(ExpectedConditions.urlContains("mail.google.com"));
            System.out.println("Successfully logged in to Gmail!");
            System.out.println("Current URL: " + driver.getCurrentUrl());

            // You can now interact with Gmail elements if needed,
            // but be prepared for potential security challenges.
            // Example: Wait for the compose button (if it's present and stable)
            // wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[role='button'][gh='cm']")));


            // Keep the browser open for observation for a few seconds
            Thread.sleep(10000); // 10 seconds to observe

        } catch (Exception e) {
            System.err.println("An error occurred during Google Mail login: " + e.getMessage());
            e.printStackTrace();
            // Optional: Save a screenshot on error
            // File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            // try {
            //     FileUtils.copyFile(screenshotFile, new File("google_login_error.png"));
            //     System.out.println("Screenshot saved: google_login_error.png");
            // } catch (IOException ioException) {
            //     System.err.println("Failed to save screenshot: " + ioException.getMessage());
            // }
        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("Browser closed.");
            }
        }
    }
}
