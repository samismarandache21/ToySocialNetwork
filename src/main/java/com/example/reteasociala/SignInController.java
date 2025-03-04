package com.example.reteasociala;

import domain.Utilizator;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import services.ServiceUtilizatori;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Optional;


public class SignInController {
    HelloApplication app;
    public Utilizator utilizatorCurent;

    public Utilizator getUtilizatorCurent() {
        return utilizatorCurent;
    }

    public void setUtilizatorCurent(Utilizator utilizatorCurent) {
        this.utilizatorCurent = utilizatorCurent;
    }

    @FXML
    private TextField textUsername;
    @FXML
    private PasswordField textPassword;

    public SignInController(HelloApplication app) {
        this.app = app;
    }

    @FXML
    public void handleLogIn(ActionEvent event) {
        try {
            Optional< Utilizator> utilizatorCurent = app.serviceUtilizatori.existaUtilizator(textUsername.getText());
            if (utilizatorCurent.isPresent() && BCrypt.checkpw(textPassword.getText(), utilizatorCurent.get().getParola())) {
                System.out.println(utilizatorCurent.get());
                setUtilizatorCurent(utilizatorCurent.get());
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainWindow.fxml"));
                fxmlLoader.setControllerFactory(_ -> new MainWindowController(app, utilizatorCurent.get()));
                Scene newScene = new Scene(fxmlLoader.load());
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setScene(newScene);
            }
            else{
                MessageAlert.showMessage(null,Alert.AlertType.WARNING,"The user does not exist","The user with the given username and password does not exist");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleNewUser(ActionEvent event) {
        try {
            if(app.serviceUtilizatori.existaUsername(textUsername.getText()).isPresent()) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"The username already exists","The user with the given username already exists");
                return;
            }
            app.serviceUtilizatori.addUtilizator(textUsername.getText(), BCrypt.hashpw(textPassword.getText(), BCrypt.gensalt()));
            MessageAlert.showMessage(null,Alert.AlertType.CONFIRMATION,"New user created","");
        }catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error",e.getMessage());
        }
    }

    public void setApp(HelloApplication app) {
        this.app = app;
    }


}
