/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public boolean isPalindrome(ListNode head) {
        ListNode current = head ;
        ArrayList<Integer> list = new ArrayList<>() ;
        while(current != null) {
            list.add(current.val) ;
            current = current.next ;
        }
        for(int i = 0 ; i < list.size() ; i++ ) {
            if(list.get(i) != list.get(list.size()-1-i)) return false ;
        }
        return true ;

    }
}
