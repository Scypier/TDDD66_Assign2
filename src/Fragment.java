public class Fragment {
    private final int number;
    private final EncodingRate encRate;
    private final int length;

    public Fragment(int number, EncodingRate encRate, int length){
        this.number = number;
        this.encRate = encRate;
        this.length = length;
    }

    public int getNumber() {
        return number;
    }
    public int getRate() {
        return encRate.getBps();
    }
    public int getLength() { return length; }
    public EncodingRate getRateEnum() { return encRate;}
    public int getSize() {return getRate()*getLength();}
}
