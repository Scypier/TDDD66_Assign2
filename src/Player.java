import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
public class Player {
    //The minimum buffer size
    private final int minBuff;
    //The maximum buffer size
    private final int maxBuff;
    //The current estimated available bandwidth;
    private double oldEstBandwidth;
    //The new estimated bandwidth when calculated
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
    //For how long the player has been running
    private int time;
    //True if we should download a new fragment, else false
    private boolean newFrag;
    //The  umber of the next fragment
    private int nextFragNum;
    //The duration of the last/current download
    private int downloadTime;
    //The EWMA alpha value
    private double EWMA;
    //The fragments length for the video
    private int fragLength;
    //The bufferqueue for the qualities
    private Queue<EncodingRate> buffQual;
    //The current quality being played
    private EncodingRate playQual;


    /**
     * Creates a new player object with minBuff and maxBuff as provided, ready to download and play a video of length videoLenght;
     * @param minBuff The minimum buffer size of the player
     * @param maxBuff The maximum buffer size of the player
     * @param videoLength The length in minutes of the video to be streamed
     */
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

    /**
     * Runs the simulation with the provided bandwidths
     * @param bandwidth The bandwidths in chronological order in bits per second
     */
    public void run(Vector<Integer> bandwidth) {
        while(currState!=State.FINISHED) {
            //testPrint();
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

    /**
     * Prints information about the player every tick of the simulation
     */
    private void testPrint() {
        System.out.println("Current state:           " + currState);
        System.out.println("Current buffer:          " + currBuff);
        System.out.println("Current quality:         " + currFrag.getRateEnum());
        System.out.println("Current time:            " + time);
        System.out.println("Current fragment:        " + currFrag.getNumber());
        System.out.println("Current playing quality: " + playQual);
        //System.out.println(time + " " + currBuff);
        //System.out.println(time + " " + EncodingRate.valueOf(playQual.toString()).ordinal());
    }

    /**
     * Starts download of a new fragment and downloads it for one second
     */
    private void beginDownload() {
        currFrag = new Fragment(nextFragNum, nextQuality, fragLength);
        //System.out.println(time + " " + EncodingRate.valueOf(currFrag.getRateEnum().toString()).ordinal());
        downloadSec();
    }

    /**
     * Downloads one second of the current fragment.
     * If the download finishes it initiates a new estimation of the bandwidth.
     */
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

    /**
     * Plays one second of the buffer
     */
    private void playSec() {
        currBuff--;
        playQual = buffQual.remove();
    }

    /**
     * Estimates a new bandwidth using the size and download time of the last fragment.
     */
    private void estimateBandwidth() {
        newEstBandwidth = currFrag.getSize()/downloadTime;
        oldEstBandwidth = ((1-EWMA)*oldEstBandwidth) + (EWMA * newEstBandwidth);
        nextQuality = EncodingRate.getHighestRate(oldEstBandwidth*currBuff,fragLength, nextQuality);
    }

    /**
     * Updates the state of the player
     */
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
