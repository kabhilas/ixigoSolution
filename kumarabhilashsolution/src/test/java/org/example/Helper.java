package org.example;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static WebDriver driver;
    public static String ifileChrome = "./resources/chromedriver.exe";

    public WebDriver getDriverInstance() {
        System.setProperty("webdriver.chrome.driver", ifileChrome);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new ChromeDriver(capabilities);
        driver.manage().deleteAllCookies();
        return driver;
    }

    public void enterCity(String fromorTo, String givenCity) throws InterruptedException {
        waitForElementVisibilty("//div[@class='form-fields']//div[contains(text(),'" + fromorTo + "')]" +
                "//following-sibling::input[@placeholder='Enter city or airport']").clear();
        waitForElementVisibilty("//div[@class='form-fields']//div[contains(text(),'" + fromorTo + "')]" +
                "//following-sibling::input[@placeholder='Enter city or airport']").sendKeys(givenCity);
        sleep(2);
        WebElement airport = driver.findElement(By.xpath("//div[contains(text(),'"+givenCity+"')]/../../../div"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();", airport);
    }

    public void clickOnSearch() throws InterruptedException {
        waitForElementVisibilty("//button[contains(text(),'Search')]").click();
        sleep(15);
    }

    public void sleep(int secs) throws InterruptedException {
        Thread.sleep(secs * 1000);
    }

    public WebElement waitForElementVisibilty(String xPath) {
        List<WebElement> lsDisplayed = new ArrayList<WebElement>();
        try {
            for (int i = 0; i < 5; i++) {

                try {
                    lsDisplayed = driver.findElements(By.xpath(xPath));

                    if (lsDisplayed.size() > 0) {
                        break;
                    } else {
                        System.out.println("waiting for Element Visible : " + i);
                    }
                    if (i == 5) {
                        throw new Exception("Not able to find the element");
                    }
                } catch (Exception e) {
                    System.out.println("waiting for Element Visible : ****" + i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lsDisplayed.get(0);
    }

    public void selectdate(String givenDate) throws InterruptedException {
        sleep(2);
        WebElement date = waitForElementVisibilty("//div[contains(text(),'December 2020')]//following-sibling::table//tbody//tr/td/div[contains(@class,'day') and text()='"+givenDate+"']");
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();", date);
        sleep(2);
    }

    public void selectCheckboxFollowingLabel(String labelName, boolean check) {
        String classData;
        WebElement element = waitForElementVisibilty("//div[contains(text(),'"+labelName+"')]/../../span");
        classData = element.getAttribute("class");
        if (check) {
            if (!classData.contains("selected")) {
                element.click();
            } else {
                System.out.println(labelName + " is already checked");
            }
        } else {
            if (classData.contains("selected")) {
                element.click();
            } else {
                System.out.println(labelName + " is already unchecked");
            }
        }
    }

    public boolean isStopChecked(String labelName){
        String classData;
        WebElement element = waitForElementVisibilty("//div[contains(text(),'"+labelName+"')]/..//preceding-sibling::span");
        if(element.getAttribute("class").contains("selected")){ return true; } else { return false; }
    }

    public boolean isAirlinesChecked(String labelName){
        String classData;
        WebElement element = waitForElementVisibilty("//div[contains(text(),'Air India')]//..//..//../span[contains(@class,'checkbox-button')]");
        if(element.getAttribute("class").contains("selected")){ return true; } else { return false; }
    }

    public void listAirlines(){
        List<WebElement> airplanes = driver.findElements(By.xpath("//div[@class='summary-section']//i[@class='ixi-icon-inr icon']//..//following-sibling::span"));
        List<String> nameOfAirplanes = new ArrayList<String>();
        
        for(WebElement airplane : airplanes){
            if(Integer.valueOf(airplane.getText()) < 5000){
                String names = driver.findElement(By.xpath("//span[contains(text(),'"+Integer.valueOf(airplane.getText())+"')]//..//..//..//..//..//../../div[contains(@class,'flight-info')]//a//following-sibling::div")).getText();

               if(!("".equals(names))) {
                   nameOfAirplanes.add(names);
               }
            }
        }
        System.out.println("Flights with fare less than 5000: ");
        for(String n: nameOfAirplanes){
            System.out.println(n);
        }
    }
}
