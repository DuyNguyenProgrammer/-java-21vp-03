package com.book.app.Entity;

import java.time.LocalDateTime;

public class BookEntity {
    private String id;
    private String title;
    private String authorName; // Changed from authorId
    private String publisherName; // Changed from publisherId
    private String categoryName; // Changed from categoryId
    private String description;
    private Boolean isEnable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor, getters, and setters
    // You can generate them using your IDE or write them manually
}
