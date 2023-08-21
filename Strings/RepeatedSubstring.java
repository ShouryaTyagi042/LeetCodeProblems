class Solution {
    public boolean repeatedSubstringPattern(String s) {
        int l = s.length() ;
        for(int i = 0 ; i <= l/2 ; i++ ) {
            for(n % i == 0) {
                String subString = s.substring(0,i) ;
                StringBuilder repeated = new StringBuilder() ;
                for(int j = 0 ; j < n/i ; j++ ){
                    repeated.append(subString) ;
                }
                if (repeated.toString().equals(s)) return true;
            }
        }
        return false ;
    }
}

class Solution {
    public boolean repeatedSubstringPattern(String s) {
        StringBuilder concatenated = new StringBuilder(s) ;
        concatenated.append(s)

    }
}
