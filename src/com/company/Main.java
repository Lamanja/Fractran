package com.company;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    static int sizex = 20;
    static int sizey = 20;
    static Canvas canvas;
    static GraphicsContext gc;
    static BigInteger state = BigInteger.valueOf(2);

    static ArrayList<Long> primes = new ArrayList<>(Arrays.asList((long) 2, (long) 3));
    static Button run = new Button("RUN");
    static Button grid = new Button("GRID");
    static FractranProgram program;
    static ScrollPane displayState = new ScrollPane();
    static ScrollPane displayFractions = new ScrollPane();
    static TextField tstate = new TextField();
    static Canvas fractions;
    static AnchorPane root;
    
    
    static boolean black = false;
    static boolean white = false;
    static boolean hasGrid = true;

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
                gc.fillRect(x*20+10, y*20+10, 20, 20);
                addGrid();
                tstate.setText(state.toString());
                tstate.setPrefSize(state.toString().length() * 7, 20);

            }
        }
    };

    EventHandler<MouseEvent> draggedBoard = mouseEvent ->
    {
        int x = (int) ((mouseEvent.getSceneX()-10)/20.0);
        int y = (int) ((mouseEvent.getSceneY()-10)/20.0);
        if (mouseEvent.getSceneX() > 10 && mouseEvent.getSceneX() < 10 + sizex*20) {
            if (mouseEvent.getSceneY() > 10 && mouseEvent.getSceneY() < 10 + sizey * 20) {
                long p = primes.get(y * sizex + x + 2);
                if (white && state.mod(BigInteger.valueOf(p)).equals(BigInteger.valueOf(0))) {
                    state = state.divide(BigInteger.valueOf(p));
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x * 20 + 10, y * 20 + 10, 20, 20);
                }
                if (black && !state.mod(BigInteger.valueOf(p)).equals(BigInteger.valueOf(0))) {
                    state = state.multiply(BigInteger.valueOf(p));
                    gc.setFill(Color.BLACK);
                    gc.fillRect(x * 20 + 10, y * 20 + 10, 20, 20);

                }
                addGrid();
                tstate.setText(state.toString());
                tstate.setPrefSize(state.toString().length() * 7, 20);

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
        paint();
        tstate.setText(state.toString());
        tstate.setPrefSize(state.toString().length() * 7, 20);
    };

    EventHandler<ActionEvent> clickGrid = actionEvent ->
    {
        hasGrid = !hasGrid;
        paint();

    };
    
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        int windowheight = sizey * 20 + 60;
        FractranGenerator.generateProgram(sizex, sizey);
        program = new FractranProgram();
        root = new AnchorPane();
        boolean isFractions = displayFractions();
        if (isFractions)
        {
            windowheight += 110;
        }
        String title = "Conway games";
        stage.setTitle(title);
        stage.setScene(new Scene(root, sizex * 20 + 20, windowheight));
        canvas = new Canvas(sizex * 20 + 20, windowheight);
        stage.show();
        gc = canvas.getGraphicsContext2D();
        
        root.getChildren().add(canvas);
        root.getChildren().add(run);
        root.getChildren().add(grid);
        root.getChildren().add(displayState);

        stage.setResizable(false);
    
        canvas.setOnMousePressed(clickedBoard);
        canvas.setOnMouseDragged(draggedBoard);
        canvas.setOnMouseReleased(releasedBoard);
        run.setOnAction(clickRun);
        grid.setOnAction(clickGrid);
        if (isFractions) {
            root.getChildren().add(displayFractions);
            AnchorPane.setTopAnchor(displayFractions, (double) (10 + sizey * 20 + 50));
            AnchorPane.setLeftAnchor(displayFractions, (double) 10);
            displayFractions.setContent(fractions);
            displayFractions.setPrefSize(sizex * 20, 100);
            displayFractions.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            displayFractions.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        }

        AnchorPane.setTopAnchor(run, (double) (sizey * 20 + 20));
        AnchorPane.setLeftAnchor(run, 10.0);
        AnchorPane.setTopAnchor(displayState, (double) (sizey * 20 + 20));
        AnchorPane.setLeftAnchor(displayState, 70.0);


        tstate.setText("2");
        tstate.setMinSize(sizex * 20 - 60, 20);
        displayState.setContent(tstate);
        displayState.setPrefSize(sizex * 20 - 60, 20);
        displayState.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        displayState.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        
        gc.setFill(Color.ANTIQUEWHITE);
        gc.fillRect(0, 0, sizex * 20 + 20, windowheight);
        gc.setFill(Color.WHITE);
        gc.fillRect(10, 10, sizex * 20, sizey * 20);

        addGrid();

        while (primes.size() < sizex*sizey + 2)
        {
            nextPrime();
        }
    }
    
    static boolean displayFractions() throws IOException {
    
        int totlen = 0;
        String input = new Scanner(Paths.get("fractrancode.txt"), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();
        String[] fracts = input.split(", ");
        for (int i = 0; i < fracts.length; i++)
        {
            String[] splitted = fracts[i].split("/");
            totlen += 7*Math.max(splitted[0].length(), splitted[1].length());
        }
    
        int canvaswidth = 4096;
        int rowheight = 40;
        if (totlen < 4096)
        {
            canvaswidth = totlen;
        }
        fractions = new Canvas(canvaswidth, rowheight * Math.ceil((double) totlen / (double) canvaswidth));
        GraphicsContext gc2 = fractions.getGraphicsContext2D();
        gc2.setFill(Color.BLACK);
        int[] pos = {0, 20};
        for (int i = 0; i < fracts.length; i++)
        {
            pos[0] += 10;
            String[] splitted = fracts[i].split("/");
            int thiswidth = 7 * Math.max(splitted[0].length(), splitted[1].length());
            if (pos[0] + thiswidth > 4096)
            {
                pos[0] = 10;
                pos[1] += rowheight;
            }
            
            double x = pos[0] + thiswidth / 2.0;
            
            gc2.fillText(splitted[0], x - 7 * splitted[0].length() / 2.0, pos[1]);
            gc2.fillText(splitted[1], x - 7 * splitted[1].length() / 2.0, pos[1] + 17);
            gc2.fillRect(pos[0], pos[1] + 3, thiswidth, 2);
        
            pos[0] += thiswidth;
        }
        
        if (pos[1] < 4096 - rowheight)
        {
            /*root.getChildren().add(displayFractions);
            displayFractions.setContent(fractions);
            displayFractions.setPrefSize(sizex*20, 100);
            displayFractions.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            displayFractions.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            AnchorPane.setTopAnchor(displayFractions, (double) (10 + sizey * 20 + 50));
            AnchorPane.setLeftAnchor(displayFractions, (double) 10);*/

            return true;
        }
        return false;
    
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

    public static void addGrid()
    {
        if(hasGrid)
        {
            gc.setFill(Color.LIGHTGRAY);
            for (int x = 1; x < sizex; x++) {
                gc.fillRect(x * 20 + 10, 10, 1, sizey * 20);
            }
            for (int y = 1; y < sizey; y++) {
                gc.fillRect(10, y * 20 + 10, sizex * 20, 1);
            }
        }

    }

    public static void paint()
    {
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
                gc.fillRect(x*20+10, y*20+10, 20, 20);
            }
        }
        addGrid();
    }

}
