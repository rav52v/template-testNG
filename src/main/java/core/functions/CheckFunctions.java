package main.java.core.functions;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static main.java.core.ConfigService.getConfigService;

public class CheckFunctions extends BaseFunction {

  public boolean isElementDisplayed(WebElement element, int maxWaitTimeSec) {
    log.debug("Check if element {" + getElementInfo(element) + "} is displayed, max waiting time {" + maxWaitTimeSec + " seconds}");

    changeImplicitlyWaitTime(0);
    try {
      new WebDriverWait(driver.getDriver(), maxWaitTimeSec).until(ExpectedConditions.visibilityOf(element));
      turnOnImplicitlyWaitTime();
      return true;
    } catch (TimeoutException e) {
      turnOnImplicitlyWaitTime();
      log.debug("Element is not displayed");
      return false;
    }
  }

  public boolean isElementFound(By locator, int maxWaitTimeSec) {
    log.debug("Check if element {" + locator + "} is found, max waiting time {" + maxWaitTimeSec + " seconds}");

    changeImplicitlyWaitTime(0);
    try {
      new WebDriverWait(driver.getDriver(), maxWaitTimeSec).until(ExpectedConditions.presenceOfElementLocated(locator));
      turnOnImplicitlyWaitTime();
      return true;
    } catch (TimeoutException e) {
      turnOnImplicitlyWaitTime();
      log.debug("Found not element");
      return false;
    }
  }

  public boolean isElementFound(WebElement element, int maxWaitTimeSec) {
    log.debug("Check if element {" + getElementInfo(element)
            + "} is found, max waiting time {" + maxWaitTimeSec + " seconds}");

    changeImplicitlyWaitTime(0);
    try {
      new WebDriverWait(driver.getDriver(), maxWaitTimeSec).until(ExpectedConditions.visibilityOf(element));
      turnOnImplicitlyWaitTime();
      return true;
    } catch (TimeoutException e) {
      turnOnImplicitlyWaitTime();
      log.debug("Found not element");
      return false;
    }
  }

  public boolean isElementFound(List<WebElement> element, int maxWaitTimeMillis) {
    log.debug("Check if element is found (using list), max waiting time {"
            + maxWaitTimeMillis + " milliseconds}");

    changeImplicitlyWaitTime(maxWaitTimeMillis);
    if (!element.isEmpty()) {
      turnOnImplicitlyWaitTime();
      return true;
    } else {
      turnOnImplicitlyWaitTime();
      log.debug("Found not element");
      return false;
    }
  }

  public boolean containsRegexValue(WebElement element, String regex) {
    return element.getText().matches(regex);
  }

  public boolean containsRegexValue(List<WebElement> elementList, String regex) {
    for (WebElement element : elementList) {
      if (element.getText().matches(regex))
        return true;
    }
    return false;
  }

  public boolean isElementClickable(WebElement element) {
    changeImplicitlyWaitTime(0);
    try {
      new WebDriverWait(driver.getDriver(), 5, 40)
              .until(ExpectedConditions.elementToBeClickable(element));
      turnOnImplicitlyWaitTime();
      return true;
    } catch (TimeoutException e) {
      turnOnImplicitlyWaitTime();
      return false;
    }
  }

  public boolean isElementSelected(WebElement element) {
    return element.isSelected();
  }

  public boolean nElementsAreDisplayed(By locator, int numberOfExpectedElements) {
    log.debug("Check if {" + numberOfExpectedElements + "} elements {" + locator + "} are displayed");

    changeImplicitlyWaitTime(0);
    try {
      new WebDriverWait(driver.getDriver(), 5).until(
              ExpectedConditions.numberOfElementsToBe(locator, numberOfExpectedElements));
      turnOnImplicitlyWaitTime();
      return true;
    } catch (TimeoutException e) {
      turnOnImplicitlyWaitTime();
      log.debug("Wrong number of elements is displayed");
      return false;
    }
  }

  public boolean pageTitleContains(String title, int maxWaitTimeInSec) {
    changeImplicitlyWaitTime(0);
    try {
      new WebDriverWait(driver.getDriver(), maxWaitTimeInSec)
              .withMessage("Page title actual: " + driver.getDriver().getTitle() + " expected: " + title)
              .until(ExpectedConditions.titleContains(title));
      turnOnImplicitlyWaitTime();
      return true;
    } catch (TimeoutException e) {
      turnOnImplicitlyWaitTime();
      log.error("Page title actual: " + driver.getDriver().getTitle() + " expected: " + title);
      log.debug("Probably page was loading too long, page load time is: " + getConfigService()
              .getLongProperty("General.pageLoadTime"));
      return false;
    }
  }
}