package Matrix;


public class Rotate90 {
    public static void main(String[] args) {
        int[][] matrix =  {{1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}} ;
        rotateby90(matrix,matrix.length);

    }
    static void rotateby90(int[][] matrix, int n)
    {
        int current = 0 ;
        int[] arr = new int[n*n] ;
        for (int i = n-1; i >= 0 ; i--) {
            for (int j = 0; j < n; j++) {
                arr[current] = matrix[j][i] ;
                current ++ ;
            }
        }
        current = 0 ;
        for (int i = 0; i < n ; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = arr[current];
                current++ ;
            }
        }



    }
}
