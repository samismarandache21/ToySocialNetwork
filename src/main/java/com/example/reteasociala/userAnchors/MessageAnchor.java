package com.example.reteasociala.userAnchors;

import com.example.reteasociala.HelloApplication;
import domain.Mesaj;
import domain.Utilizator;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import com.example.reteasociala.MainWindowController;

public class MessageAnchor extends AnchorPane {

    private Label senderLabel;   // Etichetă pentru numele utilizatorului care trimite
    private Label messageLabel;  // Etichetă pentru textul mesajului
    private Label replyToLabel;  // Etichetă pentru mesajul la care se răspunde
    private Button replyButton;  // Buton pentru a răspunde
    private Button deleteButton; // Buton pentru a șterge mesajul
    private Mesaj mesaj;         // Mesajul curent
    private HelloApplication application; // Aplicația principală
    private Utilizator utilizatorCurent;  // Utilizatorul curent
    private MainWindowController mainWindowController;

    public MessageAnchor(Mesaj mesaj, HelloApplication app, Utilizator utilizatorCurent, MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
        this.mesaj = mesaj;
        this.application = app;
        this.utilizatorCurent = utilizatorCurent;

        // Inițializare elemente UI
        senderLabel = new Label(app.serviceUtilizatori.getUtilizator(mesaj.getFrom()).getUsername());
        messageLabel = new Label(mesaj.getMesaj());
        replyToLabel = new Label(); // Label pentru mesajul "Reply To"
        replyButton = new Button("Reply");
        deleteButton = new Button("Delete");

        // Layout-ul componentelor
        setPrefSize(300.0, 70.0); // Dimensiune ajustată pentru a include și replyToLabel
        replyToLabel.setLayoutX(10.0);
        replyToLabel.setLayoutY(5.0); // Afișăm label-ul deasupra senderLabel
        replyToLabel.setStyle("-fx-font-style: italic; -fx-text-fill: gray;"); // Stil pentru mesajul citat

        senderLabel.setLayoutX(10.0);
        senderLabel.setLayoutY(25.0);
        messageLabel.setLayoutX(10.0);
        messageLabel.setLayoutY(45.0);
        replyButton.setLayoutX(200.0);
        replyButton.setLayoutY(10.0);
        deleteButton.setLayoutX(270.0);
        deleteButton.setLayoutY(10.0);

        // Populăm replyToLabel cu textul mesajului la care se răspunde (dacă există)
        populateReplyToLabel();

        // Acțiuni pe butoane
        replyButton.setOnAction(actionEvent -> handleReplyClick());
        deleteButton.setOnAction(actionEvent -> handleDeleteClick());

        // Adăugăm componentele în AnchorPane
        getChildren().addAll(replyToLabel, senderLabel, messageLabel, replyButton, deleteButton);
    }

    private void populateReplyToLabel() {
        if (mesaj.getReplyTo() != null) {
            try {
                // Recuperăm mesajul replyTo folosind ServiceMesaje
                Mesaj replyToMessage = application.serviceMesaje.getMesajById(mesaj.getReplyTo());
                if (replyToMessage != null) {
                    String replyText = replyToMessage.getMesaj();
                    replyToLabel.setText("Reply to: " + (replyText.length() > 30 ? replyText.substring(0, 30) + "..." : replyText));
                } else {
                    replyToLabel.setText("Reply to: [Mesaj inexistent]");
                }
            } catch (RuntimeException e) {
                replyToLabel.setText("Reply to: [Eroare la încărcare]");
            }
        } else {
            replyToLabel.setVisible(false); // Ascundem label-ul dacă nu există replyTo
        }
    }

    private void handleReplyClick() {
        System.out.println("Replying to message with ID: " + mesaj.getId());
        mainWindowController.setReplyToMessageId(mesaj.getId()); // Setăm ID-ul mesajului în aplicație
    }

    private void handleDeleteClick() {
        System.out.println("Deleting message: " + mesaj.getMesaj());
        // Notificăm ServiceMesaje să șteargă mesajul (implementați metoda delete dacă nu există)
    }
}
