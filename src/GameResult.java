public class GameResult {
    public enum ResultType { WIN, LOSE, BLACKJACK, PUSH }

    private final ResultType type;
    private final double payout; // netto w jednostkach bazowego betu
    private final double betMultiplier; // 1.0 lub 2.0 dla double
    private final boolean isSplitHand;
    private final int handIndex; // 0 dla pierwszej ręki, 1 dla split
    private final boolean isDouble;

    public GameResult(ResultType type, double payout, double betMultiplier, boolean isSplitHand, int handIndex, boolean isDouble) {
        this.type = type;
        this.payout = payout;
        this.betMultiplier = betMultiplier;
        this.isSplitHand = isSplitHand;
        this.handIndex = handIndex;
        this.isDouble = isDouble;
    }

    public static GameResult win(double betMultiplier, boolean isSplitHand, int handIndex, boolean isDouble) {
        return new GameResult(ResultType.WIN, 1.0 * betMultiplier, betMultiplier, isSplitHand, handIndex, isDouble);
    }

    public static GameResult blackjack(double betMultiplier, boolean isSplitHand, int handIndex, boolean isDouble) {
        // blackjack pays 1.5 only if not a split hand (common rule); otherwise treat as normal win
        double payout = isSplitHand ? 1.0 * betMultiplier : 1.5 * betMultiplier;
        ResultType t = ResultType.BLACKJACK;
        return new GameResult(t, payout, betMultiplier, isSplitHand, handIndex, isDouble);
    }

    public static GameResult lose(double betMultiplier, boolean isSplitHand, int handIndex, boolean isDouble) {
        return new GameResult(ResultType.LOSE, -1.0 * betMultiplier, betMultiplier, isSplitHand, handIndex, isDouble);
    }

    public static GameResult push(double betMultiplier, boolean isSplitHand, int handIndex, boolean isDouble) {
        return new GameResult(ResultType.PUSH, 0.0, betMultiplier, isSplitHand, handIndex, isDouble);
    }

    public ResultType getType() { return type; }
    public double getPayout() { return payout; }
    public double getBetMultiplier() { return betMultiplier; }
    public boolean isSplitHand() { return isSplitHand; }
    public int getHandIndex() { return handIndex; }
    public boolean isDouble() { return isDouble; }

    @Override
    public String toString() {
        return "GameResult{" +
                "type=" + type +
                ", payout=" + payout +
                ", betMultiplier=" + betMultiplier +
                ", isSplitHand=" + isSplitHand +
                ", handIndex=" + handIndex +
                ", isDouble=" + isDouble +
                '}';
    }
}