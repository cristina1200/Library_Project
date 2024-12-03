package view.model;

import javafx.beans.property.*;

public class BookDTO {
    private StringProperty author;

    public void setAuthor(String author) {
        authorProperty().set(author);
    }

    public String getAuthor() {
        return authorProperty().get();
    }

    public StringProperty authorProperty() {
        if (author == null) {
            author = new SimpleStringProperty(this, "author");
        }
        return author;
    }

    private StringProperty title;

    public void setTitle(String title) {
        titleProperty().set(title);
    }

    public String getTitle() {
        return titleProperty().get();
    }

    public StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(this, "title");
        }
        return title;
    }
    public void setPrice(double price) {
        priceProperty().set(price);
    }

    public double getPrice() {
        return priceProperty().get();
    }

    private DoubleProperty price;
    public DoubleProperty priceProperty() {
        if (price == null) {
            price = new SimpleDoubleProperty(this, "price");
        }
        return price;
    }

    private LongProperty stock;

    public void setStock(long stock) {
        stockProperty().set(stock);
    }

    public long getStock() {
        return stockProperty().get();
    }

    public LongProperty stockProperty() {
        if (stock == null) {
            stock = new SimpleLongProperty(this, "stock");
        }
        return stock;
    }
}
