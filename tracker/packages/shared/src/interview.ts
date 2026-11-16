// The 50 problems from this library worth going over before any interview.
//
// Picked from what is actually solved here (problem statements are not
// stored, so the picks go by title, judge link, solution and notes), weighted
// toward patterns that interviews keep returning to, one or two
// representatives per pattern rather than every variant. `sheets` records
// which of the common public lists carry the same problem or its direct
// LeetCode equivalent (`aka`), so overlap with them is visible at a glance.

export type Sheet = 'Striver' | 'NeetCode 150' | 'Blind 75'

export interface InterviewPick {
  /** Problem slug in the tracker, e.g. 'graphs/number-of-islands'. */
  slug: string
  group: string
  /** The pattern this problem stands for on the list. */
  why: string
  /** Better-known name on LeetCode / the sheets, when it differs. */
  aka?: string
  sheets: Sheet[]
}

const S: Sheet = 'Striver'
const N: Sheet = 'NeetCode 150'
const B: Sheet = 'Blind 75'

export const INTERVIEW_SET = 'interview'

export const INTERVIEW_TOP_50: InterviewPick[] = [
  // ---- Arrays, hashing, two pointers, sliding window ----
  { slug: 'prefix-sums/count-subarrays-with-sum-k', group: 'Arrays & Sliding Window', why: 'Prefix sum + hash map', aka: 'Subarray Sum Equals K', sheets: [S] },
  { slug: 'two-pointers/3-sum', group: 'Arrays & Sliding Window', why: 'Sort + two pointers', aka: '3Sum', sheets: [S, N, B] },
  { slug: 'two-pointers/container-with-most-water', group: 'Arrays & Sliding Window', why: 'Converging two pointers', sheets: [N, B] },
  { slug: 'two-pointers/longest-repeating-character-replacement', group: 'Arrays & Sliding Window', why: 'Variable window with a validity check', sheets: [S, N, B] },
  { slug: 'two-pointers/consecutive-ones', group: 'Arrays & Sliding Window', why: 'Window with at most k flips', aka: 'Max Consecutive Ones III', sheets: [S] },
  { slug: 'two-pointers/length-of-longest-subarray-with-atmost-kfrequency', group: 'Arrays & Sliding Window', why: 'Window + frequency map', sheets: [] },
  { slug: 'deque-and-ordered-set/maximum-in-window', group: 'Arrays & Sliding Window', why: 'Monotonic deque', aka: 'Sliding Window Maximum', sheets: [S, N] },
  { slug: 'two-pointers/smallest-range-covering-from-klists', group: 'Arrays & Sliding Window', why: 'Merge k lists, then minimum window', sheets: [] },
  { slug: 'two-pointers/generating-permutations', group: 'Arrays & Sliding Window', why: 'Next permutation', aka: 'Next Permutation', sheets: [S] },
  { slug: 'contribution-technique/count-unique-char-in-substring', group: 'Arrays & Sliding Window', why: 'Contribution technique', aka: 'Count Unique Characters of All Substrings', sheets: [] },

  // ---- Binary search ----
  { slug: 'binary-search/rotated-binary-search', group: 'Binary Search', why: 'Binary search on a rotated array', aka: 'Search in Rotated Sorted Array', sheets: [S, N, B] },
  { slug: 'binary-search/find-peak-element', group: 'Binary Search', why: 'Binary search on slope', sheets: [S] },
  { slug: 'binary-search/painter-partition', group: 'Binary Search', why: 'Binary search on the answer: minimise the max', aka: "Painter's Partition / Allocate Books", sheets: [S] },
  { slug: 'binary-search/class-room', group: 'Binary Search', why: 'Binary search on the answer: maximise the min', aka: 'Aggressive Cows', sheets: [S] },
  { slug: 'binary-search/factory-machines', group: 'Binary Search', why: 'Binary search on time', aka: 'like Koko Eating Bananas', sheets: [] },
  { slug: 'binary-search/minimize-max-difference', group: 'Binary Search', why: 'Binary search on the answer with a counting check', aka: 'Minimise Max Distance to Gas Station', sheets: [S] },

  // ---- Stacks, heaps, design ----
  { slug: 'stack-and-queues/valid-paranthesis-az101', group: 'Stacks, Heaps & Design', why: 'Stack matching', aka: 'Valid Parentheses', sheets: [S, N, B] },
  { slug: 'stack-and-queues/queue-using2-stacks', group: 'Stacks, Heaps & Design', why: 'Amortised two-stack queue', aka: 'Implement Queue using Stacks', sheets: [S] },
  { slug: 'priority-queue/find-median-from-data-stream', group: 'Stacks, Heaps & Design', why: 'Two heaps', sheets: [S, N, B] },
  { slug: 'priority-queue/reduce-array', group: 'Stacks, Heaps & Design', why: 'Greedy with a min-heap', aka: 'Connect Ropes', sheets: [S] },

  // ---- Bits ----
  { slug: 'bit-manipulation/single-number-iii', group: 'Bit Manipulation', why: 'XOR, then split on a differing bit', sheets: [S] },

  // ---- Backtracking & recursion ----
  { slug: 'backtracking/nqueens', group: 'Backtracking', why: 'Backtracking with constraint checks', aka: 'N-Queens', sheets: [S, N] },
  { slug: 'backtracking/generate-balanced-paranthesis', group: 'Backtracking', why: 'Backtracking with pruning', aka: 'Generate Parentheses', sheets: [S, N] },
  { slug: 'recursion/kth-permutation-easy', group: 'Backtracking', why: 'Counting blocks of (n-1)!', aka: 'Permutation Sequence', sheets: [S] },

  // ---- Dynamic programming ----
  { slug: 'dynamic-programming/frog2', group: 'Dynamic Programming', why: '1D DP ending at index', aka: 'Frog Jump with k distances', sheets: [S] },
  { slug: 'dynamic-programming/vacation', group: 'Dynamic Programming', why: 'DP with a last-choice state', aka: "Ninja's Training", sheets: [S] },
  { slug: 'dynamic-programming/acode', group: 'Dynamic Programming', why: 'Counting decodings', aka: 'Decode Ways', sheets: [N, B] },
  { slug: 'dynamic-programming/coin-change', group: 'Dynamic Programming', why: 'Unbounded knapsack', sheets: [S, N, B] },
  { slug: 'dynamic-programming/knapsack1', group: 'Dynamic Programming', why: '0/1 knapsack', aka: '0/1 Knapsack', sheets: [S] },
  { slug: 'dynamic-programming/number-of-subsets-with-sumequalto-x', group: 'Dynamic Programming', why: 'Subset-sum counting', aka: 'Count Subsets with Sum K', sheets: [S] },
  { slug: 'dynamic-programming/longest-common-subsequence', group: 'Dynamic Programming', why: 'Two-sequence DP', sheets: [S, N, B] },
  { slug: 'dynamic-programming/edit-distance', group: 'Dynamic Programming', why: 'Two-sequence DP with three moves', sheets: [S, N] },
  { slug: 'dynamic-programming/longest-common-substring', group: 'Dynamic Programming', why: 'Two-sequence DP that resets on a mismatch', sheets: [S] },
  { slug: 'dynamic-programming/numberof-longest-increasing-subsequence', group: 'Dynamic Programming', why: 'LIS with counts', sheets: [S] },
  { slug: 'dynamic-programming/maximum-areaof-square', group: 'Dynamic Programming', why: 'Grid DP: 1 + min of three neighbours', aka: 'Maximal Square', sheets: [S] },
  { slug: 'dynamic-programming/dungeon-game', group: 'Dynamic Programming', why: 'Grid DP filled backwards', sheets: [] },
  { slug: 'dynamic-programming/burst-balloons', group: 'Dynamic Programming', why: 'Interval DP: choose the last one to burst', sheets: [S, N] },
  { slug: 'dynamic-programming/predict-the-winner', group: 'Dynamic Programming', why: 'Minimax game on an interval', sheets: [] },

  // ---- Graphs ----
  { slug: 'graphs/number-of-islands', group: 'Graphs', why: 'Flood fill', sheets: [S, N, B] },
  { slug: 'graphs/rotting-oranges', group: 'Graphs', why: 'Multi-source BFS', sheets: [S, N] },
  { slug: 'graphs/shortest-path-in-binary-matrix', group: 'Graphs', why: 'BFS on a grid', sheets: [S] },
  { slug: 'graphs/possible-bipartition', group: 'Graphs', why: 'Bipartite check by 2-colouring', aka: 'Is Graph Bipartite', sheets: [S] },
  { slug: 'graphs/round-trip2', group: 'Graphs', why: 'Directed cycle detection', aka: 'like Course Schedule', sheets: [S, N, B] },
  { slug: 'graphs/smallest-permutation', group: 'Graphs', why: "Kahn's topological sort with a heap", aka: 'like Course Schedule II', sheets: [S, N] },
  { slug: 'graphs/network-delay-time', group: 'Graphs', why: "Dijkstra's algorithm", sheets: [S, N] },
  { slug: 'graphs/edge-reverse', group: 'Graphs', why: '0-1 BFS', sheets: [] },
  { slug: 'graphs/disjoint-set-union', group: 'Graphs', why: 'Union-find with rank and path compression', sheets: [S, N] },
  { slug: 'graphs/road-reparation', group: 'Graphs', why: "Kruskal's MST", aka: 'like Min Cost to Connect All Points', sheets: [S, N] },

  // ---- Trees ----
  { slug: 'trees/diameterof-tree', group: 'Trees', why: 'Diameter by two passes', aka: 'Tree Diameter', sheets: [S, N] },
  { slug: 'trees/minimum-height-trees', group: 'Trees', why: 'Tree centres', sheets: [] },
]

export const INTERVIEW_SLUGS: string[] = INTERVIEW_TOP_50.map((p) => p.slug)

// ---------- gaps ----------
//
// Problems the public sheets lean on that nothing in the tracker covers yet.
// Matched to the tracker by LeetCode slug in the judge URL, so adding one
// with its LeetCode link marks it covered without editing this list.
// `elsewhere` points at a solution that exists in the repo but outside
// AlgoZenith/topics, which the tracker does not index.

export interface InterviewGap {
  title: string
  /** LeetCode problem slug, e.g. 'reverse-linked-list'. */
  leetcode: string
  group: string
  sheets: Sheet[]
  elsewhere?: string
}

const gap = (group: string, title: string, leetcode: string, sheets: Sheet[], elsewhere?: string): InterviewGap =>
  ({ group, title, leetcode, sheets, ...(elsewhere ? { elsewhere } : {}) })

const LL = 'Linked Lists'
const BT = 'Binary Trees & BST'
const TR = 'Tries'
const MS = 'Stacks & Monotonic Stack'
const AI = 'Arrays & Intervals'
const SW = 'Strings & Sliding Window'
const HP = 'Heaps'
const DP = 'Dynamic Programming'
const GR = 'Graphs'
const BK = 'Backtracking'
const GD = 'Greedy'
const BS = 'Binary Search'

export const INTERVIEW_GAPS: InterviewGap[] = [
  gap(LL, 'Reverse Linked List', 'reverse-linked-list', [S, N, B], 'StriverSDE/LinkedList/ReverseLL.java'),
  gap(LL, 'Merge Two Sorted Lists', 'merge-two-sorted-lists', [S, N, B], 'StriverSDE/LinkedList/MergeSorted.java'),
  gap(LL, 'Linked List Cycle', 'linked-list-cycle', [S, N, B], 'StriverSDE/LinkedList/LinkedListCycle.java'),
  gap(LL, 'Remove Nth Node From End of List', 'remove-nth-node-from-end-of-list', [S, N, B], 'StriverSDE/LinkedList/RemoveNth.java'),
  gap(LL, 'Add Two Numbers', 'add-two-numbers', [S, N], 'StriverSDE/LinkedList/AddTwoNumbers.java'),
  gap(LL, 'Reorder List', 'reorder-list', [N, B]),
  gap(LL, 'Copy List with Random Pointer', 'copy-list-with-random-pointer', [S, N]),
  gap(LL, 'LRU Cache', 'lru-cache', [S, N]),
  gap(LL, 'Merge k Sorted Lists', 'merge-k-sorted-lists', [S, N, B]),
  gap(LL, 'Reverse Nodes in k-Group', 'reverse-nodes-in-k-group', [S, N]),

  gap(BT, 'Maximum Depth of Binary Tree', 'maximum-depth-of-binary-tree', [S, N, B], 'StriverSDE/BinaryTree/HeightOfBinaryTree.java'),
  gap(BT, 'Diameter of Binary Tree', 'diameter-of-binary-tree', [S, N], 'StriverSDE/BinaryTree/DiameterOfBinaryTree.java'),
  gap(BT, 'Balanced Binary Tree', 'balanced-binary-tree', [S, N], 'StriverSDE/BinaryTree/CheckTreeBalanced.java'),
  gap(BT, 'Same Tree', 'same-tree', [S, N, B], 'StriverSDE/BinaryTree/IdenticalTrees.java'),
  gap(BT, 'Binary Tree Level Order Traversal', 'binary-tree-level-order-traversal', [S, N, B], 'StriverSDE/BinaryTree/LevelOrder.java'),
  gap(BT, 'Binary Tree Maximum Path Sum', 'binary-tree-maximum-path-sum', [S, N, B], 'StriverSDE/BinaryTree/MaxPathSum.java'),
  gap(BT, 'Invert Binary Tree', 'invert-binary-tree', [N, B]),
  gap(BT, 'Binary Tree Right Side View', 'binary-tree-right-side-view', [S, N]),
  gap(BT, 'Lowest Common Ancestor of a BST', 'lowest-common-ancestor-of-a-binary-search-tree', [S, N, B]),
  gap(BT, 'Validate Binary Search Tree', 'validate-binary-search-tree', [S, N, B]),
  gap(BT, 'Kth Smallest Element in a BST', 'kth-smallest-element-in-a-bst', [S, N, B]),
  gap(BT, 'Construct Tree from Preorder and Inorder', 'construct-binary-tree-from-preorder-and-inorder-traversal', [S, N, B]),
  gap(BT, 'Serialize and Deserialize Binary Tree', 'serialize-and-deserialize-binary-tree', [S, N, B]),

  gap(TR, 'Implement Trie', 'implement-trie-prefix-tree', [S, N, B]),
  gap(TR, 'Design Add and Search Words', 'design-add-and-search-words-data-structure', [N, B]),
  gap(TR, 'Word Search II', 'word-search-ii', [N, B]),

  gap(MS, 'Min Stack', 'min-stack', [S, N]),
  gap(MS, 'Daily Temperatures', 'daily-temperatures', [N]),
  gap(MS, 'Next Greater Element II', 'next-greater-element-ii', [S]),
  gap(MS, 'Largest Rectangle in Histogram', 'largest-rectangle-in-histogram', [S, N]),
  gap(MS, 'Evaluate Reverse Polish Notation', 'evaluate-reverse-polish-notation', [N]),

  gap(AI, 'Maximum Subarray', 'maximum-subarray', [S, N, B], 'StriverSDE/Arrays/MaxSubArray.java'),
  gap(AI, 'Merge Intervals', 'merge-intervals', [S, N, B], 'StriverSDE/Arrays/MergeIntervals.java'),
  gap(AI, 'Insert Interval', 'insert-interval', [N, B]),
  gap(AI, 'Non-overlapping Intervals', 'non-overlapping-intervals', [N, B]),
  gap(AI, 'Product of Array Except Self', 'product-of-array-except-self', [N, B]),
  gap(AI, 'Longest Consecutive Sequence', 'longest-consecutive-sequence', [S, N, B]),
  gap(AI, 'Trapping Rain Water', 'trapping-rain-water', [S, N]),

  gap(SW, 'Longest Substring Without Repeating Characters', 'longest-substring-without-repeating-characters', [S, N, B], 'StriverSDE/TwoPointers/LongestSubstringWithoutRepeatition.java'),
  gap(SW, 'Minimum Window Substring', 'minimum-window-substring', [S, N, B]),
  gap(SW, 'Longest Palindromic Substring', 'longest-palindromic-substring', [S, N, B]),
  gap(SW, 'Palindromic Substrings', 'palindromic-substrings', [N, B]),
  gap(SW, 'Group Anagrams', 'group-anagrams', [N, B]),

  gap(HP, 'Kth Largest Element in an Array', 'kth-largest-element-in-an-array', [S, N]),
  gap(HP, 'Top K Frequent Elements', 'top-k-frequent-elements', [S, N, B]),
  gap(HP, 'Task Scheduler', 'task-scheduler', [N]),

  gap(DP, 'Unique Paths', 'unique-paths', [S, N, B], 'StriverSDE/Arrays/GridUniquePaths.java'),
  gap(DP, 'House Robber', 'house-robber', [S, N, B]),
  gap(DP, 'House Robber II', 'house-robber-ii', [S, N, B]),
  gap(DP, 'Word Break', 'word-break', [S, N, B]),
  gap(DP, 'Partition Equal Subset Sum', 'partition-equal-subset-sum', [S, N]),
  gap(DP, 'Longest Increasing Subsequence', 'longest-increasing-subsequence', [S, N, B]),
  gap(DP, 'Maximum Product Subarray', 'maximum-product-subarray', [S, N, B]),

  gap(GR, 'Clone Graph', 'clone-graph', [N, B]),
  gap(GR, 'Pacific Atlantic Water Flow', 'pacific-atlantic-water-flow', [N, B]),
  gap(GR, 'Word Ladder', 'word-ladder', [S, N]),
  gap(GR, 'Alien Dictionary', 'alien-dictionary', [S, N, B]),
  gap(GR, 'Cheapest Flights Within K Stops', 'cheapest-flights-within-k-stops', [S, N]),

  gap(BK, 'Combination Sum', 'combination-sum', [S, N, B], 'StriverSDE/Recursion/CombinationSumI.java'),
  gap(BK, 'Subsets II', 'subsets-ii', [S, N], 'StriverSDE/Recursion/Subset2.java'),
  gap(BK, 'Permutations', 'permutations', [S, N], 'StriverSDE/Recursion/PrintPermutation.java'),
  gap(BK, 'Letter Combinations of a Phone Number', 'letter-combinations-of-a-phone-number', [N], 'StriverSDE/Recursion/LetterCombination.java'),
  gap(BK, 'Word Search', 'word-search', [S, N, B]),
  gap(BK, 'Palindrome Partitioning', 'palindrome-partitioning', [S, N]),

  gap(GD, 'Jump Game', 'jump-game', [S, N, B]),
  gap(GD, 'Gas Station', 'gas-station', [N]),

  gap(BS, 'Median of Two Sorted Arrays', 'median-of-two-sorted-arrays', [S, N], 'StriverSDE/BinarySearch/MinToMaxPattern/MedianOfSortedArrays.java'),
  gap(BS, 'Koko Eating Bananas', 'koko-eating-bananas', [S, N], 'StriverSDE/BinarySearch/MinMaxToThreshold/KokoEatingBanana.java'),
  gap(BS, 'Find Minimum in Rotated Sorted Array', 'find-minimum-in-rotated-sorted-array', [S, N, B]),
]
