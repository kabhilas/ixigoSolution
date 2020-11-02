package org.example;

import javafx.scene.control.CheckBox;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.internal.ITestResultNotifier;

import java.util.concurrent.TimeUnit;

public class ixigoTest extends Helper{
    public Helper helper;
    public String url = "https://www.ixigo.com/";
    public WebDriver driver = getDriverInstance();
    
    @BeforeTest
    public void setBaseUrl(){
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        //Launch https://www.ixigo.com/
        driver.get(url);
    }
    
    @Test
    public void runTest() throws InterruptedException {
        // Validate the page
        Assert.assertTrue(driver.getTitle().contains("ixigo"), "Title is incorrect, Home not launched successfully");
        driver.navigate().refresh();
        driver.findElement(By.xpath("//span[contains(text(),'One Way')]")).click();

        // Enter From – Pune , To – Hyderabad , Departure – 17 Sep 2020 , Return – 24 Oct 2020 , Travelers - 2
        enterCity("From", "Pune");
        enterCity("To", "Hyderabad");

        waitForElementVisibilty("//input[@placeholder='Depart']").click();
        // Selecting date as 17 December for one way
        selectdate("17");
        waitForElementVisibilty("//div[normalize-space(.) ='Adult']//parent::div//following-sibling::div/span[contains(text(),'2')]").click();
        waitForElementVisibilty("//div[contains(text(),'Travellers')]//following-sibling::div[contains(@class, 'close-btn')]").click();

        //waitForElementVisibilty("//input[@placeholder='Return']").click();

        //Click on Search, Validate the result page
        clickOnSearch();

        Assert.assertEquals(driver.findElements(By.xpath("//span[contains(text(),'CHEAPEST')]")).size(), 2, "Search resulted no flights");

        // Validate filter option for Stops and Select Non-Stop in Stops filter option
        selectCheckboxFollowingLabel("Non stop", true);
        Assert.assertTrue(isStopChecked("Non stop"), "CheckBox is not checked");
        Assert.assertFalse(isStopChecked("1 stop"), "CheckBox is checked");
        Assert.assertFalse(isStopChecked("1+ stop"), "CheckBox is checked");

        // Verify Departure and Airlines elements to be enabled and clickable respectively
        Assert.assertTrue(waitForElementVisibilty("//div[contains(text(),'Early Morning')]//../button").isEnabled(),"Early Morning is not enabled");
        Assert.assertTrue(waitForElementVisibilty("//div[contains(text(),'Morning')]//../button").isEnabled(),"Morning is not enabled");
        Assert.assertTrue(waitForElementVisibilty("//div[contains(text(),'Mid Day')]//../button").isEnabled(),"Mid Day is not enabled");
        Assert.assertTrue(waitForElementVisibilty("//div[contains(text(),'Night')]//../button").isEnabled(),"Night is not enabled");

        Assert.assertFalse(isAirlinesChecked("Air India"), "CheckBox is checked");
        Assert.assertFalse(isAirlinesChecked("Go Air"), "CheckBox is checked");
        Assert.assertFalse(isAirlinesChecked("IndiGo"), "CheckBox is checked");

        // Print the list of airlines details (Only Airline Number, Departure Time and Fare) having fare < 5000
        listAirlines();
    }

    @AfterTest
    public void tearDown(){
        driver.close();
        System.out.println("Test Completed");
    }
}

