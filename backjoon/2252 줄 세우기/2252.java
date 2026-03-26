import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.StringTokenizer;

public class _2252 {
	
	static int n;
	static int m;
	
	static ArrayList<Integer>[] arr;
	static int[] indegree;
	static Deque<Integer> queue = new ArrayDeque();
	
	static int[] ans;

	public static void main(String[] args) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		arr = new ArrayList[n + 1];
		indegree = new int[n + 1];
		ans = new int[n];

		for (int i = 0; i < n; i++) {
			arr[i + 1] = new ArrayList<>();
		}
		
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			
			int A = Integer.parseInt(st.nextToken());
			int B = Integer.parseInt(st.nextToken());
			arr[A].add(B);
			indegree[B] += 1;
		}
		
		solve();
		for (int i = 0; i < n; i++) {
			sb.append(ans[i]).append(" ");
		}
		System.out.println(sb);
	}
	
	static void solve() {
		
		int idx = 0;
		while (idx < n) {
			for (int i = 1; i < n + 1; i++) {
				if (indegree[i] == 0) {
					indegree[i] = -1;
					ans[idx++] = i;
					queue.add(i);
				}
			}
			
			int node = queue.pollFirst();
			for (int next_node : arr[node]) {
				if (indegree[next_node] <= 0) continue;
				indegree[next_node] -= 1;
			}
		}
	
	}

}
