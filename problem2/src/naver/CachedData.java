package naver;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class CachedData {
	Object data;
	volatile boolean cacheValid;
	ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	void processCachedData() {
		rwl.readLock().lock();
		//无效事更新数据
		if (!cacheValid) {
			rwl.readLock().unlock();
			rwl.writeLock().lock();
			if (!cacheValid) {
				data = "";
				cacheValid = true;
			}
			rwl.readLock().lock();
			rwl.writeLock().unlock(); 
		}
		//有效时处理数据
		deal(data);
		rwl.readLock().unlock();
	}

	private void deal(Object data2) {
		//处理数据
		
	}
	
	
}