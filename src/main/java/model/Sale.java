package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Sale {
    private Long id;
    private String bookTitle;
    private int quantity;
    private Double totalPrice;
    private LocalDateTime orderDate;

    public Sale(Long id, String bookTitle,  int quantity, Double totalPrice, LocalDateTime orderDate) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }
    public Sale(){}

    public Long getId() {
        return id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", bookTitle='" + bookTitle + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                '}';
    }
}
