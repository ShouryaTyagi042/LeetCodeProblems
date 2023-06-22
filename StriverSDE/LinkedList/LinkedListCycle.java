/**
 * Definition for singly-linked list.
 * class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    public boolean hasCycle(ListNode head) {
        HashSet<ListNode> set = new HashSet<>() ;
        ListNode current = head ;
        while (current != null) {
            if(set.contains(current)) return true ;
            set.add(current) ;
            current = current.next ;
        }
        return false ;
    }
}

public class Solution {
    public boolean hasCycle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;

        while(fast != null && fast.next != null && fast.next.next != null){
            fast = fast.next.next;
            if(fast == slow){
                return true;
            }
            slow = slow.next;
        }

        return false;
    }
}
