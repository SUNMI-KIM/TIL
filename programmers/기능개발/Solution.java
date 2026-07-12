package 기능개발;

import java.util.ArrayDeque;
import java.util.Deque;

public class Solution {
    public int[] solution(int[] progresses, int[] speeds) {
        Deque<Integer> queue = new ArrayDeque<>();
        int i = 0;

        while (i < progresses.length) {
            int time = (int) Math.ceil((100.0 - progresses[0]) / speeds[0]);
            int count = 0;
            while (i < progresses.length && time * speeds[i] + progresses[i] >= 100) {
                i++;
                count++;
            }
            queue.add(count);
        }
        return queue.stream().mapToInt(Integer::intValue).toArray();
    }
}
