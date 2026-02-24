import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class CountingExercisePanel extends JPanel {
    private But shoe;
    private HiLoCounter counter;
    private final BufferedImage cardBackImage;

    private JLabel cardLabel;
    private Timer timer;
    private int delayMs = 500;
    private boolean running = false;
    private Listener listener;

    public interface Listener {
        void onCardDealt(Card c);
        void onShuffle();
    }

    public CountingExercisePanel(But shoe, HiLoCounter counter, BufferedImage cardBackImage) {
        this.shoe = shoe;
        this.counter = counter;
        this.cardBackImage = cardBackImage;
        initUI();
    }

    public void setShoeAndCounter(But shoe, HiLoCounter counter) {
        this.shoe = shoe;
        this.counter = counter;
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(false);
        cardLabel = new JLabel();
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(cardLabel, BorderLayout.CENTER);
        timer = new Timer(delayMs, e -> tick());
    }

    public void setListener(Listener l) {
        this.listener = l;
    }

    public void setDelay(int ms) {
        this.delayMs = ms;
        if (timer != null) timer.setDelay(ms);
    }

    public void start() {
        if (running) return;
        running = true;
        if (timer != null) timer.start();
    }

    public void pause() {
        running = false;
        if (timer != null) timer.stop();
    }

    public void togglePause() {
        if (running) pause(); else start();
    }

    public boolean isRunning() {
        return running;
    }

    private void tick() {

        if (shoe.remainingDecks() < 0.5) {
            shoe.resetAndShuffle();
            counter.reset();
            if (listener != null) listener.onShuffle();
            return;
        }
        Card c = shoe.dealOne();
        counter.update(c);
        ImageIcon ic = loadCardIcon(c);
        if (ic != null) cardLabel.setIcon(ic);
        else cardLabel.setText(c.toString());
        if (listener != null) listener.onCardDealt(c);
    }

    private ImageIcon loadCardIcon(Card c) {
        String filename = getCardFilename(c);
        String path = "/resources/cards/" + filename;
        URL imgUrl = getClass().getResource(path);
        if (imgUrl == null) return null;
        try {
            Image img = ImageIO.read(imgUrl).getScaledInstance(180, 270, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception ignored) { }
        return null;
    }

    private String getCardFilename(Card c) {
        String suitStr = c.getSuit().name().toLowerCase();
        String rankStr;
        switch (c.getRank()) {
            case 1: rankStr = "A"; break;
            case 11: rankStr = "J"; break;
            case 12: rankStr = "Q"; break;
            case 13: rankStr = "K"; break;
            default: rankStr = String.valueOf(c.getRank()); break;
        }
        return suitStr + "_" + rankStr + ".png";
    }
}