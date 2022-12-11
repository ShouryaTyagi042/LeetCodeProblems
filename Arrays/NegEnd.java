package Arrays;

import java.util.Arrays;

public class NegEnd {
    public static void main(String[] args) {
        int[] arr = {-5, 7, -3, -4, 9, 10, -1, 11} ;
        segregateElements(arr, arr.length);

    }
    static void segregateElements(int[] arr, int n)
    {
        int[] pos = new int[n] ;
        int[] neg = new int[n] ;
        int p1=0,p2=0 ;
        for (int a:
             arr) {
            if (a < 0 ) {
                neg[p2] = a ;
                p2++ ;
            }
            else {
                pos[p1] = a ;
                p1++ ;
            }

        }
        System.arraycopy(pos, 0, arr, 0, p1);
        System.arraycopy(neg, 0, arr, p1, p2);
//        System.out.println(Arrays.toString(arr));
    }
}
