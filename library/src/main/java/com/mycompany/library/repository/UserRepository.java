package com.mycompany.library.repository;

import com.mycompany.library.database.DatabaseConnection;
import com.mycompany.library.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    
    public void insertUser(String userId, String name, String phone, String sex) {
        String query = "INSERT INTO user (id, name, phone, sex) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userId);
            statement.setString(2, name);
            statement.setString(3, phone);
            statement.setString(4, sex);
//            statement.setBoolean(5, false); // permission is set to false (0)

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> findAll() {{
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setName(resultSet.getString("name"));
                user.setSex(resultSet.getString("sex"));
                user.setPhone(resultSet.getString("phone"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }}
    
    public User findUserById(String id) {
        String query = "SELECT * FROM user WHERE id = ?";
        User user = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getString("id"));
                    user.setName(resultSet.getString("name"));
                    user.setSex(resultSet.getString("sex"));
                    user.setPhone(resultSet.getString("phone"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public void updateUser(User user) {
        String query = "UPDATE user SET name = ?, sex = ?, phone = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSex());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 判斷id是否存在
    public boolean isUserIdExists(String id) {
        String query = "SELECT COUNT(*) FROM user WHERE id = ? ";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
           
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    // 關鍵字查詢
    public List<User> searchUser(String keyword) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE id LIKE '%" + keyword + "%' OR name LIKE '%" + keyword + "%' OR sex LIKE '%" + keyword + "%' OR phone LIKE '%" + keyword + "%'";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setName(resultSet.getString("name"));
                user.setSex(resultSet.getString("sex"));
                user.setPhone(resultSet.getString("phone"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
     public static String findName(String userId) {
        String query = "SELECT name FROM user WHERE id = ?";
        String name = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    name = resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }

}

