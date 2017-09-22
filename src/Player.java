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
    //The current fragment being downloaded
    private Fragment currFrag;
    //The maximum number of fragments
    private int maxFragNum;
    //The amount of bits downloaded of the current fragment
    private int currDownloaded;
    //The current state of the player deciding what actions to take
    private State currentState;


    public Player(int minBuff, int maxBuff, int videoLength) {
        this.minBuff = minBuff;
        this.maxBuff = maxBuff;
        currFrag = new Fragment(0, EncodingRate.ZERO); // Dummy to download first fragment
        nextQuality = EncodingRate.ZERO;
        currBuff = 0;
        estBandwidth = 0;
        maxFragNum = (videoLength*60)/4;
    }

    public void start() {
        downloadFrag();
        while(downloadSec());
    }

    private void downloadFrag() {
        Fragment fragment = new Fragment(currFrag.getNumber()+1, nextQuality);
        downloadSec(fragment);
    }

    private boolean downloadSec(Fragment frag) {
        //TODO: Decide how to implement downloading

        return false;
    }

    private EncodingRate estimateBandwidth() {
        //TODO: Calculations here
        estBandwidth = 0;
        return EncodingRate.ZERO;
    }


}
