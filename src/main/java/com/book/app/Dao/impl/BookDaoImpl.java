package com.book.app.Dao.impl;

import com.book.app.Config.DBConnection;
import com.book.app.Dao.BookDao;
import com.book.app.Entity.BookEntity;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    private DBConnection db = new DBConnection();
    private ResultSet resultSet;

    @Override
    public List<BookEntity> getAllBooks(String keyword, String sort) {
        List<BookEntity> bookEntityList = new ArrayList<>();
        String sql = "SELECT * FROM book";
        // You can adjust the SQL query to apply keyword search and sorting as needed

        try {
            db.initPrepar(sql);
            resultSet = db.executeSelect();
            while (resultSet.next()) {
                BookEntity book = new BookEntity();
                book.setId(resultSet.getString("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthorName(resultSet.getString("author_name")); // Changed from authorId
                book.setPublisherName(resultSet.getString("publisher_name")); // Changed from publisherId
                book.setCategoryName(resultSet.getString("category_name")); // Changed from categoryId
                book.setDescription(resultSet.getString("description"));
                int isEnableValue = resultSet.getInt("isEnable");
                Boolean isEnable = (isEnableValue == 1);
                book.setEnable(isEnable);
                book.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                book.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
                bookEntityList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return bookEntityList;
    }

    @Override
    public boolean addBook(BookEntity book) {
        String sql = "INSERT INTO book (title, author_name, publisher_name, category_name, description, isEnable, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPreparedStatement().setString(1, book.getTitle());
            db.getPreparedStatement().setString(2, book.getAuthorName());
            db.getPreparedStatement().setString(3, book.getPublisherName());
            db.getPreparedStatement().setString(4, book.getCategoryName());
            db.getPreparedStatement().setString(5, book.getDescription());
            db.getPreparedStatement().setBoolean(6, book.getEnable());
            db.getPreparedStatement().setTimestamp(7, Timestamp.valueOf(book.getCreatedAt()));
            db.getPreparedStatement().setTimestamp(8, Timestamp.valueOf(book.getUpdatedAt()));
            db.getPreparedStatement().executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public boolean editBook(BookEntity book) {
        String sql = "UPDATE book " +
                "SET title = ?, author_name = ?, publisher_name = ?, category_name = ?, description = ?, isEnable = ?, updated_at = ? " +
                "WHERE book_id = ?";
        try {
            db.initPrepar(sql);
            db.getPreparedStatement().setString(1, book.getTitle());
            db.getPreparedStatement().setString(2, book.getAuthorName());
            db.getPreparedStatement().setString(3, book.getPublisherName());
            db.getPreparedStatement().setString(4, book.getCategoryName());
            db.getPreparedStatement().setString(5, book.getDescription());
            db.getPreparedStatement().setBoolean(6, book.getEnable());
            db.getPreparedStatement().setTimestamp(7, Timestamp.valueOf(book.getUpdatedAt()));
            db.getPreparedStatement().setString(8, book.getId());
            int rowsUpdated = db.getPreparedStatement().executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public boolean deleteBook(String id) {
        String sql = "DELETE FROM book WHERE book_id = ?";
        try {
            db.initPrepar(sql);
            db.getPreparedStatement().setString(1, id);
            int rowsDeleted = db.getPreparedStatement().executeUpdate();
            return rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }

    @Override
    public boolean hideOrShowBook(String id, boolean isEnable) {
        String sql = "UPDATE book SET isEnable = ? WHERE book_id = ?";
        try {
            db.initPrepar(sql);
            db.getPreparedStatement().setBoolean(1, isEnable);
            db.getPreparedStatement().setString(2, id);
            int rowsUpdated = db.getPreparedStatement().executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.closeConnection();
        }
    }
}
