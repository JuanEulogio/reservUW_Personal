package com.cs407.reservuw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class buildingView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_view);

        RecyclerView recyclerView = findViewById(R.id.buildingRecyclerView);

        List<item> items = new ArrayList<>();
        items.add(new item("2029","College Library",1));
        items.add(new item("2028","College Library",2));
        items.add(new item("2026","College Library",3));
        items.add(new item("2025","College Library",4));
        items.add(new item("3029","College Library",5));
        items.add(new item("3029","College Library",6));
        items.add(new item("4029","College Library",7));
        items.add(new item("5029","College Library",8));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BuildingAdapter(getApplicationContext(), items));
    }
}