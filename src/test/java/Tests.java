import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.bellIntegrator.NotebookCollector;
//import ru.bellIntegrator.NotebooksListPage;
import ru.bellIntegrator.YandexStartPage;

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
        NotebookCollector notebookCollector = NotebookCollector.getInstance(chromeDriver);
        notebookCollector.find(lowPrice, highPrice);
        notebookCollector.fillNotebookList();

        Assertions.assertTrue(notebookCollector.getMapSize() > 12,
                "На странице представлено ноутбуков меньше чем 12");
    }

    @ParameterizedTest
    @MethodSource ("argumentsProvider")
    public void doTheAllElementsMatchTheFilter() throws InterruptedException {

     }



}
