package org.example.webpages;


import org.example.base.Content;
import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class AmnestyDetails extends PageObject {
    private static final Logger log = LoggerFactory.getLogger(AmnestyDetails.class);

    @FindBy(how = How.XPATH, using = "//*[@id=\"main\"]//article[contains(@class, \"article-content\")]")
    @CacheLookup
    protected WebElement details;
    @FindBy(how = How.XPATH, using = "//*[@id=\"article-title\"]")
    @CacheLookup
    protected WebElement title;
//    @FindBy(how = How.XPATH, using = "//*[@id=\"main\"]//figure/img")
//    @CacheLookup
//    protected WebElement imageParent;
    public AmnestyDetails(WebDriver driver){
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(details, "amnesty page details");
//        domIsReady("amnesty page dom is ready");
    }
    public static AmnestyDetails go(WebDriver driver) {
        return new AmnestyDetails(driver);
    }
    public Content checkHtml() throws IOException {
//        String image=imageParent.getAttribute("src");
        List<WebElement> objs=details.findElements(By.tagName("object"));
        int height=700;
        String objContent="";
        if (!objs.isEmpty()) {
            WebElement object=objs.get(0);
            String myurl = object.getAttribute("data");
            PdfPage pdfPage=PdfPage.go(driver);
            objContent= pdfPage.getPdfContent(myurl);
            log.info("pdf content -> "+objContent);
//           height = objs.get(0).getRect().getDimension().height;
//           WebElement button = details.findElement(By.xpath("//a[contains(@class,\"btn--download\")]"));
//           button.click();
//           String href= button.getAttribute("href");
//           String[] urls= href.split("/");
//           String src="/tmp/"+ urls[urls.length - 1];
//           objContent = "<iframe src="+src+"  height="+height+" width='60%'></iframe>";
        }
        String con=details.getText();
        log.info("checkHtml -> "+con);
        return new Content(title.getText(), con+objContent, Collections.emptyList());
    }

    public void backCall() throws InterruptedException {
        driver.navigate().back();
        Thread.sleep(2000L);
    }
}
