package com.fadedbytes.becachecker;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Start {

    public static void main(String[] args) {

        Setup.setup();

        WebDriver browser = Setup.getDriver();

        browser.get(Setup.getHomeURL());

        WebElement username     = browser.findElement(By.ByCssSelector.cssSelector("#login"));
        WebElement password     = browser.findElement(By.ByCssSelector.cssSelector("#clave"));
        WebElement loginButton  = browser.findElement(By.ByCssSelector.cssSelector("#boton_entrar"));

        username.sendKeys(Setup.getUserName());
        password.sendKeys(Setup.getPassword());

        loginButton.click();

        boolean hasErrors = false;
        if (browser.findElements(By.ByCssSelector.cssSelector("#infoError > ul > li:nth-child(1) > span")).size() > 0) {
            hasErrors = true;
            WebElement firstError   = browser.findElement(By.ByCssSelector.cssSelector("#infoError > ul > li:nth-child(1) > span"));

            System.out.println(firstError.getText());
            String error = firstError.getText();
            if (error.toLowerCase().matches(".*contraseña incorrecta.*")) {
                // TODO: ask for new password
            } else if (error.toLowerCase().matches(".*usuario no registrado.*")) {
                // TODO: ask for new credentials
            }
        }

        if (hasErrors) browser.quit();

    }

}
