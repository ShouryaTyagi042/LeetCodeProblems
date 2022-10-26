package Arrays;

import java.util.Arrays;

public class Sort0to2 {
    public static void main(String[] args) {
        int[] arr = {0,2,1,2,0,1} ;
        int n = 6 ;
        sort012(arr,n);


    }
     static void sort012(int[] arr, int n)
    {
        int countZ = 0  , countO = 0  ;
        for (int i = 0; i < n; i++) {
            if (arr[i] == 0) {
                countZ ++ ;
            }
            if (arr[i] == 1) {
                countO ++ ;
            }

        }
        int counter = 0 ;
        while(countZ >  0) {
            arr[counter] = 0 ;
            counter ++ ;
            countZ -- ;
        }
        while (countO > 0) {
            arr[counter] = 1 ;
            counter ++ ;
            countO -- ;
        }
         while (counter < n ) {
             arr[counter] = 2;
             counter++;
         }

        System.out.println(Arrays.toString(arr));

    }

}
