package repository.user;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.USER;
import static java.util.Collections.singletonList;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "Select * from `" + USER + "`";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new UserBuilder()
                        .setId(resultSet.getLong("id"))
                        .setUsername(resultSet.getString("username"))
                        .setPassword(resultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // SQL Injection Attacks should not work after fixing functions
    // Be careful that the last character in sql injection payload is an empty space
    // alexandru.ghiurutan95@gmail.com' and 1=1; --
    // ' or username LIKE '%admin%'; --

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        String fetchUserSql = "SELECT * FROM `" + USER + "` WHERE `username` = ? AND `password` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet userResultSet = preparedStatement.executeQuery();
            if (userResultSet.next()) {
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();

                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }

        return findByUsernameAndPasswordNotification;
    }

    @Override
    public boolean existsByUsername(String email) {
        String fetchUserSql = "SELECT * FROM `" + USER + "` WHERE `username` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql)) {
            preparedStatement.setString(1, email);
            ResultSet userResultSet = preparedStatement.executeQuery();
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM `" + USER + "` WHERE id >= 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //salvam un nou user
    @Override
    public boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean delete(User user) {
        String sql = "DELETE FROM `" + USER + "` WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUsername());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public boolean upgradeUserRole(String username, Role role) {
        // cautam user dupa username
        User user = findByUsername(username);
        if (user == null) {
            return false;
        }

        String sql = "UPDATE user_role SET role_id = ? WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, role.getId());
            preparedStatement.setLong(2, user.getId());

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private User findByUsername(String username) {
        String sql = "SELECT * FROM `" + USER + "` WHERE `username` = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new UserBuilder()
                        .setId(resultSet.getLong("id"))
                        .setUsername(resultSet.getString("username"))
                        .setPassword(resultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}