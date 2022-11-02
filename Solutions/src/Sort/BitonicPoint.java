package Sort;

public class BitonicPoint {
    public static void main(String[] args) {
        int[] arr = {1,15,25,45,6} ;
        System.out.println(findMaximum(arr, arr.length));

    }
    public static int findMaximum(int[] arr, int n) {
        int start = 0 ;
        int end = n -1 ;
        while (start <= end) {
            int mid = start + (end-start) / 2 ;
            if(mid + 1 == n ) return  arr[n-1];
            if (arr[mid] > arr[mid-1] && arr[mid] > arr[mid+1]) return arr[mid] ;
            else if (arr[mid] < arr[mid+1]) start = mid + 1 ;
            else end = mid - 1  ;
        }
        return -1 ;
    }
}
