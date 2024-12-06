package service.order;

import model.Order;
import repository.order.OrderRepository;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getOrders()
    {
        return orderRepository.getOrders();
    }
}
