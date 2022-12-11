package Arrays;

import java.util.Arrays;

public class CyclicRotate {
    public static void main(String[] args) {
        int[] arr = {9, 8, 7, 6, 4, 2, 1, 3} ;
        rotate(arr, arr.length);

    }
    static void rotate(int[] arr, int n)
    {
        int current = 0 ;
        while(current <= n - 2 ) {
            swap(current,n-1,arr);
            current++ ;
        }
        System.out.println(Arrays.toString(arr));



    }
    static void swap(int i , int j , int[] arr) {
        int temp = arr[i] ;
        arr[i] = arr[j] ;
        arr[j] = temp ;
    }
}
