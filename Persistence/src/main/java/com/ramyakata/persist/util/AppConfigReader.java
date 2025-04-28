package com.ramyakata.persist.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfigReader {

	private static Properties properties = new Properties();

    static {
        try (InputStream input = AppConfigReader.class.getClassLoader()
                .getResourceAsStream("appconfig.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot find appconfig.properties in resources");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading appconfig.properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
