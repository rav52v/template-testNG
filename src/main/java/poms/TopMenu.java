package main.java.poms;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

abstract class TopMenu extends PageBase {
  @FindBy(css = "a[sample='allow samples']")
  WebElement reallySampleSmpl;
}