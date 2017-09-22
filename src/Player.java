/**
 * Created by David on 2017-09-22.
 */
public class Player {
    private final int minBuff;
    private final int maxBuff;
    private int estBandwidth;
    private EncodingRate nextQuality;
    private int currBuff;
    private int nextFragNum;

    public Player(int minBuff, int maxBuff) {
        this.minBuff = minBuff;
        this.maxBuff = maxBuff;
        nextQuality = EncodingRate.ZERO;
        currBuff = 0;
        estBandwidth = 0;
        nextFragNum = 0;
    }

    public void start() {
        downloadNextFrag();
    }
    
    private void downloadNextFrag() {
        download(new Fragment(nextFragNum, nextQuality));
        nextFragNum++;
        nextQuality = estimateBandwidth();
    }

    private void download(Fragment frag) {

    }

    private EncodingRate estimateBandwidth() {
        return EncodingRate.ZERO;
    }
}
