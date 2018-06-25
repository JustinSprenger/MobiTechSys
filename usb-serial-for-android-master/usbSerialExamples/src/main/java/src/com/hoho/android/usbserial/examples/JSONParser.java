package src.com.hoho.android.usbserial.examples;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JSONParser {

    JSONObject reader;
    JSONObject settings = null;
    String in;

    public JSONParser(Context context, String sets){
        try {
            AssetManager mngr = context.getAssets();
            InputStream input = mngr.open("settings.json");
            int size = input.available();
            byte[] bytebuffer = new byte[size];
            input.read();
            input.close();

            in = new String(bytebuffer, "UTF8");
            JSONArray jsonArray = new JSONArray(in);


            for (int i = 0;i<jsonArray.length();i++){
                reader = jsonArray.getJSONObject(i);
            }

            reader = new JSONObject(in);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getBaudrate(){
        String baud = null;
        try {
            settings = reader.getJSONObject("settings");
            baud = settings.getString("baudrate");
        }catch (Exception e){
            e.printStackTrace();
        }
        return baud;
    }

    public String getDataBit(){
        String databit = null;
        try {
            settings = reader.getJSONObject("settings");
            databit = settings.getString("username");
        }catch (Exception e){
            e.printStackTrace();
        }
        return databit;
    }

    public String getStartStop(){
        String startstop = null;
        try {
            settings = reader.getJSONObject("settings");
            startstop = settings.getString("username");
        }catch (Exception e){
            e.printStackTrace();
        }
        return startstop;
    }

    public String getParity(){
        String parity = null;
        try {
            settings = reader.getJSONObject("settings");
            parity = settings.getString("username");
        }catch (Exception e){
            e.printStackTrace();
        }
        return parity;
    }

    public String getUsername(){
        String user = null;
        try {
            settings = reader.getJSONObject("settings");
            user = settings.getString("username");
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public void setSettings(JSONObject settings){

    }

    public void setBaudrate(int baudrate){
        try {
            settings.put("baudrate", baudrate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDataBit(int databit){
        try {
            settings.put("databit", databit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setStartStop(int startstop){
        try {
            settings.put("startstop", startstop);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setParity(int parity){
        try {
            settings.put("parity", parity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username){
        try {
            settings.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
