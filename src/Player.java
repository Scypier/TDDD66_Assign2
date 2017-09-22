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
    private int nextFragNum;


    public Player(int minBuff, int maxBuff, int videoLength) {
        this.minBuff = minBuff;
        this.maxBuff = maxBuff;
        currentState = State.DOWNLOADING;
        nextQuality = EncodingRate.ZERO;
        newFrag = true;
        currBuff = 0;
        estBandwidth = 0;
        time = 0;
        maxFragNum = (videoLength*60)/4;
        nextFragNum = 1;
    }

    public void run(Vector<Integer> bandwidth) {
        while(currentState!=State.FINISHED) {
            availBandwidth = bandwidth.get(time);
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
        if(currFrag.getNumber()+1 <= maxFragNum) {
            currFrag = new Fragment(currFrag.getNumber(), nextQuality, 4);
            downloadSec();
        }
    }

    private void downloadSec() {
        currDownloaded += availBandwidth;
        //TODO: Record statistics about the download that can be used to estimate bandwidth
        if(currDownloaded >= currFrag.getRate()*currFrag.getLength()) {
            currBuff += currFrag.getLength();
            currDownloaded = 0;
            estimateBandwidth();
            newFrag = true;
            nextFragNum++;
        } else {
            newFrag = false;
        }
    }

    private void playSec() {
        currBuff--;
    }

    private EncodingRate estimateBandwidth() {
        //TODO: Calculations here
        estBandwidth = 0;
        return EncodingRate.ZERO;
    }

    private void updateState() {
        if(currBuff > 0) {
            if(currBuff <= maxBuff && nextFragNum <= maxFragNum ) {
                currentState = State.PLAYINGDOWNLOADING;
            } else {
                currentState = State.PLAYING;
            }
        } else if(nextFragNum <= maxFragNum) {
            currentState = State.DOWNLOADING;
        } else {
            currentState = State.FINISHED;
        }
    }
}
