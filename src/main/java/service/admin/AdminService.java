package service.admin;


import model.Role;
import model.User;
import model.validator.Notification;

import java.util.List;

public interface AdminService {
    Notification<Boolean> AddEmployee (String username, String password);
    public List<User> findAll();
    boolean delete (User user);
    boolean updateUserRole (String uername, Role role);
}
