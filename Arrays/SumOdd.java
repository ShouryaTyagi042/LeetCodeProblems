package Arrays;

public class SumOdd {
    public static void main(String[] args) {
        int[] arr = {1,4,2,5,3} ;
        System.out.println(sumOddLengthSubarrays(arr));

    }
    static int sumOddLengthSubarrays(int[] arr) {
        int ans = 0 ;
        int c = 1 ;
        while (c<=arr.length) {
            int start = 0 , end =  c-1  , sum = 0 , CurrentSum = 0  ;
            for(int i = 0 ; i <= end ; i++  ) {
                sum += arr[i] ;
            }
            while(end < arr.length-1) {
                CurrentSum += sum ;
                sum = sum - arr[start] + arr[end+1] ;
                start ++ ;
                end ++ ;
            }
            CurrentSum += sum ;
            ans += CurrentSum ;
            c+=2 ;
        }
        return  ans ;



    }
}
