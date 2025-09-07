n = int(input())
dp = [float("inf")] * (n + 1)
dp[0], dp[1], dp[2] = -1, -1, -1

for i in range(3, n + 1):
    if i % 3 == 0:
        dp[i] = i // 3
    elif i % 5 == 0:
        dp[i] = min(i // 5, dp[i])

    if dp[i - 3] != -1:
            dp[i] = dp[i - 3] + 1
    if dp[i - 5] != -1:
            dp[i] = min(dp[i - 5] + 1, dp[i])

    if dp[i] == float("inf"):
          dp[i] = -1

print(dp[n])

N = int(input())
count = 0

while (N >= 0):
    if (N % 5 == 0):
        count += N // 5
        print(count)
        break
    N -= 3
    count += 1
else:
    print(-1) 