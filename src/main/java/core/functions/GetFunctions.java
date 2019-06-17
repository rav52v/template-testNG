package main.java.core.functions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetFunctions extends BaseFunction {

  public String getTextFromElement(WebElement element) {
    log.debug("Get text from element {" + getElementInfo(element) + "}");
    String value = element.getText();

    if (value == null) value = element.getAttribute("value").trim();
    else value = value.trim();

    log.debug("Got value {" + (value.length() < 50 ? value.replaceAll("[\n]", "") : value
            .substring(0, 50).concat("...")).replaceAll("([\n])|(^\\s*)|(\\s*$)|([ ]{3,})", "") + "}");
    return value;
  }

  public String getTextFromParentElement(WebElement element) {
    element = element.findElement(By.xpath("./.."));
    log.debug("Get text from parent of element {" + getElementInfo(element) + "}");
    String value = element.getText();

    if (value == null) value = element.getAttribute("value").trim();
    else value = value.trim();

    log.debug("Got value {" + (value.length() < 50 ? value.replaceAll("[\n]", "") : value
            .substring(0, 50).concat("...")).replaceAll("([\n])|(^\\s*)|(\\s*$)|([ ]{3,})", "") + "}");
    return value;
  }

  public String getAttributeFromElement(WebElement element, String attribute) {
    return element.getAttribute(attribute);
  }

  public String getValueFromReadOnlyElement(WebElement element) {
    return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].value", element);
  }

  public String searchElementInElementAndGetText(WebElement element, By by) {
    log.debug("Search element located By {" + by + "} in element {"
            + getElementInfo(element) + "} and get text");
    String result = "";
    for (int i = 0; i < 5; i++) {
      try {
        result = element.findElement(by).getText();
        break;
      } catch (StaleElementReferenceException e) {
        sleeper(500);
        if (i == 4) {
          result = "";
          log.error("Element {" + getElementInfo(element) + "} was stale, tried 4 times.");
        }
      }
    }
    return result;
  }

  public String searchElementInElementAndGetAttribute(WebElement element, By by, String attributeName) {
    log.debug("Search element located By {" + by + "} in element {"
            + getElementInfo(element) + "} and get attribute {" + attributeName + "}");
    String result = "";
    for (int i = 0; i < 5; i++) {
      try {
        result = element.findElement(by).getAttribute(attributeName);
        break;
      } catch (StaleElementReferenceException e) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }
        if (i == 4) {
          result = "";
          log.error("Element {" + getElementInfo(element) + "} was stale, tried 4 times.");
        }
      }
    }
    return result;
  }

  public String getCurrentUrl() {
    return driver.getDriver().getCurrentUrl();
  }

  /**
   * @param elementList list of given elements
   * @param regex       text value to find - regex allowed
   * @return WebElement containing given regex value
   */
  public WebElement getElementContaingRegexValue(List<WebElement> elementList, String regex) {
    if (elementList.isEmpty()) {
      log.error("Given list is empty");
      return null;
    }
    for (WebElement element : elementList) if (element.getText().matches(regex)) return element;
    log.debug("List doesn't contain given regex value {" + regex + "}");
    return null;
  }

  /**
   * @param selectElement Select type element
   * @return all selected options as List<WebElement>
   */
  public List<WebElement> getAllSelectedOptions(WebElement selectElement) {
    return new Select(selectElement).getAllSelectedOptions();
  }

  /**
   * @param selectElement Select type element
   * @return all options List<WebElement>
   */
  public List<WebElement> getAllOptions(WebElement selectElement) {
    return new Select(selectElement).getOptions();
  }

  public Object returnJavaScriptValue(String script, Object... args) {
    return ((JavascriptExecutor) driver.getDriver()).executeScript("return " + script, args);
  }

  public WebElement getElementWithDifferentCssProperty(List<WebElement> webElements, String cssProperty) {
    List<String> values = new ArrayList<>();
    webElements.forEach(element -> values.add(element.getCssValue(cssProperty)));
    Collections.sort(values);
    String differentValue = values.get(0).equals(values.get(1)) ? values.get(values.size() - 1) : values.get(0);
    for (WebElement element : webElements) if (element.getCssValue(cssProperty).equals(differentValue)) return element;
    throw new RuntimeException("Couldn't find element with one different cssProperty: " + cssProperty);
  }

  public String getElementXPath(WebElement element) {
    return (String) ((JavascriptExecutor) driver.getDriver()).executeScript(
            "gPt = function (c) {if (c.id !== '') {return '//'+c.tagName + '[@id=\\'' + c.id + '\\']'}" +
                    "if (c === document.body) {return c.tagName}var a = 0;var e = c.parentNode.childNodes;" +
                    "for (var b = 0; b < e.length; b++) {var d = e[b];if (d === c) {return gPt(c.parentNode) " +
                    "+ '/' + c.tagName + '[' + (a + 1) + ']'}if (d.nodeType === 1 && d.tagName === c.tagName) " +
                    "{a++}}};return gPt(arguments[0]);", element);
  }
}