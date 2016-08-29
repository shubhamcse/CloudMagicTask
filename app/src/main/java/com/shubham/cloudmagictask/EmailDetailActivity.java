package com.shubham.cloudmagictask;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.shubham.cloudmagictask.models.MessageDetails;
import com.shubham.cloudmagictask.utilityClass.Config;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Shubham Gupta on 29-08-2016.
 */
@EActivity(R.layout.email_detail_activity)
public class EmailDetailActivity extends AppCompatActivity{
    @ViewById(R.id.textview_subject)
    TextView subject;
    @ViewById(R.id.textview_participants)
    TextView participants;
    @ViewById(R.id.textview_emailSender)
    TextView emailSender;
    @ViewById(R.id.imageview_isStarred)
    ImageView isStarred;
    @ViewById(R.id.textview_time)
    TextView time;
    @ViewById(R.id.emailBody)
    TextView emailBody;


    @AfterViews
    void init(){
        Bundle bundle = getIntent().getExtras();
        final int id = bundle.getInt("id");

     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            MyApplication.initCache(this.getExternalCacheDir());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        request(id);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void request(int id) throws Exception {
        Request request = new Request.Builder()
                .url(Config.URL_MESSAGES+id)
                .build();

        Response response = MyApplication.client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        //  Log.i(Config.TAG,"response:"+response.body().string());
        String jsonData = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonData);
        Log.i(Config.TAG,"jsonObject:" +"sub: " + jsonObject.getString("subject"));

        MessageDetails messageDetails = new MessageDetails(jsonObject);
        updateUI(messageDetails);

    }

    @UiThread
    void updateUI(MessageDetails messageDetails){
        subject.setText(messageDetails.getSubject());
        Log.i(Config.TAG,"participantsSize:"+messageDetails.getParticipants_name().size());
        for(int i=0;i<messageDetails.getParticipants_name().size();i++) {
            participants.setText(participants.getText()+messageDetails.getParticipants_name().get(i));
            if(i+1!=messageDetails.getParticipants_name().size()) {
                Log.i(Config.TAG,"participantsSize:"+i);
                participants.setText(participants.getText() + ", ");
            }
        }
        participants.setText(participants.getText()+" & me");
        emailSender.setText(messageDetails.getParticipants_name().get(0));
        time.setText(messageDetails.getTime());
        if(messageDetails.isStarred())
           isStarred.getDrawable().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.starredEmailColor), PorterDuff.Mode.SRC_ATOP);
        else
            isStarred.getDrawable().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.unstarredEmailColor), PorterDuff.Mode.SRC_ATOP);
        emailBody.setText(messageDetails.getBody());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
