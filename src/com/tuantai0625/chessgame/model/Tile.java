package com.tuantai0625.chessgame.model;

import com.tuantai0625.chessgame.factory.PieceFactory;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;


public class Tile implements Piece.OnDragCompleteListener {
    public static final String DARK = "dark";
    public static final String LIGHT = "light";

    private ChessBoard board;
    private Pane pane;
    private String color;
    private Piece piece;
    private int row, col;

    public Tile(ChessBoard board, int row, int col) {
        this.board = board;
        this.row = row;
        this.col = col;
        this.pane = new Pane();
        if ((row % 2 == 0 && col % 2 == 1) || (row % 2 == 1 && col % 2 == 0)) {
            this.color = DARK;
            this.pane.setStyle("-fx-background-color: #33939A");
        } else {
            this.color = LIGHT;
            this.pane.setStyle("-fx-background-color: #98DCE1");
        }

        createDragDropEvent();
    }

    @Override
    public void onDragComplete() {
        removePiece();
    }

    /**
     * Xử lý sự kiện Drag and drop
     */
    private void createDragDropEvent() {
        this.pane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != pane &&
                        event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            }
        });

        this.pane.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != pane &&
                        event.getDragboard().hasString()) {
                    pane.setStyle("-fx-background-color: #D9DC96");
                }

                event.consume();
            }
        });

        this.pane.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (color.equals(DARK)) {
                    pane.setStyle("-fx-background-color: #33939A");
                } else {
                    pane.setStyle("-fx-background-color: #98DCE1");
                }

                event.consume();
            }
        });

        this.pane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;

                if(db.hasString()) {
                    String[] pieceInfo = db.getString().split("_");
                    int oldRow = Integer.parseInt(pieceInfo[2]);
                    int oldCol = Integer.parseInt(pieceInfo[3]);
                    Piece newPiece = PieceFactory.getPiece(pieceInfo[1], pieceInfo[0]);
                    newPiece.setTileOn(board.getTile(oldRow, oldCol));
                    if (newPiece.isLegalMove(board, row, col)) {
                        if (hasPiece()) {
                            removePiece();
                        }
                        newPiece.addImageDragEvent();
                        setPiece(newPiece);
                        board.setLastMove(new Move(oldRow, oldCol, row, col));
                        success = true;
                    }
                }

                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    public boolean hasPiece() {
        return (this.piece != null);
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        this.piece.setTileOn(this);
        this.pane.getChildren().add(piece.getImage());
        this.piece.setOnDragCompleteListener(this);
    }

    public void removePiece() {
        this.pane.getChildren().remove(this.piece.getImage());
        this.piece = null;
    }

    public Pane getPane() {
        return this.pane;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
