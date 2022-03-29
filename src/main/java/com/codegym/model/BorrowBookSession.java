package com.codegym.model;

import javax.persistence.*;

@Entity
@Table(name = "borrow_book_sessions")
public class BorrowBookSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name="book_id")
    private Book book;

    @Column(unique=true)
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BorrowBookSession(Book book, String code) {
        this.book = book;
        this.code = code;
    }

    public BorrowBookSession() {
    }
}
