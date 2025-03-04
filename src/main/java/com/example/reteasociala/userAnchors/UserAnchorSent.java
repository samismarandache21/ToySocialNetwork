package com.example.reteasociala.userAnchors;

import com.example.reteasociala.HelloApplication;
import com.example.reteasociala.SignInController;
import domain.Utilizator;

public class UserAnchorSent extends GenericUserAnchor{
    public UserAnchorSent(Utilizator utilizator, HelloApplication app, Utilizator utilizatorCurent) {
        super(utilizator,app, utilizatorCurent);
        stButton.setVisible(false);
        drButton.setText("-");
        drButton.setOnAction(event -> handleDeleteClick());
    }

    public void handleDeleteClick() {
        application.serviceFriendships.deleteFriendship(utilizator.getId(),utilizatorCurent.getId());
    }
}
