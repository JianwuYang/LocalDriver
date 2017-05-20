package com.vikingyang.localdriver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vikingyang.localdriver.gson.BusLine;
import com.vikingyang.localdriver.gson.BusLineDetil;
import com.vikingyang.localdriver.gson.ListDetail;
import com.vikingyang.localdriver.gson.Station;
import com.vikingyang.localdriver.gson.StationDetail;
import com.vikingyang.localdriver.util.HttpUtil;
import com.vikingyang.localdriver.util.ScrollViewWithListView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SearchActivity extends AppCompatActivity {

    private TextView cityNameView;

    private String cityName;

    private ScrollViewWithListView scrollViewWithListView;

    private EditText editText;

    ArrayAdapter<String> adapter;

    private List<String> contentList;

    private Map<String,String> contentMap;

    private FloatingActionButton floatingActionButton;

    private Button routePlanButton;

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
        contentMap = new HashMap<>();

        adapter=new ArrayAdapter<String>(SearchActivity.this,android.R.layout.simple_list_item_1,contentList);
        scrollViewWithListView.setAdapter(adapter);


        Intent intent = getIntent();
        cityName = intent.getStringExtra("city");
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(cityName==null){
            cityName = prefs.getString("city",null);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("city",cityName);
        editor.apply();

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
                hintKb();
                showProgressDialog();
                if(checked == 1){
                    sendForBusLineRequest();
                }else if(checked == 2){
                    sendForStationRequest();
                }

            }
        });

        scrollViewWithListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = contentList.get(position);
                String detail = contentMap.get(item);
                ListDetail listDetail = new ListDetail();
                listDetail.setList(convertString2List(detail));
                listDetail.setTitle(item);
                Intent intent = new Intent(SearchActivity.this,ListActivity.class);
                intent.putExtra("cityName",cityName);
                intent.putExtra("list",listDetail);
                startActivity(intent);
            }
        });

        routePlanButton = (Button)findViewById(R.id.routePlan_button);
        routePlanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SearchActivity.this,RoutePlanAcivity.class);
                intent2.putExtra("cityName",cityName);
                startActivity(intent2);
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
                contentMap.clear();
                for(BusLineDetil detil : busLine.getBusLineBody().getRetList()){
                    contentList.add(detil.getName());
                    contentMap.put(detil.getName(),detil.getContent());
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
     * 通过站台来查询信息
     */
    public void sendForStationRequest() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(date);
        String address = "https://route.showapi.com/844-1?city="+cityName+"&showapi_appid=38394&showapi_test_draft=false&showapi_timestamp="+timestamp+"&station="+editText.getText().toString()+"&showapi_sign=76a067eab2964b0482e08229436abc99";
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                Station station = gson.fromJson(response.body().string(),Station.class);
                contentList.clear();
                contentMap.clear();
                for(StationDetail detail : station.getStationBody().getRetList()){
                    contentList.add(detail.getName());
                    contentMap.put(detail.getName(),detail.getLineNames());
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

    private List<String> convertString2List(String str){
        String[] strs = str.split(";",0);
        return Arrays.asList(strs);
    }

    /**
     * 关闭软键盘
     */
    private void hintKb() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
