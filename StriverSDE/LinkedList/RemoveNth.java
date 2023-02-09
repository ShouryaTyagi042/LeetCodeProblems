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
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode ahead = head ;
        ListNode behind = head ;
        for(int i = 0 ; i < n ; i++ ) {
            ahead = ahead.next ;
        }
        while(ahead.next != null ) {
            ahead = ahead.next ;
            behind = behind.next ;
        }
        ListNode remove = behind.next ;
        behind.next = remove.next ;
        remove.next = null ;
        return head ;



    }
}
