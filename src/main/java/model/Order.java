package model;

import java.sql.Timestamp;

public class Order {
    private int id;
    private String bookTitle;
    private int quantity;
    private String sellerName;
    ;
    private double totalPrice;
    private Timestamp orderDate;

    public Order(int id, String bookTitle, int quantity, String sellerName, double totalPrice, Timestamp orderDate) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.sellerName = sellerName;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public String getSellerName() {
        return sellerName;

    }
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Timestamp getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", bookTitle='" + bookTitle + '\'' +
                ", quantity=" + quantity +
                ", sellerName='" + sellerName + '\'' +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                '}';
    }
}
