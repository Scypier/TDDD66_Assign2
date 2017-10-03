import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
public class Player {
    private final int minBuff;
    private final int maxBuff;
    //The current estimated available bandwidth;
    private double oldEstBandwidth;
    private int newEstBandwidth;
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
    private int downloadTime;
    private double EWMA;
    private int fragLength;
    private Queue<EncodingRate> buffQual;
    private EncodingRate playQual;


    public Player(int minBuff, int maxBuff, int videoLength) {
        this.minBuff = minBuff;
        this.maxBuff = maxBuff;
        currState = State.DOWNLOADING;
        nextQuality = EncodingRate.ZERO;
        fragLength = 4;
        currFrag = new Fragment(0, nextQuality, fragLength);
        newFrag = true;
        currBuff = 0;
        oldEstBandwidth = 0;
        newEstBandwidth = 0;
        currDownloaded = 0;
        time = 0;
        EWMA = 1;
        maxFragNum = (videoLength*60)/fragLength;
        nextFragNum = 1;
        downloadTime = 0;
        buffQual = new LinkedList<>();
        playQual = EncodingRate.ZERO;
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
        //System.out.println("Current state:           " + currState);
        //System.out.println("Current buffer:          " + currBuff);
        //System.out.println("Current quality:         " + currFrag.getRateEnum());
        //System.out.println("Current time:            " + time);
        //System.out.println("Current fragment:        " + currFrag.getNumber());
        //System.out.println("Current playing quality: " + playQual);
        //System.out.println(time + " " + currBuff);
        //System.out.println(time + " " + EncodingRate.valueOf(playQual.toString()).ordinal());
    }

    private void beginDownload() {
        currFrag = new Fragment(nextFragNum, nextQuality, fragLength);
        //System.out.println(time + " " + EncodingRate.valueOf(currFrag.getRateEnum().toString()).ordinal());
        downloadSec();
    }

    private void downloadSec() {
        currDownloaded += availBandwidth;
        downloadTime++;
        if(currDownloaded >= currFrag.getSize()) {
            currBuff += currFrag.getLength();
            for(int i = 0; i < currFrag.getLength(); i++) {
                buffQual.add(currFrag.getRateEnum());
            }
            currDownloaded = 0;
            estimateBandwidth();
            newFrag = true;
            nextFragNum++;
            downloadTime = 0;
        } else {
            newFrag = false;
        }
    }

    private void playSec() {
        currBuff--;
        playQual = buffQual.remove();
    }

    private void estimateBandwidth() {
        newEstBandwidth = currFrag.getSize()/downloadTime;
        oldEstBandwidth = ((1-EWMA)*oldEstBandwidth) + (EWMA * newEstBandwidth);
        nextQuality = EncodingRate.getHighestRate(oldEstBandwidth*currBuff,fragLength, nextQuality);
    }

    private void updateState() {
        if(currState == State.PLAYING) {
            if(currBuff < minBuff && nextFragNum <= maxFragNum) {
                currState = State.PLAYINGDOWNLOADING;
            } else if(currBuff == 0 && nextFragNum > maxFragNum) {
                currState = State.FINISHED;
            }
        }
         else if(currState == State.PLAYINGDOWNLOADING) {
            if(currBuff > maxBuff || nextFragNum > maxFragNum) {
                currState = State.PLAYING;
            } else if(currBuff == 0) {
                currState = State.DOWNLOADING;
            }
        }
        else if(currState == State.DOWNLOADING) {
            if(nextFragNum > maxFragNum) {
                currState = State.PLAYING;
            } else if(currBuff >= minBuff) {
                currState = State.PLAYINGDOWNLOADING;
            }
        }
    }
}
