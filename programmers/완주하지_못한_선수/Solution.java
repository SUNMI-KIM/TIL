package 완주하지_못한_선수;

import java.util.HashMap;
import java.util.Map;

class Solution {
    public String solution(String[] participant, String[] completion) {
        Map<String, Integer> map = new HashMap<>();

        for (String s : participant) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }

        for (String s : completion) {
            map.put(s, map.get(s) - 1);
            if (map.get(s) == 0) {
                map.remove(s);
            }
        }

        String answer = map.keySet().iterator().next();
        return answer;
    }
}