package org.example.webpages;

import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;

public class YoutubeEmbedPage extends PageObject {

    @FindBy(how = How.XPATH, using = "//yt-sharing-embed-renderer//yt-button-renderer/yt-button-shape/button")
    @CacheLookup
    protected WebElement embed_copy_button;

    @FindBy(how = How.XPATH, using = "//yt-third-party-share-target-section-renderer//yt-share-target-renderer[1]/button[@id='target']")
    @CacheLookup
    protected WebElement embed_button;

    @FindBy(how = How.XPATH, using = "//ytd-watch-metadata//*[@id=\"top-level-buttons-computed\"]/yt-button-view-model/button-view-model/button[@aria-label='Share']")
    @CacheLookup
    protected WebElement share_button;

    protected YoutubeEmbedPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnShareButton(WebDriver driver) {
        Actions actions = new Actions(driver);
        actions.moveToElement(share_button).click().perform();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }
    public void clickOnEmbedButton(WebDriver driver) {
        embed_button.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }
    private void clickOnCopyButton(WebDriver driver) {
        embed_copy_button.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }
    public String getEmbedCode() {
        clickOnShareButton(driver);
        clickOnEmbedButton(driver);
        clickOnCopyButton(driver);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();

        try {
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onLoad() {
        waitFor(share_button, "Youtube Page");
    }
    public static YoutubeEmbedPage press(WebDriver driver) {
        return new YoutubeEmbedPage(driver);
    }

    public static YoutubeEmbedPage go(WebDriver driver, String PAGE_URL) {
        String url="https://www.youtube.com"
                + PAGE_URL.replace(" ", "+");
        driver.get(url);
        return new YoutubeEmbedPage(driver);
    }
}
