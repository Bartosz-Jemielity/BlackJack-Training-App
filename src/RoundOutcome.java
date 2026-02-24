import java.util.ArrayList;
import java.util.List;

public class RoundOutcome {
    private final List<GameResult> handResults = new ArrayList<>();

    public RoundOutcome() {}

    public void addHandResult(GameResult r) {
        handResults.add(r);
    }

    public List<GameResult> getHandResults() {
        return handResults;
    }

    public double totalPayout() {
        double s = 0.0;
        for (GameResult r : handResults) s += r.getPayout();
        return s;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RoundOutcome:\n");
        for (GameResult r : handResults) {
            sb.append("  ").append(r).append("\n");
        }
        sb.append("Total payout: ").append(totalPayout());
        return sb.toString();
    }
}

