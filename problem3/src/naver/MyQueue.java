package naver;
import java.util.*;

//数组实现队列
public class MyQueue<E> {
  
  //用数组来保存
  private Object[] queue;
  
  //队列容量
  private int capacity;
  
  //队列中元素的个数
  private int size;
  
  //队列头部元素对应的下标
  private int head;
  
  //队列的尾部的下一个位置下标
  private int tail;
  
  public MyQueue(int capacity){
      this.capacity = capacity;
      this.queue = new Object[capacity];
  }
  
  //将元素加入到队列的队尾，如果队列空间不足，抛出异常
  public boolean push(E e) throws Exception{
      //先确定空间是否足够,已经满了就抛出异常
      if(size == capacity){
          throw new Exception("queue full");
      }
      //没满就加入到队尾
      queue[tail] = e;
      //计算新的队尾
      tail = (tail+1) % capacity;
      size ++;
      return true;
  }
  
  //返回并删除队列头部的元素，如果队列为空，抛出异常
  public E pop() throws NoSuchElementException{
      //判断是否为空,为空则报错
      if(size == 0){
          throw new NoSuchElementException();
      }
      E pop = elementData(head);
      //将头部元素设置为null
      queue[head] = null;
      //重新计算head的值
      head = (head+1) % capacity;
      size -- ;
      return pop;
  }
  
  
  //返回队列头部的元素，如果队列为空，返回null
  public E peek(){
      //判断是否为空,为空则返回null
      if(size == 0){
          return null;
      }
      E e = elementData(head);
      return e;
  }
  
  //清空队列
  public void empty (){
	  tail = 0;
	  head =0;
      size = 0;
  }
  
  @SuppressWarnings("unchecked")
  private E elementData(int index) {
      return (E) queue[index];
  }
  
  
}