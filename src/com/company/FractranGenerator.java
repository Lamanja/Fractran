package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import static jdk.nashorn.internal.objects.NativeString.substring;

public class FractranGenerator {
    static ArrayList<Long> primes = new ArrayList<>(Arrays.asList((long) 2, (long) 3));

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

    static public void generateProgram(int xd, int yd) throws FileNotFoundException, UnsupportedEncodingException {
        while (primes.size() < 3*xd*yd + 2)
        {
            nextPrime();
        }

        BigInteger[][][] variables = new BigInteger[3][xd][yd];
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < xd; j++)
            {
                for (int k = 0; k < yd; k++)
                {
                    variables[i][j][k] = BigInteger.valueOf(primes.get(i*xd*yd + j*yd + k + 2));
                }
            }
        }

        String program = "";
        BigInteger nominator;
        program += primes.get(0).toString() + "/" + primes.get(1).toString() + ", ";
        for(int i =0; i < xd; i++)
        {
            for(int j =0; j < yd; j++)
            {
                nominator = variables[2][i][j].multiply(BigInteger.valueOf(primes.get(1)));
                for(int x = -1; x < 2; x++)
                {
                    int xindex = i + x;
                    if(xindex == -1 || xindex == xd)
                    {
                        continue;
                    }
                    for(int y = -1; y < 2; y++)
                    {
                        int yindex = j + y;
                        if(yindex == -1 || yindex == yd || (x == 0 && y == 0))
                        {
                            continue;
                        }
                        nominator = nominator.multiply(variables[1][xindex][yindex]);

                    }
                }

                program += nominator.toString() + "/" + variables[0][i][j].multiply(BigInteger.valueOf(primes.get(0))) + ", ";

            }

        }
        program += "1/" + primes.get(0).toString() + ", ";

        BigInteger var0;
        BigInteger var1;
        BigInteger var2;
        for(int i = 0; i < xd; i++)
        {
            for(int j = 0; j < yd; j++)
            {

                var0 = variables[0][i][j];
                var1 = variables[1][i][j];
                var2 = variables[2][i][j];

                program += "1/" + var1.pow(7).toString() + ", ";
                program += "1/" + var1.pow(6).toString() + ", ";
                program += "1/" + var1.pow(4).toString() + ", ";
                program += var0.toString() + "/" + var1.pow(3).toString() + ", ";
                program += var0.toString() + "/" + var1.pow(2).multiply(var2).toString() + ", ";
                program += "1/" + var1.toString() + ", ";
                program += "1/" + var2.toString() + ", ";
            }

        }
        PrintWriter writer = new PrintWriter("fractrancode.txt");
        writer.print(substring(program, 0, program.length() - 2));
        writer.close();


    }
}
