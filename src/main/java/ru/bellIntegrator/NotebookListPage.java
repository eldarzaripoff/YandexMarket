package ru.bellIntegrator;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class NotebookListPage {
    private static NotebookListPage instance;
    private ChromeDriver chromeDriver;
    private WebDriverWait wait;
    private Actions actions;
    public Dictionary<String, List<PageItem>> onePageCatalog = new Hashtable<>();
    private int currentPage = 0;

    private NotebookListPage(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        this.wait = new WebDriverWait(chromeDriver, 22);
        wait.pollingEvery(Duration.ofSeconds(4));
        wait.ignoring(NoSuchElementException.class);
        this.actions = new Actions(chromeDriver);
    }

    public static synchronized NotebookListPage getInstance(ChromeDriver chromeDriver) {
        if (instance == null) {
            instance = new NotebookListPage(chromeDriver);
        }
        return instance;
    }
    private WebElement getLowPriceField() {
        return chromeDriver.findElement(By.xpath("//div[@data-auto='filter-range-glprice']/descendant::span[@data-auto='filter-range-min']/descendant::input[@type='text']"));
    }

    private WebElement getHighPriceField() {
        return chromeDriver.findElement(By.xpath("//div[@data-auto='filter-range-glprice']/descendant::span[@data-auto='filter-range-max']/descendant::input[@type='text']"));
    }

    private WebElement getLenovoButton() {
        return chromeDriver.findElement(By.xpath("//label[@data-auto='filter-list-item-152981']/descendant::span[@class='_1Mp5C']"));
    }

    private WebElement getHPButton(){
        return chromeDriver.findElement(By.xpath("//label[@data-auto='filter-list-item-152722']/descendant::span[@class='_1Mp5C']"));
    }

    private WebElement getRecommendation() {
        return chromeDriver.findElement(By.xpath("//div[@class = '_2q2DD']"));
    }

    private List<WebElement> getItems() {
        return chromeDriver.findElements(By.xpath("//div[@data-apiary-widget-name = '@light/Organic']"));
    }
    private WebElement getName(int i) {
        return chromeDriver.findElement(By.xpath("(//h3[@data-auto='snippet-title'])[" + i + "]"));
    }
    private WebElement getPrice(int i) {
        return chromeDriver.findElement(By.xpath("(//span[@class='_3MgRl'])[" + 1 + "]"));
    }

    private List<WebElement> getForward() {
        return chromeDriver.findElements(By.xpath("//span[@class='_3e9Bd']"));
    }


    private WebElement getCurrentPage() {
        return chromeDriver.findElement(By.xpath("//div/div[@class='Xe4rX _18sEx _3-NJO']"));
    }

    public void initFilters(String lowPrice, String highPrice) throws InterruptedException {

        getLowPriceField().click();
        getLowPriceField().sendKeys(lowPrice);

        getHighPriceField().click();
        getHighPriceField().sendKeys(highPrice);

        getLenovoButton().click();
        getHPButton().click();
    }

    public void Parsing() {
        currentPage = currentPage + 1;
        onePageCatalog.put(Integer.toString(currentPage), new ArrayList<>());
        actions.moveToElement(getRecommendation()).perform();
        for (int i = 0; i < getItems().size(); ++i) {
            String name = getName(i+1).getText();
            //String name = getItems().get(i).findElement(By.xpath("//h3[@data-auto='snippet-title']")).getText();
            String strprice = getPrice(i+1).getText();
            //String stringPrice = getItems().get(i).findElement(By.xpath("//span[@class='_3MgRl']")).getText().replaceAll("[^0-9]", "");
            int price = Integer.parseInt(strprice.replaceAll("[^0-9]", ""));
            onePageCatalog.get(Integer.toString(currentPage)).add(new PageItem(name, price));
        }
        if (!getForward().isEmpty()) {
            getForward().get(0).click();
            wait.until(d -> Integer.parseInt(getCurrentPage().getText()) != currentPage);
            Parsing();
        }

    }
}


