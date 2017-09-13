/**
 * 
 */
package cse4006;

/**
 * Arraylist와 같은 기능을 하기 위해 구현한 class입니다. 최대 저장 한도는 50으로 설정되어 있습니다.
 * 
 * @author camelia
 *
 */
public class CustomArrayList<T> {
	private int size = 0;
	private T[] list;

	private static final int UPPER_LIMIT = 50;

	/*
	 * CustomArrayList의 생성자입니다.
	 */
	@SuppressWarnings("unchecked")
	public CustomArrayList() {
		list = (T[]) new Object[UPPER_LIMIT];
	}

	/*
	 * CustomArrayList에 t를 추가
	 * 
	 * @param T t
	 * 
	 * @exception IndexOutOfBoundsException, Exception (최대 개수 초과,이미 존재하는 이름 추가)
	 */
	public void add(T t) throws Exception {
		if (size >= UPPER_LIMIT) {
			throw new IndexOutOfBoundsException("최대 " + UPPER_LIMIT + "개까지 저장할 수 있습니다.");
		} else {
			for (int i = 0; i < size; i++) {
				if (list[i].toString().equals(t.toString())) {
					throw new Exception(t.toString() + "은 이미 존재하는 이름입니다.");
				}
			}
			list[size] = t;
			size++;
		}
	}

	/*
	 * list의 getter
	 */
	public T[] getList() {
		return list;
	}

	/*
	 * CustomArrayList에서 s를 검색
	 * 
	 * @param String s
	 * 
	 * @return T if exist, or null if not exist
	 */
	public T find(String s) {
		for (int i = 0; i < size; i++) {
			if (s.equals(list[i].toString())) {
				return list[i];
			}
		}
		return null;
	}

}
