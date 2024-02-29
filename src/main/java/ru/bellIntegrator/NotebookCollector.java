package ru.bellIntegrator;

import helper.Notebook;
import org.junit.jupiter.params.provider.Arguments;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class NotebookCollector {
    /**
     * Инстанс для синглтона
     */
    private static NotebookCollector instance;

    /**
     * Возвращаемый список "Ноутбуков" для параметризированного теста
     */
    List<Notebook> notebookList;
    /**
     * Хромдрайвер
     */
    static ChromeDriver chromeDriver;
    /**
     * Поле для ввода минимальной суммы
     */
    WebElement lowPriceField;
    /**
     * Поле для ввода максимальной суммы
     */
    WebElement highPriceField;
    /**
     * Поле для галочки выбора производителя Леново
     */
    WebElement lenovoButton;
    /**
     * Поле для галочки выбора производителя НР
     */
    WebElement HPButton;
    /**
     * Объект ожидания
     */
    WebDriverWait wait;
    /**
     * Объект действия
     */
    Actions actions;
    /**
     * Флаг, сигнализирующий пустая ли страница или нет
     */
    boolean emptyPageFlag;
    /**
     * Счётчик страниц
     */
    int pageCount = 1;

    private NotebookCollector() {
        this.lowPriceField = chromeDriver.findElement(By.xpath("//div[@data-auto='filter-range-glprice']/descendant::span[@data-auto='filter-range-min']/descendant::input[@type='text']"));
        this.highPriceField = chromeDriver.findElement(By.xpath("//div[@data-auto='filter-range-glprice']/descendant::span[@data-auto='filter-range-max']/descendant::input[@type='text']"));
        this.lenovoButton = chromeDriver.findElement(By.xpath("//label[@data-auto='filter-list-item-152981']/descendant::span[@class='_1Mp5C']"));
        this.HPButton = chromeDriver.findElement(By.xpath("//label[@data-auto='filter-list-item-152722']/descendant::span[@class='_1Mp5C']"));
        this.wait = new WebDriverWait(chromeDriver, 25);
        this.actions = new Actions(chromeDriver);
        this.emptyPageFlag = false;
    }

    /**
     * Этот метод выполняет создание объекта NotebookCollector.
     * @return объект NotebookCollector
     */
    public static synchronized NotebookCollector getInstance() {
        if (instance == null) {
            instance = new NotebookCollector();
        }
        return instance;
    }
    /**
     * Этот метод выполняет настройку фильтров на странице Яндекса.
     * @param highPrice поле с наибольшей возможной суммой
     * @param lowPrice поле с наименьшей возможной суммой
     * lenovoButton и HPButton - поля для галочек с производителями НР и Lenovo
     * @return объект NotebookCollector
     */
    public void find(String lowPrice, String highPrice) throws InterruptedException {

        //wait.until(visibilityOfElementLocated(By.xpath("//div[@data-auto='filter-range-glprice']/descendant::span[@data-auto='filter-range-min']/descendant::input[@type='text']")));
        lowPriceField.click();
        lowPriceField.sendKeys(lowPrice);

        highPriceField.click();
        highPriceField.sendKeys(highPrice);

        lenovoButton.click();
        HPButton.click();

    }

    public void fillNotebookList() {
        //to do
    }

    public Stream<Notebook> notebookStream() {
        return notebookList.stream();
    }




}
