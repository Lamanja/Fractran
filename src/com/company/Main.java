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
    static ArrayList<Integer> primes = new ArrayList<>(Arrays.asList(2, 3));
    public static int s = primes.size();
    static Number[] deNominators;
    static Number[] nominators;
    static Number num;
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(Paths.get("fractrancode.txt"), StandardCharsets.UTF_8.name());
        String content = scanner.useDelimiter("\\A").next();
        scanner.close();

        createProgram(content);
        for(int i = 0; i < nominators.length; i++)
        {
            deNominators[i].normalize();
            nominators[i].normalize();
        }
        while (true){
            System.out.println("Next");
            scanner = new Scanner(System.in);
            BigInteger a = scanner.nextBigInteger();
            run(a);

        }
    }
    static void createProgram(String input)
    {
        String[] fracs = input.split(", ");
        String[] nums;
        deNominators = new Number[fracs.length];
        nominators = new Number[fracs.length];
        for(int i = 0; i < fracs.length; i++)
        {
            nums = fracs[i].split("/");
            deNominators[i] = new Number(new BigInteger(nums[1]));
            nominators[i] = new Number(new BigInteger(nums[0]));

        }

    }

    static void nextPrime()
    {
        s++;
        int p = primes.get(primes.size() - 1);
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

    static boolean checkPrime(int p)
    {
        for (int q : primes)
        {
            if(p%q == 0)
            {
                return false;
            }
        }
        return true;
    }

    static public Number run(BigInteger n)
    {
        boolean isDone;

        num = new Number(n);

        while(true)
        {
            isDone = true;
            for (int j = 0; j < nominators.length; j++)
            {
                if (num.isDivisible(deNominators[j]))
                {
                    num.multiply(nominators[j]);
                    isDone = false;
                    break;
                }
            }
            if(isDone)
            {
                System.out.println(num.calc());
                break;
            }
        }
        return(num);
    }
}
