class Solution {
    public double myPow(double x, int n) {
        double ans = 1 ;
        while(n != 0 ) {
            if(n%2==0) {
                x = x * x  ;
                n / = 2 ;
            }
            else {
                ans = ans * x ;
                n--;
            }
        }
        return ans ;

    }
}
