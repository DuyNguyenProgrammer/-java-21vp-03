package com.book.app.Controller.employee.book;

import com.book.app.Config.ImageUploader;
import com.book.app.Dao.impl.AuthorDaoImpl;
import com.book.app.Dao.impl.BookDaoImpl;
import com.book.app.Dao.impl.CategoryDaoImpl;
import com.book.app.Dao.impl.PublisherDaoImpl;
import com.book.app.DTO.CloudinaryForm;
import com.book.app.Entity.AuthorEntity;
import com.book.app.Entity.BookEntity;
import com.book.app.Entity.CategoryEntity;
import com.book.app.Entity.PublisherEntity;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class EditBookController implements Initializable {
    @FXML
    private TextField titleField, descriptionField, priceField;
    @FXML
    private ComboBox<AuthorEntity> authorComboBox;
    @FXML
    private ComboBox<CategoryEntity> categoryComboBox;
    @FXML
    private ComboBox<PublisherEntity> publisherComboBox;
    @FXML
    private Button chooseImageButton, submitButton, cancelButton;
    @FXML
    private ImageView imageView;

    private Stage stage;
    private Dialog<String> dialog;
    private BookDaoImpl bookDao = new BookDaoImpl();
    private File selectedFile;
    private CloudinaryForm imageBook = new CloudinaryForm();
    private AuthorDaoImpl authorDao = new AuthorDaoImpl();
    private CategoryDaoImpl categoryDao = new CategoryDaoImpl();
    private PublisherDaoImpl publisherDao = new PublisherDaoImpl();
    private BookEntity bookToUpdate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeAuthorComboBox();
        initializeCategoryComboBox();
        initializePublisherComboBox();

        chooseImageButton.setOnAction(event -> chooseImage());
        submitButton.setOnAction(event -> submit());
        cancelButton.setOnAction(event -> dialog.close());
    }

    private void initializeAuthorComboBox() {
        List<AuthorEntity> authors = authorDao.getAllAuthor(null, null);
        authorComboBox.setItems(FXCollections.observableArrayList(authors));
    }

    private void initializeCategoryComboBox() {
        List<CategoryEntity> categories = categoryDao.getAllCategory(null, null);
        categoryComboBox.setItems(FXCollections.observableArrayList(categories));
    }

    private void initializePublisherComboBox() {
        List<PublisherEntity> publishers = publisherDao.getAllPublisher(null, null);
        publisherComboBox.setItems(FXCollections.observableArrayList(publishers));
    }

    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Book Image");
        selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imageView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    private void submit() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        double price = Double.parseDouble(priceField.getText().trim());
        AuthorEntity author = authorComboBox.getValue();
        CategoryEntity category = categoryComboBox.getValue();
        PublisherEntity publisher = publisherComboBox.getValue();

        // Upload the selected image to Cloudinary
        if (selectedFile != null) {
            try {
                ImageUploader imageUploader = new ImageUploader();
                CloudinaryForm uploadResult = imageUploader.uploadImage(selectedFile, "book");
                imageBook.setUrl(uploadResult.getUrl());
                imageBook.setPublic_id(uploadResult.getPublic_id());
            } catch (IOException e) {
                e.printStackTrace();
                // Handle image upload error
                return;
            }
        }

        // Update book entity
        bookToUpdate.setTitle(title);
        bookToUpdate.setDescription(description);
        bookToUpdate.setPrice(price);
        bookToUpdate.setAuthorId(author.getId());
        bookToUpdate.setCategoryId(category.getId());
        bookToUpdate.setPublisherId(publisher.getId());
        bookToUpdate.setImageUrl(imageBook.getUrl());
        bookToUpdate.setImagePublicId(imageBook.getPublic_id());
        bookToUpdate.setUpdatedAt(LocalDateTime.now());

        boolean success = bookDao.editBook(bookToUpdate);
        if (success) {
            // Close the dialog and refresh the book list
            dialog.setResult("success");
            dialog.close();
        } else {
            // Show error message or handle failure
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setDialog(Dialog<String> dialog) {
        this.dialog = dialog;
    }

    public void setBookToUpdate(BookEntity bookToUpdate) {
        this.bookToUpdate = bookToUpdate;
        // Populate fields with existing book data
        titleField.setText(bookToUpdate.getTitle());
        descriptionField.setText(bookToUpdate.getDescription());
        priceField.setText(String.valueOf(bookToUpdate.getPrice()));
        authorComboBox.getSelectionModel().select(bookToUpdate.getAuthor());
        categoryComboBox.getSelectionModel().select(bookToUpdate.getCategory());
        publisherComboBox.getSelectionModel().select(bookToUpdate.getPublisher());
        // Set image preview
        imageView.setImage(new Image(bookToUpdate.getImageUrl()));
    }
}
