package 올바른_괄호;

public class Solution {
    boolean solution(String s) {
        StringBuffer stack = new StringBuffer();
        stack.append(' ');
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.append(c);
            } else if (c == ')') {
                if (stack.charAt(stack.length() - 1) == '(') stack.deleteCharAt(stack.length() - 1);
                else return false;
            }
        }
        return stack.length() == 1;
    }
}
