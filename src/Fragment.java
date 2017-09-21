/**
 * Created by David on 2017-09-21.
 */
public class Fragment {
    private int number;
    private EncodingRate encRate;

    public Fragment(int number, EncodingRate encRate){
        this.number = number;
        this.encRate = encRate;
    }

    public int getRate() {
        return encRate.getKbps();
    }
}
