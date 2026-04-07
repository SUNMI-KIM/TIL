import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class _11048 {
	
	static int[] dx = {0, -1, -1};
	static int[] dy = {-1, 0, -1};
	
	static int n;
	static int m;
	static int[][] arr;

	public static void main(String[] args) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new int[n][m];
		
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int r = 0; r < m; r++) {
				arr[i][r] = Integer.parseInt(st.nextToken());
			}
		}
		System.out.println(solve());
	}
	
	static int solve() {
		int[][] dp = new int[n][m];
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < m; y++) {
				dp[x][y] = arr[x][y];
				for (int d = 0; d < 3; d++) {
					if (check(x + dx[d], y + dy[d])) continue;
					dp[x][y] = Math.max(dp[x][y], arr[x][y] + dp[x + dx[d]][y + dy[d]]);
				}
			} 
		}
		return dp[n - 1][m - 1];
	}
	
	static boolean check(int x, int y) {
		return 0 > x || n <= x || 0 > y || m <= y; 
	}

}