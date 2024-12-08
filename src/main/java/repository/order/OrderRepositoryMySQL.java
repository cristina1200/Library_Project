package repository.order;

import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OrderRepositoryMySQL implements OrderRepository{
    private Connection connection;

    public OrderRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }
@Override
    public List<Order> getOrders()
    {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM `orders` WHERE order_date >= NOW() - INTERVAL 1 MONTH";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("id"),
                        resultSet.getString("book_title"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("total_price"),
                        resultSet.getTimestamp("order_date")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    }


