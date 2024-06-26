package com.book.app.Controller;

import com.book.app.Dao.impl.EmployeeDaoImpl;
import com.book.app.Entity.EmployeeEntity;
import com.book.app.Utils.AppUtils;
import com.book.app.Utils.TokenUtil;
import com.book.app.Utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AuthenticationController implements Initializable {
    @FXML
    private Label userNameLogin, passwordLogin;
    @FXML
    private TextField inputNameLogin;
    @FXML
    private PasswordField inputPasswordLogin;
    @FXML
    private Button submitButton;
    @FXML
    private CheckBox remember;
    private EmployeeDaoImpl dao = new EmployeeDaoImpl();
    private String rootDirectory = "/com/book/app/";
    private boolean validateFields() {
        return inputNameLogin.getText() != null && !inputNameLogin.getText().trim().isEmpty() &&
                inputPasswordLogin.getText() != null && !inputPasswordLogin.getText().trim().isEmpty();
    }
    private void checkFields() {
        submitButton.setDisable(!validateFields());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLogin.setOnMouseClicked(mouseEvent -> {
            inputNameLogin.requestFocus();
        });
        passwordLogin.setOnMouseClicked(mouseEvent -> {
            inputPasswordLogin.requestFocus();
        });
        submitButton.setOnAction(event -> {
            try {
                login(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        inputNameLogin.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        inputPasswordLogin.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        checkFields();
    }
    private void login(ActionEvent event) throws Exception {
        System.out.println("chay vao day");

        EmployeeEntity user = dao.login(inputNameLogin.getText().trim(), inputPasswordLogin.getText().trim());
        if (user != null) {
            if (remember.isSelected()) {
                Key key = TokenUtil.generateKey();
                String token = TokenUtil.encrypt(user.getUsername()+"."+ user.getPassword() + "." + (user.getAdmin() ? "admin" : "employee") + "." + user.getId(), key);
                TokenUtil.saveToken(token);
            }
            String fxmFile, cssFile;
            AppUtils.setRole(user.getAdmin() ? "admin" : "user");
            AppUtils.setUsername(user.getUsername());
            AppUtils.setId(user.getId());
            fxmFile = rootDirectory + (user.getAdmin() ? "admin/user-management.fxml": "home/home.fxml");
            cssFile = user.getAdmin() ? "static/css/user-management.css": "static/css/home.css";
            UIUtils.handleSwitchOtherScene(event, fxmFile, cssFile);
        }
        else {

        }
    }
}
