/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.model;

import java.util.Date;

/**
 *
 * @author user
 */
public class Borrowed {
    private int id;
    private String bookId;
    private String userId;
    private Date borrowDate;
    private Date returnDate;
    private String fine;
    
    public int getId(){
        return  id;
    }
    
    public void setId(int id){
        this.id=id;
    }
    
    public String getBookId(){
        return  bookId;
    }
    
    public void setBookId(String bookId){
        this.bookId=bookId;
    }
    
    public String getUserId(){
        return  bookId;
    }
    
    public void setUserId(String userId){
        this.userId=userId;
    }
    
    public Date get_borrowDate(){
        return  borrowDate;
    }
    
    public void set_borrowDate(Date borrowDate){
        this.borrowDate=borrowDate;
    }
    
    public void borrowDate(Date borrowDate){
        this.borrowDate=borrowDate;
    }
    
    public Date get_returnDate(){
        return  returnDate;
    }
    
    public void set_returnDate(Date returnDate){
        this.returnDate=returnDate;
    }
    
     public String fine(){
        return  fine;
    }
    
    public void setFine(String fine){
        this.fine=fine;
    }
}
