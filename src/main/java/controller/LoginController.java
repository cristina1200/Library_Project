package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.AdminComponentFactory;
import launcher.CustomerComponentFactory;
import launcher.EmployeeComponentFactory;
import launcher.LoginComponentFactory;
import model.User;
import model.validator.Notification;
import model.validator.UserValidator;
import service.user.AuthenticationService;
import service.user.CurrentUserService;
import view.LoginView;

import java.util.EventListener;
import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;

//constructor
    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }


    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();


            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else {
                loginView.setActionTargetText("LogIn Successfull!");
//                CurrentUserService.setCurrentUsername(username);
//                System.out.println("CurrentUser: " + username);

                // verificam rolul utilizatorului
                User loggedInUser = loginNotification.getResult();
                boolean isAdmin = loggedInUser.getRoles()
                        .stream()
                        .anyMatch(role -> role.getRole().equalsIgnoreCase("ADMINISTRATOR"));
                boolean isEmployee = loggedInUser.getRoles()
                        .stream()
                        .anyMatch(role -> role.getRole().equalsIgnoreCase("EMPLOYEE"));
                if (isAdmin) {
                    //daca este admin il trimitem spre fereastra de admin
                    AdminComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage());

                } else if (isEmployee) {
                    //daca este angajat il trimitem spre cea a angajatilor
                    EmployeeComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage());
                }
                else {
                    //altfel inseamna ca acesta este client
                    CustomerComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage());
                }
            }
        }
    }


    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}