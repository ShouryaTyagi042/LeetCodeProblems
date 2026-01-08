public class NumberOfOnes {
    public static void main(String[] args) {
        Solution solve = new Solution();
        int[][] arr = {{0,0,0,0}, {0,0,0,0},{0,0,0,0}, {0,0,0,0}} ;
        System.out.println(solve.rowWithMax1s(arr));
    }

}

// User function Template for Java

class Solution {
    public int rowWithMax1s(int arr[][]) {
        int max_count = 0 ;
        int row_index = -1;
        for (int i = 0 ; i < arr.length ; i++) {
            int count = this.countOnes(arr[i]) ;
            if (max_count < count) {
                max_count = count ;
                row_index = i ;
            }
        }
        return row_index ;
    }

    public static int lowerBound(int[] arr, int target) {
    int low = 0, high = arr.length;   // notice: high = n

    while (low < high) {
        int mid = low + (high - low) / 2;

        if (arr[mid] < target) {
            low = mid + 1;
        } else {
          high = mid;
        }
    }
    return low;
    }

    public int countOnes(int[] row ) {
        int count = 0 ;
        int index = this.lowerBound(row, 1) ;
        count = row.length -  index ;
        return count ;
    }


}
