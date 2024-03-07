package ru.bellIntegrator;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotebookCollector {
    /**
     * Инстанс для синглтона
     */
    private static NotebookCollector instance;

    /**
     * Хромдрайвер
     */
    ChromeDriver chromeDriver;
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
    List<WebElement> listOfNotebooks = new ArrayList<>();
    List<WebElement> forwardButtons;
    Map<String, String> onePageCatalog = new HashMap<>();

    private NotebookCollector(ChromeDriver chromeDriver) {
        this.lowPriceField = chromeDriver.findElement(By.xpath("//div[@data-auto='filter-range-glprice']/descendant::span[@data-auto='filter-range-min']/descendant::input[@type='text']"));
        this.highPriceField = chromeDriver.findElement(By.xpath("//div[@data-auto='filter-range-glprice']/descendant::span[@data-auto='filter-range-max']/descendant::input[@type='text']"));
        this.lenovoButton = chromeDriver.findElement(By.xpath("//label[@data-auto='filter-list-item-152981']/descendant::span[@class='_1Mp5C']"));
        this.HPButton = chromeDriver.findElement(By.xpath("//label[@data-auto='filter-list-item-152722']/descendant::span[@class='_1Mp5C']"));
        this.wait = new WebDriverWait(chromeDriver, 27);
        this.actions = new Actions(chromeDriver);
        this.emptyPageFlag = false;
        this.chromeDriver = chromeDriver;
    }

    /**
     * Этот метод выполняет создание объекта NotebookCollector.
     * @return объект NotebookCollector
     */
    public static synchronized NotebookCollector getInstance(ChromeDriver chromeDriver) {
        if (instance == null) {
            instance = new NotebookCollector(chromeDriver);
        }
        return instance;
    }
    /**
     * Этот метод выполняет настройку фильтров на странице Яндекса.
     * @param highPrice поле с наибольшей возможной суммой
     * @param lowPrice поле с наименьшей возможной суммой
     * lenovoButton и HPButton - поля для галочек с производителями НР и Lenovo
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
    /**
     * Этот метод выполняет собирает список ноутбуков с одной страницы Яндекс Маркета.
     */
    public void fillNotebookList() {
        forwardButtons = getForwardButtons();

        if(forwardButtons.isEmpty()) {
            System.out.println("Кнопку не дождались, поехали по циклу ду-вайл");

            int previousSize = 0;
            int currentSize = 0;
            int count = 1;

            do {
                previousSize = listOfNotebooks.size();
                listOfNotebooks.clear();

                // Заполнение списка текущими элементами каталога
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-known-size='249']")));
                List<WebElement> currentElements = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    currentElements = chromeDriver.findElements(By.xpath("//div[@data-known-size='249']"));
                    if (!currentElements.isEmpty()) {
                        break;
                    }
                }
                listOfNotebooks.addAll(currentElements);
                // Прокрутка к последнему элементу
                WebElement lastElement = listOfNotebooks.get(listOfNotebooks.size() - 1);
                actions.moveToElement(lastElement).perform();

                // Обновление размера списка
                currentSize = listOfNotebooks.size();
                count++;
                if (count == 5) {
                    System.out.println("Прошло 5 попыток, но страница не наполнилась до 24");
                    break;
                }
            } while (currentSize > previousSize && listOfNotebooks.size() < 24);
        } else {
            actions.moveToElement(forwardButtons.get(0)).perform();
            //wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-known-size='249']")));
            while (listOfNotebooks.size() < 24) {
                listOfNotebooks = chromeDriver.findElements(By.xpath("//div[@data-known-size='249']"));
            }
        }
        //System.out.println("ListOfNotebooks.size is: " + getListSize());


        for (int i = 0; i < getListSize(); ++i) {
            //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-title-header']//span")));
//            try {
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-price-current']")));
//            } catch (TimeoutException e) {
//                e.printStackTrace();
//                listOfNotebooks = chromeDriver.findElements(By.xpath("//div[@data-known-size='249']"));
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-price-current']")));
//            }
            //listOfNotebooks = chromeDriver.findElements(By.xpath("//div[@data-known-size='249']"));
            actions.moveToElement(chromeDriver.findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]")));
            onePageCatalog.put(
                  listOfNotebooks.get(i).findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-title-header']//span")).getText(),
                    listOfNotebooks.get(i).findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-price-current']")).getText()
            );
            //System.out.println("Counter i is: " + i);
        }

        // Вывод результата
        System.out.println("Размер мапы: " + onePageCatalog.size());


    }

    public List<WebElement> getForwardButtons() {
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='_3e9Bd']")));
        } catch (TimeoutException e) {
            System.out.println("Не дождались кнопки Вперёд");
            e.printStackTrace();
        }
        for (int i = 0; i < 5; i++) {
            forwardButtons = chromeDriver.findElements(By.xpath("//span[@class='_3e9Bd']"));
            System.out.println("Попытка №" + (i+1) + " использована");
            if (forwardButtons.size() > 0) {
                break;
            }
            if (i == 4 && forwardButtons.size() == 0) {
                System.out.println("Использовали все попытки но кнопку Вперёд не нашли");
            }
        }
        System.out.println("Размер списка с кнопкой Вперёд: " + forwardButtons.size());
        return forwardButtons;
    }




    public int getMapSize(){
        return onePageCatalog.size();
    }
    public int getListSize() {return listOfNotebooks.size();}
}
