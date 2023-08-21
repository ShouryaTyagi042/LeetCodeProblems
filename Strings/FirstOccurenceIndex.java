class Solution {
    public int strStr(String haystack, String needle) {
        int needleLength = needle.length() ;
        int haystackLength = haystack.length() ;
        if(haystackLength < needleLength ) return false ;
        for(int i = 0 ; i < haystackLength ; i++ ) {
            if(haystack.charAt(i) == needle.charAt(0) && i + needleLength <= haystackLength ) {
                if(haystack.substring(i,i+needleLength).equals(needle)) return i ;
            }
        }
        return -1;


    }
}
