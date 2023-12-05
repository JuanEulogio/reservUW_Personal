package com.cs407.reservuw;

import android.os.Bundle;

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
        items.add(new item("2029","College Library"));
        items.add(new item("2028","College Library"));
        items.add(new item("2026","College Library"));
        items.add(new item("2025","College Library"));
        items.add(new item("3029","College Library"));
        items.add(new item("3029","College Library"));
        items.add(new item("4029","College Library"));
        items.add(new item("5029","College Library"));
        items.add(new item("15","Steenbock Library"));
        items.add(new item("17B","Union South"));
        items.add(new item("2029","College Library"));
        items.add(new item("2028","College Library"));
        items.add(new item("2026","College Library"));
        items.add(new item("2025","College Library"));
        items.add(new item("3029","College Library"));
        items.add(new item("3029","College Library"));
        items.add(new item("4029","College Library"));
        items.add(new item("5029","College Library"));
        items.add(new item("15","Steenbock Library"));
        items.add(new item("17B","Union South"));



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), items));
    }

}
