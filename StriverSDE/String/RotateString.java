class RotateString {
     public static void main(String[] args) {
        String s = "abcde" ;
        String goal = "eabcd" ;
        System.out.println(rotateString(s, goal)) ;

     }

    public static boolean rotateString(String s, String goal) {
        if(s.length() != goal.length()) return false ;
        if (s == goal ) return true ;
        for (int i = 1; i < s.length()  ; i++) {
            String rotate = s.substring(i) ;
            String add = s.substring(0,i) ;
            String result = rotate + add ;
            if (result.equals(goal)) return true ;
        }
        return false ;
    }

 }
