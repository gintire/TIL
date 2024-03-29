# Sort Algorithm
## MergeSort
> 참고 : https://en.wikipedia.org/wiki/Merge_sort
* 한국어로 합병 정릴, 병합 정렬 (merge sort)은 O(n logN) 비교 기반 정렬 알고리즘이다. 이 정렬은 안정 정렬에 속하며, 분할 정복 알고리즘의 하나이다.  
* **폰노이만**이 1945년에 개발했다. (😶 wow...)
* 시간복잡도 : O(n logN)
* 공간복잡도 : O(n)

### 설명
n-way 합병 정렬의 개념은 다음과 같다.
1. 정렬되지 않은 리스트를 각각 하나의 원소만 포함하는 n개의 부분 리스트로 분할한다. (한 원소만 든 리스트는 정렬된 것과 같음)
2. 부분리스트가 하나만 남을 때까지 반복해서 병합하며 정렬된 부분리스트를 생성한다. 마지막 남은 부분리스트가 정렬된 리스트이다.


흔히 쓰이는 하향식 2-way 합병 정렬은 다음과 같이 작동한다.
1. 리스트의 길이가 1 이하이면 이미 정렬된 것으로 본다. 그렇지 않을 경우에는
2. 분할 (divide) : 정렬되지 않은 리스트를 절반으로 잘라 비슷한 크기의 두 부분 리스트로 나눈다.
3. 정복 (conquer) : 정렬되지 않은 리스트를 절반으로 잘라 비슷한 크기의 두 부분 리스트로 나눈다.
4. 결합 (combine) : 두 부분 리스트를 다시 하나의 정렬된 리스트로 합병한다. 이때 정렬 결과가 임시배열에 저장된다.
5. 복사 (copy) : 임시 배열에 저장된 결과를 원래 배열에 복사한다.