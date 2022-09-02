package org.example.webpages;

import org.apache.commons.io.FileUtils;
import org.example.base.PageObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GooPicsPage extends PageObject {
    private final String searchTerm;

    private static final String PAGE_URL="https://images.google.com";

    @FindBy(how = How.XPATH, using = "//div[@id='islrg']//img[@src]")
    protected List<WebElement> imgs;

    @FindBy(how = How.NAME, using = "q")
    protected WebElement imgInput;

    public GooPicsPage(WebDriver driver, String searchTerm){
        super(driver);
        this.searchTerm=searchTerm;
    }

    public void downloadImgs(){
        imgInput.sendKeys(searchTerm);
        imgInput.submit();
        // wait until the google page shows the result
        waitFor(imgs, "images results");
        for (int i = 1; i <= 3; i++) { // imgs.size()
           File image = imgs.get(i).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(image, new File("/tmp/test-"+searchTerm.substring(0,5)+'-'+i+".png"));
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
            images.add("<img src=\"data:image/png;base64,"+imgs.get(i).getScreenshotAs(OutputType.BASE64)+"\" width=\"700\" height=\"427\" />");
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
}
