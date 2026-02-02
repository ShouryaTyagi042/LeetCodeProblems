import java.lang.Math ;
import java.util.* ;

public class LongestSubstringWithoutRepeatition {
    public static void main(String[] args) {
        Solution solve = new Solution();
        String check = "abc" ;
        System.out.println(solve.lengthOfLongestSubstring(check));
 }
}

class Solution {
    public int lengthOfLongestSubstring(String s) {
        if(s.length() == 1) return 1 ;
        HashMap<Character, Integer> map = new HashMap<>();
        int p1  = 0 ;
        int p2 = 0 ;
        int ans = 0 ;
        while(p2 < s.length()) {
            Character current =  s.charAt(p2) ;
            if(!map.containsKey(current) || map.get(current) < p1) {
                map.put(current, p2) ;
            } else {
                ans = Math.max(ans, p2 - p1) ;
                p1 = map.get(current) + 1 ;
                map.put(current,p2) ;
            }
            p2 += 1 ;
        }
        return Math.max(ans, p2 - p1 ) ;
    }
}
