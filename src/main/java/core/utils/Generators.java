package main.java.core.utils;

import java.util.Random;

public class Generators {

  public enum Names {
    MALE("Adam", "Adolf", "Adrian", "Albert", "Aleksander", "Aleksy", "Alfred", "Amadeusz", "Andrzej", "Antoni",
            "Arkadiusz", "Arnold", "Artur", "Bartłomiej", "Bartosz", "Benedykt", "Beniamin", "Bernard", "Błażej",
            "Bogdan", "Bogumił", "Bogusław", "Bolesław", "Borys", "Bronisław", "Cezary", "Cyprian", "Cyryl", "Czesław",
            "Damian", "Daniel"),
    FEMALE("Ada", "Adela", "Adelajda", "Adrianna", "Agata", "Agnieszka", "Aldona", "Aleksandra", "Alicja",
            "Alina", "Amanda", "Amelia", "Anastazja", "Andżelika", "Aneta", "Anita", "Anna", "Barbara", "Beata",
            "Berenika", "Bernadeta", "Blanka", "Bogusława", "Bożena", "Cecylia", "Celina", "Czesława", "Dagmara",
            "Danuta", "Daria", "Diana", "Dominika", "Dorota");

    private String[] namesArray;

    Names(String... values) {
      namesArray = values;
    }

    public String getName() {
      return namesArray[new Random().nextInt(namesArray.length)];
    }
  }

  private final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private final String LOWER = UPPER.toLowerCase();
  private final String NUMBER = "0123456789";
  private Random random = new Random();

  public String gName() {
    Names[] namesArray = {Names.MALE, Names.FEMALE};
    return namesArray[random.nextInt(2)].getName();
  }

  public String gNumbers(int wordSize) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < wordSize; i++) result.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
    return result.toString();
  }

  public int randomFromRange(int from, int to) {
    return random.nextInt(to - from + 1) + from;
  }

  public String gLower(int wordSize) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < wordSize; i++) result.append(LOWER.charAt(random.nextInt(LOWER.length())));
    return result.toString();
  }

  public String gUpper(int wordSize) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < wordSize; i++) result.append(UPPER.charAt(random.nextInt(UPPER.length())));
    return result.toString();
  }

  public String gPass(int wordSize) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < wordSize; i++) {
      char[] charArray = {gLower(1).charAt(0), gUpper(1).charAt(0), gNumbers(1).charAt(0)};
      result.append(charArray[random.nextInt(charArray.length)]);
    }
    return result.toString();
  }

  public String gEmail() {
    String[] server = {"gmail", "googlemail", "outlook", "email", "yahoo", "aol", "outlook"};
    String[] suffix = {"pl", "com", "gf", "cz", "gb", "it", "sk", "tr", "cn", "be", "au", "is", "fr"};
    return String.format("%s@%s.%s", gLower(10), server[random.nextInt(server.length)],
            suffix[random.nextInt(suffix.length)]);
  }
}