import java.util.* ;

public class FindPeakElement {
    public static void main(String[] args) {
        Solution solve = new Solution() ;
        int[][] mat = {{1,2,3,1,8},{2,3,4,5,6}} ;
        System.out.println(Arrays.toString(solve.findPeakGrid(mat))) ;
    }
}

class Solution {

    class FindPeak {
        int index ;
        boolean check ;

        FindPeak(int index, boolean check) {
            this.index = index ;
            this.check = check ;
        }
    }

    public int[] findPeakGrid(int[][] mat) {
        int col_end = mat[0].length - 1  ;
        int col_start = 0 ;
        while(col_start <= col_end) {
            int mid_col = col_start + (col_end - col_start) / 2 ;
            // find the max element
            int max = -1 ;
            int row = 0 ;
            for(int i = 0 ; i < mat.length ; i++) {
                if(mat[i][mid_col] > max) {
                    max = mat[i][mid_col] ;
                    row = i ;
                }
            }
            // check if the max is peak
            FindPeak ans = this.checkForPeak(mat, row, mid_col) ;
            System.out.println(ans.index);
            if (ans.check) {
                return new int[]{row,mid_col} ;
             } else {
                if (ans.index > mid_col) {
                    col_start = mid_col + 1 ;
                } else {
                    col_end = mid_col - 1 ;
                }
            }
        }

        int[] ans = {-1,-1} ;
        return ans ;

    }

    public FindPeak checkForPeak(int[][] mat, int row, int col) {
        System.out.print(row);
        System.out.print(" ");
        System.out.println(col);
        if ( row < mat.length && row >= 0 && col < mat[0].length && col >= 0) {
            if (col != 0 && mat[row][col] < mat[row][col-1]) {
                return new FindPeak(col - 1 , false) ;
            }
            if (col != mat[0].length - 1  && mat[row][col] < mat[row][col + 1] ) {
                return new FindPeak(col + 1 , false) ;
            }
            return new FindPeak(col , true) ;
        }
        return new FindPeak(col , false) ;
    }
}
