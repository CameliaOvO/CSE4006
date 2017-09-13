/**
 * 
 */
package cse4006;

/**
 * BFS에서 최단거리를 구하기 위해 거리 저장을 위해 사용
 * 
 * @author camelia
 *
 */
public class DistanceNode {
	private final String name;
	private int distance;

	/*
	 * DistanceNode의 생성자
	 */
	public DistanceNode(String name, int distance) {
		this.name = name;
		this.distance = distance;
	}

	/*
	 * distance의 getter
	 */
	public int getDistance() {
		return distance;
	}

	/*
	 * name의 getter
	 */
	public String getName() {
		return name;
	}

	/*
	 * distance를 변경하기 위해 사용.
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

}
