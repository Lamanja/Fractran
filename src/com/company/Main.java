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
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Main extends Application {
    int sizex = 20;
    int sizey = 20;
    Canvas canvas = new Canvas(sizex*20+20, sizey*20+20+100);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    BigInteger state = BigInteger.valueOf(2);
    static ArrayList<Long> primes = new ArrayList<>(Arrays.asList((long) 2, (long) 3));
    Button run = new Button("RUN");
    FractranProgram program;
    ScrollPane displayFrations = new ScrollPane();
    ScrollPane displayState = new ScrollPane();
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

                if (state.mod(BigInteger.valueOf(primes.get(y * sizex + x + 2))).equals(BigInteger.valueOf(0)))
                {
                    state = state.divide(BigInteger.valueOf(primes.get(y * sizex + x + 2)));
                    gc.setFill(Color.WHITE);
                    white = true;
                }
                else
                {
                    state = state.multiply(BigInteger.valueOf(primes.get(y * sizex + x + 2)));
                    gc.setFill(Color.BLACK);
                    black = true;
                }
                lblstate.setText(state.toString());
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
        lblstate.setText(state.toString());
        for (int x = 0; x < sizex; x++)
        {
            for (int y = 0; y < sizey; y++)
            {
                if (state.mod(BigInteger.valueOf(primes.get(y * sizex + x + 2))).equals(BigInteger.valueOf(0)))
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
        FractranGenerator.generateProgram(sizex, sizey);
        program = new FractranProgram();
        Pane root = new AnchorPane();
        String title = "Conway games";
        stage.setTitle(title);
        stage.setScene(new Scene(root, sizex*20+20, sizey*20+20+150));
        stage.show();
    
        root.getChildren().add(canvas);
        root.getChildren().add(run);
        root.getChildren().add(displayState);
        root.getChildren().add(displayFrations);
        stage.setResizable(false);
    
        canvas.setOnMousePressed(clickedBoard);
        canvas.setOnMouseDragged(draggedBoard);
        canvas.setOnMouseReleased(releasedBoard);
        run.setOnAction(clickRun);
        
        AnchorPane.setTopAnchor(run, (double) (10 + sizey * 20 + 10));
        AnchorPane.setLeftAnchor(run, (double) 10);
        AnchorPane.setTopAnchor(displayState, (double) (10 + sizey * 20 + 10));
        AnchorPane.setLeftAnchor(displayState, (double) 70);
        AnchorPane.setTopAnchor(displayFrations, (double) (10 + sizey * 20 + 50));
        AnchorPane.setLeftAnchor(displayFrations, (double) 10);
        
        lblstate.setText("2");
        displayState.setContent(lblstate);
        displayState.setPrefSize(sizex*20-60, 20);
        displayState.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        
        int totlen = 0;
        String input = new Scanner(Paths.get("fractrancode.txt"), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();
        System.out.println("blblblbl: "+input);
        String[] fracts = input.split(", ");
        for (int i = 0; i < fracts.length; i++)
        {
            String[] splitted = fracts[i].split("/");
            totlen += 7*Math.max(splitted[0].length(), splitted[1].length());
        }
        
        int canvaswidth = 4096;
        if (totlen < 4096)
        {
            canvaswidth = totlen;
        }
        System.out.println(totlen);
        System.out.println(40*Math.ceil(((double) totlen / (double) canvaswidth)));
        Canvas fractions = new Canvas(canvaswidth, 40*Math.ceil((double) totlen / (double) canvaswidth));
        System.out.println(totlen);
        GraphicsContext gc2 = fractions.getGraphicsContext2D();
        gc2.setFill(Color.BLACK);
        int pos = 0;
        for (int i = 0; i < fracts.length; i++)
        {
            String[] splitted = fracts[i].split("/");
            int x = pos % 4096;
            int y = 40 * (int) Math.floor(pos / 4096.0);
            pos += 7*Math.max(splitted[0].length(), splitted[1].length());
            if (pos - y/40 > 4096 - 20)
            {
                x = 0;
                y ++;
            }
            gc2.fillText(splitted[0], x + 10, y);
        }
        
        displayFrations.setContent(fractions);
        displayFrations.setPrefSize(sizex*20, 60);
        displayState.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        displayState.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        
        System.out.println("bj");
        
        gc.setFill(Color.ANTIQUEWHITE);
        gc.fillRect(0, 0, sizex * 20 + 20, sizey * 20 + 20 + 100);
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
