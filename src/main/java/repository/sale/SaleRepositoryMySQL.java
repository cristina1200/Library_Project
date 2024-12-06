package repository.sale;

import model.Book;
import model.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleRepositoryMySQL implements SaleRepository {
    public final Connection connection;
    public SaleRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }
    @Override
    public boolean save(Sale sale) {
        String query = "INSERT INTO `orders` (book_title, quantity,seller_name, total_price, order_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sale.getBookTitle());
            preparedStatement.setInt(2, sale.getQuantity());
            preparedStatement.setString(3, sale.getSellerName());
            preparedStatement.setDouble(4, sale.getTotalPrice());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(sale.getOrderDate()));
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Sale> findAll() {
        String query = "SELECT * FROM `orders`";
        List<Sale> sales = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Sale sale = new Sale();
                sale.setId(resultSet.getLong("id"));
                sale.setBookTitle(resultSet.getString("book_title"));
                sale.setQuantity(resultSet.getInt("quantity"));
                sale.setSellerName(resultSet.getString("seller_name"));
                sale.setTotalPrice(resultSet.getDouble("total_price"));
                sale.setOrderDate(resultSet.getTimestamp("order_date").toLocalDateTime());
                sales.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sales;
    }
}