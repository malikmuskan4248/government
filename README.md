package com.schemefinder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class HeaderBanner extends Pane {

    private Canvas buildingCanvas;

    public HeaderBanner() {
        getStyleClass().add("header-banner");
        setPrefHeight(150);
        setMinHeight(140);

        // Gradient background via canvas
        Canvas gradientCanvas = new Canvas();
        gradientCanvas.widthProperty().bind(widthProperty());
        gradientCanvas.heightProperty().bind(heightProperty());
        gradientCanvas.widthProperty().addListener((obs, o, n) -> drawGradient(gradientCanvas));
        gradientCanvas.heightProperty().addListener((obs, o, n) -> drawGradient(gradientCanvas));

        // Text area (left side)
        VBox textBox = new VBox(8);
        textBox.setAlignment(Pos.CENTER_LEFT);
        textBox.setPadding(new Insets(30, 0, 30, 36));

        // Accent underline + title
        Label mainTitle = new Label("Government Scheme Finder");
        mainTitle.getStyleClass().add("banner-title");

        // Orange accent line
        Rectangle accentLine = new Rectangle(48, 3);
        accentLine.setFill(Color.web("#F59E0B"));
        accentLine.setArcWidth(3);
        accentLine.setArcHeight(3);

        Label subTitle = new Label("Find government schemes you are eligible for");
        subTitle.getStyleClass().add("banner-subtitle");

        textBox.getChildren().addAll(mainTitle, accentLine, subTitle);

        // Building illustration (right side)
        buildingCanvas = new Canvas(200, 140);
        drawBuilding(buildingCanvas);

        // Layout
        StackPane textContainer = new StackPane(textBox);
        StackPane.setAlignment(textBox, Pos.CENTER_LEFT);

        HBox contentBox = new HBox();
        contentBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(textContainer, Priority.ALWAYS);
        contentBox.getChildren().addAll(textContainer, buildingCanvas);

        contentBox.layoutXProperty().set(0);
        contentBox.layoutYProperty().set(0);
        contentBox.prefWidthProperty().bind(widthProperty());
        contentBox.prefHeightProperty().bind(heightProperty());

        getChildren().addAll(gradientCanvas, contentBox);
    }

    private void drawGradient(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        if (w <= 0 || h <= 0) return;

        // Gradient: Indigo (#4F46E5) â Teal (#0D9488)
        javafx.scene.paint.LinearGradient grad = new LinearGradient(
            0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#4338CA")),
            new Stop(0.5, Color.web("#4F46E5")),
            new Stop(1, Color.web("#0D9488"))
        );
        gc.setFill(grad);
        gc.fillRoundRect(0, 0, w, h, 14, 14);

        // Subtle overlay circles for depth
        gc.setFill(Color.web("#FFFFFF", 0.04));
        gc.fillOval(w * 0.3, -h * 0.3, h * 1.2, h * 1.2);
        gc.setFill(Color.web("#FFFFFF", 0.03));
        gc.fillOval(w * 0.6, h * 0.1, h * 0.9, h * 0.9);
    }

    private void drawBuilding(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        gc.setStroke(Color.web("#FFFFFF", 0.35));
        gc.setLineWidth(1.2);
        gc.setFill(Color.TRANSPARENT);

        // Ground line
        gc.strokeLine(10, h - 20, w - 10, h - 20);

        // Main building body
        gc.strokeRect(30, h - 80, 140, 60);

        // Columns on main building
        double[] colX = {40, 60, 80, 100, 120, 140, 155};
        for (double x : colX) {
            gc.strokeLine(x, h - 80, x, h - 20);
        }

        // Portico / triangular pediment
        gc.setLineWidth(1.5);
        double[] pedX = {20, 100, 180};
        double[] pedY = {h - 80, h - 110, h - 80};
        gc.strokePolygon(pedX, pedY, 3);

        // Dome base
        gc.setLineWidth(1.8);
        gc.strokeRect(70, h - 130, 60, 20);

        // Dome arc
        gc.strokeArc(60, h - 155, 80, 50, 0, 180, javafx.scene.shape.ArcType.OPEN);

        // Dome neck
        gc.strokeRect(92, h - 165, 16, 12);

        // Flag pole
        gc.setLineWidth(1.2);
        gc.strokeLine(100, h - 165, 100, h - 195);

        // Flag
        gc.setFill(Color.web("#FFFFFF", 0.4));
        double[] flagX = {102, 125, 102};
        double[] flagY = {h - 195, h - 188, h - 181};
        gc.fillPolygon(flagX, flagY, 3);
        gc.setFill(Color.TRANSPARENT);
        gc.setStroke(Color.web("#FFFFFF", 0.35));
        gc.strokePolygon(flagX, flagY, 3);

        // Side wings
        gc.setLineWidth(1.0);
        gc.strokeRect(5, h - 55, 28, 35);
        gc.strokeRect(167, h - 55, 28, 35);

        // Wing windows
        gc.strokeRect(10, h - 50, 8, 8);
        gc.strokeRect(20, h - 50, 8, 8);
        gc.strokeRect(172, h - 50, 8, 8);
        gc.strokeRect(182, h - 50, 8, 8);

        // Clouds
        gc.setLineWidth(1.0);
        gc.setStroke(Color.web("#FFFFFF", 0.25));
        drawCloud(gc, 10, 18);
        drawCloud(gc, 145, 25);
    }

    private void drawCloud(GraphicsContext gc, double x, double y) {
        gc.strokeOval(x, y, 22, 12);
        gc.strokeOval(x + 10, y - 6, 18, 14);
        gc.strokeOval(x + 22, y + 2, 16, 10);
    }
}
