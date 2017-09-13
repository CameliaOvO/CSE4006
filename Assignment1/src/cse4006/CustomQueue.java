/**
 * 
 */
package cse4006;

/**
 * BFS에서 사용하기위해 Circular Queue 구현
 * 
 * @author camelia
 *
 */

public class CustomQueue {

	private static final int UPPER_LIMIT = 50;

	private DistanceNode[] queue;
	private int front, rear;

	/*
	 * CustomQueue의 생성자
	 */
	public CustomQueue() {
		queue = new DistanceNode[UPPER_LIMIT + 1];
		front = 0;
		rear = 0;
	}

	/*
	 * 비어있으면 true 비어있지 않으면 false
	 */
	public boolean isEmpty() {
		return (front == rear);
	}

	/*
	 * 꽉 차있으면 true , 차있지 않으면 false
	 */
	public boolean isFull() {
		return (rear + 1) % UPPER_LIMIT == front;
	}

	/*
	 * DistanceNode의 push
	 */
	public void push(DistanceNode n) throws Exception {
		if (isFull()) {
			throw new IndexOutOfBoundsException("Queue가 가득차 push할 수 없습니다.");
		} else {
			queue[rear] = n;
			rear = (rear + 1) % UPPER_LIMIT;
		}
	}

	/*
	 * DistanceNode의 pop
	 */
	public DistanceNode pop() throws Exception {
		if (isEmpty()) {
			throw new IndexOutOfBoundsException("Queue가 비어있어 pop할 수 없니다.");
		} else {
			DistanceNode n = queue[front];
			front = (front + 1) % UPPER_LIMIT;
			return n;
		}
	}

}
