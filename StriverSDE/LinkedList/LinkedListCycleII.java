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
    public ListNode detectCycle(ListNode head) {
                HashSet<ListNode> set = new HashSet<>() ;
        ListNode current = head ;
        while (current != null) {
            if(set.contains(current)) break ;
            set.add(current) ;
            current = current.next ;
        }
        return current ;

    }
}
