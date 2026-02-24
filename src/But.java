import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class But {
    private final List<Card> cards = new ArrayList<>();
    private final int deckCount;
    private final Random rnd = new Random();

    public But(int deckCount) {
        if (deckCount < 1) throw new IllegalArgumentException("deckCount >= 1");
        this.deckCount = deckCount;
        resetAndShuffle();
    }

    private void buildDecks() {
        cards.clear();
        for (int d = 0; d < deckCount; d++) {
            for (Suit suit : Suit.values()) {
                for (int rank = 1; rank <= 13; rank++) {
                    cards.add(new Card(rank, suit));
                }
            }
        }
    }

    public void resetAndShuffle() {
        buildDecks();
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards, rnd);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card dealOne() {
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }

    public int remainingCards() {
        return cards.size();
    }

    public double remainingDecks() {
        return cards.size() / 52.0;
    }
}
