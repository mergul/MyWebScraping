package org.example.webpages;

import org.apache.commons.io.FileUtils;
import org.example.base.PageObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import java.io.File;
import java.io.IOException;

public class KoronaPage extends PageObject {
    private static final String PAGE_URL="https://covid19.saglik.gov.tr";

    @FindBy(how = How.XPATH, using = "//*[@id='svg-turkiye-haritasi-tamamlanan']")
    @CacheLookup
    protected WebElement img;

    public KoronaPage(WebDriver driver){
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(img, "saglik sayfası");
    }

    public String getImage(){
       return  "<img src=\"data:image/png;base64,"+img.getScreenshotAs(OutputType.BASE64)+"\" width=\"700\" height=\"427\" />";
    }
    public String getFile(){
        return "data:image/png;base64,"+img.getScreenshotAs(OutputType.BASE64);
    }
    public static KoronaPage go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new KoronaPage(driver);
    }
    public void findImg(){
        File image = img.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(image, new File("/tmp/saglik.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
