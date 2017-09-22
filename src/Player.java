/**
 * Created by David on 2017-09-22.
 */
public class Player {
    private final int minBuff;
    private final int maxBuff;
    //The current estimated available bandwidth;
    private int estBandwidth;
    //The encoding rate the player wants the next fragment at
    private EncodingRate nextQuality;
    //The current buffer size
    private int currBuff;
    //The number of the next fragment
    private int nextFragNum;
    //The current fragment being downloaded
    private Fragment currFrag;
    //The maximum number of fragments
    private int maxFragNum;


    public Player(int minBuff, int maxBuff, int videoLength) {
        this.minBuff = minBuff;
        this.maxBuff = maxBuff;
        nextQuality = EncodingRate.ZERO;
        currBuff = 0;
        estBandwidth = 0;
        nextFragNum = 0;
        maxFragNum = (videoLength*60)/4;
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
