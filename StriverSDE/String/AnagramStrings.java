import java.util.*;

public class AnagramStrings {
    public static void main(String args[]) {
        String s = "anagram" ;
        String t = "nagaram" ;
        Solution solve = new Solution() ;
        System.out.println(solve.isAnagram(s,t)) ;
    }
}

class Solution {
    public boolean isAnagram(String s, String t) {
        if(s.length() != t.length()) return false ;
        HashMap<Character, Integer> map = new HashMap<>() ;
        for(int i = 0 ; i < s.length() ; i++) {
            if (map.containsKey(s.charAt(i))) {
                map.put(s.charAt(i) , map.get(s.charAt(i)) + 1) ;
            } else {
                map.put(s.charAt(i) , 1) ;
            }
        }

        for(int i = 0 ; i < t.length() ; i++) {
            if (!map.containsKey(t.charAt(i))) return false ;
            int count = map.get(t.charAt(i)) ;
            count -= 1 ;
            if (count < 0) return false ;
        }

        return true ;

    }
}
