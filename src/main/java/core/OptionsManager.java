package main.java.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import static main.java.core.ConfigService.getConfigService;

class OptionsManager {
  private boolean headless = getConfigService().getBooleanProperty("General.headless");

  ChromeOptions setChromeOptions() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions chromeOptions = new ChromeOptions()
            .addArguments("--disable-infobars")
            .setHeadless(headless);
    if (headless) {
      chromeOptions.addArguments("--window-size=1500,4000");
      chromeOptions.addArguments("--disable-gpu");
    } else chromeOptions.addArguments("--start-maximized");

    return chromeOptions;
  }

  FirefoxOptions setFirefoxOptions() {
    WebDriverManager.firefoxdriver().setup();
    FirefoxOptions firefoxOptions = new FirefoxOptions()
            .addArguments("--disable-notifications")
            .setHeadless(headless);
    if (headless) firefoxOptions.addArguments("window-size=1500,4000");

    return firefoxOptions;
  }
}