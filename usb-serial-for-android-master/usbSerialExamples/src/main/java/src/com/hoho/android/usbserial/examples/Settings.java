package src.com.hoho.android.usbserial.examples;

public class Settings {
    private int baudrate;
    private int parity;
    private int stopbit;
    private int databits;

    public Settings(int baudrate, int parity, int stopbit, int databits){
        this.baudrate = baudrate;
        this.parity = parity;
        this.stopbit = stopbit;
        this.databits = databits;
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
}
