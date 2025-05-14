package GreedyAlgorithms;
import java.util.Arrays;
import java.util.Collections;
public class MinimumCostCutASquare {
// Java program to divide a 
// board into m*n squares

 

    // method returns minimum cost to break board into
    // m*n squares
    static int minimumCostOfBreaking(Integer X[], Integer Y[], 
                                                 int m, int n)
    {
        int res = 0;
     
        // sort the horizontal cost in reverse order
        Arrays.sort(X, Collections.reverseOrder());
     
        // sort the vertical cost in reverse order
        Arrays.sort(Y, Collections.reverseOrder());
     
        // initialize current width as 1
        int hzntl = 1, vert = 1;
     
        // loop until one or both
        // cost array are processed
        int i = 0, j = 0;
        while (i < m && j < n)
        {
            if (X[i] > Y[j])
            {
                res += X[i] * vert;
     
                // increase current horizontal
                // part count by 1
                hzntl++;
                i++;
            }
            else
            {
                res += Y[j] * hzntl;
     
                // increase current vertical 
                // part count by 1
                vert++;
                j++;
            }
        }
     
        // loop for horizontal array, 
        // if remains
        int total = 0;
        while (i < m)
            total += X[i++];
        res += total * vert;
     
        // loop for vertical array, 
        // if remains
        total = 0;
        while (j < n)
            total += Y[j++];
        res += total * hzntl;
     
        return res;
    }
     
    // Driver program
    public static void main(String arg[])
    {
        int m = 6, n = 4;
        Integer X[] = {2, 1, 3, 1, 4};
        Integer Y[] = {4, 1, 2};
        System.out.print(minimumCostOfBreaking(X, Y, m-1, n-1));
    }
}
 
// This code is contributed by Anant Agarwal.

