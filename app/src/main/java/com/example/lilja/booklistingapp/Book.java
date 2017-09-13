package com.example.lilja.booklistingapp;

/**
 * Created by lilja on 7/10/17.
 */

public class Book {
    private String author;
    private String title;
    private String description;

    // will be converted to url to open browser for more info about the book
    private String url;
    private String image;

    // constructor
    public Book(String mAuthor, String mTitle, String mDescription, String mUrl, String mImage) {
        author = mAuthor;
        title = mTitle;
        description = mDescription;
        url = mUrl;
        image = mImage;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

}
