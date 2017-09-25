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

    public EncodingRate getHighestRate(int fragSize, int fragLength) {
        EncodingRate highest = EncodingRate.ZERO;
        for (EncodingRate rate : EncodingRate.values()) {
            if(rate.getBps()*fragLength <= fragSize) {
                highest = rate;
            } else {
                return highest;
            }
        }
        return highest;
    }
}
