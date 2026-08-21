# Migration: source-based tree -> topic-based tree

All 202 problem folders were consolidated under `topics/`. Every move was a
`git mv` (pure rename, zero content change), so history is intact and
`git log --follow <path>` still works.

**Why:** `Graphs` previously existed in 3 places (`CohortAssignments/`,
`LiveSessions/`, `LeetCode/`) and `BinarySearch` in 2, so revising a topic
meant remembering to check several trees.

This file is the provenance record: it preserves where each problem came
from (cohort assignment vs. live session vs. self-practice on a judge),
which the folder path no longer encodes.

## Review these — judgment calls, not mechanical moves

| Item | What I did | Change it? |
|---|---|---|
| `PrefixSums/GoodNumbers2` | Genuine duplicate: same problem and identical `input.txt` as `PrefixSums/GoodNumbers`, solved twice (older `BufferedReader` version vs. newer `FastScanner`). Kept **both** rather than deleting. | Delete `GoodNumbers2` if you don't want the older version. |
| `ModularArithmetic/ExponentiationTower` | Was `Mathematics/ModularArithmetic/Exponentiation`. Renamed because it collided with CSES `Exponentiation`; they are different problems (`a^(b^c) mod p` vs. `a^b mod`). | Rename if you prefer another name. |
| `Combinatorics/LuckyNumbersCount` | Was `CodeForces/Practise/LuckyNumbers`. Renamed to avoid colliding with `Loops/LuckyNumbers` (a genuinely different problem). | — |
| `DynamicProgramming/` | New topic. Your taxonomy had none; the 5 Stone Game / Predict the Winner problems and the palindrome one needed a home. | Split into `GameTheory/` if you prefer. |
| `STL/` | Merged 5 near-identical topics: `STLBasics`, `STLUsages`, `STLApplications`, `STLApplicationIdeas`, `LeetCode/STL`. | Re-split if the basics/applications distinction matters to you. |
| `GreedyAndSweepLine/` | Was `Greedy&SweepLine`. The `&` broke unquoted shell paths. | — |
| `Combinatorics/` | Was `Comibnatorics` (typo). | — |
| `Backtracking/Fundamentals` | Was lowercase `fundamentals`. | — |
| `BinarySearch/FindPeakElement/` | A stray `FindPeakElement.java` sitting loose in `LeetCode/BinarySearch/` with no folder, `input.txt`, or `expected.txt`. Given a folder like everything else. **Note: it does not compile** — the method takes `nums` but the body indexes `arr`. | Fix or delete. |

The 39 problems that were under source-named folders (`AtCoder/`, `CodeForces/`,
`CSES/`, `HackerRank/`, `LeetCodeDailyProblems/`) had no topic recorded anywhere,
so I classified them by reading each solution. Those rows are marked ★ below —
they are the most likely to be filed somewhere you disagree with.

## Full mapping

| Topic | Problem | Came from | |
|---|---|---|---|
| 2DArrays | 8Neighbours | `CohortAssignments/2DArrays/8Neighbours` |  |
| 2DArrays | Matrix | `CohortAssignments/2DArrays/Matrix` |  |
| 2DArrays | MirrorArray | `CohortAssignments/2DArrays/MirrorArray` |  |
| 2DArrays | SearchInMatrix | `CohortAssignments/2DArrays/SearchInMatrix` |  |
| 2DPrefixSum | KthValue | `CohortAssignments/2DPrefixSum/KthValue` |  |
| 2DPrefixSum | MaxValueInRectangle | `CohortAssignments/2DPrefixSum/MaxValueInRectangle` |  |
| 2DPrefixSum | RectangleSumQuery | `CohortAssignments/2DPrefixSum/RectangleSumQuery` |  |
| Arrays | FrequencyArrays | `CohortAssignments/Arrays/FrequencyArrays` |  |
| Arrays | LuckyArray | `CohortAssignments/Arrays/LuckyArray` |  |
| Arrays | PaskmakAndFlowers | `CodeForces/Practise/PaskmakAndFlowers` | ★ |
| Arrays | PermutationWithArrays | `CohortAssignments/Arrays/PermutationWithArrays` |  |
| Arrays | PositionInArray | `CohortAssignments/Arrays/PositionInArray` |  |
| Arrays | Replacement | `CohortAssignments/Arrays/Replacement` |  |
| Arrays | ReplaceMinMax | `CohortAssignments/Arrays/ReplaceMinMax` |  |
| Arrays | Reverse | `CohortAssignments/Arrays/Reverse` |  |
| Arrays | SortBubble | `CohortAssignments/Arrays/SortBubble` |  |
| Backtracking | AndroidUnlockPattern | `CohortAssignments/Backtracking/AndroidUnlockPattern` |  |
| Backtracking | Fundamentals | `CohortAssignments/Backtracking/fundamentals` |  |
| Backtracking | GenerateBalancedParanthesis | `CohortAssignments/Backtracking/GenerateBalancedParanthesis` |  |
| Backtracking | GenerateBalancedParanthesisDepth | `CohortAssignments/Backtracking/GenerateBalancedParanthesisDepth` |  |
| Backtracking | NQueens | `CohortAssignments/Backtracking/NQueens` |  |
| Backtracking | NQueensRevisited | `CohortAssignments/Backtracking/NQueensRevisited` |  |
| BinarySearch | BitonicArray | `CohortAssignments/BinarySearch/BitonicArray` |  |
| BinarySearch | ClassRoom | `CohortAssignments/BinarySearch/ClassRoom` |  |
| BinarySearch | FactoryMachines | `CSES/SortingAndSearching/FactoryMachines` | ★ |
| BinarySearch | KthSumValue | `CohortAssignments/BinarySearch/KthSumValue` |  |
| BinarySearch | MaximiseTheFraction | `CohortAssignments/BinarySearch/MaximiseTheFraction` |  |
| BinarySearch | MinimizeMaxDifference | `CohortAssignments/BinarySearch/MinimizeMaxDifference` |  |
| BinarySearch | NumberAndSumOfDigits | `CohortAssignments/BinarySearch/NumberAndSumOfDigits` |  |
| BinarySearch | PainterPartition | `CohortAssignments/BinarySearch/PainterPartition` |  |
| BinarySearch | RotatedBinarySearch | `CohortAssignments/BinarySearch/RotatedBinarySearch` |  |
| BinarySearch | TwoPointers | `CohortAssignments/BinarySearch/TwoPointers` |  |
| Combinatorics | BinomialCoefficients | `CSES/BinomialCoefficients` | ★ |
| Combinatorics | BrokenKeyboard | `CodeForces/Practise/PncPractise/BrokenKeyboard` | ★ |
| Combinatorics | ChristmasParty | `CSES/ChristmasParty` | ★ |
| Combinatorics | CombinationsPascalsTriangle | `Mathematics/Comibnatorics/CombinationsPascalsTriangle` |  |
| Combinatorics | CombinationsPrecompute | `Mathematics/Comibnatorics/CombinationsPrecompute` |  |
| Combinatorics | CombinationsRecursive | `Mathematics/Comibnatorics/CombinationsRecursive` |  |
| Combinatorics | DearrangementDP | `Mathematics/Comibnatorics/DearrangementDP` |  |
| Combinatorics | DreamoonAndWifi | `CodeForces/Practise/DreamoonAndWifi` | ★ |
| Combinatorics | FactorialRecursive | `Mathematics/Comibnatorics/FactorialRecursive` |  |
| Combinatorics | KolyaAndTanya | `CodeForces/Practise/PncPractise/KolyaAndTanya` | ★ |
| Combinatorics | KthBeautifulString | `CodeForces/Practise/PncPractise/KthBeautifulString` | ★ |
| Combinatorics | LuckyNumbersCount | `CodeForces/Practise/LuckyNumbers` | ★ |
| Combinatorics | MultiplesOf3 | `CodeForces/Practise/PncPractise/MultiplesOf3` | ★ |
| Combinatorics | NCRTable | `HackerRank/Mathematics/Combinatorics/NCRTable` | ★ |
| Combinatorics | NumberOfDiagonals | `Mathematics/Comibnatorics/NumberOfDiagonals` |  |
| Combinatorics | NumberOfIntersections | `Mathematics/Comibnatorics/NumberOfIntersections` |  |
| Combinatorics | NumberOfParts | `Mathematics/Comibnatorics/NumberOfParts` |  |
| Combinatorics | NumberOfWays | `Mathematics/Comibnatorics/NumberOfWays` |  |
| Combinatorics | PashaAndStick | `CodeForces/Practise/PncPractise/PashaAndStick` | ★ |
| Combinatorics | RandomTeams | `CodeForces/Practise/PncPractise/RandomTeams` | ★ |
| Combinatorics | SkiResort | `CodeForces/Practise/PncPractise/SkiResort` | ★ |
| Combinatorics | Stars | `CodeForces/Practise/Stars` | ★ |
| ContributionTechnique | CountDistinctCharInSubstring | `CohortAssignments/ContributionTechnique/CountDistinctCharInSubstring` |  |
| ContributionTechnique | CountUniqueCharInSubstring | `CohortAssignments/ContributionTechnique/CountUniqueCharInSubstring` |  |
| DequeAndOrderedSet | DequeAZ101 | `CohortAssignments/DequeAndOrderedSet/DequeAZ101` |  |
| DequeAndOrderedSet | IndexedSet | `CohortAssignments/DequeAndOrderedSet/IndexedSet` |  |
| DynamicProgramming | MinimumOperationsToMakeStringPalindrome | `LeetCodeDailyProblems/MinimumOperationsToMakeStringPalindrome` | ★ |
| DynamicProgramming | PredictTheWinner | `LeetCodeDailyProblems/PredictTheWinner` | ★ |
| DynamicProgramming | StoneGameII | `LeetCodeDailyProblems/StoneGameII` | ★ |
| DynamicProgramming | StoneGameIII | `LeetCodeDailyProblems/StoneGameIII` | ★ |
| DynamicProgramming | StoneGameIV | `LeetCodeDailyProblems/StoneGameIV` | ★ |
| DynamicProgramming | StoneGameIX | `LeetCodeDailyProblems/StoneGameIX` | ★ |
| Functions | PrintPrimes | `CohortAssignments/Functions/PrintPrimes` |  |
| Functions | ShiftRight | `CohortAssignments/Functions/ShiftRight` |  |
| Functions | SumOfConsecutiveOdds | `CohortAssignments/Functions/SumOfConsecutiveOdds` |  |
| Graphs | AllPairsShortestPath | `CohortAssignments/Graphs/AllPairsShortestPath` |  |
| Graphs | AreaPerimeterOfConnectedComponents | `CohortAssignments/Graphs/AreaPerimeterOfConnectedComponents` |  |
| Graphs | BellmanFord | `LiveSessions/Graphs/BellmanFord` |  |
| Graphs | BellmanFordRevisited | `CohortAssignments/Graphs/BellmanFordRevisited` |  |
| Graphs | BFS | `LiveSessions/Graphs/BFS` |  |
| Graphs | BudgetTravelling | `CohortAssignments/Graphs/BudgetTravelling` |  |
| Graphs | BuildingRoads | `CohortAssignments/Graphs/BuildingRoads` |  |
| Graphs | BurnThemAll | `CohortAssignments/Graphs/BurnThemAll` |  |
| Graphs | ColourTree | `CohortAssignments/Graphs/ColourTree` |  |
| Graphs | CompleteTheGame | `CohortAssignments/Graphs/CompleteTheGame` |  |
| Graphs | ComponentNumbering | `LiveSessions/Graphs/ComponentNumbering` |  |
| Graphs | ConnectedComponentsSize | `CohortAssignments/Graphs/ConnectedComponentsSize` |  |
| Graphs | CreatingTeams | `CohortAssignments/Graphs/CreatingTeams` |  |
| Graphs | Dijkstras | `LiveSessions/Graphs/Dijkstras` |  |
| Graphs | DirectedGraphCycleDetection | `LiveSessions/Graphs/DirectedGraphCycleDetection` |  |
| Graphs | EasyGraphQueries | `CohortAssignments/Graphs/EasyGraphQueries` |  |
| Graphs | EdgeReverse | `CohortAssignments/Graphs/EdgeReverse` |  |
| Graphs | FindTheNumberOfRooms | `CohortAssignments/Graphs/FindTheNumberOfRooms` |  |
| Graphs | InfectedPeople | `CohortAssignments/Graphs/InfectedPeople` |  |
| Graphs | JumpGame | `CohortAssignments/Graphs/JumpGame` |  |
| Graphs | Kwalk | `CohortAssignments/Graphs/Kwalk` |  |
| Graphs | Labyrinth | `CSES/Labyrinth` | ★ |
| Graphs | MinCosttoTravelwithCarAndFuel | `LiveSessions/Graphs/MinCosttoTravelwithCarAndFuel` |  |
| Graphs | Monsters | `CSES/Monsters` | ★ |
| Graphs | OneEdge | `CohortAssignments/Graphs/OneEdge` |  |
| Graphs | OnePiece | `CohortAssignments/Graphs/OnePiece` |  |
| Graphs | OpenTheLocks | `LeetCode/Graphs/OpenTheLocks` |  |
| Graphs | RemoveMethodsFromProject | `LeetCodeDailyProblems/RemoveMethodsFromProject` | ★ |
| Graphs | RoundTrip | `CohortAssignments/Graphs/RoundTrip` |  |
| Graphs | RoundTrip2 | `CohortAssignments/Graphs/RoundTrip2` |  |
| Graphs | ShortestPathGridWithKWallBreaks | `LiveSessions/Graphs/ShortestPathGridWithKWallBreaks` |  |
| Graphs | ShortestPathI | `CohortAssignments/Graphs/ShortestPathI` |  |
| Graphs | ShortestPathII | `CohortAssignments/Graphs/ShortestPathII` |  |
| Graphs | SmallestPermutation | `CohortAssignments/Graphs/SmallestPermutation` |  |
| Graphs | SnakesAndLadder | `CohortAssignments/Graphs/SnakesAndLadder` |  |
| Graphs | SSSP | `LiveSessions/Graphs/SSSP` |  |
| Graphs | UndirectedGraphCycleDetection | `LiveSessions/Graphs/UndirectedGraphCycleDetection` |  |
| GreedyAndSweepLine | FastSolving | `CohortAssignments/Greedy&SweepLine/FastSolving` |  |
| GreedyAndSweepLine | FibonnaciBreakdown | `CohortAssignments/Greedy&SweepLine/FibonnaciBreakdown` |  |
| GreedyAndSweepLine | KillMonsters | `CohortAssignments/Greedy&SweepLine/KillMonsters` |  |
| GreedyAndSweepLine | LightEmUp | `CohortAssignments/Greedy&SweepLine/LightEmUp` |  |
| GreedyAndSweepLine | MakeItSmooth | `CohortAssignments/Greedy&SweepLine/MakeItSmooth` |  |
| GreedyAndSweepLine | MaxPointsOnLine | `CohortAssignments/Greedy&SweepLine/MaxPointsOnLine` |  |
| GreedyAndSweepLine | MinimisingDotProduct | `CohortAssignments/Greedy&SweepLine/MinimisingDotProduct` |  |
| GreedyAndSweepLine | PermuteTheArrays | `CohortAssignments/Greedy&SweepLine/PermuteTheArrays` |  |
| GreedyAndSweepLine | SellingCookies | `CohortAssignments/Greedy&SweepLine/SellingCookies` |  |
| GreedyAndSweepLine | SmallestDivisibleDigitProductII | `LeetCodeDailyProblems/SmallestDivisibleDigitProductII` | ★ |
| GreedyAndSweepLine | SquareSum | `CohortAssignments/Greedy&SweepLine/SquareSum` |  |
| IfElseConditions | AgeInDays | `CohortAssignments/IfElseConditions/AgeInDays` |  |
| IfElseConditions | TwoIntervals | `CohortAssignments/IfElseConditions/TwoIntervals` |  |
| Loops | Digits | `CohortAssignments/Loops/Digits` |  |
| Loops | EvenOdd | `CohortAssignments/Loops/EvenOdd` |  |
| Loops | LuckyNumbers | `CohortAssignments/Loops/LuckyNumbers` |  |
| Loops | MaxNumber | `CohortAssignments/Loops/MaxNumber` |  |
| Maps | CommonAbsoluteDifference | `CohortAssignments/Maps/CommonAbsoluteDifference` |  |
| Maps | GoodSequenceAZ101 | `CohortAssignments/Maps/GoodSequenceAZ101` |  |
| Maps | MapsAZ101 | `CohortAssignments/Maps/MapsAZ101` |  |
| Maps | RegistrationAZ101 | `CohortAssignments/Maps/RegistrationAZ101` |  |
| ModularArithmetic | BitString | `CSES/BitString` | ★ |
| ModularArithmetic | Exponentiation | `CSES/Exponentiation` | ★ |
| ModularArithmetic | ExponentiationTower | `Mathematics/ModularArithmetic/Exponentiation` |  |
| ModularArithmetic | RubiksCubeEasy | `CodeForces/Practise/RubiksCubeEasy` | ★ |
| ModularArithmetic | SolveEquation | `Mathematics/ModularArithmetic/SolveEquation` |  |
| MultiSet | ConcatenateArray | `CohortAssignments/MultiSet/ConcatenateArray` |  |
| MultiSet | MultiSetAZ101 | `CohortAssignments/MultiSet/MultiSetAZ101` |  |
| MultiSet | TowersAZ101 | `CohortAssignments/MultiSet/TowersAZ101` |  |
| NumberTheory | ArpasHardExam | `CodeForces/Practise/ArpasHardExam` | ★ |
| NumberTheory | CalculateGCD | `Mathematics/NumberTheory/CalculateGCD` |  |
| NumberTheory | CanYouMakeC | `Mathematics/NumberTheory/CanYouMakeC` |  |
| NumberTheory | DoubleFactorial | `AtCoder/tasks/DoubleFactorial` | ★ |
| NumberTheory | Factorization | `Mathematics/NumberTheory/Factorization` |  |
| NumberTheory | KthDigitOfInfiniteString | `LeetCodeDailyProblems/KthDigitOfInfiniteString` | ★ |
| NumberTheory | ModuloSummation | `AtCoder/tasks/ModuloSummation` | ★ |
| NumberTheory | NumberOfDivisors | `Mathematics/NumberTheory/NumberOfDivisors` |  |
| NumberTheory | NumberOfPiles | `Mathematics/NumberTheory/NumberOfPiles` |  |
| NumberTheory | PrimeCheck | `Mathematics/NumberTheory/PrimeCheck` |  |
| NumberTheory | RecurringFractions | `Mathematics/NumberTheory/RecurringFractions` |  |
| NumberTheory | RemainderMinimisation | `AtCoder/tasks/RemainderMinimisation` | ★ |
| NumberTheory | SegmentedSeive | `Mathematics/NumberTheory/SegmentedSeive` |  |
| NumberTheory | SherlockAndGirlfriend | `CodeForces/Practise/SherlockAndGirlfriend` | ★ |
| PrefixSums | APAddition | `PrefixAndPartialSUMS/APAddition` |  |
| PrefixSums | GoodNumbers | `CohortAssignments/PrefixSums/GoodNumbers` |  |
| PrefixSums | GoodNumbers2 | `PrefixAndPartialSUMS/GoodNumbers` |  |
| PrefixSums | LRPowerSumQuery | `PrefixAndPartialSUMS/LRPowerSumQuery` |  |
| PrefixSums | LRSumQuery | `PrefixAndPartialSUMS/LRSumQuery` |  |
| PrefixSums | MultiplySumQuery | `PrefixAndPartialSUMS/MultiplySumQuery` |  |
| PriorityQueue | MagicalCandyBag | `CohortAssignments/PriorityQueue/MagicalCandyBag` |  |
| PriorityQueue | MultiMapAZ101 | `CohortAssignments/PriorityQueue/MultiMapAZ101` |  |
| PriorityQueue | PriorityQueue | `CohortAssignments/PriorityQueue/PriorityQueue` |  |
| PriorityQueue | ReduceArray | `CohortAssignments/PriorityQueue/ReduceArray` |  |
| Recursion | KthPermutationEasy | `CohortAssignments/Recursion/KthPermutationEasy` |  |
| Recursion | Recur | `CohortAssignments/Recursion/Recur` |  |
| Recursion | TowerOfHanoi | `CohortAssignments/Recursion/TowerOfHanoi` |  |
| Recursion | VariableLoops | `CohortAssignments/Recursion/VariableLoops` |  |
| Sets | SetOperationsAZ101 | `CohortAssignments/Sets/SetOperationsAZ101` |  |
| Sets | SetsAZ101 | `CohortAssignments/Sets/SetsAZ101` |  |
| Sets | StudentAndGrades | `CohortAssignments/Sets/StudentAndGrades` |  |
| StackAndQueues | QueueAZ101 | `CohortAssignments/StackAndQueues/QueueAZ101` |  |
| StackAndQueues | QueueUsing2Stacks | `CohortAssignments/StackAndQueues/QueueUsing2Stacks` |  |
| StackAndQueues | StackAZ101 | `CohortAssignments/StackAndQueues/StackAZ101` |  |
| STL | BuyMaximumObjects1 | `CohortAssignments/STLUsages/BuyMaximumObjects1` |  |
| STL | BuyMaximumObjects2 | `CohortAssignments/STLApplications/BuyMaximumObjects2` |  |
| STL | CountThePairs | `CohortAssignments/STLApplications/CountThePairs` |  |
| STL | DesignEventManager | `LeetCode/STL/DesignEventManager` |  |
| STL | GeneratingPermutations | `CohortAssignments/STLUsages/GeneratingPermutations` |  |
| STL | InterestingGame | `CohortAssignments/STLApplications/InterestingGame` |  |
| STL | MaximumInWindow | `CohortAssignments/STLApplications/MaximumInWindow` |  |
| STL | ProductOfDigits | `CohortAssignments/STLBasics/ProductOfDigits` |  |
| STL | SortTheRollNumbers | `CohortAssignments/STLApplications/SortTheRollNumbers` |  |
| STL | STLSearching | `CohortAssignments/STLApplications/STLSearching` |  |
| STL | StreamingInput | `LiveSessions/STLApplicationIdeas/StreamingInput` |  |
| STL | SupportQueries1 | `CohortAssignments/STLApplications/SupportQueries1` |  |
| STL | ValidParanthesisAZ101 | `CohortAssignments/STLApplications/ValidParanthesisAZ101` |  |
| STL | VectorAZ101 | `CohortAssignments/STLBasics/VectorAZ101` |  |
| Strings | CountLetters | `CohortAssignments/Strings/CountLetters` |  |
| Strings | Dictionary | `CodeForces/Practise/Dictionary` | ★ |
| Strings | ILoveStrings | `CohortAssignments/Strings/ILoveStrings` |  |
| Strings | ReverseWords | `CohortAssignments/Strings/ReverseWords` |  |
| Strings | SmallestPalindromicArrangement | `LeetCodeDailyProblems/SmallestPalindromicArrangement` | ★ |
| Strings | SmallestStringConcatenation | `CodeForces/Practise/SmallestStringConcatenation` | ★ |
| Strings | StringFunctions | `CohortAssignments/Strings/StringFunctions` |  |
| Strings | StringScore | `CohortAssignments/Strings/StringScore` |  |
| Strings | URL | `CohortAssignments/Strings/URL` |  |
| Strings | WayTooLongStrings | `CohortAssignments/Strings/WayTooLongStrings` |  |
| TwoPointers | 3Sum | `CohortAssignments/TwoPointers/3Sum` |  |
| TwoPointers | ConsecutiveOnes | `CohortAssignments/TwoPointers/ConsecutiveOnes` |  |
| TwoPointers | LengthOfLongestSubarrayWithAtmostKFrequency | `LeetCodeDailyProblems/LengthOfLongestSubarrayWithAtmostKFrequency` | ★ |
| TwoPointers | MedianOfSubarraySum | `CohortAssignments/TwoPointers/MedianOfSubarraySum` |  |
| TwoPointers | MinimizeMaximize2D | `CohortAssignments/TwoPointers/MinimizeMaximize2D` |  |
| TwoPointers | NumberOfSubarrayWithSumAtmostK | `CohortAssignments/TwoPointers/NumberOfSubarrayWithSumAtmostK` |  |
| TwoPointers | PointsOnLine | `CodeForces/Practise/PointsOnLine` | ★ |
| TwoPointers | SmallestRangeCoveringFromKLists | `CohortAssignments/TwoPointers/SmallestRangeCoveringFromKLists` |  |
| VariablesAndOperators | CapitalOrSmallDigit | `CohortAssignments/VariablesAndOperators/CapitalOrSmallDigit` |  |
| VariablesAndOperators | CoordinatesOfPoint | `CohortAssignments/VariablesAndOperators/CoordinatesOfPoint` |  |
| VariablesAndOperators | PowerOfTwo | `CohortAssignments/VariablesAndOperators/PowerOfTwo` |  |
| BinarySearch | FindPeakElement | `LeetCode/BinarySearch/FindPeakElement.java` (loose file) | |
