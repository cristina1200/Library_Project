import database.DatabaseConnectionFactory;
import model.Order;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import service.order.OrderService;
import service.order.OrderServiceImpl;

import java.sql.Connection;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Debugging Orders Table...");

        // Creăm conexiunea la baza de date
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();

        // Inițializăm repository-ul și service-ul pentru orders
        OrderRepository orderRepository = new OrderRepositoryMySQL(connection);
        OrderService orderService = new OrderServiceImpl(orderRepository);

        // Apelăm metoda getOrders() și afișăm rezultatele
        List<Order> orders = orderService.getOrders();

        // Verificăm dacă lista de comenzi este goală
        if (orders.isEmpty()) {
            System.out.println("No orders found in the database.");
        } else {
            System.out.println("Orders found in the database:");
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }
}
