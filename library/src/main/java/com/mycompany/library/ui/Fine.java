
package com.mycompany.library.ui;

import com.mycompany.library.model.BorrowRecord;
import com.mycompany.library.repository.BorrowRecordRepository;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author qian
 */
public class Fine extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private String userId;
    private String adminId;
    
    Fine(String userId,String adminId) {
        this.userId = userId;
        this.adminId = adminId;
        initComponents();
        initializeUI();
        showBorrowHistory();
    }
    
    private void initializeUI() {
        setTitle("繳納罰金");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = (DefaultTableModel) borrowedList.getModel();
        
        // Add buttons to the table
        TableColumn returnColumn = borrowedList.getColumn("借閱狀態");
        returnColumn.setCellRenderer(new ButtonRenderer());
        returnColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        indexBtn.addActionListener((ActionEvent e) -> {
            openIndexFrame(adminId);
        });
    }
    
    // 顯示借閱清單
    private void showBorrowHistory() {
        // 根据用户ID从数据库中获取借阅历史记录
        BorrowRecordRepository borrowRecordRepository = new BorrowRecordRepository();
        List<BorrowRecord> borrowHistory = borrowRecordRepository.findBorrowHistoryByUserId(userId);

        // 将借阅历史记录显示在表格中
        DefaultTableModel model = (DefaultTableModel) borrowedList.getModel();
        for (BorrowRecord record : borrowHistory) {
            String returnText = record.isReturn() ? "繳納並歸還" : "已歸還";
            if(record.isReturn()==true & record.getFine()>0){
            model.addRow(new Object[] {record.getBookId(), record.getTitle(), record.getAuthor(), record.getBorrowDate(), record.getReturnDate(), record.getFine(), returnText});
            }
            
        }
    }
    
    
    // 在表格中顯示"還書"按鈕
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);  // 設置按鈕為不透明
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if ("已歸還".equals(value)) {
                return new JLabel("已歸還");
            } else {
                setText((value == null) ? "" : value.toString());
                return this;
            }
        }
    }
    
    // 按鈕點擊事件
    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private final JButton button;
        private final JLabel label;
        private String currentText;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(this);

            label = new JLabel("已歸還");
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            currentText = (String) value;
            if ("已歸還".equals(currentText)) {
                return label;
            } else {
                button.setText(currentText);
                return button;
            }
        }

        @Override
        public Object getCellEditorValue() {
            return currentText;
        }

        @Override//計算罰金
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
            
            DefaultTableModel model = (DefaultTableModel) borrowedList.getModel();
            String returnDateStr = (String) model.getValueAt(row, 4); // 獲取到期時間
            int fine = (int) model.getValueAt(row, 5); // 獲取罰金
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            try {
                LocalDate returnDate = LocalDate.parse(returnDateStr, formatter);
                LocalDate today = LocalDate.now(); // 或去兼天日期
                
                    // 更新表格中的狀態為已歸還
                    model.setValueAt("已歸還", row, 6);

                    // 更新数据库中的借阅记录状态为已归还
                    Long bookId = (Long) model.getValueAt(row, 0); // 假设 bookId 在第 0 列
                    BorrowRecordRepository borrowRecordRepository = new BorrowRecordRepository();
                    borrowRecordRepository.updateturnStatus(userId, bookId, false);
                
            } catch (DateTimeParseException ex) {
                ex.printStackTrace();
            }
        }
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        borrowedList = new javax.swing.JTable();
        indexBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        borrowedList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "書名", "作者", "借閱時間", "到期時間", "罰金", "借閱狀態"
            }
        ));
        jScrollPane1.setViewportView(borrowedList);

        indexBtn.setText("回首頁");
        indexBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                indexBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 661, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(indexBtn))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(indexBtn)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void indexBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_indexBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_indexBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {// main 方法将不会被使用，因为我们直接从 index 跳转到 borrowedList
    }
    
    // 跳至index.java
    private void openIndexFrame(String adminId) {
        SwingUtilities.invokeLater(() -> {
            new AdminIndex(adminId).setVisible(true);
        });
        this.dispose(); // 关闭登录窗口
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable borrowedList;
    private javax.swing.JButton indexBtn;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
