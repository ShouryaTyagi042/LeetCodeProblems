import java.util.HashMap ;
public class LargestSubArrayZeroSum {
public static void main(String[] args) {
        Solution solve = new Solution() ;
    int[] arr  = {15,-10,-5,1,8,-7,-1,0,0,0} ;
    System.out.println(solve.maxLength(arr));
}
}

class Solution {
    static int maxLength(int arr[]) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int sum = 0 ;
        int ans = 0 ;

        for(int i = 0 ; i < arr.length ; i++) {
            sum += arr[i] ;
            if (sum ==0 ) {
                ans = Math.max(ans , i + 1 ) ;
                continue;
            }
            if(map.containsKey(sum)) {
                ans = Math.max(ans , i - map.get(sum) ) ;
            } else {
                map.put(sum, i ) ;
            }
        }

        return ans ;

    }
}
