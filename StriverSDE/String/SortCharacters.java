import java.util.* ;

public class SortCharacters  {
    public static void main(String[] args) {
        String check = "tree";
        Solution solve = new Solution() ;
        System.out.println(solve.frequencySort(check));

    }
}

class Solution {
    public String frequencySort(String s) {
        HashMap<Integer, Character> map =  new HashMap<>() ;
        for(int i = 0; i < s.length() ; i++) {
            Character ch = s.charAt(i) ;
            if ( map.containsKey(ch) ) {
            map.put( map.get(ch) + 1  , ch );
            } else {
            map.put(1 , ch) ;
        }
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for(Map.Entry<Integer, Character> entry  : new map.entrySet()) {
            queue.add(entry.getKey());
        }
        Character[] ansArray = new Character[s.length()] ;
        int j = 0 ;
        while(!queue.isEmpty()) {
            int val = queue.poll() ;
            Character freq = map.get(val) ;
            while(val != 0) {
                ansArray[j] = freq ;
                j++;
            }
        }

        return new String(ansArray) ;

        }
    }
}

