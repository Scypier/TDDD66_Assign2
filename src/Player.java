import java.util.Vector;
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
    private State currState;
    //Actual available bandwidth
    private int availBandwidth;
    private int time;
    private boolean newFrag;
    private int nextFragNum;


    public Player(int minBuff, int maxBuff, int videoLength) {
        this.minBuff = minBuff;
        this.maxBuff = maxBuff;
        currState = State.DOWNLOADING;
        nextQuality = EncodingRate.ZERO;
        newFrag = true;
        currBuff = 0;
        estBandwidth = 0;
        currDownloaded = 0;
        time = 0;
        maxFragNum = (videoLength*60)/4;
        nextFragNum = 1;
    }

    public void run(Vector<Integer> bandwidth) {
        while(currState!=State.FINISHED) {
            testPrint();
            availBandwidth = bandwidth.get(time);
            if(currState == State.PLAYING || currState == State.PLAYINGDOWNLOADING)
                playSec();
            if (currState == State.DOWNLOADING || currState == State.PLAYINGDOWNLOADING) {
                if(newFrag)
                    beginDownload();
                else
                    downloadSec();
            }
            time++;
            updateState();
        }
    }

    private void testPrint() {
        System.out.println("Current state:    " + currState);
        System.out.println("Current buffer:   " + currBuff);
        System.out.println("Current quality:  " + nextQuality);
        System.out.println("Current time:     " + time);
        System.out.println("Current fragment: " + (nextFragNum-1));
    }

    private void beginDownload() {
        currFrag = new Fragment(nextFragNum, nextQuality, 4);
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
            nextFragNum++;
        } else {
            newFrag = false;
        }
    }

    private void playSec() {
        currBuff--;
    }

    private void estimateBandwidth() {
        //TODO: Calculations here
        //TODO: Set nextQuality
    }

    private void updateState() {
        if(currBuff > 0 && currState != State.DOWNLOADING) {
            if(currBuff <= maxBuff && nextFragNum <= maxFragNum ) {
                currState = State.PLAYINGDOWNLOADING;
            } else {
                currState = State.PLAYING;
            }
        } else if(nextFragNum <= maxFragNum) {
            if(currBuff >= minBuff)
                currState = State.PLAYINGDOWNLOADING;
            else
                currState = State.DOWNLOADING;
        } else {
            currState = State.FINISHED;
        }
    }
}
