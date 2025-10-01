

![](https://velog.velcdn.com/images/hariaus/post/a3ecd720-af12-46a9-b02f-c33d50d907e2/image.png)


## Greedy



Greedy의 뜻은 ‘탐욕스러운, 욕심많은’ 이다. Greedy 알고리즘은 말 그대로 선택의 순간마다 당장 눈 앞에 보이는 최적의 상황을 고르는 것이다.



### Greedy 사용 조건

---

1. **탐욕적 선택 속성 (greedy choice property)**

탐욕적인 선택이 항상 안전하다는 것이 보장된다는 의미이다. 즉, 그리디한 선택이 언제나 최적해를 보장해야한다.

1. **Optimal Substructure ( 최적 부분 구조 )**

부분 문제의 최적 결과 값을 사용해 전체 문제의 최적 결과를 낼 수 있는 경우를 의미한다. 

→ **부분 문제가 항상 최적 값을 내고, 부분 문제들의 결과가 최종 값을 계산하는 데 꼭 필요하다면 DP를 사용할 수 있다.**

## 문제 해결 방식


예를 들어, 입력이 `"201"`이라면 만들 수 있는 모든 수는 다음과 같다:

```
201, 210, 021, 012, 120, 102
```

여기서 `"021"`과 `"012"`는 각각 `"21"`, `"12"`와 같기 때문에 더 이상 **3자리 수가 아님**을 알 수 있다.

즉, **맨 앞자리에 0이 올 수 없다**는 조건이 붙는다.

따라서 모든 조합을 다 구할 필요 없이, 조건에 맞게 **가장 작은 수를 만들어주는 방식**으로 접근할 수 있다.

---

### 1. 맨 앞자리 숫자 선택

- 맨 앞자리에는 0이 올 수 없다.
- 따라서 입력 숫자 중에서 **0이 아닌 가장 작은 숫자**를 맨 앞에 둔다.

```python
for i in range(1, 10):
        if counts[i] > 0:
            ans += str(i)
            counts[i] -= 1
            break
```

---

### 2. 그 다음에 오는 숫자

- 이후에는 가능한 한 작은 수부터 차례대로 나열하면 된다.

```python
for i in range(10):
        if counts[i] > 0:
            ans += str(i) * counts[i]
```

---

### 최종 코드 (Python)

```python
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
```

여기서 `counts` 배열은 **숫자의 개수를 저장하는 배열**이다.

- 인덱스(`0~9`)는 숫자를 의미하고,
- 값은 해당 숫자의 개수를 나타낸다.