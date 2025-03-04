package com.example.reteasociala;

import ObserverClasses.Observer;
import ObserverClasses.ObserverAction;
import com.example.reteasociala.paging.Page;
import com.example.reteasociala.paging.Pageable;
import com.example.reteasociala.userAnchors.*;
import domain.Friendship;
import domain.Mesaj;
import domain.Utilizator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MainWindowController implements Observer {
    HelloApplication app;
    Utilizator utilizatorCurent;
    int selectedButton;
    Utilizator cautat, utilizatorMesaje;

    @FXML
    protected Label labelUsername;
    @FXML
    protected Button buttonReceivedRequests;
    @FXML
    protected Button buttonSentRequests;
    @FXML
    protected Button buttonFriends;
    @FXML
    protected VBox vBoxDreapta;
    @FXML
    protected TextField textSearch;
    @FXML
    protected Button buttonRezultat;
    @FXML
    protected AnchorPane paneCautare;
    @FXML
    protected BorderPane paneMesaje;
    @FXML
    protected VBox vBoxMesaje;
    @FXML
    protected TextField textMesaj;
    @FXML
    private Button btnSendMessage;
    @FXML
    private Label labelFrom;

    private int pageSize = 1;
    private int currentPage = 0;
    private int totalNumberOfElements = 0;
    private ActionEvent event;


    @FXML
    public void initialize() {
        // Legătura evenimentului de trimitere mesaj
        btnSendMessage.setOnAction(event -> sendMessage());
        labelUsername.setText("Hello user " + utilizatorCurent.getUsername());
        updateRightPane();
        setPaneCautareVisible();
    }



    public MainWindowController(HelloApplication app, Utilizator utilizator) {
        this.app = app;
        app.serviceFriendships.addObserver(this);
        this.utilizatorCurent = utilizator;
        this.utilizatorMesaje = utilizator;
        selectedButton = 1;
    }

    @FXML
    private void searchUsername() {
        if (textSearch.getText().isEmpty())
            return;
        Optional<Utilizator> rez = app.serviceUtilizatori.existaUsername(textSearch.getText());
        buttonRezultat.setVisible(true);
        if (rez.isEmpty()) {
            buttonRezultat.setStyle("-fx-background-color: red");
            buttonRezultat.setDisable(true);
            buttonRezultat.setText("This username does not exist");
            return;
        }
        buttonRezultat.setStyle("");
        cautat = rez.get();
        Optional<Friendship> friendship = app.serviceFriendships.getFriendship(utilizatorCurent.getId(), rez.get().getId());
        if (friendship.isEmpty()) {
            buttonRezultat.setDisable(false);
            buttonRezultat.setText("Send friend request");
            return;
        }
        if (friendship.get().getStatus() == 1) {
            buttonRezultat.setDisable(true);
            buttonRezultat.setText("Friends since " + friendship.get().getDataPrietenie().toString());
            return;
        }
        if (Objects.equals(friendship.get().getId().getFirst(), utilizatorCurent.getId())) {
            buttonRezultat.setDisable(true);
            buttonRezultat.setText("You sent a request on " + friendship.get().getDataPrietenie().toString());
            return;
        }

        buttonRezultat.setDisable(true);
        buttonRezultat.setText("Request received on " + friendship.get().getDataPrietenie().toString());
    }

    @FXML
    public void functieButonRezultat() {
        if (Objects.equals(buttonRezultat.getText(), "Send friend request")) {
            app.serviceFriendships.addRequest(utilizatorCurent.getId(), cautat.getId());
        }
    }

    private void fillSentRequests() {
        List<Friendship> req = app.serviceFriendships.getSentRequests(utilizatorCurent.getId());
        vBoxDreapta.getChildren().clear();
        req.forEach(friendship -> {
            vBoxDreapta.getChildren().add(new UserAnchorSent(app.serviceUtilizatori.getUtilizator(friendship.getId().getSecond()), app, utilizatorCurent));
        });
    }

    private void fillReceivedRequests() {
        List<Friendship> req = app.serviceFriendships.getReceivedRequests(utilizatorCurent.getId());
        vBoxDreapta.getChildren().clear();
        req.forEach(friendship -> {
            vBoxDreapta.getChildren().add(new UserAnchorReceived(app.serviceUtilizatori.getUtilizator(friendship.getId().getFirst()), app, utilizatorCurent));
        });
    }

    private HBox configurePagingControls() {
        HBox res = new HBox();
        Button prev = new Button("<");
        Label pageLabel = new Label();
        Button next = new Button(">");

        int maxPage = (int)Math.ceil((double)totalNumberOfElements / pageSize) - 1;
        prev.setDisable(currentPage == 0);
        next.setDisable((currentPage + 1) * pageSize >= totalNumberOfElements);
        pageLabel.setText("Page " + (currentPage + 1) + "/" + (maxPage + 1));

        prev.setOnAction(_ -> {
            currentPage--;
            fillFriends();
        });

        next.setOnAction(_ -> {
            currentPage++;
            fillFriends();
        });

        res.getChildren().addAll(prev, pageLabel, next);
        res.setAlignment(Pos.CENTER);
        return res;
    }

    private void fillFriends() {
        Page<Friendship> page = app.serviceFriendships.findAllUserFirendshipsOnPage(new Pageable(currentPage, pageSize), utilizatorCurent.getId());
        totalNumberOfElements = page.getTotalNumberOfElements();
        vBoxDreapta.getChildren().clear();
        page.getElementsOnPage().forEach(friendship -> {
            UserAnchorFriend userAnchor;
            if (utilizatorCurent.equals(app.serviceUtilizatori.getUtilizator(friendship.getId().getFirst()))) {
                userAnchor = new UserAnchorFriend(app.serviceUtilizatori.getUtilizator(friendship.getId().getSecond()), app, utilizatorCurent, this);
            } else {
                userAnchor = new UserAnchorFriend(app.serviceUtilizatori.getUtilizator(friendship.getId().getFirst()), app, utilizatorCurent, this);
            }
            userAnchor.getStyleClass().add("user-anchor-friend");
            vBoxDreapta.getChildren().add(userAnchor);
        });
        vBoxDreapta.getChildren().add(configurePagingControls());
    }





    public void updateRightPane() {
        switch (selectedButton) {
            case 0:
                fillSentRequests();
                break;
            case 1:
                fillReceivedRequests();
                break;
            case 2:
                fillFriends();
                break;
        }
    }

    public void putMessages() {
        int ok = 0;
    }

    @FXML
    protected void setPaneCautareVisible() {
        paneCautare.setVisible(true);
        paneMesaje.setVisible(false);
    }

    public void setPaneMesajeVisible() {
        paneMesaje.setVisible(true);
        fillMessages();
        paneCautare.setVisible(false);
    }




    private Long replyToMessageId = null; // ID-ul mesajului la care răspunzi

    // Setăm ID-ul mesajului la care se dă reply
    public void setReplyToMessageId(Long messageId) {
        this.replyToMessageId = messageId;
    }

    // Obținem ID-ul mesajului la care se dă reply
    public Long getReplyToMessageId() {
        return replyToMessageId;
    }

    // Resetăm ID-ul mesajului după ce s-a trimis un răspuns
    public void clearReplyToMessageId() {
        this.replyToMessageId = null;
    }

    public void fillMessages() {
        //System.out.println("Getting messages between: " + utilizatorCurent.getId() + " and " + utilizatorMesaje.getId());
        // Retrieve the list of messages for the current conversation
        List<Mesaj> mesaje = app.serviceMesaje.getMessagesBetweenUsers(utilizatorCurent.getId(), utilizatorMesaje.getId());
        // Clear the existing items in the VBox
        vBoxMesaje.getChildren().clear();

        // Iterate through the messages and create MessageAnchor for each message
        mesaje.forEach(mesaj -> {
            //System.out.println("Displaying message: " + mesaj);
            MessageAnchor messageAnchor = new MessageAnchor(mesaj, app, utilizatorCurent,this);
            messageAnchor.getStyleClass().add("message-anchor");
            vBoxMesaje.getChildren().add(messageAnchor);
        });
    }

    @FXML
    public void sendMessage() {
        String mesaj = textMesaj.getText();
        if (mesaj.isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Empty message", "The message cannot be empty");
            return;
        }

        try {
            // Obținem ID-ul mesajului la care se răspunde (dacă există)
            Long replyToId = getReplyToMessageId();

            // Creăm mesajul nou, cu replyTo setat dacă este cazul
            Mesaj newMessage = new Mesaj(mesaj, utilizatorCurent.getId(), List.of(utilizatorMesaje.getId()), LocalDateTime.now(), replyToId);

            // Salvăm mesajul
            app.serviceMesaje.saveMessage(newMessage);

            // Resetăm replyTo după ce mesajul a fost trimis
            clearReplyToMessageId();

            // Curățăm input-ul și actualizăm lista de mesaje
            textMesaj.clear();
            fillMessages();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Could not send the message: " + e.getMessage());
        }
    }


    @FXML
    public void broadcastMessage() {
        String messageContent = textMesaj.getText();
        if (messageContent.isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Empty message", "The message cannot be empty.");
            return;
        }

        try {
            // Retrieve all friends of the current user
            List<Friendship> friendsList = app.serviceFriendships.getFriends(utilizatorCurent.getId());
            if (friendsList.isEmpty()) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "No Friends", "You have no friends to send a message to.");
                return;
            }

            // Send a message to each friend
            for (Friendship friendship : friendsList) {
                Long friendId = Objects.equals(friendship.getId().getFirst(), utilizatorCurent.getId())
                        ? friendship.getId().getSecond()
                        : friendship.getId().getFirst();

                // Create and save the message
                Mesaj newMessage = new Mesaj(messageContent, utilizatorCurent.getId(), List.of(friendId), LocalDateTime.now(), null);
                app.serviceMesaje.saveMessage(newMessage);
            }

            // Clear the message input field
            textMesaj.clear();

            // Notify the user
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Broadcast Sent", "Your message has been sent to all your friends!");

            // Optionally, refresh the current messages view
            fillMessages();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Could not send the broadcast message: " + e.getMessage());
        }
    }



    private void loadMesaje() {
        vBoxMesaje.getChildren().clear();  // Clear existing messages

        // Fetch messages between the current user and the selected user
        List<Mesaj> messages = app.serviceMesaje.getMessagesBetweenUsers(utilizatorCurent.getId(), utilizatorMesaje.getId());

        // Display each message in the VBox
        for (Mesaj mesaj : messages) {
            // Create a new UserAnchorMessage for each message, passing the service to fetch the username
            UserAnchorMessage userMessage = new UserAnchorMessage(mesaj, app.serviceUtilizatori);

            // Add the UserAnchorMessage to the VBox (vBoxMesaje)
            vBoxMesaje.getChildren().add(userMessage);
        }
    }


    @FXML
    protected void buttonSent() {
        if (selectedButton == 0) {
            return;
        }
        selectedButton = 0;
        fillSentRequests();
    }

    @FXML
    protected void buttonReceived() {
        if (selectedButton == 1) {
            return;
        }

        selectedButton = 1;
        fillReceivedRequests();
    }

    @FXML
    protected void buttonFriends() {
        if (selectedButton == 2) {
            return;
        }
        selectedButton = 2;
        fillFriends();
    }

    public Utilizator getUtilizatorMesaje() {
        return utilizatorMesaje;
    }

    public void setUtilizatorMesaje(Utilizator utilizatorMesaje) {
        this.utilizatorMesaje = utilizatorMesaje;
        // Actualizăm label-ul cu numele utilizatorului
        if (labelFrom != null) {
            labelFrom.setText("From: " + utilizatorMesaje.getUsername());
        }
    }

    @Override
    public void update(ObserverAction action) {
        switch (action.getActionType()) {
            case NEWREQUEST -> {
                if (Objects.equals(action.getFrom(), utilizatorCurent.getId()) || Objects.equals(action.getTo(), utilizatorCurent.getId())) {
                    updateRightPane();
                    searchUsername();
                }
                if (Objects.equals(action.getTo(), utilizatorCurent.getId())) {
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friend request", "You received a friend request from " + app.serviceUtilizatori.getUtilizator(action.getFrom()).getUsername());
                }
            }
            case DELETEFRIEND -> {
                if (Objects.equals(action.getFrom(), utilizatorCurent.getId()) || Objects.equals(action.getTo(), utilizatorCurent.getId())) {
                    updateRightPane();
                }
            }
            case NEWFRIEND -> {
                if (Objects.equals(action.getFrom(), utilizatorCurent.getId()) || Objects.equals(action.getTo(), utilizatorCurent.getId())) {
                    updateRightPane();
                }
            }
            case NEWMESSAGE -> {
                fillMessages();
            }
        }
    }

    @FXML
    public void handleOpenProfile(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
            fxmlLoader.setControllerFactory(_ -> new ProfileController(utilizatorCurent, app.serviceUtilizatori, app.serviceFriendships));
            Scene profileScene = new Scene(fxmlLoader.load());

            Stage profileStage = new Stage();
            profileStage.setScene(profileScene);
            profileStage.setTitle("Profilul meu");
            profileStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
