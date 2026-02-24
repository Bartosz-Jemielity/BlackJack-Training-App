public class BasicStrategyImpl implements Strategy {
    
    
    private static final char[][] HARD = {
        {'H','H','H','H','H','H','H','H','H','H'}, 
        {'H','H','H','H','H','H','H','H','H','H'}, 
        {'H','H','H','H','H','H','H','H','H','H'}, 
        {'H','H','H','H','H','H','H','H','H','H'}, 
        {'H','D','D','D','D','H','H','H','H','H'}, 
        {'D','D','D','D','D','D','D','D','H','H'}, 
        {'D','D','D','D','D','D','D','D','D','H'}, 
        {'H','H','S','S','S','H','H','H','H','H'}, 
        {'S','S','S','S','S','H','H','H','H','H'}, 
        {'S','S','S','S','S','H','H','H','H','H'}, 
        {'S','S','S','S','S','H','H','H','H','H'}, 
        {'S','S','S','S','S','H','H','H','H','H'}, 
        {'S','S','S','S','S','S','S','S','S','S'}  
    };

    
    private static final char[][] SOFT = {
        {'H','H','H','D','D','H','H','H','H','H'}, 
        {'H','H','H','D','D','H','H','H','H','H'}, 
        {'H','H','D','D','D','H','H','H','H','H'}, 
        {'H','H','D','D','D','H','H','H','H','H'}, 
        {'H','D','D','D','D','H','H','H','H','H'}, 
        {'S','D','D','D','D','S','S','H','H','H'}, 
        {'S','S','S','S','S','S','S','S','S','S'}, 
        {'S','S','S','S','S','S','S','S','S','S'}  
    };

    
    private static final char[][] PAIRS = {
        {'P','P','P','P','P','P','H','H','H','H'}, 
        {'P','P','P','P','P','P','H','H','H','H'}, 
        {'H','H','H','P','P','H','H','H','H','H'}, 
        {'D','D','D','D','D','D','D','D','H','H'}, 
        {'P','P','P','P','P','H','H','H','H','H'}, 
        {'P','P','P','P','P','P','H','H','H','H'}, 
        {'P','P','P','P','P','P','P','P','P','P'}, 
        {'P','P','P','P','P','S','P','P','S','S'}, 
        {'S','S','S','S','S','S','S','S','S','S'}, 
        {'P','P','P','P','P','P','P','P','P','P'}  
    };

    @Override
    public Action decide(Hand player, Card dealerUp, StrategyContext ctx) {
        if (player == null || dealerUp == null) return Action.HIT;
        int dealerCol = dealerColumn(dealerUp);
        boolean isPair = player.getCards().size() == 2 &&
                         player.getCards().get(0).getRank() == player.getCards().get(1).getRank();
        boolean isSoft = isSoftHand(player);
        boolean canDouble = ctx == null || ctx.canDouble;
        boolean canSplit = ctx == null || ctx.canSplit;

        if (isPair && canSplit) {
            int idx = pairIndex(player.getCards().get(0).getRank());
            if (idx >= 0) {
                char c = PAIRS[idx][dealerCol];
                if (c == 'P') return Action.SPLIT;
                if (c == 'D') return canDouble ? Action.DOUBLE : Action.HIT;
                if (c == 'S') return Action.STAND;
                return Action.HIT;
            }
        }

        if (isSoft) {
            int softIdx = softIndex(player);
            if (softIdx >= 0) {
                char c = SOFT[softIdx][dealerCol];
                if (c == 'D') return canDouble ? Action.DOUBLE : Action.HIT;
                if (c == 'S') return Action.STAND;
                return Action.HIT;
            }
        }

        int total = player.bestValue();
        int hardIdx = hardIndex(total);
        char c = HARD[hardIdx][dealerCol];
        if (c == 'D') return canDouble ? Action.DOUBLE : Action.HIT;
        if (c == 'S') return Action.STAND;
        return Action.HIT;
    }

    protected static int dealerColumn(Card card) {
        int r = card.getRank();
        if (r == 1) return 9;
        if (r >= 10) return 8;
        return r - 2;
    }

    protected static boolean isSoftHand(Hand h) {
        int sum = 0, aces = 0;
        for (Card c : h.getCards()) {
            int r = c.getRank();
            if (r == 1) { aces++; sum += 1; }
            else if (r >= 10) sum += 10;
            else sum += r;
        }
        return aces > 0 && sum + 10 <= 21;
    }

    protected static int softIndex(Hand h) {
        int nonAceSum = 0;
        for (Card c : h.getCards()) {
            int r = c.getRank();
            if (r != 1) nonAceSum += (r >= 10 ? 10 : r);
        }
        if (nonAceSum >= 2 && nonAceSum <= 9) return nonAceSum - 2;
        return -1;
    }

    protected static int hardIndex(int total) {
        if (total <= 5) return 0;
        if (total >= 17) return 12;
        return total - 5;
    }

    protected static int pairIndex(int rank) {
        if (rank >= 2 && rank <= 9) return rank - 2;
        if (rank >= 10) return 8;
        if (rank == 1) return 9;
        return -1;
    }
}

