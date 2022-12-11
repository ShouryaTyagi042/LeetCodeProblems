package Matrix;

public class SummedMatrix {
    public static void main(String[] args) {
        long n = 5 ;
        long q = 4 ;
        System.out.println(sumMatrix(n,q));

    }
    static long sumMatrix(long n, long q) {
        return n - Math.abs(n+1-q) ;
    }
}
