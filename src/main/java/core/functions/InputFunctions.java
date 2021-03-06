package main.java.core.functions;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;

public class InputFunctions extends BaseFunction {

  /**
   * @param elementToClick    element to clickOn
   * @param elementToSendKeys input element to send keys
   * @param value             CharSequence values
   */
  public void clickElementAndSendKeysToAnother(WebElement elementToClick, WebElement elementToSendKeys, CharSequence... value) {
    log.debug("Click element {" + getElementInfo(elementToClick) + "}...");
    new Actions(driver.getDriver()).moveToElement(elementToClick).click().sendKeys(value).perform();
    sendKeysToElement(elementToSendKeys, value);
  }

  public void sendKeysToElement(WebElement element, CharSequence... value) {
    log.debug("Send keys {" + Arrays.toString(value) + "} to element {" + getElementInfo(element) + "}");
    try {
      element.clear();
    } catch (Exception ignored) {
    }
    element.sendKeys(value);
  }

  public void sendKeysToElementUsingClipboard(WebElement element, String value) {
    log.debug("Storing text {" + value + "} in cache");
    try {
      element.clear();
    } catch (Exception ignored) {
    }
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
    sendKeysToElement(element, Keys.CONTROL + "v", Keys.TAB);
  }

  public void sendKeys(CharSequence... value) {
    log.debug("Send keys {" + Arrays.toString(value) + "}");
    new Actions(driver.getDriver()).sendKeys(value).perform();
  }

  public void selectByVisibleTextWithRegex(WebElement selectElement, String regex) {
    Select select = new Select(selectElement);
    for (WebElement option : select.getOptions()) {
      if (option.getText().matches(regex)) {
        select.selectByVisibleText(option.getText());
        log.debug("Option selected by visible text: {" + option.getText() + "}");
        break;
      }
    }
  }

  public void selectByValue(WebElement selectElement, String value) {
    try {
      new Select(selectElement).selectByValue(value);
      log.debug("Option selected by value: {" + value + "}");
    } catch (NoSuchElementException e) {
      log.error(e.toString());
      throw new RuntimeException(e.toString());
    }
  }

  public void selectByIndex(WebElement selectElement, int index) {
    try {
      new Select(selectElement).selectByIndex(index);
      log.debug("Option selected by index: {" + index + "}");
    } catch (NoSuchElementException e) {
      log.error(e.toString());
      throw new RuntimeException(e.toString());
    }
  }
}