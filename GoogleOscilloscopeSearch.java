package task01_google_oscilloscope;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GoogleOscilloscopeSearch {
	
	private WebDriver driver;
	private String baseURL;
	private String sendKeysWord;
	private String searchOnPage;
	private String screenshotPath;
	private String allPageText;
	
	@Before
	public void startTest(){																	//method that is executed before the test
		driver = new FirefoxDriver();															//start Firefox driver
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);						//implicitly wait
		driver.manage().window().setPosition(new Point(306, 10));								//set window position
		driver.manage().window().setSize(new Dimension(1050, 490));								//set window size
		baseURL = "http://google.com.ua";														//base URL for test
		sendKeysWord = "осциллограф";															//word, which will be entered in search line
		searchOnPage = "vit.ua";																//phrase, which will be searched for on google search result pages
		screenshotPath = "C:\\SELENIUM\\screenshots_Google_Oscilloscope_Search\\screen01.png";	//path to save screenshot here
	}
	
	@Test
	public void testOscilloscopeSearch(){														//method that executes the test
		driver.get(baseURL);																	//open base URL
		
		WebElement googleSearchLine = driver.findElement(By.cssSelector("#lst-ib"));			//locating the search line
		googleSearchLine.clear();
		googleSearchLine.sendKeys(sendKeysWord);												//input sendKeysWord-phrase into search line
		
		WebElement findButton = driver.findElement(By.cssSelector("button[name='btnG']"));		//locating the find button
		findButton.click();																		//click find button
		
		WebDriverWait waiter = new WebDriverWait(driver, 15);									//explicitly wait until element is present
		waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("td.cur")));
		
		do{
			allPageText = driver.findElement(By.cssSelector("body")).getText();					//get text from current page
			
			WebDriverWait wait = new WebDriverWait(driver, 10);									//explicitly wait until element is present
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("td.cur")));
			
			if(allPageText.contains(searchOnPage)){												//if searchOnPage-phrase was found:
				String curPage = driver.findElement(By.cssSelector("td.cur")).getText();		//identify page where earchOnPage-phrase was found
				System.out.println("Phrase '" + searchOnPage + "' was found on page number " + curPage);
				File screenShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);	//take page screenshot
				try {
					FileUtils.copyFile(screenShot, new File(screenshotPath));
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Page screenshot was saved at: " + screenshotPath);		//print message where screenshot was saved
				break;																			//break the loop
			}else{
				if(isElementPresent(By.cssSelector("#pnnext"))){								//verify that next-button is present on page
					driver.findElement(By.cssSelector("#pnnext")).click();
				}else{																			//else if next-button isn't present on a page - print message and break the loop
					System.out.println("No pages more. Phrase '" + searchOnPage + "' was not found");
					break;
				}
			}			
		}while(!allPageText.contains(searchOnPage));
	}
	
	@After																	
	public void finishTest(){																	//method that is executed after the test
		driver.quit();																			//quit driver and complete the test
	}
	
	private boolean isElementPresent(By locator){												//method to determine whether the element is present on a page
		try{
			driver.findElement(locator);
			return true;
		}catch(NoSuchElementException noSuchElement){
			return false;
		}
	}
	
}
