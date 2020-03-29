package com.lucene.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.lucene.bean.Book;
import com.lucene.dao.BookDao;
import com.lucene.dao.BookDaoImpl;

public class IndexManager {
	@Test
	public void createIndex() throws IOException {
		//采集数据
		BookDao bo=new BookDaoImpl();
		List<Book> listb=bo.queryBooks();
		//将采集的数据存到Document中
		List<Document> list=new ArrayList<>();
		Document doc;
		for (Book book : listb) {
			doc=new Document();
			// store:如果是yes，则说明存储到文档域中,no内存
			Field bid=new StringField("bid", book.getBid().toString(),Store.YES);
			Field bname=new TextField("bname", book.getBname().toString(),Store.YES);
			Field btype=new StoredField("bid", book.getBtype().toString());
			Field description=new TextField("bid", book.getDescription().toString(),Store.NO);

			// 设置boost值
			if (book.getBid() == 2)
				description.setBoost(100f);

			// 将field域设置到Document对象中
			doc.add(bid);
			doc.add(bname);
			doc.add(btype);
			doc.add(description);

			list.add(doc);
		}

		// 创建分词器，标准分词器
		// Analyzer analyzer = new StandardAnalyzer();
		// 使用ikanalyzer
		Analyzer analyzer = new IKAnalyzer();

		// 创建IndexWriter
		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_4_10_3,
				analyzer);
		// 指定索引库的地址
		File indexFile = new File("E:\\11-index\\hm19\\");
		Directory directory = FSDirectory.open(indexFile);
		IndexWriter writer = new IndexWriter(directory, cfg);

		// 通过IndexWriter对象将Document写入到索引库中
		for (Document doc1 : list) {
			writer.addDocument(doc1);
		}

		// 关闭writer
		writer.close();
	}
	@Test
	public void deleteIndex() throws Exception {
		// 创建分词器，标准分词器
		Analyzer analyzer = new StandardAnalyzer();

		// 创建IndexWriter
		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_4_10_3,
				analyzer);
		Directory directory = FSDirectory
				.open(new File("E:\\11-index\\hm19\\"));
		// 创建IndexWriter
		IndexWriter writer = new IndexWriter(directory, cfg);

		// Terms
		// writer.deleteDocuments(new Term("id", "1"));

		// 删除全部（慎用）
		writer.deleteAll();

		writer.close();
	}

	//@Test
	public void updateIndex() throws Exception {
		// 创建分词器，标准分词器
		Analyzer analyzer = new StandardAnalyzer();

		// 创建IndexWriter
		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_4_10_3,
				analyzer);

		Directory directory = FSDirectory
				.open(new File("E:\\11-index\\hm19\\"));
		// 创建IndexWriter
		IndexWriter writer = new IndexWriter(directory, cfg);

		// 第一个参数：指定查询条件
		// 第二个参数：修改之后的对象
		// 修改时如果根据查询条件，可以查询出结果，则将以前的删掉，然后覆盖新的Document对象，如果没有查询出结果，则新增一个Document
		// 修改流程即：先查询，再删除，在添加
		Document doc = new Document();
		doc.add(new TextField("name", "lisi", Store.YES));
		writer.updateDocument(new Term("name", "zhangsan"), doc);

		writer.close();
	}
}
