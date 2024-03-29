package steps;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.NotebookListPage;
import pages.YandexStartPage;

public class StepsAll {
    private static ChromeDriver driver;

    @Step("Переходим на сайт {url}")
    public static void openSite(String url, ChromeDriver currentWebdriver) {
        driver = currentWebdriver;
        driver.get(url);
    }

    @Step("Переход в раздел НОУТБУКИ")
    public static void goToNotepadChapter() {
        YandexStartPage startPage = new YandexStartPage(driver);
        startPage.find();
    }

    @Step("Проверка, что вы перешли в раздел НОУТБУКИ")
    public static void checkAreWeAtNotepadChapter() {
        Assertions.assertTrue(driver.getCurrentUrl().contains("catalog--noutbuki"), "It isn't necessary site");
    }

    @Step("Задаём параметры поиска ноутбуков")
    public static void setFilters(String lowPrice, String highPrice, ChromeDriver driver) {
        NotebookListPage notebookListPage = NotebookListPage.getInstance(driver);
        notebookListPage.initFilters(lowPrice, highPrice);
    }

    @Step("Проверка, что на странице отображено более 12 элементов")
    public static void areThereMoreThan12ElementsOnThePage() {
        NotebookListPage notebookListPage = NotebookListPage.getInstance(driver);
        notebookListPage.checkAre12ElementsAtThePage();
    }

    @Step("Парсинг всех страниц в каталоге Яндекса")
    public static void parsingAllThePages() {
        NotebookListPage notebookListPage = NotebookListPage.getInstance(driver);
        notebookListPage.parsingAllThePages();
    }

    @Step("Проверка содержимого всех страниц на соответствие цене")
    public static void checkingPrice() {
        NotebookListPage notebookListPage = NotebookListPage.getInstance(driver);
        notebookListPage.checkingPriceOfAllThePages();
    }

    @Step("Проверка содержимого всех страниц на соответствие производителю {manufacturer}")
    public static void checkingName(String lenovo, String lenovoCaps, String hpAllCaps, String hpCapsOnlyH, String thinkpad, String thinkPad) {
        NotebookListPage notebookListPage = NotebookListPage.getInstance(driver);
        notebookListPage.checkingNameOfAllThePages(lenovo, lenovoCaps, hpAllCaps, hpCapsOnlyH, thinkpad, thinkPad);
    }

    @Step("Возвращение на первую страницу")
    public static void goBack(ChromeDriver chromeDriver) {
        NotebookListPage notebookListPage = NotebookListPage.getInstance(chromeDriver);
        notebookListPage.rollThePagesBack();
    }
    @Step("Парсинг первой страницы")
    public static void parsingFirstPage() {
        NotebookListPage notebookListPage = NotebookListPage.getInstance(driver);
        notebookListPage.onePageCatalogCleaner();
        notebookListPage.parsingOnePage();
    }
    @Step("Ввод в поисковую строку запомненное значение")
    public static void typeTheRememberedWord() {
        NotebookListPage notebookListPage = NotebookListPage.getInstance(driver);
        notebookListPage.getTheResultsOfSearch(notebookListPage.rememberedWord());
    }
    @Step("Проверка наличия запомненного значения на первой странице")
    public static void checkingTheFirstPageForSearchText() {
        NotebookListPage notebookListPage = NotebookListPage.getInstance(driver);
        notebookListPage.onePageCatalogCleaner();
        notebookListPage.parsingOnePage();
        notebookListPage.checkingForTheSearchText();
    }
}
