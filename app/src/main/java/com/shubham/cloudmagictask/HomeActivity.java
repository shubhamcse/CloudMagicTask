package com.shubham.cloudmagictask;

import android.content.Intent;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.shubham.cloudmagictask.adapters.MessageListAdapter;
import com.shubham.cloudmagictask.customView.DividerItemDecoration;
import com.shubham.cloudmagictask.models.Message;
import com.shubham.cloudmagictask.utilityClass.Config;
import com.shubham.cloudmagictask.utilityClass.RecyclerItemClickListener;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Shubham Gupta on 28-08-2016.
i */
@EActivity(R.layout.home_activity_layout)
public class HomeActivity extends AppCompatActivity{
    @ViewById(R.id.recyclerView)
    RecyclerView recyclerView;
    MessageListAdapter messageListAdapter;

    @AfterViews
    void init(){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, this.getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha),
                        true, true, R.color.dividerColor));
        recyclerView.setLayoutManager(llm);
        onClickRecyclerView(recyclerView);

        try {
            MyApplication.initCache(this.getExternalCacheDir());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        request();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void request() throws Exception {
        Request request = new Request.Builder()
                .url(Config.URL_MESSAGES)
                .build();

        Response response = MyApplication.client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
      //  Log.i(Config.TAG,"response:"+response.body().string());
        String jsonData = response.body().string();
       JSONArray jsonArray = new JSONArray(jsonData);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        Log.i(Config.TAG,"jsonObject:" +jsonArray.length()+"sub: " + jsonObject.getString("subject"));


        ArrayList<Message> messages = new ArrayList();

        for (int i = 0; i < jsonArray.length(); i++) {
            Message message = new Message(jsonArray.getJSONObject(i));
            messages.add(message);
        }
        setAdapter(messages);
    }

    @UiThread
    void setAdapter(ArrayList<Message> messages){
        messageListAdapter = new MessageListAdapter();
        recyclerView.setAdapter(messageListAdapter);
        messageListAdapter.messages = messages;
        messageListAdapter.notifyDataSetChanged();
    }
    public void onClickRecyclerView(final RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        /**/
                        Intent intent = new Intent(HomeActivity.this, EmailDetailActivity_.class);
                        intent.putExtra("id",messageListAdapter.messages.get(position).getId());
                        startActivity(intent);
                    }
                })
        );
    }



}
