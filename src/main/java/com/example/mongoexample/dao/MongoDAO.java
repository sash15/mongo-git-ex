/**
 * 
 */
package com.example.mongoexample.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.mongoexample.entity.BlogPost;


/**
 * 
 *
 */
public class MongoDAO
{
    public void addBlogPost( BlogPost post )
    {
        // TODO: Add Code
    }


    public void updateContent( String titleOfBlogPost, String newContent )
    {
        // TODO: Add Code
    }


    public void removeBlogPost( String title )
    {
        // TODO: Add Code
    }


    public List<BlogPost> getAllBlogPosts()
    {
        List<BlogPost> blogPosts = new ArrayList<BlogPost>();

        // TODO: Add Code

        return blogPosts;
    }


    public List<BlogPost> getBlogPostsByAuthor( String author )
    {
        List<BlogPost> blogPosts = new ArrayList<BlogPost>();
        // TODO: Add Code
        return blogPosts;
    }


    public static void main( String[] args )
    {
        // TODO: Add Test Cases
    }
}
