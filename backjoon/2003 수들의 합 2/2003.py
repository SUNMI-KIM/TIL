import sys
input = sys.stdin.readline

n, m = map(int, input().split())
arr = list(map(int, input().split()))

start, end = 0, 0
sum = arr[0]
ans = 0

while end < n:
    if sum < m:
        if end == n - 1:   # 오른쪽 포인터가 끝에 도달하면 더 이동할 수 없음
            break
        end += 1
        sum += arr[end]
    
    elif sum > m:
        sum -= arr[start]
        start += 1
    
    else: # 구간 합이 m과 같으면 정답을 세고 왼쪽 포인터를 이동
        ans += 1
        sum -= arr[start]
        start += 1

print(ans)