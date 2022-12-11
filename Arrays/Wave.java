package Arrays;

import java.util.Arrays;

public class Wave {
    public static void main(String[] args) {
        int[] a = {1,2,3,4,5} ;
        convertToWave(a.length, a);
        System.out.println(Arrays.toString(a));

    }
    public static void convertToWave(int n, int[] a) {
        int i = 1 ,temp;
        while(i < n ) {
            temp = a[i] ;
            a[i] = a[i-1];
            a[i-1] = temp ;
            i+=2 ;
        }

    }
}
