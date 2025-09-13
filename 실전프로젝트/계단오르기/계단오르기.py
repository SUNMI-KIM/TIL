import sys
input = sys.stdin.readline

t = int(input())
for _ in range(t):
    n, p = map(int, input().split())
    idx = 0
    ans = 0

    while idx < n:
        if idx + 2 == p:
            idx += 1
        else:
            idx += 2
        ans += 1

    print(ans)