public class AdvancedStrategyImpl implements Strategy {
    
    

    
    
    

    
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
        int dealerVal = dealerUp.getRank();
        if (dealerVal == 1) dealerVal = 11;
        else if (dealerVal >= 10) dealerVal = 10;

        boolean isPair = player.getCards().size() == 2 &&
                         player.getCards().get(0).getRank() == player.getCards().get(1).getRank();
        boolean isSoft = isSoftHand(player);
        boolean canDouble = ctx == null || ctx.canDouble;
        boolean canSplit = ctx == null || ctx.canSplit;
        double tc = (ctx == null || Double.isNaN(ctx.trueCount)) ? 0 : ctx.trueCount;
        int total = player.bestValue();

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        

        if (!isSoft && !isPair) {
            
            if (total == 16 && dealerVal == 10) {
                return tc >= 0 ? Action.STAND : Action.HIT;
            }
            
            if (total == 16 && dealerVal == 9) {
                return tc >= 5 ? Action.STAND : Action.HIT;
            }
            
            if (total == 15 && dealerVal == 10) {
                return tc >= 4 ? Action.STAND : Action.HIT;
            }
            
            if (total == 14 && dealerVal == 2) {
                return tc >= -4 ? Action.STAND : Action.HIT;
            }
            if (total == 14 && dealerVal == 3) {
                return tc >= -5 ? Action.STAND : Action.HIT;
            }
            
            if (total == 13 && dealerVal == 2) {
                return tc >= -1 ? Action.STAND : Action.HIT;
            }
            
            if (total == 13 && dealerVal == 3) {
                return tc >= -2 ? Action.STAND : Action.HIT;
            }
            
            if (total == 13 && dealerVal == 4) {
                return tc >= -4 ? Action.STAND : Action.HIT;
            }
            
            if (total == 13 && dealerVal == 5) {
                return tc >= -5 ? Action.STAND : Action.HIT;
            }
            
            if (total == 13 && dealerVal == 6) {
                return tc >= -5 ? Action.STAND : Action.HIT;
            }
            
            if (total == 12 && dealerVal == 2) {
                return tc >= 3 ? Action.STAND : Action.HIT;
            }
            
            if (total == 12 && dealerVal == 3) {
                return tc >= 2 ? Action.STAND : Action.HIT;
            }
            
            if (total == 12 && dealerVal == 4) {
                return tc >= 0 ? Action.STAND : Action.HIT;
            }
            
            if (total == 12 && dealerVal == 5) {
                return tc >= -2 ? Action.STAND : Action.HIT;
            }
            
            if (total == 12 && dealerVal == 6) {
                return tc >= -1 ? Action.STAND : Action.HIT;
            }
            
            if (total == 11 && dealerVal == 11 && canDouble) {
                return tc >= 1 ? Action.DOUBLE : Action.HIT;
            }
            if (total == 11 && dealerVal == 10 && canDouble) {
                return tc >= -5 ? Action.DOUBLE : Action.HIT;
            }
            if (total == 11 && dealerVal == 9 && canDouble) {
                return tc >= -5 ? Action.DOUBLE : Action.HIT;
            }
            
            if (total == 10 && dealerVal == 10 && canDouble) {
                return tc >= 4 ? Action.DOUBLE : Action.HIT;
            }
            
            if (total == 10 && dealerVal == 11 && canDouble) {
                return tc >= 4 ? Action.DOUBLE : Action.HIT;
            }
            if (total == 10 && dealerVal == 9 && canDouble) {
                return tc >= -2 ? Action.DOUBLE : Action.HIT;
            }
            if (total == 10 && dealerVal == 8 && canDouble) {
                return tc >= -5 ? Action.DOUBLE : Action.HIT;
            }
            
            if (total == 9 && dealerVal == 2 && canDouble) {
                return tc >= 1 ? Action.DOUBLE : Action.HIT;
            }
            if (total == 9 && dealerVal == 3 && canDouble) {
                return tc >= -1 ? Action.DOUBLE : Action.HIT;
            }
            if (total == 9 && dealerVal == 4 && canDouble) {
                return tc >= -3 ? Action.DOUBLE : Action.HIT;
            }
            if (total == 9 && dealerVal == 5 && canDouble) {
                return tc >= -5 ? Action.DOUBLE : Action.HIT;
            }
            
            if (total == 9 && dealerVal == 7 && canDouble) {
                return tc >= 3 ? Action.DOUBLE : Action.HIT;
            }
            if (total == 8 && dealerVal == 4 && canDouble) {
                return tc >= 5 ? Action.DOUBLE : Action.HIT;
            }
            if (total == 8 && dealerVal == 5 && canDouble) {
                return tc >= 3 ? Action.DOUBLE : Action.HIT;
            }
            if (total == 8 && dealerVal == 6 && canDouble) {
                return tc >= 1 ? Action.DOUBLE : Action.HIT;
            }
        }

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        if (isSoft && !isPair) {
            int nonAceSum = 0;
            for (Card c : player.getCards()) {
                int r = c.getRank();
                if (r != 1) nonAceSum += (r >= 10 ? 10 : r);
            }
            
            if (nonAceSum == 2) {
                if (dealerVal == 4 && canDouble) return tc >= 3 ? Action.DOUBLE : Action.HIT;
                if (dealerVal == 5 && canDouble) return tc >= 0 ? Action.DOUBLE : Action.HIT;
                if (dealerVal == 6 && canDouble) return tc >= -2 ? Action.DOUBLE : Action.HIT;
            }
            
            if (nonAceSum == 3) {
                if (dealerVal == 4 && canDouble) return tc >= 1 ? Action.DOUBLE : Action.HIT;
                if (dealerVal == 5 && canDouble) return tc >= -2 ? Action.DOUBLE : Action.HIT;
                if (dealerVal == 6 && canDouble) return tc >= -5 ? Action.DOUBLE : Action.HIT;
            }
            
            if (nonAceSum == 4) {
                if (dealerVal == 4 && canDouble) return tc >= -1 ? Action.DOUBLE : Action.HIT;
                if (dealerVal == 5 && canDouble) return tc >= -5 ? Action.DOUBLE : Action.HIT;
            }
            
            if (nonAceSum == 5) {
                if (dealerVal == 3 && canDouble) return tc >= 4 ? Action.DOUBLE : Action.HIT;
                if (dealerVal == 4 && canDouble) return tc >= -3 ? Action.DOUBLE : Action.HIT;
            }
            
            if (nonAceSum == 6) {
                if (dealerVal == 2 && canDouble) return tc >= 1 ? Action.DOUBLE : Action.HIT;
                if (dealerVal == 3 && canDouble) return tc >= -4 ? Action.DOUBLE : Action.HIT;
            }
            
            if (nonAceSum == 7) {
                if (dealerVal == 2 && canDouble) return tc >= 0 ? Action.DOUBLE : Action.STAND;
                if (dealerVal == 3 && canDouble) return tc >= -3 ? Action.DOUBLE : Action.STAND;
            }
            
            if (nonAceSum == 8) {
                if (dealerVal == 3) return tc >= 5 ? Action.DOUBLE : Action.STAND;
                if (dealerVal == 4) return tc >= 3 ? Action.DOUBLE : Action.STAND;
                if (dealerVal == 5) return tc >= 1 ? Action.DOUBLE : Action.STAND;
                if (dealerVal == 6) return tc >= 1 ? Action.DOUBLE : Action.STAND;
            }
            if (nonAceSum == 9) {
                if (dealerVal == 5) return tc >= 5 ? Action.DOUBLE : Action.STAND;
                if (dealerVal == 6) return tc >= 4 ? Action.DOUBLE : Action.STAND;
            }
        }

        
        
        
        
        
        
        
        
        
        
        
        
        if (isPair && canSplit) {
            int pairRank = player.getCards().get(0).getRank();
            
            if (pairRank == 2) {
                if (dealerVal == 2) return tc >= -4 ? Action.SPLIT : Action.HIT;
            }
            
            if (pairRank == 3) {
                if (dealerVal == 2) return tc >= 0 ? Action.SPLIT : Action.HIT;
                if (dealerVal == 3) return tc >= -5 ? Action.SPLIT : Action.HIT;
                if (dealerVal == 8) return tc >= 4 ? Action.SPLIT : Action.HIT;
            }
            
            if (pairRank == 4) {
                if (dealerVal == 4) return tc >= 1 ? Action.SPLIT : Action.HIT;
                if (dealerVal == 5) return tc >= -2 ? Action.SPLIT : Action.HIT;
                if (dealerVal == 6) return tc >= -5 ? Action.SPLIT : Action.HIT;
            }
            
            if (pairRank == 5) {
                if (dealerVal == 8) return tc >= -5 ? Action.DOUBLE : Action.HIT;
                if (dealerVal == 9) return tc >= -2 ? Action.DOUBLE : Action.HIT;
                if (dealerVal == 10) return tc >= 4 ? Action.DOUBLE : Action.HIT;
                if (dealerVal == 11) return tc >= 1 ? Action.DOUBLE : Action.HIT;
            }
            if (pairRank == 6) {
                if (dealerVal == 2) return tc >= -2 ? Action.SPLIT : Action.HIT;
                if (dealerVal == 3) return tc >= -2 ? Action.SPLIT : Action.HIT;
                if (dealerVal == 4) return tc >= -5 ? Action.SPLIT : Action.HIT;
            }
            if (pairRank == 7) {
                if (dealerVal == 8) return tc >= 5 ? Action.SPLIT : Action.HIT;
            }
            
            if (pairRank == 9) {
                if (dealerVal == 2) return tc >= -3 ? Action.SPLIT : Action.STAND;
                if (dealerVal == 3) return tc >= -4 ? Action.SPLIT : Action.STAND;
                if (dealerVal == 4) return tc >= -6 ? Action.SPLIT : Action.STAND;
            }
            
            if (pairRank == 10 || pairRank == 11 || pairRank == 12 || pairRank == 13) {
                if (dealerVal == 4) return tc >= 6 ? Action.SPLIT : Action.STAND;
                if (dealerVal == 5) return tc >= 5 ? Action.SPLIT : Action.STAND;
                if (dealerVal == 6) return tc >= 4 ? Action.SPLIT : Action.STAND;
            }
        }

        
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

        int hardIdx = hardIndex(total);
        char c = HARD[hardIdx][dealerCol];
        if (c == 'D') return canDouble ? Action.DOUBLE : Action.HIT;
        if (c == 'S') return Action.STAND;
        return Action.HIT;
    }

    private static int dealerColumn(Card card) {
        int r = card.getRank();
        if (r == 1) return 9;
        if (r >= 10) return 8;
        return r - 2;
    }

    private static boolean isSoftHand(Hand h) {
        int sum = 0, aces = 0;
        for (Card c : h.getCards()) {
            int r = c.getRank();
            if (r == 1) { aces++; sum += 1; }
            else if (r >= 10) sum += 10;
            else sum += r;
        }
        return aces > 0 && sum + 10 <= 21;
    }

    private static int softIndex(Hand h) {
        int nonAceSum = 0;
        for (Card c : h.getCards()) {
            int r = c.getRank();
            if (r != 1) nonAceSum += (r >= 10 ? 10 : r);
        }
        if (nonAceSum >= 2 && nonAceSum <= 9) return nonAceSum - 2;
        return -1;
    }

    private static int hardIndex(int total) {
        if (total <= 5) return 0;
        if (total >= 17) return 12;
        return total - 5;
    }

    private static int pairIndex(int rank) {
        if (rank >= 2 && rank <= 9) return rank - 2;
        if (rank >= 10) return 8;
        if (rank == 1) return 9;
        return -1;
    }
}

