package Strings;

import java.util.HashMap;

public class Anagram {
    public static void main(String[] args) {

        String a = "pwngmzajc" ;
        String b = "ncgamzwjp" ;
        System.out.println(isAnagram(a,b));
//        String[] arr = {"a","b","c"} ;
//        System.out.println(String.join(".",arr));

    }
    public static boolean isAnagram(String a,String b)
    {
        if (a.length() != b.length()) return false ;
        HashMap<Character,Integer> freq = new HashMap<>() ;
        for (int i = 0; i < a.length(); i++) {
            if(freq.containsKey(a.charAt(i))) {
                freq.put(a.charAt(i),freq.get(a.charAt(i)) + 1 ) ;
            }
            else freq.put(a.charAt(i),1) ;

        }
        for (int i = 0; i < b.length(); i++) {
            if(!freq.containsKey(b.charAt(i))) return  false ;
            else if(freq.get(b.charAt(i)) == 0) return  false ;
            else {
                freq.put(b.charAt(i),freq.get(b.charAt(i)) - 1 ) ;
            }
        }
        return true ;
    }
}
