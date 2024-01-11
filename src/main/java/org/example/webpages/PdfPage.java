package org.example.webpages;

import org.apache.pdfbox.io.*;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.example.base.PageObject;
import org.openqa.selenium.WebDriver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PdfPage extends PageObject {

    protected PdfPage(WebDriver driver) {
        super(driver);
    }

    public static PdfPage go(WebDriver driver) {
        return new PdfPage(driver);
    }

    public String getPdfContent(String url) throws IOException {

        URL pdfURL = new URL(url);
        InputStream is = pdfURL.openStream();
        BufferedInputStream bis = new BufferedInputStream(is);

        PDFParser pdfParser = new PDFParser(new RandomAccessReadBuffer(bis));
        PDDocument doc = pdfParser.parse();

        PDFTextStripper strip = new PDFTextStripper();
        strip.setStartPage(1);
        strip.setEndPage(2);
        String stripText = strip.getText(doc);
        doc.close();
        return stripText;
    }

    @Override
    protected void onLoad() {
        domIsReady("amnesty page dom is ready");
    }
}
