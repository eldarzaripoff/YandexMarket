import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.bellIntegrator.NotebookListPage;
import ru.bellIntegrator.YandexStartPage;

import java.util.List;
import java.util.Map;

public class Tests extends BaseClass {
    @Test
    public void isItNotepadPage() {
        String startLink = "https://market.yandex.ru/";
        chromeDriver.get(startLink);
        YandexStartPage startPage = new YandexStartPage(chromeDriver);
        startPage.find();
        System.out.println(chromeDriver.getCurrentUrl());
        Assertions.assertTrue(chromeDriver.getCurrentUrl().contains("catalog--noutbuki"), "It isn't necessary site");

    }

    @Test
    public void do12ItemsAppearOnTheFirstPage() throws InterruptedException {
        String startLink = "https://market.yandex.ru/catalog--noutbuki/54544/list?hid=91013";
        chromeDriver.get(startLink);
        String lowPrice = "10000";
        String highPrice = "30000";
        NotebookListPage notebookListPage = NotebookListPage.getInstance(chromeDriver);
        notebookListPage.find(lowPrice, highPrice);
        Map<String, Integer> map = notebookListPage.fillNotebookList();

        Assertions.assertTrue(map.size() > 12,
                "На странице представлено ноутбуков меньше чем 12");
    }

//    @ParameterizedTest
//    @MethodSource ("argumentsProvider")
    @Test
    public void doTheAllElementsMatchTheFilter() throws InterruptedException {
        String startLink = "https://market.yandex.ru/catalog--noutbuki/54544/list?hid=91013";
        chromeDriver.get(startLink);
        String lowPrice = "10000";
        String highPrice = "30000";
        NotebookListPage notebookListPage = NotebookListPage.getInstance(chromeDriver);
        notebookListPage.find(lowPrice, highPrice);

        List<Map<String, Integer>> mapList = notebookListPage.allTheCatalog();
        System.out.println(mapList.size());
     }



}
