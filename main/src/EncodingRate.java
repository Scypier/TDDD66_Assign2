import com.sun.media.jfxmedia.track.Track;

/**
 * Created by David on 2017-09-21.
 */
public enum EncodingRate {
    ZERO(250),ONE(500),TWO(850),THREE(1300);

    private final int kbps;

    EncodingRate(int kbps) {
        this.kbps = kbps;
    }
}
