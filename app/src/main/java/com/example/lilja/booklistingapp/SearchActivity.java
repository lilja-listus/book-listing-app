package com.example.lilja.booklistingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends Activity {
    public String searchKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchByTitle = (EditText) findViewById(R.id.search_keyword);
                searchKeyword = searchByTitle.getText().toString();
                Intent intent = new Intent(SearchActivity.this, BookActivity.class);
                intent.putExtra("SearchKeyword", searchKeyword);
                startActivity(intent);
            }
        });
    }
}
