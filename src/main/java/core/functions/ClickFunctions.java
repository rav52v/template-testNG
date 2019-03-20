package main.java.core.functions;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClickFunctions extends BaseFunction {

  public void clickOn(WebElement element) {
    log.debug("Click element {" + getElementInfo(element) + "}");

    changeImplicitlyWaitTime(0);
    new WebDriverWait(driver.getDriver(), 7)
            .until(ExpectedConditions.elementToBeClickable(element));
    turnOnImplicitlyWaitTime();
    try {
      new Actions(driver.getDriver()).moveToElement(element).click().perform();
    } catch (WebDriverException e) {
      log.debug("Problem with click noticed, trying to scroll into viewport, then repeat.");
      scrollToElement(element, 500);
      sleeper(1000);
      new Actions(driver.getDriver()).moveToElement(element).click().perform();
    }
  }

  public void clickOn(By by) {
    log.debug("Wait for element {" + by.toString() + "} to be clickable, then click it");

    changeImplicitlyWaitTime(0);
    new WebDriverWait(driver.getDriver(), 10)
            .until(ExpectedConditions.elementToBeClickable(by));
    turnOnImplicitlyWaitTime();
    WebElement element = driver.getDriver().findElement(by);
    try {
      new Actions(driver.getDriver()).moveToElement(element).click().perform();
    } catch (WebDriverException e) {
      log.debug("Problem with click noticed, trying to scroll into viewport, then repeat.");
      scrollToElement(element, 500);
      sleeper(1000);
      new Actions(driver.getDriver()).moveToElement(element).click().perform();
    }
  }

  public void clickAndWaitForElement(WebElement element, By locator) {
    try {
      clickOn(element);
      changeImplicitlyWaitTime(0);
      new WebDriverWait(driver.getDriver(), 5).until(ExpectedConditions
              .visibilityOfElementLocated(locator));
      turnOnImplicitlyWaitTime();
    } catch (TimeoutException e) {
      log.error(e.toString());
      throw new RuntimeException(e.toString());
    }
  }

  public void moveToElementThenClickAnother(WebElement element, By by) {
    try {
      new Actions(driver.getDriver()).moveToElement(element).build().perform();
      clickOn(by);
      waitForPageLoading();
    } catch (WebDriverException e) {
      log.error(e.toString());
      throw new RuntimeException(e.toString());
    }
  }

  public void moveToElementThenClickAnother(WebElement elementToMove, WebElement elementTClick) {
    try {
      new Actions(driver.getDriver()).moveToElement(elementToMove).build().perform();
      clickOn(elementTClick);
      waitForPageLoading();
    } catch (WebDriverException e) {
      log.error(e.toString());
      throw new RuntimeException(e.toString());
    }
  }

  public void clickNTimes(WebElement element, int number) {
    for (int i = 0; i < number; i++) clickOn(element);
  }

  public void doubleClick(WebElement element) {
    new Actions(driver.getDriver()).doubleClick(element).perform();
  }
}