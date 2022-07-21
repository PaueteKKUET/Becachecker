package com.fadedbytes.becachecker;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Setup {

    private static WebDriver driver = null;

    private static String userName      = null;
    private static String password      = null;
    private static String url           = null;
    private static String browserName   = null;

    public static void setup() {
        setupConfig();
        setupDriver();
    }

    @NotNull
    public static WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("Driver is not initialized, use Setup.setup() first");
        }
        return driver;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getHomeURL() {
        return url;
    }

    private static void setupDriver() {
        switch (getBrowserType()) {
            case CHROME -> {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            }
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            }
            case SAFARI -> {
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
            }
        }
    }

    private static void setupConfig() {
        File configFile = new File("config.properties");
        // if the config file does not exist, create it
        try {
            configFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                FileReader reader = new FileReader(configFile)
                ) {

            Properties props = new Properties();
            props.load(reader);

            userName    = props.getProperty("username");
            password    = props.getProperty("password");
            browserName = props.getProperty("browser");
            url         = props.getProperty("url");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BrowserType getBrowserType() {
        try {
            return BrowserType.valueOf(browserName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid browser name: " + browserName);
        }
    }

    private enum BrowserType {
        CHROME,
        FIREFOX,
        SAFARI
    }


}
