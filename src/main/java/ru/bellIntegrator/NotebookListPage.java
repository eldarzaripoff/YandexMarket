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

public class NotebookListPage {
    /**
     * Инстанс для синглтона
     */
    private static NotebookListPage instance;

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

    private NotebookListPage(ChromeDriver chromeDriver) {
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
    public static synchronized NotebookListPage getInstance(ChromeDriver chromeDriver) {
        if (instance == null) {
            instance = new NotebookListPage(chromeDriver);
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
    public Map<String, Integer> fillNotebookList() {
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
                WebElement lastElement = listOfNotebooks.get(getListSize() - 1);
                actions.moveToElement(lastElement).perform();

                // Обновление размера списка
                currentSize = getListSize();
                count++;
                if (count == 5) {
                    System.out.println("Прошло 5 попыток, но страница не наполнилась до 24");
                    break;
                }
            } while (currentSize > previousSize && getListSize() < 24);
        } else {
            actions.moveToElement(forwardButtons.get(0)).perform();
            boolean isListOfNotebooksStale = ExpectedConditions.stalenessOf(chromeDriver.findElement(By.xpath("//div[@data-known-size='249']"))).apply(chromeDriver);
            if (isListOfNotebooksStale) {
                chromeDriver.findElements(By.xpath("//div[@data-known-size='249']"));
            }
            //wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-known-size='249']")));
            if (isListOfNotebooksStale) {
                chromeDriver.findElements(By.xpath("//div[@data-known-size='249']"));
            }
            while (getListSize() < 24) {
                listOfNotebooks = chromeDriver.findElements(By.xpath("//div[@data-known-size='249']"));
            }
        }

        for (int i = 0; i < getListSize(); ++i) {
            boolean isTitleStale = ExpectedConditions.stalenessOf(chromeDriver.findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-title-header']//span"))).apply(chromeDriver);
            boolean isPriceStale = ExpectedConditions.stalenessOf(chromeDriver.findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-title-header']//span"))).apply(chromeDriver);
            if (isPriceStale) {
                chromeDriver.findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-title-header']//span"));
            }
            //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-price-current']")));
            if (isPriceStale) {
                chromeDriver.findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-title-header']//span"));
            }
            if (isTitleStale) {
                chromeDriver.findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-title-header']//span"));
            }
            chromeDriver.findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-title-header']//span"));
            chromeDriver.findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-title-header']//span"));

            onePageCatalog.put(
                  listOfNotebooks.get(i).findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-title-header']//span")).getText(),
                    listOfNotebooks.get(i).findElement(By.xpath("//div[@data-known-size='249'][" + (i+1) + "]//h3[@data-auto='snippet-price-current']")).getText()
            );
        }
        Map<String, Integer> finishCatalog = mapConverter(onePageCatalog);
        return finishCatalog;
    }

    public Map<String, Integer> mapConverter(Map<String, String> map) {
        Map <String, Integer> integerMap = new HashMap<>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String cleanedValue = entry.getValue().replaceAll("[^0-9]", ""); // убираем все символы кроме цифр
            int integerValue = Integer.parseInt(cleanedValue); // преобразуем очищенное значение в Integer
            integerMap.put(entry.getKey(), integerValue);
        }

        System.out.println("HashMap со значениями Integer:");
        for (Map.Entry<String, Integer> integerEntry : integerMap.entrySet()) {
            System.out.println(integerEntry.getKey() + " = " + integerEntry.getValue());
        }
        System.out.println("Размер HashMap: " + integerMap.size());
        return integerMap;
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
            if (i == 4) {
                System.out.println("Использовали все попытки но кнопку Вперёд не нашли");
            }
        }
        System.out.println("Размер списка с кнопкой Вперёд: " + forwardButtons.size());
        return forwardButtons;
    }
    public List<Map<String,Integer>> allTheCatalog() {
        List<Map<String, Integer>> listOfMaps = new ArrayList<>();
        do {
            listOfMaps.add(fillNotebookList());
            chromeDriver.findElement(By.xpath("//span[@class='_3e9Bd']")).click();
        } while (!getForwardButtons().isEmpty());
        return listOfMaps;
    }



    public int getMapSize(){
        return onePageCatalog.size();
    }
    public int getListSize() {return listOfNotebooks.size();}
}
