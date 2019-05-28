package main.java.poms;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static main.java.core.ConfigService.getConfigService;

public class MainPage extends TopMenu {

  @FindBy(css = "a.sample")
  private WebElement sample;

  public void sampleMethod(){
    browser.openPage(getConfigService().getStringProperty("Data.url"));
    browser.openNewTab();
    browser.switchToSecondTab();
    browser.openPage("https://www.youtube.com/?hl=pl&gl=PL");
    browser.sleeper(500);
    browser.switchToMainTab();
    browser.openPage("http://www.google.pl/");
    browser.switchToSecondTab();
    browser.closeTab();
    browser.sleeper(1000);
    browser.switchToMainTab();
  }
}