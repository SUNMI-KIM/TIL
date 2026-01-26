import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class _14889 {
	static int min = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int n = Integer.parseInt(br.readLine());
		int[][] arr = new int[n][n];
		boolean[] team = new boolean[n];
		
		for (int i = 0; i < n; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			for (int r = 0; r < n; r++) {
				int S = Integer.parseInt(st.nextToken());
				arr[i][r] = S;
			}
		}
		
		solve(arr, team, n, 0, 0);
		
		System.out.println(min);
	}
	
	public static void solve(int[][] arr, boolean[] team, int n, int count, int idx) {
		if (count == n / 2) {
			min = Math.min(sum(team, n, arr), min);
			return;
		}
		
		for (int i = idx; i < n; i++) {
			team[i] = true;
			solve(arr, team, n, count + 1, i + 1);
			team[i] = false;
		}
	}
	
	public static int sum(boolean[] team, int n, int[][] arr) {
		int start = 0;
		int link = 0;
		
		for (int i = 0; i < n; i++) {
			for (int r = 0; r < n; r++) {
				if (team[i] == false && team[r] == false) start += arr[i][r] + arr[r][i];
				if (team[i] == true && team[r] == true) link += arr[i][r] + arr[r][i];
			}
		}
		return Math.abs(start - link) / 2;
	}
}