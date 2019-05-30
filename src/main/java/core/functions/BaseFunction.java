package main.java.core.functions;

import main.java.core.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static main.java.core.ConfigService.getConfigService;

abstract class BaseFunction {
  Driver driver;
  final Logger log = LogManager.getLogger();
  final Path pathInputFolder = Paths.get("inputFolder");
  final Path pathOutputFolder = Paths.get("outputFolder");
  final long DEFAULT_WEB_DRIVER_WAIT_TIME = getConfigService().getLongProperty("General.webDriverWait");

  BaseFunction() {
    driver = new Driver();
  }

  public void sleeper(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void waitForPageLoading() {
    // wait for Ajax actions to begin
    sleeper(1000);

    changeImplicitlyWaitTime(0);

    new WebDriverWait(driver.getDriver(), getConfigService().getLongProperty("General.pageLoadTime"))
            .ignoring(StaleElementReferenceException.class).until(new ExpectedCondition<Boolean>() {

      private final int MAX_NO_JQUERY_COUNTER = 3;
      private int noJQueryCounter = 0;

      @Override
      public Boolean apply(WebDriver driver) {
        String documentReadyState = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.readyState;");

        Long jQueryActive = (Long) ((JavascriptExecutor) driver)
                .executeScript("if(window.jQuery) { return window.jQuery.active; } else { return -1; }");

        log.debug(String.format("waitForPageLoading -> document.readyState: %s, jQuery.active: %d"
                , documentReadyState, jQueryActive));

        if (jQueryActive == -1) {
          noJQueryCounter++;

          if (noJQueryCounter >= MAX_NO_JQUERY_COUNTER) {
            return true;
          }

        } else {
          noJQueryCounter = 0;
        }

        return "complete".equals(documentReadyState) && jQueryActive == 0;
      }
    });
    turnOnImplicitlyWaitTime();

    // wait for Ajax responses to be processed
    sleeper(500);
  }

  String getElementInfo(WebElement element) {
    return (element.toString()).replaceAll("(^.*-> )|(]$)", "");
  }

  void changeImplicitlyWaitTime(int milliSeconds) {
    driver.getDriver().manage().timeouts().implicitlyWait(milliSeconds, TimeUnit.MILLISECONDS);
  }

  void turnOnImplicitlyWaitTime() {
    driver.getDriver().manage().timeouts().implicitlyWait(getConfigService()
            .getLongProperty("General.implicitlyWaitTime"), TimeUnit.SECONDS);
  }

  long getPastTimeInMillis(long startTime) {
    return System.currentTimeMillis() - startTime;
  }

  String getEstTime(long startTime, int thingsDone, int allThings) {
    double percentDone = Double.parseDouble(String.format("%.2f", (100.0 * ((double) (thingsDone)
            / (double) allThings))).replaceAll(",", "."));
    double passedTimeInMinutes = (System.currentTimeMillis() - startTime) / 60000.0;
    double timeLeftInMinutes = (100.0 / percentDone) * (passedTimeInMinutes) - passedTimeInMinutes + 1.0;
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, (int) timeLeftInMinutes);

    return new SimpleDateFormat("HH:mm").format(calendar.getTime());
  }

  void scrollIntoView(WebElement element) {
    ((JavascriptExecutor) driver.getDriver()).executeScript("arguments[0].scrollIntoView()", element);
  }
}