package com.crawl.cache;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.crawl.entity.User;

public class UserCache {
	
	public static volatile ConcurrentSkipListSet<User> setNew = new ConcurrentSkipListSet<>();
	
	public static volatile ConcurrentSkipListSet<User> set = new ConcurrentSkipListSet<>();
	
	static BlockingQueue<User> basket = new LinkedBlockingQueue<User>(500);
	
	 // 生产 
    public static void produce(User u) throws InterruptedException {
        // put方法放入一个 ，若basket满了，等到basket有位置
    	set.add(u);
        basket.put(u);
    }

    // 消费苹果，从篮子中取走
    public  static User consume() throws InterruptedException {
        // take方法取出一个 ，若basket为空，等到basket有为止(获取并移除此队列的头部)
        return basket.take();
    }
	
}
