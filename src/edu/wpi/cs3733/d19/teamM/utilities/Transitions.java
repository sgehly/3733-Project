package edu.wpi.cs3733.d19.teamM.utilities;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.awt.*;

public class Transitions {
    public TranslateTransition drop(Node anyNode, int duration, int distance){
        TranslateTransition drop = new TranslateTransition();
        drop.setDuration(Duration.millis(duration));
        drop.setNode(anyNode);
        drop.setByY(distance);

        return drop;
    }

    public TranslateTransition raise(Node anyNode, int duration, int distance){
        TranslateTransition raise = new TranslateTransition();
        raise.setDuration(Duration.millis(duration));
        raise.setNode(anyNode);
        raise.setByY(0-distance);
        return raise;
    }

    public FadeTransition fadeCusion(Node anyNode, int duration) {
        FadeTransition fadeCusion = new FadeTransition();
        fadeCusion.setDuration(Duration.millis(duration));
        fadeCusion.setNode(anyNode);
        fadeCusion.setToValue(0);
        fadeCusion.setFromValue(0);
        return fadeCusion;
    }

    public FadeTransition fadeOut(Node node, int duration) {
        node.setDisable(true);
        FadeTransition fadeOut = new FadeTransition();
        fadeOut.setDuration(Duration.millis(duration));
        fadeOut.setNode(node);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        return fadeOut;
    }

    public FadeTransition fadeIn(Node node, int duration) {
        FadeTransition fadeIn = new FadeTransition();
        fadeIn.setDuration(Duration.millis(duration));
        fadeIn.setNode(node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        return fadeIn;
    }

    /*public void pulseText(Label label, Node node, String newText){
        label.setOpacity(0);
        label.setText(newText);
        label.setTextFill(Paint.valueOf("RED"));
        this.fadeIn(label, 2000).play();
        this.fadeOut(label, 2000).play();
    }*/
}
