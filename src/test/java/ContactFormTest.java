import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;

import java.util.concurrent.TimeUnit;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.json.simple.JSONObject;

import jxl.Sheet;
import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.csv.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class ContactFormTest {
	  private WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();
	  private String finalScreenshot = null; 
          private JSONObject obj=new JSONObject();
	  @Before
	  public void setUp() throws Exception {
            String website = null;

            Reader in = new FileReader("config.csv");
            for (CSVRecord record : CSVFormat.DEFAULT.withHeader("url", "scrnshot").parse(in)) {
                website = record.get("url");
                finalScreenshot = record.get("scrnshot");
            }
	    baseUrl = website; 

            Workbook workbook = Workbook.getWorkbook(new File("name.xls"));
            Sheet sheet = workbook.getSheet(0);
            Cell cell1 = sheet.getCell(0,0);
            Cell cell2 = sheet.getCell(1,0);
            Cell cell3 = sheet.getCell(2,0);
            
            String firstName =  cell1.getContents();
            String lastName = cell2.getContents();
            String emailAddress = cell3.getContents(); 

            obj.put("first",firstName);
            obj.put("last",lastName);
            obj.put("email",emailAddress);

	    driver = new FirefoxDriver();
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  }
	  
	  
	  @Test
	  public void testSe() throws Exception {
            driver.get(baseUrl);
	    driver.findElement(By.cssSelector("a[href='/request-information.html']")).click();

	    WebElement titleDropDownListBox = driver.findElement(By.cssSelector(".form-select"));
	    Select clickThis = new Select(titleDropDownListBox);
	    clickThis.selectByVisibleText("Dr.");
	   
            String first = (String) obj.get("first"); 
            String last = (String) obj.get("last");
            String email = (String) obj.get("email");
 
	    driver.findElement(By.cssSelector("#input-480940116329100688")).sendKeys(first);
	    driver.findElement(By.id("input-480940116329100688-1")).sendKeys(last);
            driver.findElement(By.id("input-522619690687534722")).sendKeys(email);
            driver.findElement(By.id("input-583739259627992806")).sendKeys("415");
            driver.findElement(By.id("input-583739259627992806-1")).sendKeys("123");
            driver.findElement(By.id("input-583739259627992806-2")).sendKeys("1245");
            driver.findElement(By.cssSelector("span.form-radio-container > label")).click();
            driver.findElement(By.xpath(".//*[@class='form-radio-container']/*[contains(text(),'Email')]")).click();
            driver.findElement(By.xpath(".//*[@class='form-radio-container']/*[contains(text(),'Wine Tasting 360 Platform')]")).click();
            driver.findElement(By.xpath(".//*[@class='form-radio-container']/*[contains(text(),'Custom Solution')]")).click();
            driver.findElement(By.id("input-314362815774891191")).sendKeys("Tell Me More About Your Solution");
            //driver.findElement(By.linkText("Submit")).click();

            //String message = driver.findElement(By.id("591800553141919862-msg")).getText();
            //assertEquals(message, "Thank you. Your information has been submitted.");
	  }

	  @After
	  public void tearDown() throws Exception {
	    File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(finalScreenshot + ".png"));
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }

	  private boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }
	  
	  private boolean isAlertPresent() {
          try {
            driver.switchTo().alert();
            return true;
          } catch (NoAlertPresentException e) {
            return false;
          }
        }

        private String closeAlertAndGetItsText() {
          try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
              alert.accept();
            } else {
              alert.dismiss();
            }
            return alertText;
          } finally {
            acceptNextAlert = true;
          }
        }
      }
