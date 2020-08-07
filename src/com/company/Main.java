package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Main
{
    static ArrayList<Integer> primes = new ArrayList<>(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29));
    public static int s = primes.size();
    static Number[] deNominators = {new Number(91), new Number(85), new Number(51), new Number(38), new Number(33), new Number(29), new Number(23), new Number(19), new Number(17), new Number(13), new Number(11), new Number(2), new Number(7), new Number(1)};
    static Number[] nominators = {new Number(17), new Number(78), new Number(19), new Number(23), new Number(29), new Number(77), new Number(95), new Number(77), new Number(1), new Number(11), new Number(13), new Number(15), new Number(1), new Number(55)};

    static Number num = new Number(2);
    public static void main(String[] args)
    {
        while(true)
        {
            if(num.check())
            {
                System.out.println(num.v.get(0));
            }
            for (int j = 0; j < nominators.length; j++)
            {
                if (num.isDivisible(deNominators[j]))
                {
                    num.multiply(nominators[j]);
                    break;
                }
            }
        }
    }
}
