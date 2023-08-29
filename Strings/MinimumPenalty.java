class Solution {
    public int bestClosingTime(String customers) {
        int l = customers.length() ;
        int minPenalty = Integer.MAX_VALUE  ;
        int ans = 0 ;
        for(int i = 0 ; i < l ; i++ ) {
            int currentPenalty = 0 ;
            for(int j = 0 ; j < l ; j++) {
                if(j < i ) {
                    if(customers.charAt(j)=='N') currentPenalty ++ ;
                }
                else {
                    if(customers.charAt(j) == 'Y') currentPenalty ++ ;
                }
            }
            if(currentPenalty < minPenalty ) {
                minPenalty = currentPenalty ;
                ans = i ;
            }
        }
        if(customers.charAt(l-1) == 'Y' && ans = l-1) return ans+1;
        return ans ;



    }
}
