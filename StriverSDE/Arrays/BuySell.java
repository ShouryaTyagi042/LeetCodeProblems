class Solution {
    public int maxProfit(int[] prices) {
        int max = Integer.MIN_VALUE ;
        for (int i = 0 ; i < prices.length ; i++ ) {
            for(int j = i + 1 ; j < prices.length ; j++ ) {
                if (prices[j]-prices[i] > max ) {
                    max = prices[j] - prices[i] ;
                }
            }
        }
        return max ;

    }
}


//solution 2
class Solution {
    public int maxProfit(int[] prices) {
        int max = 0 ;
        int i = 0 , j = 1 ;
        while(i < prices.length) {
            if (prices[i] > prices[j]) {
                i++ ;
                j = i + 1 ;
            }
            if (prices[j] - prices[i] > max ) {
                max = prices[j] - prices[i] ;
            }
            j++ ;
        }

    }
}
