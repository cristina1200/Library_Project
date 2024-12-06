package service.admin;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.user.UserRepository;
import repository.security.RightsRolesRepository;

import java.util.Collections;
import java.util.List;

public class AdminServiceImpl implements AdminService
{
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AdminServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> AddEmployee(String username, String password) {
        Notification<Boolean> addEmployeeNotification = new Notification<>();

        //verificam existenta username-ului
        if(userRepository.existsByUsername(username)) {
            addEmployeeNotification.addError("Username already exists!");
            addEmployeeNotification.setResult(Boolean.FALSE);
            return addEmployeeNotification;
        }

        User user = new UserBuilder().setUsername(username).setPassword(password).build();

        //il facem angajat
        Role employeeRole = rightsRolesRepository.findRoleById(2L);
        if (employeeRole == null) {
            addEmployeeNotification.addError("Role not found!");
            addEmployeeNotification.setResult(Boolean.FALSE);
            return addEmployeeNotification;
        }


        // Salvam utilizatorul
        boolean isUserSaved = userRepository.save(user);
        if (!isUserSaved) {
            addEmployeeNotification.addError("Failed to save the user!");
            addEmployeeNotification.setResult(Boolean.FALSE);
        } else {
            //adaugam rolul
            rightsRolesRepository.addRolesToUser(user, Collections.singletonList(employeeRole));
            addEmployeeNotification.setResult(Boolean.TRUE);

        }

        return addEmployeeNotification;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean delete(User user) {
        return userRepository.delete(user);
    }

    @Override
    public boolean updateUserRole(String username, Role role) {
        return userRepository.upgradeUserRole(username, role);
    }
}