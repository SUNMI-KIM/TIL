import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class _2638 {
	
	static int[] dx = {1, 0, -1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int n;
	static int m;
	static int[][] arr;

	public static void main(String[] args) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new int[n][m];
		
		for (int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine());
			for (int y = 0; y < m; y++) {
				arr[x][y] = Integer.parseInt(st.nextToken());
			}
		}
		System.out.println(solve());
	}
	
	static int solve() {
		int time = 0;
		
		while (bfs() != n * m) {
			for (int x = 0; x < n; x++) {
				for (int y = 0; y < m; y++) {
					if (arr[x][y] != 1) continue;
					if (check(x, y)) arr[x][y] = 2;
				}
			}
			time++;
		}
		return time;
	}
	
	static boolean check(int x, int y) {
		int count = 0;
		for (int i = 0; i < 4; i++) {
			int nx = x + dx[i];
			int ny = y + dy[i];
			
			if (nx < 0 || nx >= n || ny < 0 || ny >= m) continue;
			if (arr[nx][ny] == -1) count++;
		}
		return count >= 2;
	}
	
	static int bfs() {
		int count = 0;
		
		boolean[][] visited = new boolean[n][m];
		Queue<Pair> queue = new ArrayDeque();
		queue.add(new Pair(0, 0));
		visited[0][0] = true;
		
		while (!queue.isEmpty()) {
			Pair pair = queue.poll();
			int x = pair.x;
			int y = pair.y;
			arr[x][y] = -1;
			count++;
			
			for (int i = 0; i < 4; i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];
				
				if (nx < 0 || nx >= n || ny < 0 || ny >= m) continue;
				if (visited[nx][ny]) continue;
				if (arr[nx][ny] == 1) continue;
				visited[nx][ny] = true;
				queue.add(new Pair(nx, ny));
			}
		}
		return count;
	}

}

class Pair {
	int x;
	int y;
	
	Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
}