package naver;

public class Main {
	
	public static void main(String[] args) throws Exception {
	
		MyQueue<Integer> myQueue = new MyQueue<Integer>(10);
		myQueue.push(1);
		myQueue.push(2);
		myQueue.push(13);
		System.out.println(myQueue.pop().intValue());
		System.out.println(myQueue.peek().intValue());
		myQueue.empty();
		System.out.println(myQueue.pop().intValue());
		System.out.println(myQueue.peek().intValue());
	}

}
