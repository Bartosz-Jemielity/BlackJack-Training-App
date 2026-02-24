public class HiLoCounter {
    private int runningCount = 0;

    public void reset() {
        runningCount = 0;
    }

    public void update(Card card) {
        if (card == null) return;
        runningCount += card.getHiLoValue();
    }

    public int getRunningCount() {
        return runningCount;
    }

    // true count = runningCount / decksRemaining (rounded to one decimal)
    public double getTrueCount(double decksRemaining) {
        if (decksRemaining <= 0) return runningCount;
        return runningCount / decksRemaining;
    }
}
