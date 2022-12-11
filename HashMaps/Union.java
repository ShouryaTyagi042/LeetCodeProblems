package HashMaps;

import java.util.HashMap;

public class Union {
    public static void main(String[] args) {
        int[] a = {85, 25, 1, 32, 54, 6} ;
        int[] b = {85 ,2} ;
        System.out.println(doUnion(a, a.length,b,b.length));

    }
    static int doUnion(int[] a, int n, int[] b, int m)
    {
        HashMap<Integer,Integer> map = new HashMap<>() ;
        for (int n1 :
             a) {
            if(!map.containsKey(n1)) {
                map.put(n1,1) ;
            }

        }
        for (int n2:
             b) {
            if(!map.containsKey(n2)) {
                map.put(n2,1) ;
            }
        }
        return map.size() ;


    }
}
