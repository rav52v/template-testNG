package main.java.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Driver {
  private String browser;
  private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

  public void setBrowser(String browser) {
    this.browser = browser;
  }

  public void setDriver() {
    OptionsManager options = new OptionsManager();
    switch (browser) {
      case "mozilla":
        driver.set(new FirefoxDriver(options.setFirefoxOptions()));
        getDriver().manage().window().maximize();
        break;
      case "chrome":
        driver.set(new ChromeDriver(options.setChromeOptions()));
        break;
    }
  }

  public WebDriver getDriver() {
    return driver.get();
  }

  public String getMainWindowHandle() {
    return getDriver().getWindowHandle();
  }

  public void switchWindow(WebDriver newDriver) {
    driver.set(newDriver);
  }
}