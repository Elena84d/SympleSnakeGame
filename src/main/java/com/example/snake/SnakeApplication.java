package com.example.snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SnakeApplication extends Application {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    public static final int BlockSize = 40;
    public static final int AppW = 20 * BlockSize;
    public static final int AppH = 15 * BlockSize;
    private Direction direction = Direction.RIGHT;
    private boolean moved = false;
    private boolean running = false;
    private Timeline timeline = new Timeline();
    private ObservableList<Node> snake;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(AppW, AppH);
        Group snakebody = new Group();
        snake = snakebody.getChildren();

        Rectangle food = new Rectangle(BlockSize, BlockSize);
        food.setFill(Color.BLUE);
        food.setTranslateX((int) (Math.random() * (AppW - BlockSize)) / BlockSize * BlockSize);
        food.setTranslateY((int) (Math.random() * (AppH - BlockSize)) / BlockSize * BlockSize);
        KeyFrame frame = new KeyFrame(Duration.seconds(0.3), event -> {
            if (!running)
                return;
            boolean toRemove = snake.size() > 1;
            Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);
            double tailX = tail.getTranslateX();
            double taily = tail.getTranslateY();

            switch (direction) {
                case UP:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() - BlockSize);
                    break;
                case DOWN:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() + BlockSize);
                    break;
                case LEFT:
                    tail.setTranslateX(snake.get(0).getTranslateX() - BlockSize);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
                case RIGHT:
                    tail.setTranslateX(snake.get(0).getTranslateX() + BlockSize);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
            }
            moved = true;
            if (toRemove) {
                snake.add(0, tail);
            }
            //collisione
            for (Node rect : snake) {
                if (rect != tail && tail.getTranslateX() == rect.getTranslateX()
                        && tail.getTranslateY() == rect.getTranslateY()) {
                    restartGame();
                    break;
                }
            }
            if (tail.getTranslateX() < 0 || tail.getTranslateX() >= AppW
                    && tail.getTranslateY() < 0 || tail.getTranslateY() >= AppH) {
                restartGame();
            }
            if (tail.getTranslateX() == food.getTranslateX()
                    && tail.getTranslateY() == food.getTranslateY()) {
                food.setTranslateX((int) (Math.random() * (AppW - BlockSize)) / BlockSize * BlockSize);
                food.setTranslateY((int) (Math.random() * (AppH - BlockSize)) / BlockSize * BlockSize);

                Rectangle rect = new Rectangle(BlockSize,BlockSize);
                rect.setTranslateX(tailX);
                rect.setTranslateY(taily);

                snake.add(rect);
            }
        });
        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(timeline.INDEFINITE);

        root.getChildren().addAll(food, snakebody);
        return root;
    }
    private void restartGame(){
        stopGame();
        startGame();
    }
    private void startGame(){
        direction = Direction.RIGHT;
        Rectangle head = new Rectangle(BlockSize,BlockSize);
        snake.add(head);
        timeline.play();
        running= true;
    }
    private void stopGame(){
        running=false;
        timeline.stop();
        snake.clear();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(event -> {
            if (moved){
                switch (event.getCode()){
                    case UP:
                        if (direction != Direction.DOWN)
                            direction = Direction.UP;
                        break;
                    case DOWN:
                        if (direction != Direction.UP)
                            direction = Direction.DOWN;
                        break;
                    case LEFT:
                        if (direction != Direction.RIGHT)
                            direction = Direction.LEFT;
                        break;
                    case RIGHT:
                        if (direction != Direction.LEFT)
                            direction = Direction.RIGHT;
                        break;
                }
            }
            moved = false;
        });
        stage.setTitle("SnakeGame!");
        stage.setScene(scene);
        stage.show();
        startGame();
    }

    public static void main(String[] args) {
        launch();
    }
}