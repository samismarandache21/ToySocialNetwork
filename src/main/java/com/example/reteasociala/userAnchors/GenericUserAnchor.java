package com.example.reteasociala.userAnchors;
import com.example.reteasociala.HelloApplication;
import com.example.reteasociala.SignInController;
import domain.Utilizator;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GenericUserAnchor extends AnchorPane {

    protected Label label;
    protected Button stButton;
    protected Button drButton;
    protected Utilizator utilizator;
    protected HelloApplication application;
    protected Utilizator utilizatorCurent;

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;

    }

    public GenericUserAnchor(Utilizator utilizator, HelloApplication app,Utilizator utilizatorCurent) {
        label = new Label(utilizator.getUsername());
        this.utilizatorCurent = utilizatorCurent;
        setUtilizator(utilizator);
        application = app;
        stButton = new Button();
        drButton = new Button();

        setPrefSize(156.0, 38.0);
        label.setLayoutX(14.0);
        label.setLayoutY(10.0);

        stButton.setLayoutX(131.0);
        stButton.setLayoutY(5.0);
        stButton.setPrefSize(34.0, 26.0);

        drButton.setLayoutX(165.0);
        drButton.setLayoutY(5.0);
        drButton.setPrefSize(33.0, 26.0);

        AnchorPane.setLeftAnchor(label, 14.0);
        AnchorPane.setRightAnchor(stButton, 35.4);
        AnchorPane.setRightAnchor(drButton, 0.6);

        getChildren().addAll(label, stButton, drButton);

    }
}
