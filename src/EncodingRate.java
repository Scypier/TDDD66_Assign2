import com.sun.media.jfxmedia.track.Track;

/**
 * Created by David on 2017-09-21.
 */
public enum EncodingRate {
    //Encoding rates expressed in bits per second
    ZERO(250000),ONE(500000),TWO(850000),THREE(1300000);

    private final int bps;

    EncodingRate(int bps) {
        this.bps = bps;
    }

    public int getBps() {
        return bps;
    }
}
