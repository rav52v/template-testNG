package main.java.core.functions;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static main.java.core.ConfigService.getConfigService;

public class BrowserFunctions extends BaseFunction {

  private String mainWindowHandle;

  /**
   * Switches to window, which is not main window
   * This method support only one additional window
   */
  public void switchToSecondTab() {
    log.debug("Switch to second tab");
    if (mainWindowHandle == null) mainWindowHandle = driver.getMainWindowHandle();
    for (String winHandle : driver.getDriver().getWindowHandles())
      if (!winHandle.equals(mainWindowHandle)) driver.switchWindow(driver.getDriver().switchTo().window(winHandle));
  }

  /**
   * Switches back to main tab
   */
  public void switchToMainTab() {
    log.debug("Switch to main tab");
    driver.switchWindow(driver.getDriver().switchTo().window(mainWindowHandle));
  }

  /**
   * Closes focused tab
   */
  public void closeTab() {
    driver.getDriver().close();
  }

  /**
   * Opens new tab and focus
   */
  public void openNewTab() {
    log.debug("Open new tab");
    ((JavascriptExecutor) driver.getDriver()).executeScript("window.open();");
    switchToSecondTab();
  }

  public void openPage(String linkAddress) {
    long startTime = System.currentTimeMillis();
    log.debug("Loading page (expected) {" + linkAddress + "}");
    try {
      driver.getDriver().get(linkAddress);
      log.debug("Page loaded {" + driver.getDriver().getCurrentUrl() + "}, operation took {"
              + getPastTimeInMillis(startTime) + "}");
    } catch (TimeoutException e) {
      log.debug("Page was loading too long, page load time is: "
              + getConfigService().getLongProperty("General.pageLoadTime"));
    }
    waitForPageLoading();
  }

  public void scrollPageBy(int widthToScroll, int heightToScroll) {
    ((JavascriptExecutor) driver.getDriver())
            .executeScript("window.scrollBy(arguments[0],arguments[1])", widthToScroll, heightToScroll);
  }

  public void scrollIntoView(WebElement element) {
    ((JavascriptExecutor) driver.getDriver()).executeScript("arguments[0].scrollIntoView()", element);
  }

  /**
   * @param element   WebElement, we want to see in view port
   * @param smooth    Set 'true' for slow animation
   * @param alignment One of "start", "center", "end", or "nearest"
   */
  public void scrollIntoView(WebElement element, String alignment, boolean... smooth) {
    ((JavascriptExecutor) driver.getDriver()).executeScript(
            "arguments[0].scrollIntoView({behavior: \""
                    + (smooth.length == 0 ? "auto" : (smooth[0] ? "smooth" : "auto"))
                    + "\", block: \"" + alignment + "\"})", element);
  }

  public void refreshPage() {
    driver.getDriver().navigate().refresh();
  }

  /**
   * @param elementList list of WebElements, which will be increase after scroll down.
   */
  public void scrollDownRefreshingPageTillEnd(List<WebElement> elementList) {
    long startTime = System.currentTimeMillis();
    int refreshingListSize, refreshingListAfterSize;
    do {
      refreshingListSize = elementList.size();
      new Actions(driver.getDriver()).sendKeys(Keys.END).perform();
      waitForPageLoading();
      refreshingListAfterSize = elementList.size();
    }
    while (refreshingListAfterSize > refreshingListSize);

    log.debug("Found {" + elementList.size() + " elements}, operation took {" + getPastTimeInMillis(startTime) + "}");
  }

  public Alert getAlertControl() {
    return driver.getDriver().switchTo().alert();
  }
}