package org.example.projet4dx.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();
    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("Fichier de configuration introuvable.");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
