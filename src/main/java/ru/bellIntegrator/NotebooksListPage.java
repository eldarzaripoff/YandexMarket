package ru.bellIntegrator;

import helper.Notebook;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class NotebooksListPage {
    static ChromeDriver chromeDriver;
    WebElement lowPriceField;
    WebElement highPriceField;
    WebElement lenovoButton;
    WebElement HPButton;
    WebDriverWait wait;
    List<WebElement> unitedListOfNames;
    List<Notebook> notebookList;

    static List<WebElement> listOfNotebooks;
    List<WebElement> forwardButtons;
    // Создаем объект Actions
    Actions actions;
    boolean emptyPageFlag;
    //Счетчик страниц
    int pageCount = 1;


    public NotebooksListPage(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        this.wait = new WebDriverWait(chromeDriver, 280);
        this.lowPriceField = chromeDriver.findElement(By.xpath("//div[@data-auto='filter-range-glprice']/descendant::span[@data-auto='filter-range-min']/descendant::input[@type='text']"));
        this.highPriceField = chromeDriver.findElement(By.xpath("//div[@data-auto='filter-range-glprice']/descendant::span[@data-auto='filter-range-max']/descendant::input[@type='text']"));
        this.lenovoButton = chromeDriver.findElement(By.xpath("//label[@data-auto='filter-list-item-152981']/descendant::span[@class='_1Mp5C']"));
        this.HPButton = chromeDriver.findElement(By.xpath("//label[@data-auto='filter-list-item-152722']/descendant::span[@class='_1Mp5C']"));
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
        this.forwardButtons = chromeDriver.findElements(By.xpath("//span[@class='_3e9Bd']"));
        this.actions = new Actions(chromeDriver);
        this.emptyPageFlag = false;
    }

    public void checkIsItEnable(WebElement webElement) {
        if (webElement.isDisplayed()) {
            System.out.println("Кнопка '" + webElement.getText() + "' доступна для нажатия");
        } else {
            System.out.println("Кнопка '" + webElement.getText() + "' недоступна для нажатия");
        }
    }

    public void checkIsItTyped(WebElement webElement, String text) {
        // Получаем текст из поля ввода
        String enteredText = webElement.getAttribute("value");

        // Проверяем, что введенный текст не пустой
        if (!enteredText.isEmpty()) {
            System.out.println("Текст '" + text + "' успешно введен в поле ввода.");
        } else {
            System.out.println("Поле ввода пустое.");
        }
    }

    public void find(String lowPrice, String highPrice) throws InterruptedException {

        wait.until(visibilityOfElementLocated(By.xpath("//div[@data-auto='filter-range-glprice']/descendant::span[@data-auto='filter-range-min']/descendant::input[@type='text']")));
        checkIsItEnable(lowPriceField);
        lowPriceField.click();
        lowPriceField.sendKeys(lowPrice);
        checkIsItTyped(lowPriceField, lowPrice);

        highPriceField.sendKeys(highPrice);
        checkIsItTyped(highPriceField, highPrice);

        lenovoButton.click();
        HPButton.click();

    }

    public List<Notebook> newCollectNotebooks() throws InterruptedException {
        unitedListOfNames = new ArrayList<>();

        while (!forwardButtons.isEmpty() && !emptyPageFlag) {

            //Залили из списка страницы элементы в общий список
            unitedListOfNames.addAll(listFromOnePage());

            //Кликнули на следующую страницу
            clickToTheNextPage();

            // "Обновили" проверочную кнопку "Вперёд"
            forwardButtons = chromeDriver.findElements(By.xpath("//span[@class='_3e9Bd']"));
        }
        if (!unitedListOfNames.isEmpty()) {
            listOfNotebooks = chromeDriver.findElements(By.xpath("//span[@class='_1E10J _2o124 _1zh3_']"));
            notebookList = createNotebooks(unitedListOfNames);
        }
        return notebookList;
    }

    public List<WebElement> listFromOnePage() throws InterruptedException {
        //Скроллим для того, чтобы список увидел элементы
        for (int i = 0; i < 10; i++) {
            try {
                // Подождали полную загрузку страницы
                new WebDriverWait(chromeDriver, 3000).until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
                // "Обновили" кнопку "Вперёд"
                forwardButtons = chromeDriver.findElements(By.xpath("//span[@class='_3e9Bd']"));
                if (forwardButtons.size() > 0) {
                    WebElement forwardButton = forwardButtons.get(0);
                    if (forwardButton != null) {
                        new WebDriverWait(chromeDriver, 3000).until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
                        //wait.until(ExpectedConditions.elementToBeClickable(forwardButton));
                        // Перемещаем курсор мыши к элементу
                        actions.moveToElement(forwardButton).perform();
                        new WebDriverWait(chromeDriver, 3000).until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
                    } else {
                        System.out.println("Кнопка 'forwardButton' равна NULL в listFromOnePage()");
                        System.out.println("КНОПКИ ВНИЗ для списка НЕ ОБНАРУЖЕНО!!!");
                        System.out.println("ДОСРОЧНО ЗАВЕРШАЮ ЦИКЛ!");
                        break; // Выход из цикла
                    }
                } else {
                    System.out.println("Кнопка 'forwardButton' не найдена. Размер forwardButtons.size() равен 0.");
                    System.out.println("КНОПКИ ВНИЗ НЕ ОБНАРУЖЕНО!!!");
                    System.out.println("ДОСРОЧНО ЗАВЕРШАЮ ЦИКЛ!");
                    break; // Выход из цикла
                }
            } catch (StaleElementReferenceException exception) {
                System.out.println("----------------------------------------------------------------");
                System.out.println("Eldar, attention!!! StaleElementReferenceException exception.");
                System.out.println("----------------------------------------------------------------");
                System.out.println(Arrays.toString(exception.getStackTrace()));
                System.out.println("----------------------------------------------------------------");
                System.out.println();
                break;
            }
            //Пытаемся налить список
            try {

                // Подождали полную загрузку страницы
                new WebDriverWait(chromeDriver, 3000).until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));

                // Заполнили список
                listOfNotebooks = chromeDriver.findElements(By.xpath("//span[@class='_1E10J _2o124 _1zh3_']"));

                // Условия:
                // Если listOfNotebooks полностью прогрузился, то досрочно выходим из цикла
                if (listOfNotebooks.size() == 48) {
                    break;
                }
                // Если listOfNotebooks пустой, то выходим из цикла, флаг МЕНЯЕТСЯ!
                if (listOfNotebooks.isEmpty()) {
                    emptyPageFlag = true;
                    System.out.println("---------------------------------------");
                    System.out.println("Страница #" + pageCount + " ПУСТАЯ!");
                    System.out.println("---------------------------------------");
                    break;
                }
            } catch (StaleElementReferenceException exception) {
                System.out.println("----------------------------------------------------------------");
                System.out.println("Eldar, attention!!! StaleElementReferenceException exception.");
                System.out.println("----------------------------------------------------------------");
                System.out.println(Arrays.toString(exception.getStackTrace()));
                System.out.println("----------------------------------------------------------------");
                System.out.println();
                break;
            }
        }
        System.out.println("=====================================================");
        System.out.println("В методе listFromOnePage() список имеет следующий размер: " + listOfNotebooks.size() + " страница №:" + pageCount);
        listOfNotebooks = chromeDriver.findElements(By.xpath("//span[@class='_1E10J _2o124 _1zh3_']"));
        infoAboutList(pageCount, listOfNotebooks);
        pageCount++;
        return listOfNotebooks;
    }

    public void infoAboutList(int pageCount, List<WebElement> listOfNotebooks) {
        System.out.println("---------------------------------------");
        new WebDriverWait(chromeDriver, 3000).until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
        System.out.println();
        System.out.println("Вот список элементов с данной страницы:");
        // Обновляю поиск элемента из-за StaleElementReferenceException
        listOfNotebooks = chromeDriver.findElements(By.xpath("//span[@class='_1E10J _2o124 _1zh3_']"));
        int notebookCounter = 0;
        for (WebElement web : listOfNotebooks) {
            notebookCounter++;
            System.out.println(notebookCounter + ") " + web.getText());
        }
        System.out.println();
        System.out.println("Размер listOfNotebooks со страницы " + pageCount + " равен: " + listOfNotebooks.size());
        System.out.println("---------------------------------------");
        System.out.println();
    }

    public void clickToTheNextPage() {
        try {
            // Подождали полную загрузку страницы
            new WebDriverWait(chromeDriver, 3000).until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
            // "Обновили" кнопку "Вперёд"
            forwardButtons = chromeDriver.findElements(By.xpath("//span[@class='_3e9Bd']"));
            if (forwardButtons.size() > 0) {
                WebElement forwardButton = forwardButtons.get(0);
                if (forwardButton != null || forwardButton.isEnabled()) {
                    wait.until(ExpectedConditions.elementToBeClickable(forwardButton));
                    Thread.sleep(10000);
                    // Перемещаем курсор мыши к элементу
                    forwardButton.click();
                } else {
                    System.out.println("Кнопка 'forwardButton' равна NULL в listFromOnePage()");
                    System.out.println("Кнопка ВПЕРЁД НЕ НАЙДЕНА!!!");
                    System.out.println("ДОСРОЧНО ЗАВЕРШАЮ ЦИКЛ!");
                }
            } else {
                System.out.println("Кнопка 'forwardButton' не найдена. Размер forwardButtons.size() равен 0.");
                System.out.println("Кнопка ВПЕРЁД НЕ НАЙДЕНА!!!");
                System.out.println("ДОСРОЧНО ЗАВЕРШАЮ ЦИКЛ!");
            }
        } catch (StaleElementReferenceException exception) {
            System.out.println("----------------------------------------------------------------");
            System.out.println("WARNING!!! StaleElementReferenceException exception.");
            System.out.println("----------------------------------------------------------------");
            System.out.println(Arrays.toString(exception.getStackTrace()));
            System.out.println("----------------------------------------------------------------");
            System.out.println();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int sizeOfListOfNotebooks() throws InterruptedException {
        return listFromOnePage().size();
    }

    public static List<Notebook> createNotebooks(List<WebElement> names) {
        List<Notebook> notebooks = new ArrayList<>();

        if (names.isEmpty()) {
            System.out.println("Список с ноутбуками ПУСТОЙ");
            return null;
        }

        // Проверка на равное количество элементов в списках names и prices
//        if (names.size() != prices.size()) {
//            System.out.println("Ошибка: количество названий ноутбуков не соответствует количеству цен");
//            return notebooks;
//        }

        // Заполнение коллекции объектов Notebook данными из списков names и prices
        for (int i = 0; i < names.size(); i++) {
            listOfNotebooks = chromeDriver.findElements(By.xpath("//span[@class='_1E10J _2o124 _1zh3_']"));
            String notebookName = names.get(i).getText();
            //int notebookPrice = Integer.parseInt(prices.get(i).getText().replaceAll("[^0-9]", ""));
            Notebook notebook = new Notebook(notebookName);
            notebooks.add(notebook);
        }
        for (int i = 0; i < notebooks.size(); i++) {
            System.out.println("===============================================");
            System.out.println((i+1) + ") " + notebooks.get(i).getName());
            System.out.println("===============================================");
        }
        return notebooks;
    }
}
