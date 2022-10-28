package Arrays;

public class MergeSorted {
    public static void main(String[] args) {

    }
    static void merge(long[] arr1, long[] arr2, int n, int m)
    {
        int p1 = 0 , p2 = 0 ;
        while(p1 < n || p2 < m) {
            long temp;
            if(arr1[p1] > arr2[p2]) {
                temp = arr1[p1] ;
                arr1[p1] = arr2[p2] ;
                arr2[p2] = temp ;
                p1++ ;
            }
            if(arr1[p1] )
        }

    }

}
