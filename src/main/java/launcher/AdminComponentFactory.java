package launcher;

import javafx.stage.Stage;
import controller.AdminController;
import database.DatabaseConnectionFactory;
import mapper.UserMapper;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.admin.AdminService;
import service.admin.AdminServiceImpl;
import service.order.OrderService;
import service.order.OrderServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import view.AdminView;
import view.model.UserDTO;

import java.sql.Connection;
import java.util.List;

public class AdminComponentFactory {
    private final AdminView adminView;
    private final AdminController adminController;
    private final AdminService adminService;
    private final OrderService orderService;
    private final AuthenticationService authenticationService;
    private static AdminComponentFactory instance;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final OrderRepository orderRepository;



    private AdminComponentFactory(Boolean componentsForTest, Stage stage) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.adminService = new AdminServiceImpl(userRepository, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        List<UserDTO> userDTOs = UserMapper.convertUserListToUserDTOList(this.adminService.findAll());
        this.adminView = new AdminView(stage, userDTOs);
        this.orderRepository = new OrderRepositoryMySQL(connection);
        this.orderService = new OrderServiceImpl(orderRepository);
        this.adminController = new AdminController(adminView, adminService, authenticationService, orderService);
    }
    public static AdminComponentFactory getInstance(Boolean componentsForTest, Stage stage) {
        if (instance == null) {
            instance = new AdminComponentFactory(componentsForTest, stage);
        }
        return instance;
    }


}
