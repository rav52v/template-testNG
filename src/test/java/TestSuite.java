package test.java;

import main.java.poms.MainPage;
import org.testng.annotations.Test;

public class TestSuite extends TestBase {

  @Test(priority = 1)
  public void testCase1() {
    new MainPage()
            .msdsd();
  }

  @Test(priority = 2)
  public void testCase2() {
    new MainPage()
            .msdsd();
  }

  @Test(priority = 3)
  public void testCase3() {
    new MainPage()
            .msdsd();
  }
}