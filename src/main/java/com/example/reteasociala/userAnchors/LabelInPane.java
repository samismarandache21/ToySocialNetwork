package com.example.reteasociala.userAnchors;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class LabelInPane extends Pane {

    private Label label;

    // Constructor to initialize the label and the pane
    public LabelInPane(String labelText) {
        label = new Label(labelText);

        // Add the label to the pane
        getChildren().add(label);

        // Automatically resize the Pane to fit the Label
        label.setLayoutX(10); // Set label position
        label.setLayoutY(10); // Set label position

        // Make the Pane's preferred size based on the label's size plus some padding
        setPrefSize(label.getWidth() + 20, label.getHeight() + 20);

    }
}