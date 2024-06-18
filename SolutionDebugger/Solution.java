import java.util.ArrayList;
import java.util.Arrays ;
import java.util.* ;

public class Solution {
    public static int[] bitManipulation(int num, int i){
        int temp = num ;
        int[] ans = new int[3] ;
        for(int x = 0 ; x < i-1 ; x++) {
            num = num / 2  ;
        }
        int fact = 1 ;
        for(int j = 1 ; j < i ; j++) {
            fact *= 2 ;
        }


        int bit = num % 2 ;
        System.out.println(fact) ;
        System.out.println(bit) ;
        ans[0] = bit ;
        if(num == 1) {
            ans[1] = temp ;
            ans[2] = temp - fact ;
        }
        else {
            ans[1] = temp + fact ;
            ans[2] = temp ;


        }
        return ans ;
    }


    public static void main(String args[]) {
        int[] a = {1,2,1,0,1} ;
        int[] b = {2,3,5} ;
        String ans = "Shourya" ;
        Character cut = 'a' ;


        // ArrayList<Integer> arr = new ArrayList<Integer>(Arrays.asList(2,3,4,5,1));
        // ArrayList<Integer> arr = new ArrayList<Integer>(Arrays.asList(2,3,4,5,1));

        System.out.println(bitManipulation(11,2)) ;
    }
    }

