package helper;

public class PageItem {
    String name;
    int price;

    public PageItem(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "Ноутбук " + getName() + " стоимостью " + getPrice();
    }
}
