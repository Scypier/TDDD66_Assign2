public enum EncodingRate {
    ZERO(250000),ONE(500000),TWO(850000),THREE(1300000);

    //The actual encoding rate expressed in bits per second
    private final int bps;

    EncodingRate(int bps) {
        this.bps = bps;
    }

    public int getBps() {
        return bps;
    }

    /**
     * Returns the highest encoding quality a fragment can have given the maximum allowed size of the fragment,
     * the length of the fragment and the quality of the last fragment downloaded.
     * @param fragSize The maximum allowed size of the new fragment
     * @param fragLength The length of the fragment
     * @param currQuality The quality of the last fragment downloaded
     * @return The highest allowed quality for the next fragment
     */
    public static EncodingRate getHighestRate(double fragSize, int fragLength, EncodingRate currQuality) {
        EncodingRate highest = ZERO;
        int ordinalDiff;
        if(currQuality == THREE)
            highest = ONE;

        for (EncodingRate rate : values()) {
            ordinalDiff = valueOf(rate.toString()).ordinal() - valueOf(currQuality.toString()).ordinal();
            if(rate.getBps() * fragLength <= fragSize && ordinalDiff >= -2 && ordinalDiff <= 1)
                highest = rate;
        }
        return highest;
    }
}
