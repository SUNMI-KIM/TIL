package 같은_숫자는_싫어;

import java.util.ArrayDeque;
import java.util.Deque;

public class Solution {
    public int[] solution(int []arr) {

        Deque<Integer> deque = new ArrayDeque<>();

        for (int value : arr) {
            // 덱이 비어있거나, 마지막 원소가 현재 값과 다를 때만 추가
            if (deque.isEmpty() || deque.peekLast() != value) {
                deque.addLast(value);
            }
        }

        int[] answer = deque.stream().mapToInt(Integer::intValue).toArray();
        return answer;
    }
}
