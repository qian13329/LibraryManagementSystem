package com.mycompany.library.repository;

import com.mycompany.library.database.DatabaseConnection;
import com.mycompany.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookRepository {

    // 列出所有書籍
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setSubject(resultSet.getString("subject"));
                book.setBorrowed(resultSet.getBoolean("borrowed"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    
    // 使用關鍵字查詢書籍
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE title LIKE '%" + keyword + "%' OR author LIKE '%" + keyword + "%' OR isbn LIKE '%" + keyword + "%' OR id LIKE '%" + keyword + "%' OR subject LIKE '%" + keyword + "%'";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setSubject(resultSet.getString("subject"));
                book.setBorrowed(resultSet.getBoolean("borrowed"));
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    // 借書
public void borrowBook(Long bookId, String userId) {
    String updateQuery = "UPDATE books SET borrowed = true WHERE id = ?";
    String maxIdQuery = "SELECT MAX(id) FROM borrow";
    String insertQuery = "INSERT INTO borrow (id, bookId, userId, borrowDate, returnDate, fine, isReturn) VALUES (?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 0, 1)";
    
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
         PreparedStatement maxIdStatement = connection.prepareStatement(maxIdQuery);
         PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
        
        // 更新书籍的 borrowed 属性
        updateStatement.setLong(1, bookId);
        updateStatement.executeUpdate();
        
        // 获取当前最大id
        ResultSet resultSet = maxIdStatement.executeQuery();
        int maxId = 0;
        if (resultSet.next()) {
            maxId = resultSet.getInt(1);
        }
        int newId = maxId + 1;
        
        // 向 borrow 表中插入一条记录
        insertStatement.setInt(1, newId);
        insertStatement.setLong(2, bookId);
        insertStatement.setString(3, userId);
        insertStatement.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    //新增書籍
    public String add(Book book) {
       String query = "INSERT INTO books (id,title,author,isbn,subject,borrowed) VALUES (?, ?, ?, ?, ?, ?)";
       try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
           preparedStatement.setLong(1, book.getId());
           preparedStatement.setString(2, book.getTitle());
           preparedStatement.setString(3, book.getAuthor());
           preparedStatement.setString(4, book.getIsbn());
           preparedStatement.setString(5, book.getSubject());
           preparedStatement.setBoolean(6, book.isBorrowed());

           int rowsAffected = preparedStatement.executeUpdate();
           if (rowsAffected > 0) {
               System.out.println("Book added successfully!");
               return "success";
           }
       } catch (SQLException e) {
           Logger.getLogger(BookRepository.class.getName()).log(Level.SEVERE, null, e);
       }
       return "error";
   }

    //刪除書籍
    public boolean delete(Long id) {
        String query = "DELETE FROM books WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setLong(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            Logger.getLogger(BookRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
      //尋找ID
    public Book findById(Long id) {
        Book book = null;
        String query = "SELECT * FROM books WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    book = new Book();
                    book.setId(resultSet.getLong("id"));
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setIsbn(resultSet.getString("isbn"));
                    book.setSubject(resultSet.getString("subject"));
                    book.setBorrowed(resultSet.getBoolean("borrowed"));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(BookRepository.class.getName()).log(Level.SEVERE, null, e);
        }
        return book;
    }
}


