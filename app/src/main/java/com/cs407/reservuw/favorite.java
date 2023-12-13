package com.cs407.reservuw;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class favorite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        List<item> items = new ArrayList<>();
        items.add(new item("2029","College Library",1));
        items.add(new item("2028","College Library",2));
        items.add(new item("2026","College Library",3));
        items.add(new item("2025","College Library",4));
        items.add(new item("3029","College Library",5));
        items.add(new item("3029","College Library",6));
        items.add(new item("4029","College Library",7));
        items.add(new item("5029","College Library",8));
        items.add(new item("15","Steenbock Library",9));
        items.add(new item("17B","Union South",10));
        items.add(new item("2029","College Library",11));
        items.add(new item("2028","College Library",12));
        items.add(new item("2026","College Library",13));
        items.add(new item("2025","College Library",14));
        items.add(new item("3029","College Library",15));
        items.add(new item("3029","College Library",16));
        items.add(new item("4029","College Library",17));
        items.add(new item("5029","College Library",18));
        items.add(new item("15","Steenbock Library",20));
        items.add(new item("17B","Union South",21));



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), items));

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
