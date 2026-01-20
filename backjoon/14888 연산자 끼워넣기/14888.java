public class _14888 {
	
	static int max = Integer.MIN_VALUE;
	static int min = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n = Integer.parseInt(br.readLine());
		int[] arr = new int[n];
		
		StringTokenizer st1 = new StringTokenizer(br.readLine());
		for (int i = 0; i < n; i++) {
			arr[i] = Integer.parseInt(st1.nextToken());
		}
		
		int[] operator = new int[4];
		StringTokenizer st2 = new StringTokenizer(br.readLine());
		for (int i = 0; i < 4; i++) {
			operator[i] = Integer.parseInt(st2.nextToken());
		}
		
		solve(1, arr[0], arr, n, operator);
		
		System.out.println(max);
		System.out.println(min);
	}
	
	public static void solve(int count, int num, int[] arr, int n, int[] operator) {
		if (count == n) {
			max = Math.max(num, max);
			min = Math.min(num, min);
			return;
		}
		
		for (int i = 0; i < 4; i++) {
			if (operator[i] < 1) continue;
			operator[i]--;
			solve(count + 1, calculate(num, arr[count], i), arr, n, operator);
			operator[i]++;
		}
	}
	
	public static int calculate(int first, int second, int idx) {
		if (idx == 0) return first + second;
		else if (idx == 1) return first - second;
		else if (idx == 2) return first * second;
		return first / second;
	}