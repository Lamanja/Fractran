package com.company;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FractranProgram {
    ArrayList<Integer> primes = new ArrayList<>(Arrays.asList(2, 3, 5, 7, 11));
    public int s = primes.size();
    Number[] deNominators;
    Number[] nominators;
    Number num;

    public FractranProgram() throws IOException {
        this(new Scanner(Paths.get("fractrancode.txt"), StandardCharsets.UTF_8.name()).useDelimiter("\\A").next());
    }
    public FractranProgram(String input)
    {
        {
            String[] fracs = input.split(", ");
            String[] nums;
            deNominators = new Number[fracs.length];
            nominators = new Number[fracs.length];
            for(int i = 0; i < fracs.length; i++)
            {
                nums = fracs[i].split("/");
                deNominators[i] = new Number(new BigInteger(nums[1]), this);
                nominators[i] = new Number(new BigInteger(nums[0]), this);

            }

            for(int i = 0; i < nominators.length; i++)
            {
                deNominators[i].normalize();
                nominators[i].normalize();
            }
        }
    }

    void nextPrime()
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

    boolean checkPrime(int p)
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

    public Number run(BigInteger n)
    {
        boolean isDone;

        num = new Number(n, this);

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
                //System.out.println(num.calc());
                break;
            }
        }
        return(num);
    }
}
