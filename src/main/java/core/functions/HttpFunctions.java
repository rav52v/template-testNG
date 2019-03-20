package main.java.core.functions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpFunctions extends BaseFunction {
  public boolean isServerResponseOk(String link) {
    try {
      HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      return connection.getResponseCode() == 200;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}