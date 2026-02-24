import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card c) {
        if (c != null) cards.add(c);
    }

    public List<Card> getCards() {
        return cards;
    }

    public int bestValue() {
        int total = 0;
        int aces = 0;
        for (Card c : cards) {
            int v = c.getBlackjackValue();
            if (v == 11) aces++;
            total += v;
        }
        // reduce aces from 11 to 1 as needed
        while (total > 21 && aces > 0) {
            total -= 10; // 11 -> 1
            aces--;
        }
        return total;
    }

    public boolean isBusted() {
        return bestValue() > 21;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && bestValue() == 21;
    }

    public boolean isPair() {
        if (cards.size() != 2) return false;
        return cards.get(0).getRank() == cards.get(1).getRank();
    }

    public void clear() {
        cards.clear();
    }

    @Override
    public String toString() {
        return cards.toString() + " (" + bestValue() + ")";
    }
}
