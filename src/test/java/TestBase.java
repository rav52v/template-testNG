package test.java;

import main.java.core.Driver;
import org.apache.logging.log4j.LogManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.util.concurrent.TimeUnit;

import static main.java.core.ConfigService.getConfigService;

public class TestBase {
  private Driver driver = new Driver();

  @BeforeTest()
  @Parameters({"browser"})
  public void beforeTest(final String browser) {
    driver.setBrowser(browser);
  }

  @BeforeMethod
  public void setWebDriver() {
    driver.setDriver();
    driver.getDriver().manage().timeouts().implicitlyWait(getConfigService()
            .getLongProperty("General.implicitlyWaitTime"), TimeUnit.SECONDS);
    driver.getDriver().manage().timeouts().pageLoadTimeout(getConfigService()
            .getLongProperty("General.pageLoadTime"), TimeUnit.SECONDS);
  }

  @AfterMethod
  public void afterTest() {
    driver.getDriver().quit();
    LogManager.getLogger().info("Test has finished.");
  }
}