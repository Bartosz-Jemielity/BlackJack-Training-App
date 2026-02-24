public class Card {
    private final int rank;
    private final Suit suit;

    public Card(int rank, Suit suit) {
        if (rank < 1 || rank > 13) throw new IllegalArgumentException("rank must be 1..13");
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }


    public int getBlackjackValue() {
        if (rank == 1) return 11;
        if (rank >= 10) return 10;
        return rank;
    }

    // Hi-Lo
    public int getHiLoValue() {
        if (rank >= 2 && rank <= 6) return 1;
        if (rank >= 7 && rank <= 9) return 0;
        return -1;
    }

    @Override
    public String toString() {
        String r;
        switch (rank) {
            case 1: r = "A"; break;
            case 11: r = "J"; break;
            case 12: r = "Q"; break;
            case 13: r = "K"; break;
            default: r = Integer.toString(rank);
        }
        return r + " of " + suit.name();
    }
}
