package main.java.poms;

import main.java.core.Driver;
import main.java.core.functions.*;
import org.openqa.selenium.support.PageFactory;

abstract class PageBase {
  BrowserFunctions browser;
  CheckFunctions check;
  ClickFunctions click;
  FileFunctions file;
  InputFunctions input;
  ActionFunctions action;
  GetFunctions get;
  HttpFunctions http;

  PageBase() {
    browser = new BrowserFunctions();
    check = new CheckFunctions();
    click = new ClickFunctions();
    file = new FileFunctions();
    input = new InputFunctions();
    action = new ActionFunctions();
    get = new GetFunctions();
    http = new HttpFunctions();

    PageFactory.initElements(new Driver().getDriver(), this);
  }
}