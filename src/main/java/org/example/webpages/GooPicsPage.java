package org.example.webpages;

import org.apache.commons.io.FileUtils;
import org.example.base.PageObject;
import org.example.base.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GooPicsPage extends PageObject {
    private static final Logger log = LoggerFactory.getLogger(GooPicsPage.class);

    private final String searchTerm;

    private static final String PAGE_URL = "https://images.google.com";
    @FindBy(how = How.XPATH, using = "//*[@id='islrg']//a[@role='button']//img[contains(@class, 'rg_i')]")
    @CacheLookup
    protected List<WebElement> imgs;

    @FindBy(how = How.XPATH, using = "//div[@id='islrg']/div[@role='list']/div[@role='listitem']/a[@role='button']")
    @CacheLookup
    protected List<WebElement> anchors;

    @FindBy(how = How.XPATH, using = "//*[@id='islrg']/div[@role='list']/div[@role='listitem']")
    @CacheLookup
    protected List<WebElement> imgDivs;

    @FindBy(how = How.NAME, using = "q")
    @CacheLookup
    protected WebElement imgInput;

    @FindBy(how = How.XPATH, using = "//*[@id='Sva75c']")
    @CacheLookup
    protected WebElement anchorRef;

    public GooPicsPage(WebDriver driver, String searchTerm) {
        super(driver);
        this.searchTerm = searchTerm;
    }

    public void downloadImgs() {
        imgInput.sendKeys(searchTerm);
        imgInput.submit();
        // wait until the google page shows the result
        waitFor(imgs, "images results");
        for (int i = 1; i <= 3; i++) { // imgs.size()
            File image = imgs.get(i).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(image, new File("/tmp/test-" + searchTerm.substring(0, 5) + '-' + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> findImages() {
        List<String> images = new ArrayList<>();
        imgInput.sendKeys(searchTerm);
        imgInput.submit();
        // wait until the google page shows the result
        waitFor(imgs, "images results");
        for (int i = 1; i <= 3; i++) { // imgs.size()
            String base64 = imgs.get(i).getAttribute("src").split(",")[1];
            images.add("<img src=\"data:image/png;base64," + imgs.get(i).getScreenshotAs(OutputType.BASE64) + "\" width=\"700\" height=\"427\" />");
        }
        return images;
    }

    public List<String> findImagesBase64(String name) {
        List<String> images = new ArrayList<>();
        imgInput.sendKeys(searchTerm);
        imgInput.submit();
        // wait until the google page shows the result
        waitFor(imgDivs, "images results");
        for (int i = 1; i <= 3; i++) { // imgs.size()
            String base64 = imgDivs.get(i).findElement(By.xpath(".//a[@role='button']")).findElement(By.tagName("img")).getAttribute("src");
            String decoded = TestUtil.decodeToBase64(base64, name + "-" + i);
            images.add(decoded);
        }
        return images;
    }

    public List<String> findRawImages() throws InterruptedException {
        List<String> images = new ArrayList<>();
//        imgInput.sendKeys(searchTerm);
//        imgInput.submit();
        waitFor(anchors, "images results");
        Actions actions = new Actions(driver);
        for (int i = 0; i <= 5; i++) {
            WebElement imgButton = anchors.get(i);
            actions.moveToElement(imgButton).click().perform();
            Thread.sleep(2000L);
            List<WebElement> elements = anchorRef.findElements(By.xpath(".//div[@data-query='" + searchTerm + "']//c-wiz//a[@role='link']"));
            for (WebElement anchor : elements) {
                List<WebElement> myImgs = anchor.findElements(By.tagName("img"));
                if (!myImgs.isEmpty()) {
                    for (WebElement ima : myImgs) {
                        String srcImg = ima.getAttribute("src");
                        if (!srcImg.startsWith("data:"))
                            images.add(srcImg);
                    }
                }
            }

        }
        return images;
    }

    @Override
    protected void onLoad() {
        waitFor(imgInput, "search page");
    }

    public static GooPicsPage go(WebDriver driver, String searchTerm) {
        driver.get(PAGE_URL);
        return new GooPicsPage(driver, searchTerm);
    }
    public List<String> findMyRawImages() throws InterruptedException {
        List<String> images = new ArrayList<>();
//        imgInput.sendKeys(searchTerm);
//        imgInput.submit();
        waitFor(anchors, "images results");
        Actions actions = new Actions(driver);
        for (int i = 0; i <= 5; i++) {
            WebElement imgButton = anchors.get(i);
            actions.moveToElement(imgButton).click().perform();
            Thread.sleep(2000L);
            GooPicDetail gooPicDetail=GooPicDetail.go(driver);
            images.addAll(gooPicDetail.getImgRefs());
            gooPicDetail.backCall();
            Thread.sleep(2000L);
        }
        return images;
    }
}
