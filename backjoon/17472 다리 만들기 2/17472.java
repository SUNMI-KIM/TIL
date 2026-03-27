import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class _17472 {
	
	static int[] dx = {1, 0, -1, 0};
	static int[] dy = {0, 1, 0, -1};

	static int n;
	static int m;
	static int[][] arr; 
	static ArrayList<Node>[] graph;
	
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
		boolean[][] visited = new boolean[n][m];
		int count = 1;
		for (int i = 0; i < n; i++) {
			for (int r = 0; r < m; r++) {
				if (visited[i][r] || arr[i][r] == 0) continue;
				bfs(i, r, visited, count++);
			}
		}
		
		graph = new ArrayList[count];
		for (int i = 0; i < count; i++) {
			graph[i] = new ArrayList<>();
		}
		
		for (int i = 0; i < n; i++) {
			for (int r = 0; r < m; r++) {
				if (arr[i][r] > 0) makeNode(i, r, arr[i][r]);
			}
		}
		return prim(count);
	}
	
	static int prim(int count) {
		boolean[] visited = new boolean[count];
		PriorityQueue<Node> queue = new PriorityQueue<>();
		
		queue.add(new Node(1, 0));
		
		int sum = 0; 
		int connectedIslands = 0;

		while (!queue.isEmpty()) {
			Node node = queue.poll();
			
			if (visited[node.v]) continue;
			visited[node.v] = true;
			
			sum += node.w;
			connectedIslands++;
			
			for (Node nextNode : graph[node.v]) {
				if (!visited[nextNode.v]) {
					queue.add(nextNode);
				}
			}
		}
		
		if (connectedIslands == count - 1) {
			return sum;
		}
		return -1;
	}
	
	static void makeNode(int x, int y, int islandNum) {
		for (int i = 0; i < 4; i++) {
			int w = 0;
			int nx = x;
			int ny = y;
			
			while (true) {
				nx += dx[i];
				ny += dy[i];
				
				if (nx < 0 || nx >= n || ny < 0 || ny >= m || arr[nx][ny] == islandNum) break;
				
				if (arr[nx][ny] != 0) {
					if (w >= 2) {
						graph[islandNum].add(new Node(arr[nx][ny], w));
					}
					break;
				}
				w++;
			}
		}
	}
	
	static void bfs(int x, int y, boolean[][] visited, int count) {
		ArrayDeque<Pair> queue = new ArrayDeque<>();
		queue.add(new Pair(x, y));
		visited[x][y] = true;
		arr[x][y] = count;
		
		while (!queue.isEmpty()) {
			Pair pair = queue.pollFirst(); 
			int cx = pair.x;
			int cy = pair.y;
			
			for (int i = 0; i < 4; i++) {
				int nx = cx + dx[i];
				int ny = cy + dy[i];
				
				if (nx < 0 || nx >= n || ny < 0 || ny >= m) continue;
				if (visited[nx][ny]) continue;
				if (arr[nx][ny] == 0) continue;
				
				visited[nx][ny] = true;
				arr[nx][ny] = count;
				queue.add(new Pair(nx, ny));
			}
		}
	}
}

class Node implements Comparable<Node> {
	int v;
	int w;
	
	Node(int v, int w) {
		this.v = v;
		this.w = w;
	}

	@Override
	public int compareTo(Node node) {
		return this.w - node.w;
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