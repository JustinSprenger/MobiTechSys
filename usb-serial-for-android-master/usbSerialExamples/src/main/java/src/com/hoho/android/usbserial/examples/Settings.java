package src.com.hoho.android.usbserial.examples;

import java.io.Serializable;

public class Settings implements Serializable {
    private int baudrate;
    private int parity;
    private int stopbit;
    private int databits;
    private String username;

    public Settings(int baudrate, int parity, int stopbit, int databits, String username){
        this.baudrate = baudrate;
        this.parity = parity;
        this.stopbit = stopbit;
        this.databits = databits;
        this.username = username;
    }

    public Settings(){

    }

    public int getBaudrate() {
        return baudrate;
    }

    public int getParity() {
        return parity;
    }

    public int getStopbit() {
        return stopbit;
    }

    public int getDatabits() {
        return databits;
    }

    public String getUsername(){
        return username;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public void setStopbit(int stopbit) {
        this.stopbit = stopbit;
    }

    public void setDatabits(int databits) {
        this.databits = databits;
    }

    public void setUsername(String username){
        this.username = username;
    }
}
