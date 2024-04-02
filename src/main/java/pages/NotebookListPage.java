package pages;

import helper.PageItem;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import robust.RobustWebDriver;
import robust.RobustWebElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class NotebookListPage {
    private static NotebookListPage instance;
    private final ChromeDriver chromeDriver;
    private final WebDriverWait wait;
    private final Actions actions;
    public LinkedHashMap<Integer, List<PageItem>> allThePagesCatalog = new LinkedHashMap<>();
    public List<PageItem> onePageCatalog = new ArrayList<>();
    public int currentPage = 0;
    private final RobustWebDriver robustWebDriver;


    private NotebookListPage(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        this.wait = new WebDriverWait(chromeDriver, 22);
        wait.pollingEvery(Duration.ofSeconds(4));
        wait.ignoring(NoSuchElementException.class);
        this.actions = new Actions(chromeDriver);
        robustWebDriver = new RobustWebDriver(chromeDriver);
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
        return chromeDriver.findElement(By.xpath("//label[@data-auto='filter-list-item-152981']//span[not(descendant::span) and not(contains(text(), 'Lenovo'))]"));
    }

    private WebElement getHPButton() {
        return chromeDriver.findElement(By.xpath("//label[@data-auto='filter-list-item-152722']/descendant::span[not(contains(text(), 'HP')) and not(descendant::span)]"));
    }

    private WebElement getRecommendation() {
        return chromeDriver.findElement(By.xpath("//div[@class = '_2q2DD']"));
    }

    private List<WebElement> getItems() {
        return chromeDriver.findElements(By.xpath("//div[@data-apiary-widget-name = '@light/Organic']"));
    }

    private WebElement getName(int i) {
        WebElement webElement = null;
        for (int j = 0; j < 2; j++) {
            webElement = chromeDriver.findElement(By.xpath("(//h3[@data-auto='snippet-title'])[" + i + "]"));
        }
        return webElement;
    }

    private WebElement getPrice(int i) {
        WebElement webElement = null;
        for (int j = 0; j < 3; j++) {
            webElement = chromeDriver.findElement(By.xpath("(//span[@data-auto='snippet-price-current']/descendant::span[not(descendant::span) and not(contains(text(), '₽'))])[" + i + "]"));
        }
        return webElement;
    }

    private List<WebElement> getForward() {
        return chromeDriver.findElements(By.xpath("//span[contains(text(), 'Вперёд')]"));
    }


    private WebElement getCurrentPage(int pageNumber) {
        return chromeDriver.findElement(By.xpath("//div[@data-auto='pagination-page']/div[contains(text(),'" + pageNumber + "')]"));
    }

    public void initFilters(String lowPrice, String highPrice) {

        getLowPriceField().click();
        getLowPriceField().sendKeys(lowPrice);

        getHighPriceField().click();
        getHighPriceField().sendKeys(highPrice);

        getLenovoButton().click();
        getHPButton().click();
    }

    public void parsingAllThePages() {
        currentPage = currentPage + 1;
        allThePagesCatalog.put(currentPage, new ArrayList<>());
        actions.moveToElement(getRecommendation()).perform();
        for (int i = 1; i < getItems().size(); i++) {
            RobustWebElement robustWebElementName = new RobustWebElement(getName(i), robustWebDriver, By.xpath("(//h3[@data-auto='snippet-title'])[" + i + "]"));
            String name = robustWebElementName.getText();
            RobustWebElement robustWebElementPrice = new RobustWebElement(getPrice(i), robustWebDriver, By.xpath("(//span[@data-auto='snippet-price-current']/descendant::span[not(descendant::span) and not(contains(text(), '₽'))])[" + i + "]"));
            String strprice = robustWebElementPrice.getText();
            int price = Integer.parseInt(strprice.replaceAll("[^0-9]", ""));
            allThePagesCatalog.get(currentPage).add(new PageItem(name, price));
        }
        if (!getForward().isEmpty()) {
            actions.moveToElement(getForward().get(0)).click().perform();
            parsingAllThePages();
        }
    }


    public void checkingPriceOfAllThePages() {
        List<PageItem> failedItems = allThePagesCatalog.entrySet().stream().
                flatMap(entry -> entry.getValue().stream()).
                filter(pageItem -> pageItem.getPrice() <= 10000 || pageItem.getPrice() >= 30000).collect(Collectors.toList());
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        failedItems.forEach(pageItem -> joiner.add(pageItem.toString() + System.lineSeparator()));
        String message = "Цена отличается у следующих PageItems: " + joiner;
        Assertions.assertTrue(failedItems.isEmpty(), message);
    }

    public ChromeDriver getChromeDriver() {
        return chromeDriver;
    }

    public void checkingNameOfAllThePages(String lenovo, String lenovoCaps, String hpAllCaps, String hpCapsOnlyH, String thinkpad, String thinkPad, String smallLenovo, String lenRusOvRusO, String lenRusOvo, String lenovRusO, String rusHP, String rusHp, String HRUSP, String smallHP ) {
        List<PageItem> failedItems = allThePagesCatalog.entrySet().stream().
                flatMap(entry -> entry.getValue().stream()).filter(pageItem -> !pageItem.getName().contains(lenovo) &&
                        !pageItem.getName().contains(lenovoCaps) &&
                        !pageItem.getName().contains(hpAllCaps) &&
                        !pageItem.getName().contains(hpCapsOnlyH) &&
                        !pageItem.getName().contains(thinkpad) &&
                        !pageItem.getName().contains(smallLenovo) &&
                        !pageItem.getName().contains(lenRusOvRusO) &&
                        !pageItem.getName().contains(lenovRusO) &&
                        !pageItem.getName().contains(lenRusOvo) &&
                        !pageItem.getName().contains(smallHP) &&
                        !pageItem.getName().contains(HRUSP) &&
                        !pageItem.getName().contains(rusHP) &&
                        !pageItem.getName().contains(rusHp) &&
                        !pageItem.getName().contains(thinkPad))
                .collect(Collectors.toList());
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        failedItems.forEach(pageItem -> joiner.add(pageItem.toString() + System.lineSeparator()));
        String message = "По производителям не соответствуют следующие ноутбуки: " + joiner;
        Assertions.assertTrue(failedItems.isEmpty(), message);
    }

    public void parsingOnePage() {
        actions.moveToElement(getRecommendation()).perform();
        for (int i = 1; i < getItems().size(); i++) {
            RobustWebElement robustWebElementName = new RobustWebElement(getName(i), robustWebDriver, By.xpath("(//h3[@data-auto='snippet-title'])[" + i + "]"));
            String name = robustWebElementName.getText();
            RobustWebElement robustWebElementPrice = new RobustWebElement(getPrice(i), robustWebDriver, By.xpath("(//span[@data-auto='snippet-price-current']/descendant::span[not(descendant::span) and not(contains(text(), '₽'))])[" + i + "]"));
            String strprice = robustWebElementPrice.getText();
            int price = Integer.parseInt(strprice.replaceAll("[^0-9]", ""));
            onePageCatalog.add(new PageItem(name, price));
        }
    }

    public void checkAre12ElementsAtThePage() {
        Assertions.assertTrue(onePageCatalog.size() > 12,
                "На странице представлено ноутбуков меньше чем 12");
    }

    private List<WebElement> getBack() {
        return chromeDriver.findElements(By.xpath("//span[contains(text(), 'Назад')]"));
    }

    private WebElement getBackButton() {
        WebElement webElement = null;
        for (int j = 0; j < 3; j++) {
            webElement = chromeDriver.findElement(By.xpath("//span[contains(text(), 'Назад')]"));
        }
        return webElement;
    }

    public void rollThePagesBack() {
        actions.moveToElement(getRecommendation()).perform();
        String currentUrl = chromeDriver.getCurrentUrl();
        RobustWebElement robustWebElementGetBackButton = new RobustWebElement(getBackButton(), robustWebDriver, By.xpath("//span[contains(text(), 'Назад')]"));
        wait.until(ExpectedConditions.elementToBeClickable(robustWebElementGetBackButton)).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));
        if (!getBack().isEmpty()) {
            rollThePagesBack();
        }
    }

    public String rememberedWord() {
        return onePageCatalog.get(0).getName();
    }

    public WebElement getSearhField() {
        return chromeDriver.findElement(By.xpath("//input[@id='header-search']"));
    }

    public void getTheResultsOfSearch(String notebookName) {
        actions.moveToElement(getSearhField());
        getSearhField().click();
        getSearhField().sendKeys(notebookName);
    }

    public void onePageCatalogCleaner() {
        onePageCatalog.clear();
    }

    public void checkingForTheSearchText() {
        Assertions.assertTrue(onePageCatalog.stream().anyMatch(pageItem -> pageItem.getName().contains(rememberedWord())));
    }
}


