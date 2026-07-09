package 의상;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Solution {
    public int solution(String[][] clothes) {
        Map<String, Integer> map = new HashMap<>();
        for (String[] strings : clothes) {
            map.put(strings[1], map.getOrDefault(strings[1], 0) + 1);
        }

        int answer = 1;
        for (Entry<String, Integer> entry : map.entrySet()) {
            answer *= entry.getValue() + 1;
        }

        return answer - 1;
    }
}
