import sys
input = sys.stdin.readline

t = int(input())
for _ in range(t):
    counts = [0] * 10
    n = input().rstrip()

    for char in n:
        counts[int(char)] += 1
    
    ans = ""
    for i in range(1, 10):
        if counts[i] > 0:
            ans += str(i)
            counts[i] -= 1
            break

    for i in range(10):
        if counts[i] > 0:
            ans += str(i) * counts[i]
    print(ans)