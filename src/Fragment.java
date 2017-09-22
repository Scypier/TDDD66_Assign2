/**
 * Created by David on 2017-09-21.
 */
public class Fragment {
    private final int number;
    private final EncodingRate encRate;

    public Fragment(int number, EncodingRate encRate){
        this.number = number;
        this.encRate = encRate;
    }
    public Fragment(int number){
        this.number = number;
        this.encRate = EncodingRate.ZERO;
    }

    public int getNumber() {
        return number;
    }
    public int getRate() {
        return encRate.getKbps();
    }
}
