import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static steps.StepsAll.*;

public class Tests extends BaseClass {
    @Feature("Проверка раздела на то, что это раздел 'Ноутбуки'")
    @DisplayName("Проверка раздела на то, что это раздел 'Ноутбуки'")
    @Test
    public void isItNotepadPage() {
        String startLink = "https://market.yandex.ru/";
        openSite(startLink, chromeDriver);
        goToNotepadChapter();
        checkAreWeAtNotepadChapter();
    }
    @Feature("Есть ли на странице хотя бы 12 ноутбуков")
    @DisplayName("Есть ли на странице хотя бы 12 ноутбуков")
    @Test
    public void do12ItemsAppearOnTheFirstPage() {
        String startLink = "https://market.yandex.ru/catalog--noutbuki/54544/list?hid=91013";
        chromeDriver.get(startLink);
        String lowPrice = "10000";
        String highPrice = "30000";
        setFilters(lowPrice, highPrice, chromeDriver);
        parsingFirstPage();
        areThereMoreThan12ElementsOnThePage();
    }
    @Feature("Соответствуют ли ноутбуки фильтрам на всех страницах?")
    @DisplayName("Соответствуют ли ноутбуки фильтрам на всех страницах?")
    @ParameterizedTest
    @MethodSource("helper.DataProvider#checkingNameAndPriceProvider")
    public void doTheAllElementsMatchTheFilter(String lenovo, String lenovoCaps, String hpAllCaps, String hpCapsOnlyH, String thinkpad, String thinkPad) {
        String startLink = "https://market.yandex.ru/catalog--noutbuki/54544/list?hid=91013";
        chromeDriver.get(startLink);
        String lowPrice = "10000";
        String highPrice = "30000";
        setFilters(lowPrice, highPrice, chromeDriver);
        parsingAllThePages();
        checkingName(lenovo, lenovoCaps, hpAllCaps, hpCapsOnlyH, thinkpad, thinkPad);
        checkingPrice();
    }

    @Feature("Проверка, что в результатах поиска, на первой странице, есть искомый товар")
    @DisplayName("Проверка, что в результатах поиска, на первой странице, есть искомый товар")
    @Test
    public void isItemAtTheFirstPage() {
        String startLink = "https://market.yandex.ru/catalog--noutbuki/54544/list?hid=91013";
        chromeDriver.get(startLink);
        String lowPrice = "10000";
        String highPrice = "30000";
        setFilters(lowPrice, highPrice, chromeDriver);
        parsingAllThePages();
        goBack(chromeDriver);
        parsingFirstPage();
        typeTheRememberedWord();
        parsingFirstPage();
        checkingTheFirstPageForSearchText();
    }
}
