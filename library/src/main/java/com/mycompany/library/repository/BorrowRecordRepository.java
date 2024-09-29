package com.mycompany.library.repository;

import com.mycompany.library.database.DatabaseConnection;
import com.mycompany.library.model.BorrowRecord;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordRepository {
    // 根据用户ID查询历史借阅记录
    public List<BorrowRecord> findBorrowHistoryByUserId(String userId) {
        List<BorrowRecord> borrowHistory = new ArrayList<>();
        String query = "SELECT br.bookId, b.title, b.author, br.borrowDate, br.returnDate, br.fine, br.isReturn "
                        + "FROM books b "
                        + "JOIN borrow br ON b.id = br.bookId "
                        + "WHERE br.userId = ? "
                        + "ORDER BY br.isReturn ASC, br.borrowDate DESC";
        
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // 创建借阅记录对象并设置属性
                    BorrowRecord record = new BorrowRecord();
                    record.setBookId(resultSet.getLong("bookId"));
                    record.setTitle(resultSet.getString("title"));
                    record.setAuthor(resultSet.getString("author"));                  
                    record.setBorrowDate(resultSet.getString("borrowDate"));
                    record.setReturnDate(resultSet.getString("returnDate"));
                    record.setFine(resultSet.getInt("fine"));
                    record.setReturn(resultSet.getBoolean("isReturn"));
                    borrowHistory.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return borrowHistory;
    }
    
    // 還書Re
    public void updateturnStatus(String userId, Long bookId, boolean isReturn) {
        String updateBook = "UPDATE books SET borrowed = false WHERE id = ?";
        String updateBorrowList = "UPDATE borrow SET isReturn = ? WHERE userId = ? AND bookId = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement bookStatement = connection.prepareStatement(updateBook);
             PreparedStatement borrowListStatement = connection.prepareStatement(updateBorrowList)) {
            
            // 更改books borrowed = false
            bookStatement.setLong(1, bookId);
            bookStatement.executeUpdate();
            
            // 更改borrowList isReturn=true
            borrowListStatement.setBoolean(1, isReturn);  // 數字代表第幾個問號
            borrowListStatement.setString(2, userId);
            borrowListStatement.setLong(3, bookId);
            borrowListStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static final int FINE_RATE = 5; // 每天罰鍰5塊
    // 計算罰鍰
    public void calculateFines() {
        String selectSQL = "SELECT id, returnDate FROM borrow WHERE isReturn = FALSE";
        String updateSQL = "UPDATE borrow SET fine = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
             ResultSet rs = selectStmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                Date returnDate = rs.getDate("returnDate");
                long overdueDays = (new java.util.Date().getTime() - returnDate.getTime()) / (1000 * 60 * 60 * 24);
                
                if (overdueDays > 0) {
                    int fine = (int) overdueDays * FINE_RATE;
                    
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                        updateStmt.setInt(1, fine);
                        updateStmt.setInt(2, id);
                        updateStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //使用者有罰緩嗎
    public static int getUserFine(String userId) {
        
        String selectSQL = "SELECT fine FROM borrow WHERE userId = ? AND isReturn = TRUE";
        int totalFine = 0;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
             
                selectStmt.setString(1, userId);
                ResultSet rs = selectStmt.executeQuery();

                while (rs.next()) {
                    totalFine += rs.getInt("fine");
                }
             
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
        return totalFine;
    }


}



