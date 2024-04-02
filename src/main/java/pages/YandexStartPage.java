package pages;

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
    @FindBy(how = How.XPATH, using = "//a[contains(text(),'Ноутбуки') and not(contains(text(), 'книги'))]")
    WebElement notebooksButton;

    public YandexStartPage(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        this.catalogButton = chromeDriver.findElement(By.xpath("//span[contains(text(), 'Каталог')]"));
    }

    public void find() {
        catalogButton.click();
        WebDriverWait wait = new WebDriverWait(chromeDriver, 15);
        wait.until(ExpectedConditions.visibilityOf(getElektronikaButton()));
        new Actions(chromeDriver).moveToElement(getElektronikaButton()).perform();
        notebooksButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Ноутбуки') and not(contains(text(), 'книги'))]")));
        notebooksButton.click();
    }

    public WebElement getElektronikaButton() {
        return chromeDriver.findElement(By.xpath("//div[@data-zone-name='catalog-content']//span[contains(text(), 'Электроника')]"));
    }
}
