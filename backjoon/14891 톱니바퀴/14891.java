import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class _14891 {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String[] str_arr = new String[4];
		int[] idx_arr = new int[4]; 
		for (int i = 0; i < 4; i++) {
			str_arr[i] = br.readLine();
		}

		int t = Integer.parseInt(br.readLine());
		int score = 0;
		for (int test_case = 0; test_case < t; test_case++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken()) - 1;
			int d = Integer.parseInt(st.nextToken());

			int[] rotate = new int[4]; 
			move_left(str_arr, idx_arr, n, d, rotate);
			move_right(str_arr, idx_arr, n, d, rotate);
			spin(rotate, idx_arr);
		}
		for (int i = 0; i < 4; i++) {
			if (str_arr[i].charAt(idx_arr[i]) == '0') continue;
			score += (int) Math.pow(2, i);
		}
		System.out.println(score);
	}

	public static void move_left(String[] str_arr, int[] idx_arr, int idx, int d, int[] rotate) {
		rotate[idx] = d;
		while (idx > 0) {
			if (!check(str_arr[idx - 1], str_arr[idx], (idx_arr[idx - 1] + 2) % 8 , (idx_arr[idx] - 2 + 8) % 8)) 
				break;
			rotate[idx - 1] = rotate[idx] * -1;
			idx--;
		}
	}

	public static void move_right(String[] str_arr, int[] idx_arr, int idx, int d, int[] rotate) {
		rotate[idx] = d;
		while (idx < 3) {
			if (!check(str_arr[idx], str_arr[idx + 1], (idx_arr[idx] + 2) % 8, ((idx_arr[idx + 1] - 2 + 8)) % 8)) 
				break;
			rotate[idx + 1] = rotate[idx] * -1;
			idx++;
		}
	}

	public static void spin(int[] rotate, int[] idx_arr) {
		for (int i = 0; i < 4; i++) {
			if (rotate[i] == -1) {
				idx_arr[i] = (idx_arr[i] + 1) % 8;
			} else if (rotate[i] == 1) {
				idx_arr[i] = (idx_arr[i] - 1 + 8) % 8; 
			}
		}
	}

	public static boolean check(String left, String right, int leftIdx, int rightIdx) {
		if (left.charAt(leftIdx) != right.charAt(rightIdx))
			return true;
		return false;
	}

}