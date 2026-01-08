public class SearchInMatrixTwo {
    public static void main(String[] args) {
        Solution solve = new Solution();
        int[][] matrix = {{1,4,7,11,15},{2,5,8,12,19},{3,6,9,16,22},{10,13,14,17,24},{18,21,23,26,30}} ;
        int target = 5 ;
        System.out.println(solve.searchMatrix(matrix, target));
    }

}

class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int r = 0 ;
        int c = matrix[0].length - 1 ;
        while(r < matrix.length && c >= 0)  {
            if (matrix[r][c] == target) return true ;
            else if (matrix[r][c] > target) {
                c -= 1;
            } else {
                r += 1;
            }
        }
        return false ;

    }
}
