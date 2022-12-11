package Arrays;

import java.util.Arrays;

public class MinDiff {
    public static void main(String[] args) {
        int[] arr  = {3, 9, 12, 16, 20} ;
        System.out.println(getMinDiff(arr, arr.length, 3));
    }
    public static int getMinDiff(int[] arr, int n, int k) {
        Arrays.sort(arr);
        int max_diff = arr[n-1] - arr[0] ;
        int temp_min  ;
        int temp_max ;
        for (int i = 1; i < n; i++) {
            if (arr[i] - k <  0) continue;
            temp_min = Math.min(arr[0] + k , arr[i] - k ) ;
            temp_max = Math.max(arr[n-1] - k, arr[i-1] + k) ;
            max_diff = Math.min(max_diff,temp_max-temp_min) ;
        }
        return max_diff ;

    }
}
