/**
 * 
 * Class that interacts with the database (testDB) in MongoDB
 * Database name: testDB
 * Collection name: Blog
 * 
 * @author: Saasha Nair
 * @date: Jan 8, 2014
 * 
 */

package com.example.mongoexample.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import com.example.mongoexample.entity.BlogPost;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDAO {
	private DBCollection table;

	// Constructor -- creates the connection to the database
	MongoDAO() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB("testDB");
		table = db.getCollection("Blog");
	}

	// function to convert an object of class BlogPost to a DBObject
	public static BasicDBObject toDBObject(BlogPost post) {
		return new BasicDBObject("author", post.getAuthor())
				.append("title", post.getTitle())
				.append("content", post.getContent())
				.append("publishDate", post.getPublishDate());
	}

	// -----------------------------------
	// function to convert a DBObject in an object of class BlogPost
	public static BlogPost fromDBObject(BasicDBObject document) {
		String author = document.get("author").toString();
		String title = document.get("title").toString();
		String content = document.get("content").toString();
		java.util.Date publishDateUtil = document.getDate("publishDate");
		Date publishDate = new Date(publishDateUtil.getTime());

		BlogPost post = new BlogPost();
		post.setAuthor(author);
		post.setTitle(title);
		post.setContent(content);
		post.setPublishDate(publishDate);

		return post;

	}

	// function to add a new blog post to the collection in the database
	public void addBlogPost(BlogPost post) {
		BasicDBObject document = toDBObject(post);
		table.insert(document);
	}

	// function to update the content of a particular blog post, whose title is
	// given
	public void updateContent(String titleOfBlogPost, String newContent) {
		BasicDBObject searchQuery = new BasicDBObject("title", titleOfBlogPost);
		BasicDBObject updData = new BasicDBObject("content", newContent);

		table.update(searchQuery, new BasicDBObject("$set", updData));
	}

	// function to delete all documents with a specified title from the
	// collection
	public void removeBlogPost(String title) {
		BasicDBObject delQuery = new BasicDBObject("title", title);
		table.remove(delQuery);
	}

	// function to return all the blog post in the database sorted by date
	public List<BlogPost> getAllBlogPosts() {
		List<BlogPost> blogPosts = new ArrayList<BlogPost>();
		BlogPost post = new BlogPost();

		DBObject document = new BasicDBObject();
		BasicDBObject sortQuery = new BasicDBObject("publishDate", 1);
		DBCursor cursor = table.find();
		cursor.sort(sortQuery);
		while (cursor.hasNext()) {
			document = cursor.next();
			post = fromDBObject((BasicDBObject) document);
			blogPosts.add(post);
		}
		return blogPosts;
	}

	// function to return all blog posts by a particular author
	public List<BlogPost> getBlogPostsByAuthor(String author) {
		List<BlogPost> blogPosts = new ArrayList<BlogPost>();
		BlogPost post = new BlogPost();

		DBObject document = new BasicDBObject();
		BasicDBObject searchQuery = new BasicDBObject("author", author);
		DBCursor cursor = table.find(searchQuery);

		while (cursor.hasNext()) {
			document = cursor.next();
			post = fromDBObject((BasicDBObject) document);
			blogPosts.add(post);
		}

		return blogPosts;
	}

	public void display(BlogPost post) {
		System.out.println("Author: " + post.getAuthor());
		System.out.println("Title: " + post.getTitle());
		System.out.println("Content: " + post.getContent());
		System.out.println("PublishDate: " + post.getPublishDate());
	}

	public static void main(String[] args) throws UnknownHostException {
		MongoDAO mDAO = new MongoDAO();

		// adding posts to the database
		BlogPost post1 = new BlogPost();
		post1.setAuthor("Saasha");
		post1.setTitle("Installing Mongo");
		post1.setContent("This blog post will give you step-by-step instructions on how to install MongoDB on a linux system");
		post1.setPublishDate(Date.valueOf("2014-12-29"));
		mDAO.addBlogPost(post1);

		BlogPost post2 = new BlogPost();
		post2.setAuthor("Saasha");
		post2.setTitle("Introduction to Mongo");
		post2.setContent("This blog post will give you a brief introduction to the basic concepts of MongoDB");
		post2.setPublishDate(Date.valueOf("2014-11-11"));
		mDAO.addBlogPost(post2);

		// updating the content
		mDAO.updateContent("Introduction to Mongo",
				"This is the updated content for the post titled introduction to MongoDB");

		// reading all posts by a particular author
		List<BlogPost> blogPosts1 = mDAO.getBlogPostsByAuthor("Saasha");
		for (BlogPost post : blogPosts1)
			mDAO.display(post);

		// reading all posts, sorted by date
		List<BlogPost> blogPosts2 = mDAO.getAllBlogPosts();
		for (BlogPost post : blogPosts2)
			mDAO.display(post);

		// removing a post by title
		mDAO.removeBlogPost("Installing Mongo");
	}
}
