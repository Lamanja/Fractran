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
        createProgram("107/109, 131794161814201/214, 139662469982213/321, 143596624066219/535, 155399086318237/749, 163267394486249/1177, 175069856738267/1391, 190806473074291/1819, 198674781242303/2033, 202608935326309/2461, 1/107, 1/17249876309, 1/594823321, 1/707281, 2/24389, 2/56347, 1/29, 1/67, 1/27512614111, 1/887503681, 1/923521, 3/29791, 3/68231, 1/31, 1/71, 1/94931877133, 1/2565726409, 1/1874161, 5/50653, 5/99937, 1/37, 1/73, 1/194754273881, 1/4750104241, 1/2825761, 7/68921, 7/132799, 1/41, 1/79, 1/271818611107, 1/6321363049, 1/3418801, 11/79507, 11/153467, 1/43, 1/83, 1/506623120463, 1/10779215329, 1/4879681, 13/103823, 13/196601, 1/47, 1/89, 1/1174711139837, 1/22164361129, 1/7890481, 17/148877, 17/272473, 1/53, 1/97, 1/2488651484819, 1/42180533641, 1/12117361, 19/205379, 19/351581, 1/59, 1/101, 1/3142742836021, 1/51520374361, 1/13845841, 23/226981, 23/383263, 1/61, 1/103");
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
            deNominators[i] = new Number(Long.parseLong(nums[1]));
            nominators[i] = new Number(Long.parseLong(nums[0]));

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
