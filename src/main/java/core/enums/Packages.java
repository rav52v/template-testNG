package main.java.core.enums;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public enum Packages {
  OUTPUT_FOLDER("outputFolder"),
  INPUT_FOLDER("inputFolder");

  private String folder;

  Packages(String values) {
    folder = values;
  }

  /**
   * @return relative path to directory
   */
  public String getPackagePath() {
    return Paths.get(folder).toAbsolutePath() + "\\";
  }

  /**
   * @return amount of files inside directory
   */
  public int getElementsInsideAmount() {
    try {
      return new File(new File("").getCanonicalFile().toPath().toAbsolutePath().toString()
              + "/" + folder).listFiles().length;
    } catch (IOException | NullPointerException e) {
      return 0;
    }
  }

  /**
   * @param fileName e.g. sample_file.txt
   * @return true if files exists in directory, or false if not
   */
  public boolean isFileInDirectory(String fileName) {
    File[] files;
    try {
      files = new File(new File("").getCanonicalFile().toPath().toAbsolutePath().toString()
              + "/" + folder).listFiles();
    } catch (IOException | NullPointerException e) {
      return false;
    }
    for (File file : files) if (file.getName().equals(fileName)) return true;
    return false;
  }
}