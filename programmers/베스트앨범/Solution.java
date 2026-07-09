package 베스트앨범;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Solution {

    public int[] solution(String[] genres, int[] plays) {
        Map<String, List<Movie>> map = new HashMap<>();
        Map<String, Integer> playsMap = new HashMap<>();

        for (int i = 0; i < genres.length; i++) {
            if (!map.containsKey(genres[i])) map.put(genres[i], new ArrayList<>());
            map.get(genres[i]).add(new Movie(i, plays[i]));

            playsMap.put(genres[i], playsMap.getOrDefault(genres[i], 0) + plays[i]);
        }

        List<Entry<String, List<Movie>>> list = map.entrySet().stream()
                .sorted((e1, e2) -> playsMap.get(e2.getKey()) - playsMap.get(e1.getKey())) // e2의 키에서 e1의 키를 빼도록 수정!
                .toList();

        List<Integer> answer = new ArrayList<>();
        for (Entry<String, List<Movie>> entry : list) {
            List<Movie> movies = entry.getValue();

            movies.sort((o1, o2) -> {
                if (o1.plays == o2.plays) {
                    return o1.id - o2.id;
                }
                return o2.plays - o1.plays;
            });

            for (int i = 0; i < Math.min(movies.size(), 2); i++) {
                answer.add(movies.get(i).id);
            }
        }

        return answer.stream().mapToInt(Integer::intValue).toArray();
    }
}

class Movie {
    int id;
    int plays;

    Movie(int id, int plays) {
        this.id = id;
        this.plays = plays;
    }
}
