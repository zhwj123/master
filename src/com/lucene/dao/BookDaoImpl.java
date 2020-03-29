package com.lucene.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lucene.bean.Book;

/**
 *  
 * <p>
 * Title: BookDaoImpl
 * </p>
 *  
 * <p>
 * Description: TODO(这里用一句话描述这个类的作用) 
 * <p>
 * <p>
 * Company: www.itcast.com
 * </p>
 *  @author 传智.关云长   @date 2015-12-27 上午10:04:30    @version 1.0
 */
public class BookDaoImpl implements BookDao {

	@Override
	public List<Book> queryBooks() {
		// 数据库链接
		Connection connection = null;

		// 预编译statement
		PreparedStatement preparedStatement = null;

		// 结果集
		ResultSet resultSet = null;

		// 图书列表
		List<Book> list = new ArrayList<Book>();

		try {
			// 加载数据库驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 连接数据库
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/gjp", "root", "root");

			// SQL语句
			String sql = "SELECT * FROM book";
			// 创建preparedStatement
			preparedStatement = connection.prepareStatement(sql);

			// 获取结果集
			resultSet = preparedStatement.executeQuery();

			// 结果集解析
			while (resultSet.next()) {
				Book book = new Book();
				book.setBid(resultSet.getInt("bid"));
				book.setBname(resultSet.getString("bname"));
				book.setBtype(resultSet.getString("btype"));
				book.setDescription(resultSet.getString("description"));
				list.add(book);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
