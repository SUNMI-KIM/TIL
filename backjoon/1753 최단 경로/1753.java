import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class _1753 {
	
	static int n;
	static int e;
	static int k;
	static ArrayList<Node>[] graph;

	public static void main(String[] args) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		e = Integer.parseInt(st.nextToken());
		graph = new ArrayList[n + 1];
		for (int i = 1; i < n + 1; i++) {
			graph[i] = new ArrayList<>();
		}
		
		k = Integer.parseInt(br.readLine());
		
		for (int i = 0; i < e; i++) {
			st = new StringTokenizer(br.readLine());
			int v1 = Integer.parseInt(st.nextToken());
			int v2 = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			
			graph[v1].add(new Node(v2, w));
		}
		
		int[] distance = solve();
		for (int i = 1; i < n + 1; i++) {
			if (distance[i] == Integer.MAX_VALUE) sb.append("INF").append("\n");
			else sb.append(distance[i]).append("\n");
		}
		System.out.println(sb);
	}
	
	static int[] solve() {
		int[] distance = new int[n + 1];
		for (int i = 1; i < n + 1; i++) {
			distance[i] = Integer.MAX_VALUE;
		}
		
		PriorityQueue<Node> queue = new PriorityQueue<>();
		queue.add(new Node(k, 0));
		distance[k] = 0;
		
		while (!queue.isEmpty()) {
			Node node = queue.poll();
			int v = node.v;
			int w = node.w;
			
			if (distance[v] < w) continue;
			for (Node nextNode : graph[v]) {
				int nv = nextNode.v;
				int nw = nextNode.w;
				
				if (distance[nv] <= w + nw) continue;
				queue.add(new Node(nv, nw + w));
				distance[nv] = nw + w;
			}
		}
		return distance;
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