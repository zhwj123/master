package com.lucene.dao;

import java.util.List;

import com.lucene.bean.Book;

/**
 * <p>Title: BookDao</p>
 * <p>Description: TODO(这里用一句话描述这个类的作用) <p>
 * <p>Company: www.itcast.com</p>
 * @author 传智.关云长 
 * @date 2015-12-27 上午10:02:59  
 * @version 1.0
 */
public interface BookDao {

	public List<Book> queryBooks();
}
