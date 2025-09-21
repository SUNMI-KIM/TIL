import sys
input = sys.stdin.readline

n = int(input())
arr = []
for _ in range(n):
    num, strike, ball = map(int, sys.stdin.readline().split())
    arr.append((str(num), strike, ball))

def check(candidate_num, guess_num, strike, ball):
    strike_count = 0
    ball_count = 0
    
    for i in range(3):
        if candidate_num[i] == guess_num[i]:
            strike_count += 1
            
    shared_digits = len(set(candidate_num) & set(guess_num))
    ball_count = shared_digits - strike_count
    
    return strike == strike_count and ball == ball_count

answer = 0
for a in range(1, 10):
    for b in range(1, 10):
        for c in range(1, 10):
            if b == c or a == c or a == b:
                continue
    
            candidate_num = str(a) + str(b) + str(c)
            is_valid = True
            for guess_num, strike, ball in arr:
                if not check(candidate_num, guess_num, strike, ball):
                    is_valid = False
                    break 
            
            if is_valid:
                answer += 1
                
print(answer)
            


                
               

                
