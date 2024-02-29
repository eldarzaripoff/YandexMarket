package helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Notebook {
    WebElement notebook;

    String name;
    int price;
    int pageNumber;
    public Notebook(WebElement notebook, int pageNumber) {
        this.notebook = notebook;
        this.pageNumber = pageNumber;
        //to do
        this.name = notebook.findElement(By.xpath("")).getText();
        this.price = Integer.parseInt(notebook.findElement(By.xpath("")).getText().replaceAll("[^0-9]", ""));
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
