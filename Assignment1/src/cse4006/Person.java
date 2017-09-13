/**
 * 
 */
package cse4006;

/**
 * 이름과 자신과 Friendship관계인 Person을 저장.
 * 
 * @author camelia
 *
 */
public class Person {
	private final String name;

	private CustomArrayList<Person> friends;
	private int num_of_friend;

	/*
	 * Person Class의 생성자
	 * 
	 * @param String name Person의 이름
	 */
	public Person(String name) {
		this.name = name;
		friends = new CustomArrayList<Person>();
		num_of_friend = 0;
	}

	/*
	 * Freindship관계에 있는 Person을 friends에 추가
	 * 
	 * @param Person p friends에 추가할 Person
	 * 
	 * @throws friends의 크기 초과, 이미 있는 Person 추가
	 */
	public void addFriend(Person p) throws Exception {
		friends.add(p);
		num_of_friend += 1;
	}

	/*
	 * friends의 getter
	 */
	public CustomArrayList<Person> getFriends() {
		return friends;
	}

	/*
	 * num_of_friend의 getter
	 */
	public int getNum_of_friend() {
		return num_of_friend;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
