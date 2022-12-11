/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */

// Approach 1
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode temp = headB ;
        while(headA != null ) {
            headB = temp ;
            while(headB != null ) {
                if (headA == headB) {
                    return headA ;
                }
                headB = headB.next ;
            }
            headA = headA.next ;
        }
        return null ;
        
    }
}

//Approach 2
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode a = headA ;
        ListNode b = headB ;
        while(a!=b) {
            a = a == null ? headB : a.next ;
            b = b == null ? headA : b.next ;
        }
        return a ;
    }
}
