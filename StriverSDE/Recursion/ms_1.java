// you can also use imports, for example:
import java.util.*;

// you can write to stdout for debugging purposes, e.g.
// System.out.println("this is a debug message");

class Solution {
    public int solution(int[] A) {
        A.sort() ;
        Set<Integer> s = new HashSet<>() ;

    for(int i = 0 ; i < A.length ; i++ ){
        for(int j = i +1 ; j < A.length ; j++ ){
            int diff = A[i] - A[j] ;
            set.add(diff) ;
        }
    }
    int ans = Integer.MIN_VALUE ;
    int[] arr = set.toArray() ; 
    for(int i : arr) {
        ans=Math.max(ans,fun(i,A)); 
    }
    return ans ;
    }

     public static int fun(int d, int[] A} {
        HashMap<Integer,Integer> mp = new HashMap<>() ;
        int ans= Integer.MIN_VALUE ;
        for(int i=0;i<n;i++){
            int x=A[i]-d;
            if(mp.get(x)>0){
                mp.put(A[i], mp.get(x) +1) ;
            }
            else{
                mp.put(A[i],1);
            }
            ans=Math.max(ans,mp[A[i]]);
        }
        return ans;
}

