package tech.gralerfics.persistence.options;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public final class OptionsManager {
    public static final String optionFile = "assets/property/options.properties";

    public static Properties options = new Properties();

    public static void saveOption() {
        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(optionFile));
            for (String key : options.stringPropertyNames()) {
                fw.write(key + "=" + options.getProperty(key) + "\n");
            }
            fw.close();
        } catch (IOException e) {
            // ±£¥Ê…Ë÷√π ’œ
        }
    }

    public static void loadOption() {
        try {
            FileInputStream fin = new FileInputStream(optionFile);
            options.load(fin);
            fin.close();
        } catch (IOException e) {
            // ∂¡»°…Ë÷√π ’œ
        }
    }
}
