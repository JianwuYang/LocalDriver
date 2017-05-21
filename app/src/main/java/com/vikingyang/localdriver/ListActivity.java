package com.vikingyang.localdriver;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vikingyang.localdriver.gson.ListDetail;
import com.vikingyang.localdriver.util.ScrollViewWithListView;

import org.w3c.dom.Text;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private TextView textView;

    private ScrollViewWithListView scrollViewWithListView;

    private String busLine;

    private String cityName;

    private int checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        textView = (TextView)findViewById(R.id.title_city);
        scrollViewWithListView = (ScrollViewWithListView)findViewById(R.id.result_view_2);
        Intent intent = getIntent();
        ListDetail listDetail = (ListDetail) intent.getSerializableExtra("list");
        checked = intent.getIntExtra("type",0);
        cityName = intent.getStringExtra("cityName");
        showOnListView(listDetail);

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.search_bus);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ListActivity.this,MapActivity.class);
                intent1.putExtra("cityName",cityName);
                intent1.putExtra("busLine",busLine);
                intent1.putExtra("type",checked);
                startActivity(intent1);
            }
        });

    }

    private void showOnListView(ListDetail listDetail){
        String title = listDetail.getTitle().split("\\(",0)[0];
        busLine = title;
        List<String> details = listDetail.getList();
        textView.setText(title);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ListActivity.this, android.R.layout.simple_list_item_1, details);
        scrollViewWithListView.setAdapter(adapter);
    }

}
