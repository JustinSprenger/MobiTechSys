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

    public JSONParser(Context context){
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

    public int getBaudrate(){
        int baud = 0;
        try {
            settings = reader.getJSONObject("settings");
            baud = settings.getInt("baudrate");
        }catch (Exception e){
            e.printStackTrace();
        }
        return baud;
    }

    public int getDataBit(){
        int databit = 0;
        try {
            settings = reader.getJSONObject("settings");
            databit = settings.getInt("databit");
        }catch (Exception e){
            e.printStackTrace();
        }
        return databit;
    }

    public int getStartStop(){
        int startstop = 0;
        try {
            settings = reader.getJSONObject("settings");
            startstop = settings.getInt("startstop");
        }catch (Exception e){
            e.printStackTrace();
        }
        return startstop;
    }

    public int getParity(){
        int parity = 0;
        try {
            settings = reader.getJSONObject("settings");
            parity = settings.getInt("parity");
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
            user = user + "hat geklappt";
        }catch (Exception e){
            e.printStackTrace();
            user = "Exception";
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
