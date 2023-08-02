class Solution {
    public int maxProfit(int[] numbers, int N) {
        int[] arr = new int[10] ;
        Arrays.fill(arr,0) ;
        for(int i : numbers) {
            int tens = i / 10 ;
            int zero = i % 10 ;
            arr[tens] += 1 ;
            arr[zero] += 1  ;
            if(tens == zero) {
                arr[tens] -= 1 ;
            }
        }
        int max = Integer.MIN_VALUE ;
        for(int i : arr) {
            if(i > max) {
                max = i ;
            }
        }
        return max ;
    }
}
