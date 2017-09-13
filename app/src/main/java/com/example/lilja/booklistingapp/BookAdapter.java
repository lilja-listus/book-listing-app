package com.example.lilja.booklistingapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by lilja on 7/11/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    String image;

    /**
     * Building bookAdapter
     *
     * @param context
     * @param books
     */
    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    /**
     * Returns a list item view that displays information about the book at the current position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);
        //  Title of the book
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        String title = currentBook.getTitle();
        titleView.setText(title);

        //name of the author
        String author = currentBook.getAuthor();
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        authorView.setText(author);

        //description of the book
        String description = currentBook.getDescription();
        TextView descriptionView = (TextView) listItemView.findViewById(R.id.description);
        descriptionView.setText(description);

        //the picture of the book
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image_book);
        image = currentBook.getImage();

        /**
         * if there is no image, it is substituted by a picture,
         * if is then  String url is converted to uri and a the image is
         * placed into the imageView
         *
         */
        if (image.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_action_name);
        } else {
            Uri url = Uri.parse(image);
            Picasso.with(getContext()).load(url).fit().into(imageView);
        }

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
