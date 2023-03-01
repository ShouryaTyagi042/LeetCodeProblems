import java.util.* ;
import java.io.*;
public class Solution {
    static double multiply(double num, int n) {
    double ans = 1.0;
    for(int i = 1;i<= n;i++){
        ans*= num;
    }
    return ans;
    }

    public static double findNthRootOfM(int n, int m) {
        double low = 1,high = m,eps= 1e-8;
        while((high - low)>eps){
        double mid = (low+high)/2.0;
        if(multiply(mid,n)<m){
            low=mid;
        }
        else
            high= mid;
    }
    return low;
    }
}
