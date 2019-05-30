package main.java.core.functions;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClickFunctions extends BaseFunction {

  public void clickOn(WebElement element, long...maxWaitTime) {
    log.debug("Click element {" + getElementInfo(element) + "}");

    long timeOutInSeconds = maxWaitTime.length > 0 ? maxWaitTime[0] : DEFAULT_WEB_DRIVER_WAIT_TIME;
    changeImplicitlyWaitTime(0);
    new WebDriverWait(driver.getDriver(), timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(element));
    turnOnImplicitlyWaitTime();
    try {
      click(element);
    } catch (WebDriverException e) {
      log.debug("Problem with click noticed, trying to scroll into viewport, then repeat.");
      scrollIntoView(element);
      sleeper(1000);
      click(element);
    }
  }

  public void clickOn(By by, long...maxWaitTime) {
    log.debug("Wait for element {" + by.toString() + "} to be clickable, then click it");

    long timeOutInSeconds = maxWaitTime.length > 0 ? maxWaitTime[0] : DEFAULT_WEB_DRIVER_WAIT_TIME;
    changeImplicitlyWaitTime(0);
    new WebDriverWait(driver.getDriver(), timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(by));
    turnOnImplicitlyWaitTime();
    WebElement element = driver.getDriver().findElement(by);
    try {
      click(element);
    } catch (WebDriverException e) {
      log.debug("Problem with click noticed, trying to scroll into viewport, then repeat.");
      scrollIntoView(element);
      sleeper(1000);
      click(element);
    }
  }

  public void clickAndWaitForElement(WebElement element, By locator, long...maxWaitTime) {
    try {
      clickOn(element, maxWaitTime);
      changeImplicitlyWaitTime(0);
      new WebDriverWait(driver.getDriver(), DEFAULT_WEB_DRIVER_WAIT_TIME).until(ExpectedConditions
              .visibilityOfElementLocated(locator));
      turnOnImplicitlyWaitTime();
    } catch (TimeoutException e) {
      log.error(e.toString());
      throw new RuntimeException(e.toString());
    }
  }

  public void moveToElementThenClickAnother(WebElement element, By by, long...maxWaitTime) {
    try {
      new Actions(driver.getDriver()).moveToElement(element).build().perform();
      clickOn(by, maxWaitTime);
      waitForPageLoading();
    } catch (WebDriverException e) {
      log.error(e.toString());
      throw new RuntimeException(e.toString());
    }
  }

  public void moveToElementThenClickAnother(WebElement elementToMove, WebElement elementTClick, long...maxWaitTime) {
    try {
      new Actions(driver.getDriver()).moveToElement(elementToMove).build().perform();
      clickOn(elementTClick, maxWaitTime);
      waitForPageLoading();
    } catch (WebDriverException e) {
      log.error(e.toString());
      throw new RuntimeException(e.toString());
    }
  }

  public void clickNTimes(WebElement element, int number, long...maxWaitTime) {
    log.debug("Click element " + number + " times {" + getElementInfo(element) + "}");
    for (int i = 0; i < number; i++) clickOn(element, maxWaitTime);
  }

  public void doubleClick(WebElement element) {
    log.debug("Double click element {" + getElementInfo(element) + "}");
    new Actions(driver.getDriver()).doubleClick(element).perform();
  }

  private void click(WebElement element) {
    new Actions(driver.getDriver()).moveToElement(element).click().perform();
  }
}