package com.learnautomation.pages;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.learnautomation.utility.BrowserFactory;
import com.learnautomation.utility.ConfigDataProvider;
import com.learnautomation.utility.ExcelDataProvider;
import com.learnautomation.utility.Helper;


public class BaseCalss {
	
	public WebDriver driver;
	public ExcelDataProvider excel;
	public ConfigDataProvider config;
	public ExtentReports report;
	public ExtentTest logger;
	
	@BeforeSuite
	public void setUpSuite()
	{
		 excel=new ExcelDataProvider();
		 config=new ConfigDataProvider();
		 ExtentHtmlReporter extent =new ExtentHtmlReporter(new File(System.getProperty("user.dir")+"/Reports/FB"+Helper.getCurrentDateTime()+".html"));
		 report=new ExtentReports();
		 report.attachReporter(extent);
	}
	@Parameters({"browser","urlToBeTested"})
	@BeforeClass
	public void setUp(String browser, String urlToBeTested) 
	{
		Reporter.log("Trying to start the browser and getting application ready");
		
		//driver=BrowserFactory.startApp(driver, config.getBrowser(), config.getStringUrl()); //browser from 'ConfigDataProvider'
		
		driver=BrowserFactory.startApp(driver, browser, urlToBeTested); //browser from 'parameter'
		
		Reporter.log("Browser and application is up and running fine");
	}
	@AfterClass
	public void tearDown() {
		BrowserFactory.quitBrowser(driver);
	}
	
	@AfterMethod
	public void tearDownMethod(ITestResult result) throws IOException 
	{
		if(result.getStatus()==ITestResult.FAILURE)
		{
			logger.fail("Test Failed", MediaEntityBuilder.createScreenCaptureFromPath(Helper.captureScreenShot(driver)).build());
		}
		
//		else if(result.getStatus()==ITestResult.SUCCESS)
//		{
//			logger.pass("Test passed", MediaEntityBuilder.createScreenCaptureFromPath(Helper.captureScreenShot(driver)).build());
//		}
		else if(result.getStatus()==ITestResult.SKIP)
		{
			logger.skip("Test skipped", MediaEntityBuilder.createScreenCaptureFromPath(Helper.captureScreenShot(driver)).build());
		}
		
		report.flush(); 
	}
}
