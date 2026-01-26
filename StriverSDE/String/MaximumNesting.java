import java.lang.Math ;
import java.util.* ;

public class  MaximumNesting {
    public static void main(String[] args) {
        Solution solve = new Solution() ;
        String check = "(1+(2*3)+((8)/4))+1" ;
        System.out.println(solve.maxDepth(check));
    }
}

class Solution {
    public int maxDepth(String s) {
        if(s.length() == 0) return 0 ;
        Stack<Character> st = new Stack<>();
        int ans = 0 ;
        for(int i = 0 ; i < s.length() ; i++) {
            char ch = s.charAt(i) ;
            if(ch == '(') {
                st.push(ch) ;
            } else if (ch == ')') {
                ans = Math.max(ans, st.size()) ;
                st.pop();
            }
        }
        return ans ;
    }
}
