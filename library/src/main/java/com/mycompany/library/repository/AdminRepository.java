/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.repository;
import com.mycompany.library.database.DatabaseConnection;
import com.mycompany.library.model.Admin;
import java.sql.*;

/**
 *
 * @author user
 */
public class AdminRepository {
    
    public void insert(String id, String name, String phone, String sex,String passwd) {
        String query = "INSERT INTO admin (id, name, phone, sex,passwd) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, phone);
            statement.setString(4, sex);
            statement.setString(5, passwd);
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Admin findAdminById(String id) {
        String query = "SELECT * FROM admin WHERE id = ?";
        Admin admin = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    admin = new Admin();
                    admin.setId(resultSet.getString("id"));
                    admin.setName(resultSet.getString("name"));
                    admin.setSex(resultSet.getString("sex"));
                    admin.setPhone(resultSet.getString("phone"));
                    admin.setPasswd(resultSet.getString("passwd"));
            
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;
    }

    public void updateAdmin(Admin admin) {
        String query = "UPDATE admin SET name = ?, sex = ?, phone = ?, passwd = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, admin.getName());
            statement.setString(2, admin.getSex()); 
            statement.setString(3, admin.getPhone());                      
            statement.setString(4, admin.getPasswd());
            statement.setString(5, admin.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 判斷id是否存在
    public boolean isAdminIdExists(String userId) {
        String query = "SELECT COUNT(*) FROM admin WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userId);
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
    
   public boolean validate(String userId, String inputPassword) {
    String query = "SELECT passwd FROM admin WHERE id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, userId);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("passwd");
                return storedPassword.equals(inputPassword);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
    }
   
    public static String findName(String adminId) {
        String query = "SELECT name FROM admin WHERE id = ?";
        String name = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, adminId);
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
