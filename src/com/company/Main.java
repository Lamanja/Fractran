package com.company;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;


public class Main extends Application {
    int sizex = 40;
    int sizey = 21;
    Canvas canvas = new Canvas(sizex*20+20, sizey*20+20+60);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    BigInteger state = BigInteger.valueOf(2);
    static ArrayList<Long> primes = new ArrayList<>(Arrays.asList((long) 2, (long) 3));
    Button run = new Button("RUN");
    FractranProgram program;
    ScrollPane displayFrations = new ScrollPane();
    ScrollPane displayState = new ScrollPane();
    Label lblfractions = new Label();
    Label lblstate = new Label();
    static boolean black = false;
    static boolean white = false;

    EventHandler<MouseEvent> clickedBoard = mouseEvent ->
    {
        if (mouseEvent.getSceneX() > 10 && mouseEvent.getSceneX() < 10 + sizex*20)
        {
            if (mouseEvent.getSceneY() > 10 && mouseEvent.getSceneY() < 10 + sizey*20)
            {
                int x = (int) ((mouseEvent.getSceneX()-10)/20.0);
                int y = (int) ((mouseEvent.getSceneY()-10)/20.0);

                if (state.mod(BigInteger.valueOf(primes.get(x * sizey + y + 2))).equals(BigInteger.valueOf(0)))
                {
                    state = state.divide(BigInteger.valueOf(primes.get(x * sizey + y + 2)));
                    gc.setFill(Color.WHITE);
                    white = true;
                }
                else
                {
                    state = state.multiply(BigInteger.valueOf(primes.get(x * sizey + y + 2)));
                    gc.setFill(Color.BLACK);
                    black = true;
                }
                gc.fillRect(x*20+11, y*20+11, 18, 18);
            }
        }
    };

    EventHandler<MouseEvent> draggedBoard = mouseEvent ->
    {

        int x = (int) ((mouseEvent.getSceneX()-10)/20.0);
        int y = (int) ((mouseEvent.getSceneY()-10)/20.0);
        if (mouseEvent.getSceneX() > 10 && mouseEvent.getSceneX() < 10 + sizex*20) {
            if (mouseEvent.getSceneY() > 10 && mouseEvent.getSceneY() < 10 + sizey * 20) {
                long p = primes.get(x * sizey + y + 2);
                if (white && state.mod(BigInteger.valueOf(p)).equals(BigInteger.valueOf(0))) {
                    state = state.divide(BigInteger.valueOf(p));
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x * 20 + 11, y * 20 + 11, 18, 18);
                }
                if (black && !state.mod(BigInteger.valueOf(p)).equals(BigInteger.valueOf(0))) {
                    state = state.multiply(BigInteger.valueOf(p));
                    gc.setFill(Color.BLACK);
                    gc.fillRect(x * 20 + 11, y * 20 + 11, 18, 18);

                }
            }
        }
    };

    EventHandler<MouseEvent> releasedBoard = mouseEvent ->
    {
        white = false;
        black = false;
    };


            EventHandler<ActionEvent> clickRun = actionEvent ->
    {
        BigInteger newState = program.run(state);
        state = newState.multiply(BigInteger.valueOf(2));
        for (int x = 0; x < sizex; x++)
        {
            for (int y = 0; y < sizey; y++)
            {
                if (state.mod(BigInteger.valueOf(primes.get(x * sizey + y + 2))).equals(BigInteger.valueOf(0)))
                {
                    gc.setFill(Color.BLACK);
                }
                else
                {
                    gc.setFill(Color.WHITE);
                }
                gc.fillRect(x*20+11, y*20+11, 18, 18);
            }
        }
    };
    
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("py fractrangenerator.py "+sizex+" "+sizey);
        FractranGenerator.generateProgram(sizex, sizey);
        Thread.sleep(1000);
        program = new FractranProgram();
        Thread.sleep(1000);
        Pane root = new AnchorPane();
        String title = "Conway games";
        stage.setTitle(title);
        stage.setScene(new Scene(root, sizex*20+20, sizey*20+20+60));
        stage.show();
    
        root.getChildren().add(canvas);
        root.getChildren().add(run);
        stage.setResizable(false);
    
        canvas.setOnMousePressed(clickedBoard);
        canvas.setOnMouseDragged(draggedBoard);
        canvas.setOnMouseReleased(releasedBoard);
        run.setOnAction(clickRun);
        AnchorPane.setTopAnchor(run, (double) (10 + sizey * 20 + 10));
        AnchorPane.setLeftAnchor(run, (double) 10);
        
        gc.setFill(Color.ANTIQUEWHITE);
        gc.fillRect(0, 0, sizex * 20 + 20, sizey * 20 + 20 + 60);
        gc.setFill(Color.WHITE);
        gc.fillRect(10, 10, sizex * 20, sizey * 20);

        gc.setFill(Color.LIGHTGRAY);
        for(int x = 1; x < sizex; x++)
        {
            gc.fillRect(x * 20 + 9, 10, 2, sizey * 20);
        }
        for(int y = 1; y < sizey; y++)
        {
            gc.fillRect(10, y * 20 + 9, sizex * 20, 2);
        }
    
        while (primes.size() < sizex*sizey + 2)
        {
            nextPrime();
        }
    


    }

    static void nextPrime()
    {
        long p = primes.get(primes.size() - 1);
        while (true)
        {
            p += 2;
            if(checkPrime(p))
            {
                primes.add(p);
                return;
            }
        }
    }

    static boolean checkPrime(long p)
    {
        for (long q : primes)
        {
            if(p%q == 0)
            {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        launch(args);
    }

}
