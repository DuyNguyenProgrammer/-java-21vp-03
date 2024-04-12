package com.book.app.Controller;

import com.book.app.Dao.BookDao;
import com.book.app.Dao.impl.BookDaoImpl;
import com.book.app.Entity.BookEntity;

import java.util.List;

public class BookController {
    private BookDao bookDao;

    public BookController() {
        this.bookDao = new BookDaoImpl();
    }

    public List<BookEntity> getAllBooks(String keyword, String sort) {
        return bookDao.getAllBooks(keyword, sort);
    }

    public boolean addBook(BookEntity book) {
        return bookDao.addBook(book);
    }

    public boolean editBook(BookEntity book) {
        return bookDao.editBook(book);
    }

    public boolean deleteBook(String id) {
        return bookDao.deleteBook(id);
    }

    public boolean hideOrShowBook(String id, boolean isEnable) {
        return bookDao.hideOrShowBook(id, isEnable);
    }
}

