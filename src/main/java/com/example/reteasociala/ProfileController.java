package com.example.reteasociala;// ProfileController: Clasă responsabilă pentru logica paginii de profil

import javafx.fxml.FXML;
import domain.Utilizator;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import services.ServiceFriendships;
import services.ServiceUtilizatori;

public class ProfileController {

    private final Utilizator utilizatorCurent;

    @FXML
    private Label labelNume;

    @FXML
    private Label labelNumarPrieteni;

    @FXML
    private ImageView imagineProfil;

    private ServiceUtilizatori serviceUtilizatori;

    private ServiceFriendships serviceFriendships;

    public ProfileController(Utilizator utilizatorCurent, ServiceUtilizatori seervice, ServiceFriendships fpserv) {
        this.utilizatorCurent = utilizatorCurent;
        this.serviceUtilizatori = seervice;
        this.serviceFriendships = fpserv;
    }

    @FXML
    public void initialize() {
        labelNume.setText(utilizatorCurent.getUsername());
        labelNumarPrieteni.setText("Număr prieteni: " + serviceFriendships.numar_prieteni(utilizatorCurent.getId()));

        // Setăm imaginea de profil (placeholder sau imagine reală)
        // Înlocuiește "profile-placeholder.png" cu calea către imaginea utilizatorului, dacă există
        // Image imagine = new Image(getClass().getResourceAsStream("/images/profile-placeholder.png"));
        imagineProfil.setImage(serviceUtilizatori.getImage(utilizatorCurent.getId()));
    }
}
