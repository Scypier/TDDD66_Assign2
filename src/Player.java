import java.util.Vector;

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
    //Actual available bandwidth
    private int availBandwidth;
    private int time;
    private boolean newFrag;


    public Player(int minBuff, int maxBuff, int videoLength) {
        this.minBuff = minBuff;
        this.maxBuff = maxBuff;
        currFrag = new Fragment(-1, EncodingRate.ZERO, 4);
        currentState = State.DOWNLOADING;
        nextQuality = EncodingRate.ZERO;
        newFrag = true;
        currBuff = 0;
        estBandwidth = 0;
        time = 0;
        maxFragNum = (videoLength*60)/4;
    }

    public void run(Vector bandwidth) {
        while(currentState!=State.FINISHED) {
            if (currentState == State.DOWNLOADING || currentState == State.PLAYINGDOWNLOADING) {
                if(newFrag)
                    beginDownload();
                else
                    downloadSec();
            }
            if(currentState == State.PLAYING || currentState == State.PLAYINGDOWNLOADING)
                playSec();
            time++;
            updateState();
        }
    }

    private void beginDownload() {
        currFrag = new Fragment(currFrag.getNumber()+1, nextQuality, 4);
        downloadSec();
    }

    private void downloadSec() {
        currDownloaded += availBandwidth;
        //TODO: Record statistics about the download that can be used to estimate bandwidth
        if(currDownloaded >= currFrag.getRate()*currFrag.getLength()) {
            currBuff += currFrag.getLength();
            currDownloaded = 0;
            estimateBandwidth();
            newFrag = true;
        } else {
            newFrag = false;
        }
    }

    private void playSec() {

    }

    private EncodingRate estimateBandwidth() {
        //TODO: Calculations here
        estBandwidth = 0;
        return EncodingRate.ZERO;
    }

    private void updateState() {
    }
}
