/**
 * 
 */
package cse4006;

import cse4006.CustomArrayList;

/**
 * FriendGraph Class Person을 가지고 있고, Person간의 distance를 구할 수 있습니다.
 * 
 * @author camelia
 *
 */
public class FriendGraph {

	private CustomArrayList<Person> people;

	/*
	 * FriendGraph의 생성자입니다.
	 */
	public FriendGraph() {
		people = new CustomArrayList<Person>();
	}

	/*
	 * people에 person을 추가하는 함
	 * 
	 * @param Person name 추가할 person
	 * 
	 * @throws person을 추가하는 과정에서 people이 꽉차있거나 이미 있는 사람을 추가 하는 등의 경
	 */
	public void addPerson(Person name) {
		try {
			people.add(name);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * people내의 두 person을 받아와 Friendship을 만드는 함수
	 * 
	 * @param String name1, String name2
	 */
	public void addFriendship(String name1, String name2) {
		Person person1 = people.find(name1);
		Person person2 = people.find(name2);
		try {
			if (person1 == null || person2 == null) {
				throw new NullPointerException("Person이 존재하지 않아 Friendship을 생성할 수 없습니다.");
			}

			person1.addFriend(person2);
			person2.addFriend(person1);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/*
	 * BFS로 name1을 가진 사람과 name2를 가진 사람 간의 거리를 구합니다.
	 * 
	 * @param String name1, String name2 두 사람의 이름
	 * 
	 * @return distance between name1 and name2, 닿지 않으면 -1, 동일인이면 0, 오류가 있으면 -99
	 * 
	 * @throws NullPointerException, CustomArrayList CustomQueue에서의 exception
	 */
	public int getDistance(String name1, String name2) {
		CustomArrayList<String> visited = new CustomArrayList<String>();
		Person startperson, endperson;

		try {
			startperson = people.find(name1);
			endperson = people.find(name2);

			if (startperson == null || endperson == null) {
				throw new NullPointerException("Person이 존재하지 않아 Friendship을 생성할 수 없습니다.");
			}

			CustomQueue queue = new CustomQueue();
			DistanceNode startnode = new DistanceNode(startperson.toString(), 0);
			queue.push(startnode);
			visited.add(startperson.toString());
			while (!queue.isEmpty()) {
				DistanceNode thisnode = queue.pop();
				Person thisperson = people.find(thisnode.getName());
				if (thisperson.toString() == endperson.toString()) {
					return thisnode.getDistance();
				}

				Object people[] = thisperson.getFriends().getList();
				for (int i = 0; i < thisperson.getNum_of_friend(); i++) {

					Person p = (Person) people[i];
					if (visited.find(p.toString()) == null) {
						DistanceNode node = new DistanceNode(p.toString(), thisnode.getDistance() + 1);
						queue.push(node);
						visited.add(p.toString());
					}
				}

			}
			if (visited.find(endperson.toString()) == null) {
				return -1;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return -99;
		}
		return -99;

	}

}
