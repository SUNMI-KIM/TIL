import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class _1722 {

	static int n;
	static long[] factorial = new long[21];
	static int[] arr;
	static boolean[] visited;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		init();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();

		n = Integer.parseInt(br.readLine());
		arr = new int[n];
		visited = new boolean[n];

		StringTokenizer st = new StringTokenizer(br.readLine());
		int flag = Integer.parseInt(st.nextToken());
		if (flag == 1) {
			one(Long.parseLong(st.nextToken()) - 1);
			for (int i = 0; i < n; i++) {
				sb.append(arr[i]).append(" ");
			}
		} else {
			for (int i = 0; i < n; i++) {
				arr[i] = Integer.parseInt(st.nextToken());
			}
			sb.append(two());			
		}
		System.out.println(sb.toString());
	}
	

	static void init() {
		factorial[0] = 1;
		for (int i = 1; i < 21; i++) {
	        factorial[i] = factorial[i - 1] * i;
	    }
	}
	
	static void one(long count) {
		for (int i = 0; i < n; i++) {
			int idx = (int) (count / factorial[n - i - 1]);
			
			for (int num = 0; num < n; num++) {
				if (visited[num]) continue;
				
				if (idx-- == 0) {
					visited[num] = true;
					arr[i] = num + 1;
					break;
				}
			}
			count %= factorial[n - i - 1];
		}
	}
	
	static long two() {
		long ans = 0;
		for (int i = 0; i < n - 1; i++) {
			int count = 0;
			for (int r = arr[i] - 2; r >= 0; r--) {
				if (visited[r]) continue;
				count++;
			}
			ans += count * factorial[n - i - 1];
			visited[arr[i] - 1] = true;
		}
		return ans + 1;
	}

}