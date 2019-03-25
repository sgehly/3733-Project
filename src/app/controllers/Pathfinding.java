package app.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Cursor;

import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public class Pathfinding {
    static double initx;
    static double inity;
    static int height;
    static int width;
    public static String path;
    static double offSetX,offSetY,zoomlvl;

    @FXML
    private Pane imageView;

    @FXML
    private ImageView image;

    @FXML
    private Slider zoomLvl;

    @FXML
    protected void initialize() {

        //Adapted from: https://stackoverflow.com/questions/48687994/zooming-an-image-in-imageview-javafx
        //------------------------------------------------------------------------------------------------
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        Image source = null;
        try {
            String path = getClass().getResource("/resources/maps/01_thefirstfloor.png").toString().replace("file:","");
            System.out.println(path);
            source = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        double ratio = source.getWidth() / source.getHeight();

        if (500 / ratio < 500) {
            width = 500;
            height = (int) (500 / ratio);
        } else if (500 * ratio < 500) {
            height = 500;
            width = (int) (500 * ratio);
        } else {
            height = 500;
            width = 500;
        }
        image.setImage(source);
        System.out.println(imageView.getBoundsInParent().getWidth());
        image.setFitWidth(primaryScreenBounds.getWidth());
        image.setFitHeight(primaryScreenBounds.getHeight()-200);
        height = (int) source.getHeight();
        width = (int) source.getWidth();
        System.out.println("height = " + height + "\nwidth = " + width);
        HBox zoom = new HBox(10);
        zoom.setAlignment(Pos.CENTER);

        zoomLvl.setMax(10);
        zoomLvl.setMin(1);
        zoomLvl.setMaxWidth(200);
        zoomLvl.setMinWidth(200);
        Label hint = new Label("Zoom Level");
        Label value = new Label("1.0");

        offSetX = width / 2;
        offSetY = height / 2;

        zoom.getChildren().addAll(hint, value);

        Slider Hscroll = new Slider();
        Hscroll.setMin(0);
        Hscroll.setMax(width);
        Hscroll.setMaxWidth(image.getFitWidth());
        Hscroll.setMinWidth(image.getFitWidth());
        Hscroll.setTranslateY(20);
        Slider Vscroll = new Slider();
        Vscroll.setMin(0);
        Vscroll.setMax(height);
        Vscroll.setMaxHeight(image.getFitHeight());
        Vscroll.setMinHeight(image.getFitHeight());
        Vscroll.setOrientation(Orientation.VERTICAL);
        Vscroll.setTranslateX(-20);

        //imageView.setAlignment(Hscroll, Pos.CENTER);
        //imageView.setAlignment(Vscroll, Pos.CENTER_LEFT);

        Hscroll.valueProperty().addListener(e -> {
            offSetX = Hscroll.getValue();
            zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;
            value.setText(newValue + "");
            if (offSetX < (width / newValue) / 2) {
                offSetX = (width / newValue) / 2;
            }
            if (offSetX > width - ((width / newValue) / 2)) {
                offSetX = width - ((width / newValue) / 2);
            }

            image.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
        });
        Vscroll.valueProperty().addListener(e -> {
            offSetY = height - Vscroll.getValue();
            zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;
            value.setText(newValue + "");
            if (offSetY < (height / newValue) / 2) {
                offSetY = (height / newValue) / 2;
            }
            if (offSetY > height - ((height / newValue) / 2)) {
                offSetY = height - ((height / newValue) / 2);
            }
            image.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
        });
        //imageView.setCenter(image);
        //imageView.setTop(Hscroll);
        //imageView.setRight(Vscroll);
        zoomLvl.valueProperty().addListener(e -> {
            zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;
            value.setText(newValue + "");
            if (offSetX < (width / newValue) / 2) {
                offSetX = (width / newValue) / 2;
            }
            if (offSetX > width - ((width / newValue) / 2)) {
                offSetX = width - ((width / newValue) / 2);
            }
            if (offSetY < (height / newValue) / 2) {
                offSetY = (height / newValue) / 2;
            }
            if (offSetY > height - ((height / newValue) / 2)) {
                offSetY = height - ((height / newValue) / 2);
            }
            Hscroll.setValue(offSetX);
            Vscroll.setValue(height - offSetY);
            image.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
        });
        imageView.setCursor(Cursor.OPEN_HAND);
        image.setOnMousePressed(e -> {
            initx = e.getSceneX();
            inity = e.getSceneY();
            imageView.setCursor(Cursor.CLOSED_HAND);
        });
        image.setOnMouseReleased(e -> {
            imageView.setCursor(Cursor.OPEN_HAND);
        });
        image.setOnMouseDragged(e -> {
            Hscroll.setValue(Hscroll.getValue() + (initx - e.getSceneX()));
            Vscroll.setValue(Vscroll.getValue() - (inity - e.getSceneY()));
            initx = e.getSceneX();
            inity = e.getSceneY();
        });
    }
}
