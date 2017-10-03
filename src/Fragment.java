public class Fragment {
    //The number of the fragment
    private final int number;
    //The encoding rate of the fragment
    private final EncodingRate encRate;
    //The length of the fragment
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

    /**
     * Calculates the actual size of the fragment
     * @return The actual size of the fragment
     */
    public int getSize() {return getRate()*getLength();}
}
