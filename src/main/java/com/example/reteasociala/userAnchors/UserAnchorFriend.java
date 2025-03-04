package com.example.reteasociala.userAnchors;

import com.example.reteasociala.HelloApplication;
import com.example.reteasociala.MainWindowController;
import com.example.reteasociala.SignInController;
import domain.Utilizator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.awt.event.ActionEvent;

public class UserAnchorFriend extends GenericUserAnchor{
    private MainWindowController mainWindowController;
    public UserAnchorFriend(Utilizator utilizator, HelloApplication app, Utilizator utilizatorCurent, MainWindowController mainWindowController) {
        super(utilizator, app, utilizatorCurent);
        this.mainWindowController = mainWindowController;
        stButton.setText("...");
        drButton.setText("-");
        stButton.setOnAction(actionEvent -> {handleMClick();});
        drButton.setOnAction(actionEvent -> {handleXClick();});
    }

    public void handleXClick(){
        application.serviceFriendships.deleteFriendship(utilizator.getId(),utilizatorCurent.getId());
    }

    public void handleMClick(){
        mainWindowController.setUtilizatorMesaje(utilizator);
        mainWindowController.setPaneMesajeVisible();
    }

}
