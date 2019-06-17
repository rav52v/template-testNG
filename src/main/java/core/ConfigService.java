package main.java.core;

import org.apache.logging.log4j.LogManager;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.Environment;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class ConfigService {

  private static final ConfigurationProvider configProvider = configProvider();
  private static ConfigService instance = null;

  private ConfigService() {
  }

  public static ConfigService getConfigService() {
    if (instance == null) {
      instance = new ConfigService();
      LogManager.getLogger().debug("Config created.");
    }
    return instance;
  }

  private static ConfigurationProvider configProvider() {
    final String PATH_TO_CONFIG_PACKAGE = "./src/test/resources/config";
    ConfigFilesProvider configFilesProvider = () -> {
      File[] files = new File[0];
      try {
        files = new File(new File("").getCanonicalFile().toPath().toAbsolutePath().toString()
                + PATH_TO_CONFIG_PACKAGE).listFiles();
      } catch (IOException e) {
        LogManager.getLogger().error("Something went wrong - check path to config package, message: " + e.getMessage());
        e.printStackTrace();
      }
      ArrayList<Path> paths = new ArrayList<>();
      Arrays.asList(files).forEach(file -> paths.add(file.toPath()));
      return paths;
    };

    ConfigurationSource source = new FilesConfigurationSource(configFilesProvider);
    Environment env = new ImmutableEnvironment(PATH_TO_CONFIG_PACKAGE);

    return new ConfigurationProviderBuilder()
            .withConfigurationSource(source)
            .withEnvironment(env)
            .build();
  }

  public String getStringProperty(String property) {
    return (String) getProperty(property, String.class);
  }

  public Boolean getBooleanProperty(String property) {
    return (Boolean) getProperty(property, Boolean.class);
  }

  public Long getLongProperty(String property) {
    return (Long) getProperty(property, Long.class);
  }

  private <T> Object getProperty(String property, Class<T> tClass) {
    Object result = System.getProperty(property);
    if (result == null) result = configProvider.getProperty(property, tClass);
    else {
      switch (tClass.getCanonicalName()) {
        case "java.lang.Boolean":
          result = Boolean.parseBoolean(System.getProperty(property));
          break;
        case "java.lang.Long":
          result = Long.parseLong(System.getProperty(property));
          break;
        default:
          result = System.getProperty(property);
      }
    }

    return result;
  }
}