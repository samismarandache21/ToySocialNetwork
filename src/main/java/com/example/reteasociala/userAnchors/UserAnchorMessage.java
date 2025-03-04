package com.example.reteasociala.userAnchors;

import domain.Mesaj;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import services.ServiceUtilizatori;  // Add your service to fetch user details

public class UserAnchorMessage extends AnchorPane {
    private Label mesajLabel;
    private Label fromLabel;
    private ServiceUtilizatori serviceUtilizatori;  // To fetch the username of the sender

    public UserAnchorMessage(Mesaj mesaj, ServiceUtilizatori serviceUtilizatori) {
        // Initialize the service to fetch user data
        this.serviceUtilizatori = serviceUtilizatori;

        // Initialize the labels
        this.mesajLabel = new Label(mesaj.getMesaj());

        // Fetch the username from the service using the sender's ID
        String senderUsername = serviceUtilizatori.getUtilizator(mesaj.getFrom()).getUsername();
        this.fromLabel = new Label("From: " + senderUsername);

        // Create a VBox to stack the labels vertically
        VBox vBox = new VBox(5, fromLabel, mesajLabel);  // 5 is the spacing between the labels
        vBox.setStyle("-fx-padding: 10px;");

        // Add VBox to the AnchorPane
        this.getChildren().add(vBox);

        // Set the layout of the VBox within the AnchorPane
        AnchorPane.setTopAnchor(vBox, 0.0);
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);

        setUpStyle();
    }

    private void setUpStyle() {
        // Styling the message label
        mesajLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        // Styling the sender label
        fromLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
    }
}
