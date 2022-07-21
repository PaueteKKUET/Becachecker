package com.fadedbytes.becachecker;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Start {

    public static void main(String[] args) {

        // Setup
        Setup.setup();
        WebDriver browser = Setup.getDriver();

        // Go to the home page
        browser.get(Setup.getHomeURL());

        // Login
        WebElement username     = browser.findElement(By.ByCssSelector.cssSelector("#login"));
        WebElement password     = browser.findElement(By.ByCssSelector.cssSelector("#clave"));
        WebElement loginButton  = browser.findElement(By.ByCssSelector.cssSelector("#boton_entrar"));

        username.sendKeys(Setup.getUserName());
        password.sendKeys(Setup.getPassword());

        loginButton.click();

        // Check for login errors
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

        // If there are errors, exit
        if (hasErrors) {
            browser.quit();
            return;
        }

        // Get the procedure list
        WebElement procedures = browser.findElement(By.ByCssSelector.cssSelector("#app-contenedor-formulario > div"));

        List<WebElement> elements = procedures.findElements(By.xpath("./div"));
        WebElement procedureList = elements
                .stream()
                .filter(e -> e.getAttribute("class").matches(".*listado.*"))
                .findFirst()
                .orElse(null);

        // If there are no procedures, exit
        if (procedureList == null) {
            System.out.println("No procedures found");
            browser.quit();
            return;
        }

        // Check all procedures, search for the one we want and store it
        List<WebElement> proceduresList = procedureList.findElements(By.xpath("./div"));
        WebElement matchingProcedureButton = null;

        for (int i = 0; i < proceduresList.size(); i++) {
            WebElement procedure = proceduresList.get(i);

            String procedureName = procedure.findElement(By.xpath("./p")).getText();

            if (procedureName.toLowerCase().equals(Setup.getProcedure().toLowerCase())) {
                matchingProcedureButton = procedure.findElement(By.cssSelector("#listaConvocatoriaForm" + i));
                break;
            }
        }

        // If there is no matching procedure, exit
        if (matchingProcedureButton == null) {
            System.out.println("No matching procedure found");
            browser.quit();
            return;
        }

        matchingProcedureButton.click();

        List<WebElement> procedureData = browser.findElements(By.cssSelector("#contenido-aplicacion > div.resultados.solicitudes > div.listado > div > ul > li"));

        String status = null;
        for (WebElement dataContainer : procedureData) {
            String data = dataContainer.getText();

            if (data.matches(".*Estado:.*")) {
                status = data.replace("Estado:", "").trim();
            }
        }

        if (status == null) {
            System.out.println("No status found");
            browser.quit();
            return;
        }

        if (status.equals(Setup.getStatus())) {
            System.out.println("Nothing changed");
        } else {
            System.out.println("Status changed to: " + status);
        }

        browser.quit();

    }

}
