class Solution {
    public long kthElement( int arr1[], int arr2[], int n, int m, int k) {
        int current = arr1[0] , ptr1 = 0 , ptr2 = 0, check = k ;
        while(ptr1 < arr1.length && ptr2 < arr2.length ) {
            if(check == 0 ) {
                return current ;
            }
            if(arr1[ptr1] > arr2[ptr2]) {
                current = arr2[ptr2] ;
                ptr2++ ;
            }
            else if(arr1[ptr1] < arr2[ptr2]) {
                current = arr1[ptr1] ;
                ptr1 ++ ;
            }
            check--;
        }
        if(ptr1 != arr1.length && check != 0 ) {
            while (check > 0 ) {
                current = arr1[ptr1] ;
                check-- ;
                ptr1++ ;
            }
        }
        if(ptr2 != arr2.length && check != 0 ) {
            while (check > 0 ) {
                current = arr2[ptr2] ;
                check-- ;
                ptr2++ ;
            }
        }
        return current ;
    }
}
