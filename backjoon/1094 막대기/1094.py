x = int(input())

ans = 0
for i in range(6, -1, -1):
    if x - 2 ** i < 0:
        continue
    x = x - 2 ** i
    ans += 1

print(ans)