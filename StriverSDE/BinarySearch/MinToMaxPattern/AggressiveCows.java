import java.util.Arrays ;

public class AggressiveCows {
    public static void main(String[] args) {
        Solution solve = new Solution();
        int[] stalls = {10, 1, 2, 7, 5} ;
        int k = 3 ;
        System.out.println(solve.aggressiveCows(stalls, k));
    }

}

class Solution {
    public int aggressiveCows(int[] stalls, int k) {
        if (k > stalls.length) return -1 ;
        int maxDistance = 0  ;
        Arrays.sort(stalls) ;
        maxDistance = stalls[stalls.length - 1] - stalls[0];
        return this.doBinarySearch(stalls, k, 1, maxDistance) ;
    }

    public int doBinarySearch(int[] stalls, int k , int start, int end) {
        int ans  = -1;
        while(start <= end)  {
            int mid = start + (end - start) / 2 ;
            if (this.canCowsBePlaced(stalls, k, mid)) {
                ans = mid ;
                start = mid + 1 ;
            } else {
                end = mid - 1  ;
            }
        }
        return ans ;
    }

    public boolean canCowsBePlaced(int[] stalls, int k , int gap) {
        int count = 1 ;
        int currentIndex = 0 ;
        for(int i = 1 ; i < stalls.length ; i++ ) {
            if(stalls[i] - stalls[currentIndex] >= gap) {
                count += 1 ;
                currentIndex = i ;
            }
        }
        if(count >= k) return true ;
        return false ;
    }
}
