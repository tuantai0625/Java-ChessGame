package com.tuantai0625.chessgame.model;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

import java.io.Serializable;

/**
 * Created by Lionheart on 12-Jun-17.
 */
public abstract class Piece {
    public static final String BLACK = "black";
    public static final String WHITE = "white";

    protected ImageView image;
    protected String color;
    protected Tile tileOn;
    protected OnDragCompleteListener mListener;

    public Piece(String color) {
        this.color = color;
        String filePath = "com/tuantai0625/chessgame/assets/pieces/" + this.getColor() + "_" + this.getName() + ".png";
        this.image = new ImageView(filePath);
        this.image.setFitWidth(62.5);
        this.image.setFitHeight(62.5);
    }

    public ImageView getImage() {
        return this.image;
    }

    public String getColor() {
        return this.color;
    }

    public void addImageDragEvent() {
        this.image.setOnDragDetected(new ImageDragDetectedEvent());
        this.image.setOnDragDone(new ImageDragDoneEvent());
    }

    public void setOnDragCompleteListener(OnDragCompleteListener listener) {
        this.mListener = listener;
    }

    public Tile getTileOn() {
        return tileOn;
    }

    public void setTileOn(Tile tileOn) {
        this.tileOn = tileOn;
    }

    public abstract String getName();
    public abstract boolean isLegalMove(ChessBoard b, int newRow, int newCol);

    public interface OnDragCompleteListener {
        void onDragComplete();
    }

    private class ImageDragDetectedEvent implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            Dragboard db = image.startDragAndDrop(TransferMode.MOVE);
            Image dragShadow = image.getImage();
            db.setDragView(dragShadow, dragShadow.getWidth()/2, dragShadow.getHeight()/2 );
            ClipboardContent content = new ClipboardContent();
            content.putString(getColor() + "_"
                    + getName() + "_"
                    + Integer.toString(tileOn.getRow()) + "_"
                    + Integer.toString(tileOn.getCol()));
            db.setContent(content);

            event.consume();
        }
    }

    private class ImageDragDoneEvent implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            if (event.getTransferMode() == TransferMode.MOVE) {
                mListener.onDragComplete();
            }
            event.consume();
        }
    }
}
