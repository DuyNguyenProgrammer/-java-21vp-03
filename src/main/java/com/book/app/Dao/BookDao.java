package com.book.app.Dao;

import com.book.app.Entity.BookEntity;

import java.util.List;

public interface BookDao {
    List<BookEntity> getAllBooks(String keyword, String sort);
    boolean addBook(BookEntity book);
    boolean editBook(BookEntity book);
    boolean deleteBook(String id);
    boolean hideOrShowBook(String id, boolean isEnable);
}
