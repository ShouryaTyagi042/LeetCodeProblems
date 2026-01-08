import java.util.*;

class IsomorphicStrings {
    public static void main(String[] args) {
        String s = "badc" ;
        String t = "baba" ;
        System.out.println(isIsomorphic(s,t)) ;
    }

    public static boolean isIsomorphic(String s, String t) {
        HashMap<Character, Character> map  = new HashMap<>() ;
        if (s.length()  != t.length()) return false ;
        for (int i = 0 ; i < s.length()  ; i++) {
            if (!map.containsKey(s.charAt(i))){
                if (map.containsValue(t.charAt(i))) return false ;
                map.put(s.charAt(i), t.charAt(i)) ;
            } else {
                if (map.get(s.charAt(i)) != t.charAt(i)) return false ;
            }
        }
        return true ;
    }
}
