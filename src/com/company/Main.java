package com.company;

import javafx.scene.transform.Scale;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Scanner;

public class Main
{

    public static void main(String[] args) throws IOException {


        Scanner scanner;
        FractranProgram p = new FractranProgram();

        while (true){
            System.out.println("Next");
            scanner = new Scanner(System.in);
            BigInteger a = scanner.nextBigInteger();
            System.out.println(p.run(a).calc());

        }
    }
}
