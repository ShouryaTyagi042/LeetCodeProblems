//Solution 1 : traversing through each element and then assigning -1 to column and row except for 0
class Solution {
    public void setZeroes(int[][] matrix) {

        int[] column = new int[matrix[0].length] ;
        int[] row = new int[matrix.length] ;

        for(int i = 0 ; i < matrix.length ; i++ ) {
            for(int j = 0 ; j < matrix[0].length ; j++) {
                if (matrix[i][j] == 0) {
                    //row
                    for(int k = 0 ; k < matrix[0].length ; k++ ) {
                        if(matrix[i][k] == 0 ) {
                            continue ;
                        }
                        else {
                            matrix[i][k] = Integer.MIN_VALUE ;
                        }

                    }
                    //column
                    for(int l = 0 ; l < matrix.length ; l++) {
                        if (matrix[l][j] == 0  ) {
                            continue
                        }
                        else {
                            matrix[l][j] = Integer.MIN_VALUE ;
                        }
                    }
                }
            }
        }
        for(int i = 0 ; i < matrix.length ; i++ ) {
            for(int j = 0 ; j < matrix[0].length ; j++) {
                if(matrix[i][j] == Integer.MIN_VALUE) {
                    matrix[i][j] = 0 ;
                }
            }
        }



    }
}

//Solution2 :

class Solution {
    public void setZeroes(int[][] matrix) {

        int[] column = new int[matrix[0].length] ;
        Arrays.fill(column,0);
        int[] row = new int[matrix.length] ;
        Arrays.fill(row,0);

        for(int i = 0 ; i < matrix.length ; i++ ) {
            for(int j = 0 ; j < matrix[0].length ; j++) {
                if (matrix[i][j] == 0) {
                    column[j] = 1 ;
                    row[i] = 1 ;
                }
            }
        }
        for(int i = 0 ; i < column.length ; i++ ) {
            if(column[i] == 1) {
                for(int k = 0 ; k < matrix.length ; k++ ) {
                    matrix[k][i] = 0 ;
                }
            }
        }
        for(int j = 0 ; j < row.length ; j++) {
            if(row[j] == 1) {
                for(int k = 0 ; k < matrix.length ; k++ ) {
                    matrix[j][k] = 0 ;
                }
            }
        }



    }
}
