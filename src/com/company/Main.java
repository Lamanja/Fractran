package com.company;

import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main
{
    static ArrayList<Integer> primes = new ArrayList<>(Arrays.asList(2, 3));
    public static int s = primes.size();
    static Number[] deNominators;
    static Number[] nominators;
    static Number num;
    public static void main(String[] args)
    {
        createProgram("455/33, 11/13, 1/11, 3/7, 11/2, 1/3");
        for(int i = 0; i < nominators.length; i++)
        {
            deNominators[i].normalize();
            nominators[i].normalize();
        }
        while (true){
            Scanner scanner = new Scanner(System.in);
            int a = scanner.nextInt();
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
            deNominators[i] = new Number(Integer.parseInt(nums[1]));
            nominators[i] = new Number(Integer.parseInt(nums[0]));

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

    static public Number run(int n)
    {
        boolean isDone;

        num = new Number(n);

        while(true)
        {
            isDone = true;
            if(num.check())
            {
                System.out.println(num.v.get(0));
            }
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
