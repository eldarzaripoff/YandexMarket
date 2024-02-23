import helper.Notebook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import ru.bellIntegrator.NotebooksListPage;
import ru.bellIntegrator.YandexStartPage;

import java.util.List;

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
        NotebooksListPage notebooksListPage = new NotebooksListPage(chromeDriver);
        notebooksListPage.find(lowPrice, highPrice);

        Assertions.assertTrue(notebooksListPage.sizeOfListOfNotebooks() > 12, "На странице представлено ноутбуков меньше чем 12");
    }

    @Test
    public void doTheAllElementsMatchTheFilter() throws InterruptedException {
        String startLink = "https://market.yandex.ru/catalog--noutbuki/54544/list?hid=91013";
        chromeDriver.get(startLink);
        String lowPrice = "10000";
        String highPrice = "30000";
        NotebooksListPage notebooksListPage = new NotebooksListPage(chromeDriver);
        notebooksListPage.find(lowPrice, highPrice);

        List<Notebook> list = notebooksListPage.newCollectNotebooks();

        System.out.println(list.size());

    }

}
