package com.company;

import java.math.BigInteger;
import java.util.ArrayList;

public class Number
{
    ArrayList<Integer> v = new ArrayList<>();
    FractranProgram p;

    public Number(BigInteger n, FractranProgram _p)
    {
        p = _p;
        for (int i = 0; i < p.s; i++)
        {
            int f = 0;
            while (n.mod(BigInteger.valueOf(p.primes.get(i))).equals(BigInteger.valueOf(0)))
            {
                f++;
                n = n.divide(BigInteger.valueOf(p.primes.get(i)));
            }
            v.add(f);
            if(i == p.s - 1 && !n.equals(BigInteger.valueOf(1)))
            {
                p.nextPrime();
            }
        }
    }

    public boolean isDivisible(Number n)
    {
        for (int i = 0; i < p.s; i++)
        {
            if (n.v.get(i) > v.get(i))
            {
                return false;
            }
        }
        for (int i = 0; i < p.s; i++)
        {
            v.set(i, v.get(i) - n.v.get(i));
        }
        return true;
    }
    public void multiply(Number n)
    {
        for (int i = 0; i < p.s; i++)
        {
            v.set(i, v.get(i) + n.v.get(i));
        }
    }

    public void normalize()
    {
        while (v.size() < p.s)
        {
            v.add(0);
        }
    }

    public long calc()
    {
        int returnValue = 1;
        for (int i = 0; i < p.s; i++)
        {
            returnValue *= Math.pow(p.primes.get(i), v.get(i));
        }
        return returnValue;
    }
}