import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class _2206 {
	
	static int[] dx = {1, 0, -1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int n;
	static int m;
	static char[][] arr;

	public static void main(String[] args) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new char[n][];
		
		for (int i = 0; i < n; i++) {
			arr[i] = br.readLine().toCharArray();
		}
		
		System.out.println(solve());
		
	}
	
	static int solve() {
		int min = Integer.MAX_VALUE;
		
		Queue<Pair> queue = new ArrayDeque();
		boolean[][][] visited = new boolean[n][m][2];
		queue.add(new Pair(0, 0, 1, 0));
		visited[0][0][0] = true;
		
		while (!queue.isEmpty()) {
			Pair pair = queue.poll();
			int x = pair.x;
			int y = pair.y;
			int distance = pair.distance;
			int broken = pair.broken;
			
			if (x == n - 1 && y == m - 1) return distance;
			
			for (int i = 0; i < 4; i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];
				int nextDistance = distance + 1;
				int nextBroken = broken;
				
				if (nx < 0 || nx >= n || ny < 0 || ny >= m) continue;
				if (visited[nx][ny][broken]) continue;
				if (arr[nx][ny] == '1') {
					if (broken == 1) continue;
					nextBroken = 1;
				}
				visited[nx][ny][nextBroken] = true;
				queue.add(new Pair(nx, ny, nextDistance, nextBroken));
			}
		}
		return -1;
	}

}

class Pair {
	int x;
	int y;
	int distance;
	int broken;
	
	Pair(int x, int y, int distance, int broken) {
		this.x = x;
		this.y = y;
		this.distance = distance;
		this.broken = broken;
	}
	
}