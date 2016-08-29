package com.shubham.cloudmagictask.models;

import android.text.format.DateFormat;
import android.util.Log;

import com.shubham.cloudmagictask.utilityClass.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Shubham Gupta on 28-08-2016.
 */
public class Message{
    String subject;
    ArrayList<String> participants;
    String preview;
    boolean isRead;
    boolean isStarred;
    long timeStamp;
    String time;
    int id;


    public Message(JSONObject jsonObject){
        try {
            subject = jsonObject.optString(Config.KEY_SUBJECT);

            JSONArray participantsArray = jsonObject.optJSONArray(Config.KEY_PARTICIPANTS);
            participants = new ArrayList<>();
            for(int i=0;i<participantsArray.length();i++){
               participants.add((String) participantsArray.getString(i));
            }

            preview = jsonObject.optString(Config.KEY_PREVIEW);
            isRead = jsonObject.optBoolean(Config.KEY_IS_READ);
            isStarred = jsonObject.optBoolean(Config.KEY_IS_STARRED);
            timeStamp = jsonObject.optLong(Config.KEY_TS);
            String format = checkTime(timeStamp);
            time = getDate(timeStamp,format);
            id = jsonObject.optInt(Config.KEY_ID);


            Log.i(Config.TAG,"TimeStamp:"+timeStamp + "id:" +time);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getSubject() {
        return subject;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public String getPreview() {
        return preview;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    private String getDate(long time,String format) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(time*1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }




        String dateString = DateFormat.format(format, date).toString();
        Log.i(Config.TAG,"dateString:"+dateString);
        return dateString;
    }

    private String checkTime(long timestamp){
        Calendar cald = Calendar.getInstance();


        cald.setTimeInMillis(timestamp*1000);
        int mailYear = cald.get(Calendar.YEAR);
        int mailDate = cald.get(Calendar.DATE);
        int mailMonth = cald.get(Calendar.MONTH);

        cald.setTimeInMillis(System.currentTimeMillis());
        int curYear = cald.get(Calendar.YEAR);
        int curDate = cald.get(Calendar.DATE);
        int curMonth = cald.get(Calendar.MONTH);

        if(mailYear == curYear){
            if(mailMonth == curMonth){
                if(mailDate == curDate){
                    return "HH:mm";
                }else{
                    return "dd/MM";
                }
            }else{
                    return "dd/MM";
            }
        }else{
            return "dd/MM/yy";
        }

       /* Log.i(Config.TAG,cald.get(Calendar.YEAR)+":year");
        Log.i(Config.TAG,cald.get(Calendar.DATE)+":date");
        Log.i(Config.TAG,cald.get(Calendar.MONTH)+"");


        Log.i(Config.TAG,cald.get(Calendar.YEAR)+":year");
        Log.i(Config.TAG,cald.get(Calendar.DATE)+":date");
        Log.i(Config.TAG,cald.get(Calendar.MONTH)+"");*/
    }
}
