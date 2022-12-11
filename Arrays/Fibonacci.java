package Arrays;

import java.util.Arrays;

public class Fibonacci {
    public static long[] printFibb(int n)
    {
        long n1=0,n2=1,n3 ;
        long[] ans = new long[n] ;
        ans[0] = 1 ;
        for (int i = 1 ; i < n ; i++) {
            n3 = n2 + n1 ;
            ans[i] = n3 ;
            n1 = n2 ;
            n2 = n3 ;
        }
        return ans ;

    }
    public static void main(String[] args) {
        long[] ans = printFibb(7) ;
        System.out.println(Arrays.toString(ans));

    }

}
