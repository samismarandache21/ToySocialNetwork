package com.example.reteasociala.userAnchors;

import com.example.reteasociala.HelloApplication;
import com.example.reteasociala.SignInController;
import domain.Utilizator;

public class UserAnchorReceived extends GenericUserAnchor{
    public UserAnchorReceived(Utilizator utilizator, HelloApplication app, Utilizator utilizatorCurent) {
        super(utilizator, app, utilizatorCurent);
        stButton.setText("+");
        drButton.setText("-");
        stButton.setOnAction(handler -> {handlePlusClick();});
        drButton.setOnAction(handler -> {handleXClick();});
    }

    public void handleXClick(){
        application.serviceFriendships.deleteFriendship(utilizator.getId(),utilizatorCurent.getId());
    }

    public void handlePlusClick(){
        application.serviceFriendships.updateFriendship(utilizator.getId(),utilizatorCurent.getId());
    }
}
