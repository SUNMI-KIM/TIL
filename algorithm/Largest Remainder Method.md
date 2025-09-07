# Largest Remainder Method (가장 큰 나머지 방식)

원형 차트에서 각 항목을 정수 퍼센트로 표시하면 합이 100%가 되지 않는 경우가 발생한다. 예를 들어, 세 항목이 모두 33.3%일 때 내림을 하면 33%, 33%, 33%으로 표시되어 총합이 99%가 된다.

백엔드 프로젝트 진행하면서 감정 라벨에 따른 퍼센트를 원형 차트에 표시해야 하는 상황이 있었고, 이 값을 보정해 프론트엔드에 전달해야 했다. 이때 해결책으로 **가장 큰 나머지 방식(Largest Remainder Method)**을 찾아 적용하게 되었다.

## 💡 핵심 아이디어


1. 각 항목의 비율을 실수 퍼센트로 계산한다.
2. 각 항목을 내림(floor) 처리하여 정수 퍼센트를 1차 배정한다.
3. 내림된 값들의 합을 계산하고, 남은 보정량(100 - 합)을 구한다.
4. 소수점 나머지가 큰 항목부터 차례로 보정량만큼 1씩 더한다.
5. 최종적으로 합계가 정확히 100%가 된다. 

예시로, 33.3%, 33.3%, 33.3%의 경우 내림 후 [33, 33, 33]이 되고, 남은 1%를 소수점이 가장 큰 항목에 더해 [34, 33, 33]으로 만든다. 합계는 100이 된다.

## 코드 구현(Java)


```java
 Map<Analysis, Long> counts = analyses.stream()
        .collect(Collectors.groupingBy(SentimentAnalysis::getResult, Collectors.counting()));

    int total = analyses.size();
    if (total == 0) {
        return new HashMap<>();
    }

```

`counts`에서 각 감정 별 빈도를 집계한다. 

```java
		Map<Analysis, Integer> baseMap = new HashMap<>(); // 내림 값
    Map<Analysis, Integer> remMap  = new HashMap<>(); // 나머지

    int baseSum = 0;
    for (Analysis a : Analysis.values()) {
        long c = counts.getOrDefault(a, 0L);
        long scaled = c * 100L;         // 정수 스케일
        int base   = (int) (scaled / total);
        int rem    = (int) (scaled % total);

        baseMap.put(a, base);
        remMap.put(a, rem);
        baseSum += base;
    }

```

퍼센트를 정수로 스케일링한 뒤, `base`(내림 값)와 `rem`(나머지)을 구한다.

```java
int correction = 100 - baseSum;
    if (correction <= 0) {
        // 이미 100 이상이거나 정확히 100이면 그대로 반환
        return toResultMap(baseMap);
    }
```

합계가 100보다 작은 만큼(`correction`) 보정이 필요함을 계산한다. 이론적으로 내림만 하면 합계는 항상 100 이하라서, `correction < 0` 상황은 발생하지 않는다. 이는 안전 장치에 가깝다. 

```java
List<Analysis> order = Arrays.stream(Analysis.values())
        .sorted(
            Comparator.<Analysis>comparingInt(a -> remMap.getOrDefault(a, 0)).reversed()
                .thenComparingLong(a -> counts.getOrDefault(a, 0L)).reversed()
                .thenComparingInt(Enum::ordinal)
        ).collect(Collectors.toList());
```

보정 분배 순서를 정하기 위해 나머지가 큰 순서, 빈도수가 큰 순서, enum 선언 순서로 정렬한다.

```java
correction = Math.min(correction, order.size());
    for (int i = 0; i < correction; i++) {
        Analysis a = order.get(i);
        baseMap.put(a, baseMap.get(a) + 1);
    }
```

보정량만큼 순서대로 +1을 분배하여 합계를 맞춘다.

```java
return toResultMap(baseMap);
}

private Map<String, Long> toResultMap(Map<Analysis, Integer> map) {
    return map.entrySet().stream()
        .collect(Collectors.toMap(
            e -> e.getKey().getDescription(),
            e -> e.getValue().longValue()
        ));
}
```

최종적으로 라벨(String)과 값(Long) 형태의 맵으로 변환하여 반환한다.

아래 코드는 감정 분석 결과 리스트를 받아 각 감정별 퍼센트를 계산하고, **Largest Remainder Method**를 적용하여 총합을 정확히 100%로 맞추는 Java 전체 코드이다. 

```java
private Map<String, Long> calculateEmotionPercentagesStable(List<SentimentAnalysis> analyses) {
    // 0. 집계
    Map<Analysis, Long> counts = analyses.stream()
        .collect(Collectors.groupingBy(SentimentAnalysis::getResult, Collectors.counting()));

    int total = analyses.size();
    if (total == 0) {
        return new HashMap<>();
    }

    // 1. 1차 배정: base (floor), 나머지 rem(정수)
    Map<Analysis, Integer> baseMap = new HashMap<>();
    Map<Analysis, Integer> remMap  = new HashMap<>();

    int baseSum = 0;
    for (Analysis a : Analysis.values()) {
        long c = counts.getOrDefault(a, 0L);
        long scaled = c * 100L;         // 정수 스케일
        int base   = (int) (scaled / total);
        int rem    = (int) (scaled % total);

        baseMap.put(a, base);
        remMap.put(a, rem);
        baseSum += base;
    }

    // 2. 보정량
    int correction = 100 - baseSum;
    if (correction <= 0) {
        // 이미 100 이상이거나 정확히 100이면 그대로 반환
        return toResultMap(baseMap);
    }

    // 3. 나머지 큰 순, count 큰 순, enum 선언 순으로 정렬
    List<Analysis> order = Arrays.stream(Analysis.values())
        .sorted(
            Comparator.<Analysis>comparingInt(a -> remMap.getOrDefault(a, 0)).reversed()
                .thenComparingLong(a -> counts.getOrDefault(a, 0L)).reversed()
                .thenComparingInt(Enum::ordinal)
        ).collect(Collectors.toList());

    // 4. 보정량만큼 +1 배분
    correction = Math.min(correction, order.size());
    for (int i = 0; i < correction; i++) {
        Analysis a = order.get(i);
        baseMap.put(a, baseMap.get(a) + 1);
    }

    // 5. 결과 변환 (String 라벨 → Long 값)
    return toResultMap(baseMap);
}

private Map<String, Long> toResultMap(Map<Analysis, Integer> map) {
    return map.entrySet().stream()
        .collect(Collectors.toMap(
            e -> e.getKey().getDescription(),
            e -> e.getValue().longValue()
        ));
}

```