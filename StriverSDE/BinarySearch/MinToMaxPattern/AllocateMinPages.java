public class AllocateMinPages {
    public static void main(String[] args) {
        Solution solve = new Solution() ;
        int[] arr = {12, 34, 67, 90} ;
        int k = 2;
        solve.findPages(arr, k);
    }

}

class Solution {
    public int findPages(int[] arr, int k) {
        int minPartitionVal = 0 ;
        int maxPartitionVal = 0;
        for(int val : arr) {
            if (minPartitionVal <= val) {
                minPartitionVal = val ;
            }
            maxPartitionVal += val ;
        }

        return this.doBinarySearch(arr, k, minPartitionVal, maxPartitionVal) ;

    }

    public int doBinarySearch(int[] arr, int k, int start, int end) {
        int ans = -1;
        while (start <= end){
            int mid = start + (end - start) / 2 ;
            if (this.countPartitions(arr, mid) <= k ) {
                ans = mid ;
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return ans;
    }

    public int countPartitions(int[] arr, int maxVal) {
        int count = 1 ;
        int currentSum = 0 ;
        for(int val : arr) {
            if(val + currentSum <= maxVal) {
                currentSum += val ;
            } else {
                count += 1 ;
                currentSum = val ;
            }
        }
        return count ;
    }
}
