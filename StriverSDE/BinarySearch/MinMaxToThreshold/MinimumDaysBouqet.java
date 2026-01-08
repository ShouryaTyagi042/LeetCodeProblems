public class MinimumDaysBouqet {
    public static void main(String[] args) {
        int[] bloomDay = {7,7,7,7,12,7,7} ;
        int m = 2;
        int k = 3;
        Solution solve = new Solution() ;
        System.out.println(solve.minDays(bloomDay, m, k));
    }
}

class Solution {
    public int minDays(int[] bloomDay, int m, int k) {
        int max = 0 ;
        for(int day: bloomDay) {
            if (max <= day) {
                max = day ;
            }
        }
        return this.doBinarySearch(bloomDay, 1, max, k , m ) ;
    }

    public int doBinarySearch(int[] bloomDay, int start, int end, int k , int m) {
        int ans = -1;
        while (start <= end) {
            int mid = start + (end - start) / 2 ;
            if(this.numberOfBoquetsForDays(bloomDay, mid, k) >= m) {
                ans = mid ;
                end = mid - 1 ;
            } else {
                start = mid + 1 ;
            }
        }
        return ans;
    }

    public int numberOfBoquetsForDays(int[] bloomDay, int currentDay, int k) {
        int count = 0 ;
        int ans = 0 ;
        for(int day: bloomDay) {
            if(day <= currentDay) {
                count ++ ;
                if (count == k) {
                    ans ++ ;
                    count = 0 ;
                }
            } else {
                count = 0 ;
            }
        }
        return ans ;
    }

}
