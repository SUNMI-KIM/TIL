package 전화번호_목록;

import java.util.Arrays;
import java.util.HashMap;

public class Solution {
    public boolean solution(String[] phone_book) {
        // hash(phone_book)
        return sorting(phone_book);
    }

    public boolean sorting(String[] phone_book) {
        Arrays.sort(phone_book);

        for (int i = 0; i < phone_book.length - 1; i++) {
            if (phone_book[i + 1].startsWith(phone_book[i])) return false;
        }
        return true;
    }

    public boolean hash(String[] phone_book) {
        HashMap<String, Integer> map = new HashMap<>();
        for (String s : phone_book) {
            map.put(s, 0);
        }

        for (int i = 0; i < phone_book.length; i++) {
            for (int r = 0; r < phone_book[i].length(); r++) {
                if (map.containsKey(phone_book[i].substring(0, r))) return false;
            }
        }
        return true;
    }
}
