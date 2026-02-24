public interface Strategy {
    Action decide(Hand player, Card dealerUp, StrategyContext ctx);
}

