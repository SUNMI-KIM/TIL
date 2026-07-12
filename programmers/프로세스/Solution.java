package 프로세스;

import java.util.*;

public class Solution {
    public int solution(int[] priorities, int location) {
        Queue<Process> queue = new ArrayDeque<>();

        for (int i = 0; i < priorities.length; i++) {
            queue.add(new Process(priorities[i], i));
        }

        Integer[] sortedPriorities = Arrays.stream(priorities).boxed().toArray(Integer[]::new);
        Arrays.sort(sortedPriorities, Collections.reverseOrder());

        for (int i = 0; i < priorities.length; i++) {
            Process process = queue.poll();
            while (sortedPriorities[i] != process.priority) {
                queue.add(process);
                process = queue.poll();
            }
            if (process.index == location) return i + 1;
        }
        return priorities.length;
    }
}

class Process {
    int priority;
    int index;

    Process(int priority, int index) {
        this.priority = priority;
        this.index = index;
    }
}
