package com.example.reteasociala;

import domain.Utilizator;
import domain.validators.FriendshipValidator;
import domain.validators.MesajValidator;
import domain.validators.UtilizatorValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import repository.Repository;
import repository.database.FriendshipDatabaseRepository;
import repository.database.MesajeDatabaseRepository;
import repository.database.UtilizatoriDatabaseRepository;
import services.ServiceFriendships;
import services.ServiceMesaje;
import services.ServiceUtilizatori;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelloApplication extends Application {
    public ServiceUtilizatori serviceUtilizatori;
    public ServiceFriendships serviceFriendships;
    public ServiceMesaje serviceMesaje;

    @Override
    public void start(Stage stage1) throws IOException {

        UtilizatorValidator uValidator = new UtilizatorValidator();
        UtilizatoriDatabaseRepository repo = new UtilizatoriDatabaseRepository(uValidator,"Utilizatori");
        serviceUtilizatori = new ServiceUtilizatori(repo);
        //System.out.println(serviceUtilizatori.existaUtilizator("alex").get());
        FriendshipValidator fv = new FriendshipValidator();
        FriendshipDatabaseRepository fdbr = new FriendshipDatabaseRepository(fv,"prietenii");
        serviceFriendships = new ServiceFriendships(fdbr);
        MesajeDatabaseRepository msgdbr = new MesajeDatabaseRepository(new MesajValidator(), "mesaje");
        serviceMesaje = new ServiceMesaje(msgdbr);

        //System.out.println(BCrypt.hashpw("123", BCrypt.gensalt()));
        //System.out.println(BCrypt.checkpw("123", "$2a$10$m8uyDDGQEXLFBtWXkMXw1O.Hc6yqLYBWftM0mYO0AaZDZsLuVijtq"));

        // Initialize and show the first stage
        FXMLLoader fxmlLoader1 = new FXMLLoader(HelloApplication.class.getResource("signin.fxml"));
        fxmlLoader1.setControllerFactory(_ -> new SignInController(this));
        Scene scene1 = new Scene(fxmlLoader1.load());
        stage1.setTitle("Hello!");
        stage1.setScene(scene1);
        stage1.show();

        // Initialize and show the second stage
        Stage stage2 = new Stage();
        FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("signin.fxml"));
        fxmlLoader2.setControllerFactory(_ -> new SignInController(this));
        Scene scene2 = new Scene(fxmlLoader2.load());
        stage2.setTitle("Hello Again!");
        stage2.setScene(scene2);
        stage2.show();
    }

    public static void main(String[] args) {
        launch();
    }
}