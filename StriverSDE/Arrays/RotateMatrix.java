class Solution {
    public void rotate(int[][] matrix) {
        int l = matrix.length ;
        for(int i = 0 ; i < l  ; i++  ){
            for(int j = i + 1 ; j < l ; j++ ) {
                int temp = matrix[i][j] ;
                matrix[i][j] = matrix[j][i] ;
                matrix[j][i] = temp ;
            }
        }
        int s = 0 , e = l-1 ;
        for(int i = 0 ; i < l ; i++) {
            while(s <= e) {
                int temp = matrix[i][s] ;
                matrix[i][s] = matrix[i][e] ;
                matrix[i][e] = temp ;
                s++;
                e--;
            }
        }
    }
}
