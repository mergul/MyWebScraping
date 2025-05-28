package org.example.webpages;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.base.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AtlasPage extends PageObject {
    private static final String PAGE_URL="https://atlasbig.com.tr/istanbulun-mahalleleri";

    @FindBy(how = How.XPATH, using = "//*[@class='table-responsive']//table[contains(@class, 'dataTable')]")
    @CacheLookup
    protected WebElement mitable;

    @FindBy(how = How.XPATH, using = "//*[@class='table-accordion']/a[@id='toggle-show-data-table']")
    @CacheLookup
    protected WebElement showMoreDiv;

    protected AtlasPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void onLoad() {
        waitFor(mitable, "AtlasPage");
    }
    public static AtlasPage go(WebDriver driver) {
        driver.get(PAGE_URL);
        return new AtlasPage(driver);
    }
    public void getMitable() {

        XSSFWorkbook wkb = new XSSFWorkbook();
        XSSFSheet sheet1 = wkb.createSheet("DataStorage");
        Actions actions = new Actions(driver);
        //  mitable.findElement(By.xpath("//div[@class='divTableBody']/div[contains(@class, 'contenantgraphemonde unpays')]"));

//        WebElement act=showMoreDiv.findElement(By.xpath("//a[@id, 'toggle-show-data-table']"));
        actions.moveToElement(showMoreDiv).click().perform();
        List<WebElement> rows_table=mitable.findElements(By.tagName("tr"));
        int rows_count = rows_table.size();
        System.out.println("Number of Rows " + rows_count);

        for (int row = 0; row < rows_count; row++) {

            XSSFRow excelRow = sheet1.createRow(row);
            if(row==0){
                List<WebElement> head_row = rows_table.get(row).findElements(By.tagName("th"));
                int Head_count = head_row .size();
                System.out.println("Number of Header cells In Row 0 are "+ Head_count);

                for(int i=0;i<Head_count;i++) {
                    XSSFCell excelCell = excelRow.createCell(i);
                    excelCell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    String celtext = head_row.get(i).getText();
                    excelCell.setCellValue(celtext);
                    System.out.println("Header in valuein column number " + i + " Is " + celtext);
                }

            } else {
                List<WebElement> Columns_row = rows_table.get(row).findElements(By.tagName("td"));
                int columns_count = Columns_row.size();
                System.out.println("Number of cells In Row " + row + " are "+ columns_count);

                for (int column = 0; column < columns_count; column++) {
                    XSSFCell excelCell = excelRow.createCell(column);
                    excelCell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    String celtext = Columns_row.get(column).getText();
                    excelCell.setCellValue(celtext);
                    System.out.println("Cell Value Of row number " + row+ " and column number " + column + " Is " + celtext);
                }

            }
            System.out.println("--------------------------------------------------");
        }
        try {
            FileOutputStream fos = new FileOutputStream("/home/mesut/Downloads/WebTableTOSpreedsheet.xls");
            fos.flush();
            wkb.write(fos);
            fos.close();
            System.out.println("Your excel file has been generated!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
