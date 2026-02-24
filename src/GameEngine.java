
import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private final But but;
    private final HiLoCounter counter;

    private final List<Hand> playerHands = new ArrayList<>(2);
    private final Hand dealerHand = new Hand();

    private boolean roundActive = false;
    private boolean hasSplit = false;
    private int activeHandIndex = 0;
    private final double[] handBetMultiplier = new double[] {1.0, 1.0};
    private final boolean[] handDoubled = new boolean[] {false, false};

    //druga karta dealera została policzona przez counter
    private boolean dealerHoleCounted = true;

    public GameEngine(But but, HiLoCounter counter) {
        this.but = but;
        this.counter = counter;
    }

    public boolean isRoundActive() {
        return roundActive;
    }

    public void prepareRound() {
        if (but.remainingDecks() < 0.5) {
            but.resetAndShuffle();
            counter.reset();
        }
        playerHands.clear();
        playerHands.add(new Hand());
        dealerHand.clear();
        roundActive = true;
        hasSplit = false;
        activeHandIndex = 0;
        handBetMultiplier[0] = 1.0; handBetMultiplier[1] = 1.0;
        handDoubled[0] = false; handDoubled[1] = false;
        dealerHoleCounted = false;
    }

    public void startRound() {
        prepareRound();
        dealToPlayerHand(0);
        dealToDealer();
        dealToPlayerHand(0);
        dealToDealerNoCount();
    }
    private Card dealOneAndCount() {
        Card c = but.dealOne();
        counter.update(c);
        return c;
    }

    private Card dealOneNoCount() {
        return but.dealOne();
    }

    public void dealToPlayerHand(int index) {
        Card c = dealOneAndCount();
        if (c != null) playerHands.get(index).addCard(c);
    }

    public  void dealToDealer() {
        Card c = dealOneAndCount();
        if (c != null) dealerHand.addCard(c);
    }


    public void dealToDealerNoCount() {
        Card c = dealOneNoCount();
        if (c != null) dealerHand.addCard(c);
    }

    public void revealDealerHoleToCount() {
        if (dealerHoleCounted) return;
        List<Card> dc = dealerHand.getCards();
        if (dc.size() >= 2) {
            Card hole = dc.get(1);
            if (hole != null) counter.update(hole);
            dealerHoleCounted = true;
        }
    }

    public List<Hand> getPlayerHands() {
        return playerHands;
    }

    public Hand getActiveHand() {
        return playerHands.get(activeHandIndex);
    }

    public int getActiveHandIndex() {
        return activeHandIndex;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public double getTrueCount() {
        return counter.getTrueCount(but.remainingDecks());
    }


    public int getRunningCount() {
        return counter.getRunningCount();
    }


    public boolean canDouble() {
        if (!roundActive) return false;
        Hand h = playerHands.get(activeHandIndex);
        return h.getCards().size() == 2;
    }


    public boolean canSplit() {
        if (!roundActive) return false;
        if (hasSplit) return false;
        Hand h = playerHands.get(activeHandIndex);
        return h.isPair() && h.getCards().size() == 2;
    }

    public boolean playerSplit() {
        if (!roundActive) return false;
        Hand h0 = playerHands.get(0);
        if (hasSplit) return false;
        if (!h0.isPair()) return false;
        Card second = h0.getCards().remove(1);
        Hand h1 = new Hand();
        h1.addCard(second);
        playerHands.add(h1);
        dealToPlayerHand(0);
        dealToPlayerHand(1);
        hasSplit = true;
        activeHandIndex = 0;
        return true;
    }

    public RoundOutcome playerHit() {
        if (!roundActive) return null;
        dealToPlayerHand(activeHandIndex);
        Hand active = playerHands.get(activeHandIndex);
        if (active.isBusted()) {
            roundActive = false;
            if (hasSplit && activeHandIndex == 0) {
                activeHandIndex = 1;
                roundActive = true;
                return null;
            }
            dealerPlay();
            return resolveRound();
        }
        return null;
    }

    public RoundOutcome playerDouble() {
        if (!roundActive) return null;
        handBetMultiplier[activeHandIndex] = 2.0;
        handDoubled[activeHandIndex] = true;
        dealToPlayerHand(activeHandIndex);
        roundActive = false;
        if (hasSplit && activeHandIndex == 0) {
            activeHandIndex = 1;
            roundActive = true;
            return null;
        }
        dealerPlay();
        return resolveRound();
    }

    public RoundOutcome playerStand() {
        if (!roundActive) return null;
        roundActive = false;
        if (hasSplit && activeHandIndex == 0) {
            activeHandIndex = 1;
            roundActive = true;
            return null;
        }
        dealerPlay();
        return resolveRound();
    }

    private void dealerPlay() {
        revealDealerHoleToCount();
        while (dealerHand.bestValue() < 17) {
            dealToDealer();
        }
    }

    public RoundOutcome resolveRound() {
        RoundOutcome out = new RoundOutcome();
        for (int i = 0; i < playerHands.size(); i++) {
            Hand ph = playerHands.get(i);
            boolean isSplitHand = (playerHands.size() > 1);
            double multiplier = handBetMultiplier[i];
            boolean isDouble = handDoubled[i];

            if (ph.isBusted()) {
                out.addHandResult(GameResult.lose(multiplier, isSplitHand, i, isDouble));
                continue;
            }
            if (ph.isBlackjack() && !dealerHand.isBlackjack() && ph.getCards().size() == 2) {
                out.addHandResult(GameResult.blackjack(multiplier, isSplitHand, i, isDouble));
                continue;
            }
            if (ph.isBlackjack() && dealerHand.isBlackjack()) {
                out.addHandResult(GameResult.push(multiplier, isSplitHand, i, isDouble));
                continue;
            }
            if (dealerHand.isBlackjack() && !ph.isBlackjack()) {
                out.addHandResult(GameResult.lose(multiplier, isSplitHand, i, isDouble));
                continue;
            }

            int p = ph.bestValue();
            int d = dealerHand.bestValue();
            if (dealerHand.isBusted()) {
                out.addHandResult(GameResult.win(multiplier, isSplitHand, i, isDouble));
                continue;
            }
            if (p > d) out.addHandResult(GameResult.win(multiplier, isSplitHand, i, isDouble));
            else if (p < d) out.addHandResult(GameResult.lose(multiplier, isSplitHand, i, isDouble));
            else out.addHandResult(GameResult.push(multiplier, isSplitHand, i, isDouble));
        }
        return out;
    }
}
