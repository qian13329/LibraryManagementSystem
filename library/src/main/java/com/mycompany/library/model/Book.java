package com.mycompany.library.model;

public class Book {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String Subject;
    private boolean borrowed;

    public Book() {
    }

    public Book(long id, String title, String author, String isbn,String subject, boolean borrowed) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.Subject =subject;
        this.borrowed = borrowed;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }
    
    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        this.Subject = subject;
    }
}
