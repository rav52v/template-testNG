package main.java.core.functions;

import main.java.core.enums.Packages;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class FileFunctions extends BaseFunction {

  public FileFunctions() {
    if (new File(Packages.OUTPUT_FOLDER.getPackagePath()).mkdirs())
      log.debug("Created directory {" + PATH_TO_OUTPUT_FOLDER + "}");
  }

  /**
   * @param fileName name of captured file
   * @param zoom     zoom in: [101-...], zoom out: [1-99]
   */
  public void captureScreenshot(String fileName, int zoom) {
    JavascriptExecutor js = (JavascriptExecutor) driver.getDriver();
    js.executeScript("document.body.style.zoom='" + zoom + "%'");
    File scrFile = ((TakesScreenshot) driver.getDriver()).getScreenshotAs(OutputType.FILE);
    File target = new File(PATH_TO_OUTPUT_FOLDER + "/" + fileName + ".png");
    try {
      if (target.exists()) target.delete();
      Files.copy(scrFile.toPath(), target.toPath());
      log.debug("File copied to {" + PATH_TO_OUTPUT_FOLDER + "\\" + fileName + ".png}");
    } catch (IOException e) {
      log.error(e.toString());
      e.printStackTrace();
    }
    js.executeScript("document.body.style.zoom='0'");
  }

  public void captureScreenshotOfElement(String fileName, WebElement element) {
    log.debug("Capture image of element {" + getElementInfo(element) + "}");
    File target = new File(PATH_TO_OUTPUT_FOLDER + "/" + fileName + ".png");
    if (target.exists()) target.delete();
    File screen = ((TakesScreenshot) driver.getDriver()).getScreenshotAs(OutputType.FILE);

    Point p = element.getLocation();
    int width = element.getSize().getWidth();
    int height = element.getSize().getHeight();

    try {
      BufferedImage img = ImageIO.read(screen);
      BufferedImage dest = img.getSubimage(p.getX(), p.getY(), width, height);
      ImageIO.write(dest, "png", screen);
      Files.copy(screen.toPath(), target.toPath());
      log.debug("File copied to {" + PATH_TO_OUTPUT_FOLDER + "\\" + fileName + ".png}");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (RasterFormatException ex) {
      log.error(String.format("%s\nelement - parentX: %d, parentY: %d, width: %d, height: %d\nbrowser - x: %d, y: %d",
              ex.toString(), p.getX(), p.getY(), width, height, driver.getDriver().manage().window().getSize().width,
              driver.getDriver().manage().window().getSize().height));
    }
  }

  public void saveTextToFile(String textValue, String fileName, boolean append) {
    File target = new File(PATH_TO_OUTPUT_FOLDER + "/" + fileName + ".txt");
    try (FileWriter fw = new FileWriter(target, append);
         BufferedWriter bw = new BufferedWriter(fw);
         PrintWriter out = new PrintWriter(bw)) {
      out.print(textValue);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param fileName file name e.g. "sample.txt"
   * @return full text value from file with line separators
   */
  public String getTextFromFile(String fileName) {
    String result = "";
    try {
      BufferedReader reader = new BufferedReader(new FileReader(PATH_TO_INPUT_FOLDER + "/" + fileName));
      StringBuilder sb = new StringBuilder();
      String line;
      String ls = System.getProperty("line.separator");
      while ((line = reader.readLine()) != null) {
        sb.append(line);
        sb.append(ls);
      }
      reader.close();
      result = sb.toString();
    } catch (FileNotFoundException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * @param elementList List<WebElement> containing URL address in attribute
   * @param attribute   name of attribute containing URL e.g. "src"
   * @param fileName    output file name
   * @param fileFormat  file format e.g. "jpg"
   */
  public void saveImageFromUrl(List<WebElement> elementList, String attribute, String fileName, String fileFormat) {
    int counter = 0;
    for (WebElement x : elementList) {
      String source = x.getAttribute(attribute);
      try {
        URL imageURL = new URL(source);
        BufferedImage saveImage = ImageIO.read(imageURL);
        ImageIO.write(saveImage, fileFormat, new File(PATH_TO_OUTPUT_FOLDER + String.format("%03d ", counter++)
                + fileName + "." + fileFormat));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    log.debug("Downloaded {" + counter + " images}");
  }

  public String getRelativePathToFile(String fileName) {
    return PATH_TO_INPUT_FOLDER + "\\" + fileName;
  }
}