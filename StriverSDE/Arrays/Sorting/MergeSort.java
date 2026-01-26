import java.util.Arrays ;

public class MergeSort {
    public static void main(String[] args) {
        Solution solve = new Solution();
        int[] arr = {2, 4, 1, 3, 5} ;
        solve.mergeSort(arr, 0, arr.length - 1 );
        System.out.println(Arrays.toString(arr));
    }
}

class Solution {

    void mergeSort(int arr[], int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2 ;
            mergeSort(arr, l, m) ;
            mergeSort(arr, m + 1 , r) ;
            merge(arr, l , m , r) ;
        }
    }

    void merge(int[] arr, int l, int m, int r) {
        int n1 = m - l + 1 ;
        int n2 = r - m ;

        int arr1[] = new int[n1] ;
        int arr2[] = new int[n2] ;

        for(int i = 0 ; i < n1 ; i++) {
            arr1[i] = arr[l + i] ;
        }

        for(int j = 0 ; j < n2 ; j++) {
            arr2[j] = arr[m + 1 + j] ;
        }

        int i  = 0 , j = 0 , k = l ;
        while(i < n1 && j < n2) {
            if(arr1[i] < arr2[j]) {
                arr[k] = arr1[i] ;
                i++;
            } else {
                arr[k] = arr2[j] ;
                System.out.print(arr[i]);
                System.out.print(" ");
                System.out.println(arr[j]);
                j++;
            }
            k++;
        }

        while(i < n1) {
            arr[k] = arr1[i] ;
            i++;
            k++;
        }

        while(j < n2) {
            arr[k] = arr2[j] ;
            j++;
            k++;
        }
    }
}
