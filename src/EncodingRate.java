public enum EncodingRate {
    //Encoding rates expressed in bits per second
    ZERO(250000),ONE(500000),TWO(850000),THREE(1300000);

    private final int bps;

    EncodingRate(int bps) {
        this.bps = bps;
    }

    public int getBps() {
        return bps;
    }

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
