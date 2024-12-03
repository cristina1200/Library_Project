package repository.sale;

import model.Book;
import model.Sale;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleRepositoryMySQL implements SaleRepository {
    private Connection connection;
    public SaleRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void save(Sale sale) {
        String query = "INSERT INTO sales (book_id, sale_date, price) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, sale.getBook().getId());
            ps.setDate(2, Date.valueOf(sale.getSaleDate()));
            ps.setDouble(3, sale.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Sale> findAll() {
        List<Sale> sales = new ArrayList<>();
        String query = "SELECT * FROM sales";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                LocalDate saleDate = rs.getDate("sale_date").toLocalDate();
                double price = rs.getDouble("price");

                Book book = new Book();
                sales.add(new Sale(book, saleDate, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }
}