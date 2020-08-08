package com.company;

import java.math.BigInteger;
import java.util.ArrayList;

public class Number
{
    ArrayList<Integer> v = new ArrayList<>();

    public Number(BigInteger n)
    {
        for (int i = 0; i < Main.s; i++)
        {
            int f = 0;
            while (n.mod(BigInteger.valueOf(Main.primes.get(i))).equals(BigInteger.valueOf(0)))
            {
                f++;
                n = n.divide(BigInteger.valueOf(Main.primes.get(i)));
            }
            v.add(f);
            if(i == Main.s - 1 && !n.equals(BigInteger.valueOf(1)))
            {
                Main.nextPrime();
            }
        }
    }

    public boolean isDivisible(Number n)
    {
        for (int i = 0; i < Main.s; i++)
        {
            if (n.v.get(i) > v.get(i))
            {
                return false;
            }
        }
        for (int i = 0; i < Main.s; i++)
        {
            v.set(i, v.get(i) - n.v.get(i));
        }
        return true;
    }
    public void multiply(Number n)
    {
        for (int i = 0; i < Main.s; i++)
        {
            v.set(i, v.get(i) + n.v.get(i));
        }
    }

    public void normalize()
    {
        while (v.size() < Main.s)
        {
            v.add(0);
        }
    }

    public long calc()
    {
        int returnValue = 1;
        for (int i = 0; i < Main.s; i++)
        {
            returnValue *= Math.pow(Main.primes.get(i), v.get(i));
        }
        return returnValue;
    }
}