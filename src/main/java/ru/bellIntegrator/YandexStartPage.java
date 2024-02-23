package ru.bellIntegrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YandexStartPage {
    protected ChromeDriver chromeDriver;
    WebElement catalogButton;
    @FindBy(how = How.XPATH, using = "//span[@class='_3krX4'][contains(text(),'Ноутбуки и компьютеры')]")
    WebElement notebooksAndComputersButton;
    @FindBy(how = How.XPATH, using = "//a[@class='egKyN _1mqvV _1wg9X'][contains(text(),'Ноутбуки')]")
    WebElement notebooksButton;

    public YandexStartPage(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        this.catalogButton = chromeDriver.findElement(By.xpath("//button[@class='V9Xu6 button-focus-ring _1KI8u Lfy7z _3iB1w _35Vhw']"));
    }

    public void find() {
        checkIsItEnable(catalogButton);
        catalogButton.click();

        WebDriverWait wait = new WebDriverWait(chromeDriver, 180);
        notebooksAndComputersButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='_3krX4'][contains(text(),'Ноутбуки и компьютеры')]")));

        checkIsItDisplayed(notebooksAndComputersButton);
        new Actions(chromeDriver).moveToElement(notebooksAndComputersButton).perform();

        notebooksButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='egKyN _1mqvV _1wg9X'][contains(text(),'Ноутбуки')]")));

        checkIsItEnable(notebooksButton);
        notebooksButton.click();
    }
    public void checkIsItDisplayed(WebElement webElement) {
        if(webElement.isDisplayed()) {
            System.out.println("Кнопка '" + webElement.getText() + "' отображается на экране");
        } else {
            System.out.println("Кнопка '" + webElement.getText() + "' не отображается на экране");
        }
    }

    public void checkIsItEnable(WebElement webElement) {
        if(webElement.isDisplayed()) {
            System.out.println("Кнопка '" + webElement.getText() + "' доступна для нажатия");
        } else {
            System.out.println("Кнопка '" + webElement.getText() + "' недоступна для нажатия");
        }
    }

}
