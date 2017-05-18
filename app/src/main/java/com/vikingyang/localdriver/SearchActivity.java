package com.vikingyang.localdriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.show.api.ShowApiRequest;
import com.vikingyang.localdriver.gson.BusLine;
import com.vikingyang.localdriver.gson.BusLineDetil;
import com.vikingyang.localdriver.util.HttpUtil;
import com.vikingyang.localdriver.util.ScrollViewWithListView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SearchActivity extends AppCompatActivity {

    private TextView cityNameView;

    private String cityName;

    private ScrollViewWithListView scrollViewWithListView;

    private EditText editText;

    ArrayAdapter<String> adapter;

    Handler mHandler = new Handler();

    private List<String> contentList;

    private FloatingActionButton floatingActionButton;

    private ProgressDialog progressDialog;

    private RadioGroup radioGroup;

    private int checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        cityNameView = (TextView) findViewById(R.id.title_city);
        scrollViewWithListView = (ScrollViewWithListView) findViewById(R.id.result_list);
        editText = (EditText) findViewById(R.id.search_content);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroupId);
        contentList = new ArrayList<>();

        adapter=new ArrayAdapter<String>(SearchActivity.this,android.R.layout.simple_list_item_1,contentList);
        scrollViewWithListView.setAdapter(adapter);

        Intent intent = getIntent();
        cityName = intent.getStringExtra("city");
        cityNameView.setText(cityName);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.radioId1){
                    checked = 1;
                    Toast.makeText(SearchActivity.this,"当前选中查询条件为路线",Toast.LENGTH_SHORT).show();
                }else if(checkedId == R.id.radioId2){
                    checked = 2;
                    Toast.makeText(SearchActivity.this,"当前选中查询条件为站台",Toast.LENGTH_SHORT).show();
                }
            }
        });

        floatingActionButton = (FloatingActionButton) findViewById(R.id.search_bus);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                if(checked == 1){
                    sendForBusLineRequest();
                }else if(checked == 2){

                }

            }
        });
    }


    /**
     *通过路线来查询信息
     */
    public void sendForBusLineRequest() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(date);
        String address = "https://route.showapi.com/844-2?busNo="+editText.getText().toString()+"&city="+cityName+"&showapi_appid=38394&showapi_test_draft=false&showapi_timestamp="+timestamp+"&showapi_sign=76a067eab2964b0482e08229436abc99";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                BusLine busLine = gson.fromJson(response.body().string(),BusLine.class);
                contentList.clear();
                for(BusLineDetil detil : busLine.getBusLineBody().getRetList()){
                    contentList.add(detil.getName());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        scrollViewWithListView.setSelection(0);
                        closeProgressDialog();
                    }
                });

            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(SearchActivity.this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
