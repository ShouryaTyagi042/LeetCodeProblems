package Arrays;

public class Kadanes {
    public static void main(String[] args) {
        int[] arr = {-1,-2,-3,-4} ;
        System.out.println(maxSubarraySum(arr, arr.length));

    }
    public static long maxSubarraySum(int[] arr, int n){
        int maxSum = Integer.MIN_VALUE ;
        int currentSum = 0 ;
        for (int i = 0; i < n; i++) {
            currentSum += arr[i];
            if (maxSum < currentSum) maxSum = currentSum ;
            if ( currentSum < 0 ) currentSum = 0 ;
        }
        return maxSum ;



    }

}
