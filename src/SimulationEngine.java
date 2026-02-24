import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationEngine {
    public interface Listener {
        void onRoundStarting();
        void onStateUpdated();
        void onRoundFinished(RoundOutcome outcome);
    }

    private boolean running = false;
    private final But shoe;
    private final HiLoCounter counter;
    private final GameEngine engine;
    private final Listener listener;
    private Timer timer;
    private int delayMs = 500;

    private boolean waitingForNextRound = false;
    private int ticksToWait = 0;
    private int dealingStep = 0;

    public SimulationEngine(But shoe, HiLoCounter counter, Listener listener) {
        this.shoe = shoe;
        this.counter = counter;
        this.engine = new GameEngine(shoe, counter);
        this.listener = listener;
        this.timer = new Timer(delayMs, null);
    }

    public void setDelay(int ms) {
        this.delayMs = ms;
        if (timer != null) timer.setDelay(ms);
    }

    public void start() {
        if (running) return;
        running = true;
        waitingForNextRound = false;
        ticksToWait = 0;
        if (timer != null) timer.stop();
        timer = new Timer(delayMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                step();
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    public void pause() {
        if (!running) return;
        running = false;
        if (timer != null) timer.stop();
        waitingForNextRound = false;
        ticksToWait = 0;
    }

    public void stop() {
        running = false;
        if (timer != null) timer.stop();
        waitingForNextRound = false;
        ticksToWait = 0;
    }

    public boolean isRunning() {
        return running;
    }

    public GameEngine getEngine() {
        return engine;
    }

    public But getShoe() {
        return shoe;
    }

    private void step() {
        if (!engine.isRoundActive() && dealingStep == 0) {
            if (!waitingForNextRound) {
                waitingForNextRound = true;
                ticksToWait = 1;
                return;
            } else {
                ticksToWait--;
                if (ticksToWait <= 0) {
                    waitingForNextRound = false;
                    listener.onRoundStarting();
                    engine.prepareRound();
                    dealingStep = 1;
                    listener.onStateUpdated();
                }
                return;
            }
        }
        
        
        if (dealingStep > 0) {
            switch (dealingStep) {
                case 1: engine.dealToPlayerHand(0); break;
                case 2: engine.dealToDealer(); break;
                case 3: engine.dealToPlayerHand(0); break;
                case 4: engine.dealToDealerNoCount(); break;
            }
            dealingStep++;
            if (dealingStep > 4) {
                dealingStep = 0;
            }
            listener.onStateUpdated();
            return;
        }

        if (engine.canSplit()) {
            engine.playerSplit();
            listener.onStateUpdated();
            return;
        }

        Hand active = engine.getActiveHand();
        if (active.bestValue() < 17) {
            RoundOutcome out = engine.playerHit();
            listener.onStateUpdated();
            if (out != null) listener.onRoundFinished(out);
        } else {
            RoundOutcome out = engine.playerStand();
            listener.onStateUpdated();
            if (out != null) listener.onRoundFinished(out);
        }
    }

    private boolean isRoundActive() {
        return engine.isRoundActive();
    }
}