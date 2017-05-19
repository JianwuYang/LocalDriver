package com.vikingyang.localdriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vikingyang.localdriver.gson.ListDetail;
import com.vikingyang.localdriver.util.ScrollViewWithListView;

import org.w3c.dom.Text;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private TextView textView;

    private ScrollViewWithListView scrollViewWithListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        textView = (TextView)findViewById(R.id.title_city);
        scrollViewWithListView = (ScrollViewWithListView)findViewById(R.id.result_view_2);
        Intent intent = getIntent();
        ListDetail listDetail = (ListDetail) intent.getSerializableExtra("list");
        showOnListView(listDetail);
    }

    private void showOnListView(ListDetail listDetail){
        String title = listDetail.getTitle().split("\\(",0)[0];
        List<String> details = listDetail.getList();
        textView.setText(title);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ListActivity.this, android.R.layout.simple_list_item_1, details);
        scrollViewWithListView.setAdapter(adapter);
    }

}
