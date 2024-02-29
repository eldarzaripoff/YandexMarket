import helper.Notebook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebElement;
import ru.bellIntegrator.NotebookCollector;
//import ru.bellIntegrator.NotebooksListPage;
import ru.bellIntegrator.YandexStartPage;

import java.util.List;
import java.util.stream.Stream;

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

//    @Test
//    public void do12ItemsAppearOnTheFirstPage() throws InterruptedException {
//        String startLink = "https://market.yandex.ru/catalog--noutbuki/54544/list?hid=91013";
//        chromeDriver.get(startLink);
//        String lowPrice = "10000";
//        String highPrice = "30000";
//        NotebooksListPage notebooksListPage = new NotebooksListPage(chromeDriver);
//        notebooksListPage.find(lowPrice, highPrice);
//
//        Assertions.assertTrue(notebooksListPage.sizeOfListOfNotebooks() > 12, "На странице представлено ноутбуков меньше чем 12");
//    }

    @ParameterizedTest
    @MethodSource ("argumentsProvider")
    public void doTheAllElementsMatchTheFilter(Notebook notebook) throws InterruptedException {


//        NotebooksListPage notebooksListPage = new NotebooksListPage(chromeDriver);
//        notebooksListPage.find(lowPrice, highPrice);

        Assertions.assertTrue(notebook.getName().contains("Lenovo")||notebook.getName().contains("HP")||notebook.getName().contains("ThinkPad"),
                "На странице " + notebook.getPageNumber() + " находится ноутбук другого производителя, а именно: " + notebook.getName());

        Assertions.assertTrue(notebook.getPrice() > 10000 && notebook.getPrice() < 30000,
                "На странице " + notebook.getPageNumber() + " находится ноутбук " + notebook.getName() + " с ценой вне заданного фильтра: " + notebook.getPrice());

    }

    public static Stream<Notebook> argumentsProvider() throws InterruptedException {
        String startLink = "https://market.yandex.ru/catalog--noutbuki/54544/list?hid=91013";
        chromeDriver.get(startLink);

        String lowPrice = "10000";
        String highPrice = "30000";

        NotebookCollector notebookCollector = NotebookCollector.getInstance();
        notebookCollector.find(lowPrice, highPrice);



        return notebookCollector.notebookStream();
    }

}
