import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class _1167 {
	
	static int n;
	static ArrayList<Node>[] tree;
	
	static int maxDistance;
	static int maxIndex;

	public static void main(String[] args) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		tree = new ArrayList[n + 1];
		
		for (int i = 1; i < n + 1; i++) {
			tree[i] = new ArrayList();
		}
		
		for (int i = 0; i < n; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int v = Integer.parseInt(st.nextToken());
			while (true) {
				int nextV = Integer.parseInt(st.nextToken());
				if (nextV == -1) break;
				int nextD = Integer.parseInt(st.nextToken());
				tree[v].add(new Node(nextV, nextD));
			}
		}
		
		maxDistance = 0;
		solve(new boolean[n + 1], 0, 1);
		solve(new boolean[n + 1], 0, maxIndex);
		System.out.println(maxDistance);
	}
	
	static void solve(boolean[] visited, int distance, int v) {
		
		visited[v] = true;
		
		if (distance > maxDistance) {
			maxDistance = distance;
			maxIndex = v;
		}
		
		for (Node node : tree[v]) {
			if (visited[node.v]) continue;
			solve(visited, distance + node.w, node.v);
		}
		
	}

}

class Node {
	int v;
	int w;
	
	Node(int v, int w) {
		this.v = v;
		this.w = w;
	}
}