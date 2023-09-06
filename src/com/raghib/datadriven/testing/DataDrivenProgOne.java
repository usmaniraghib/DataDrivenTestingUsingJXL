package com.raghib.datadriven.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.raghib.selenium.BaseClass;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/*
 * JXL library doesnot support .csv and .xslx formats, which is the format used by Excel-2010. 
 * Hence, use Excel 97-2003 which is .xls foramatted and is supported by JXL library. 
 * or else if you want to use excel-2010, use APACHE POI(XSSFWorkbooks) instead of JXL. 
 * For using .csv format, google for CSVReader libraries.
 */

public class DataDrivenProgOne extends BaseClass {

	public static String browserName = "chrome";
	public static String browserVersion = "116";
	public static String url = "https://www.google.com/";

	WebDriver webDriverObj = null;
	WebDriverWait webDriverWaitObj = null;
	WebElement webElementObj = null;

	String excelFilePath = System.getProperty("user.dir");
	File fileObj = null;
	FileInputStream fis = null;

	@BeforeClass()
	public void browserOpen() {
		// Chrome Browser
		webDriverObj = BaseClass.getDriver(browserName, browserVersion);
		webDriverObj.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		webDriverObj.manage().window().maximize();
		webDriverObj.get(url);
	}

	@AfterClass()
	public void browserclose() {
		try {
			if (webDriverObj != null) {
				System.out.println("Driver Need to Close");
				BaseClass.quitDriver();
			} else {
				System.out.println("Driver Still Open");
			}
		} catch (Exception e) {
			System.out.println("Nothing to do with it");
		} finally {
			System.out.println("Finally Block - To close the driver");
			BaseClass.quitDriver();
		}
	}

	@Test(dataProvider = "testData-1")
	public void testOne(String val1, String val2) {
		System.out.println("Operation Start from testOne method");
		webDriverObj.findElement(By.linkText("Sign in")).click();
		webDriverObj.findElement(By.xpath("//input[@type='email']")).sendKeys(val1);
		webDriverObj.findElement(By.xpath(
				"//button[@class=\"VfPpkd-LgbsSe VfPpkd-LgbsSe-OWXEXe-k8QpJ VfPpkd-LgbsSe-OWXEXe-dgl2Hf nCP5yc AjY5Oe DuMIQc LQeN7 qIypjc TrZEUc lw1w4b\"]"))
				.click();
		
		/*PASSWORD IS NOT WORKING DUE TO SECURITY REASON IN GOOGLE GMAIL*/

//		Duration duration = Duration.ofSeconds(30);
//		webDriverWaitObj = new WebDriverWait(webDriverObj, duration);
//		webElementObj = webDriverWaitObj
//				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
//
//		webDriverObj.findElement(By.xpath("//input[@type='password']")).sendKeys(val2);
//		webDriverObj.findElement(By.xpath(
//				"//button[@class=\"VfPpkd-LgbsSe VfPpkd-LgbsSe-OWXEXe-k8QpJ VfPpkd-LgbsSe-OWXEXe-dgl2Hf nCP5yc AjY5Oe DuMIQc LQeN7 qIypjc TrZEUc lw1w4b\"]"))
//				.click();
		System.out.println("Operation End from testOne method");
	}

	@DataProvider(name = "testData-1")
	public Object[][] ReadInputDataFromExcel() throws BiffException, IOException {
		if (fileObj == null) {
			fileObj = new File(excelFilePath + "\\ExcelFile\\AutomationTestingInput_97-2003.xls");
		}
		Workbook workbookObj = Workbook.getWorkbook(fileObj);
		Sheet sheetObj = workbookObj.getSheet("Sheet1");

		int rowCounts = sheetObj.getRows();
		int columnCounts = sheetObj.getColumns();
		System.out.println("Rows Count :" + rowCounts);
		System.out.println("Column Count :" + columnCounts);

		String[][] sheetData = new String[rowCounts][columnCounts];
		System.out.print("Sheet Data : ");
		for (int rowLoop = 0; rowLoop < rowCounts; rowLoop++) {
			for (int columnLoop = 0; columnLoop < columnCounts; columnLoop++) {
				Cell cellObjects = sheetObj.getCell(columnLoop, rowLoop);
				sheetData[rowLoop][columnLoop] = cellObjects.getContents();
				System.out.print(sheetData[rowLoop][columnLoop] + "\t");
			}
			System.out.println(" ");
		}
		return sheetData;
	}
}