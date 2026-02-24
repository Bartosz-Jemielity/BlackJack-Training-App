import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class AdvancedTutorialPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel pagesContainer;
    private int pageCount = 0;
    private int currentPage = 0;

    public AdvancedTutorialPanel() {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        pagesContainer = new JPanel(cardLayout);


        addPage(
                "Introduction to Card Counting:\n\n" +
                        "Card counting allows you to track which cards remain in the shoe, enabling you to adjust your bet size and playing decisions.\n" +
                        "This tutorial covers the simple and popular Hi‑Lo system, as well as how to use the Running and True Count.",
                "/resources/tutorial/1adv.png"
        );

        addPage(
                "Hi‑Lo System — Card Values:\n\n" +
                        "2–6 => +1\n" +
                        "7–9 => 0\n" +
                        "10, J, Q, K, A => -1\n\n" +
                        "Add these values for every card dealt to calculate the Running Count.",
                "/resources/tutorial/2adv.jpg"
        );

        addPage(
                "Running Count vs. True Count:\n\n" +
                        "The Running Count is the sum of Hi‑Lo values for all cards visible so far.\n" +
                        "True Count = Running Count / remaining_decks\n" +
                        "The True Count better reflects the edge in multi-deck games.",
                "/resources/tutorial/3adv.png"
        );

        addPage(
                "Practical Example:\n\n" +
                        "1) After several hands, Running Count = +6, ~3 decks remain => True Count = +2.0\n" +
                        "2) With a positive True Count, increase your bet; with a negative count, decrease it.\n" +
                        "Remember risk management and table rules (cut card, reshuffle).",
                "/resources/tutorial/4adv.png"
        );

        addPage(
                "How to Read the Card Counting Chart:\n\n" +
                        "The card counting chart provides recommendations based on the True Count value.\n" +
                        "A typical chart has columns/rows corresponding to True Count ranges (e.g., -2, -1, 0, +1, +2, +3+).\n" +
                        "For each range, the chart may include decision deviations, e.g., Stand instead of Hit at a specific True Count.\n\n" +
                        "How to use: Check the current True Count (Running Count / remaining decks) and find the corresponding range in the table.\n" +
                        "- If the True Count is high (positive), increase your bet and apply recommended deviations from Basic Strategy.\n" +
                        "- If the True Count is low (negative), decrease your bet and also apply recommended deviations.\n\n" +
                        "The chart is just a tool — always consider table rules and risk management.\n",
                "/resources/tutorial/5adv.png"
        );

        addPage(
                "True Count vs. Edge:\n\n" +
                        "The table below shows approximate Casino vs. Player edge values for different True Counts (from -5 to +5).\n" +
                        "- With a negative True Count, the house has the edge.\n" +
                        "- With a positive True Count, the player gains the advantage.\n\n" +
                        "What this means in practice:\n" +
                        "- If the True Count is positive (e.g., +2), you have a slight edge (approx. 0.5%). This means you can statistically expect a profit in the long run,\n" +
                        "  so reasonably increasing your bet and using strategy deviations is justified.\n" +
                        "- If the True Count is strongly negative (e.g., -4, -5), the house has a significant edge — decrease bets or consider leaving the table.\n\n" +
                        "Rule of thumb: Play aggressively only when the True Count gives you the edge; manage your bankroll and increase bets gradually.",
                "/resources/tutorial/6adv.png"
        );

        add(pagesContainer, BorderLayout.CENTER);
        if (pageCount > 0) cardLayout.show(pagesContainer, "page0");
    }

    
    public void addPage(String text, String imageResourcePath) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JTextArea ta = new JTextArea(text);
        ta.setWrapStyleWord(true);
        ta.setLineWrap(true);
        ta.setEditable(false);
        ta.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ta.setBackground(getBackground());
        ta.setOpaque(true);

        p.add(new JScrollPane(ta), BorderLayout.CENTER);

        if (imageResourcePath != null && !imageResourcePath.isEmpty()) {
            try {
                URL u = getClass().getResource(imageResourcePath);
                if (u == null) {
                    String name = imageResourcePath;
                    if (name.startsWith("/")) name = name.substring(1);
                    String fileName = name.substring(name.lastIndexOf('/') + 1);
                    u = getClass().getResource("/resources/tutorial/" + fileName);
                    if (u == null) u = getClass().getResource("/resources/tutorials/images/" + fileName);
                    if (u == null) u = getClass().getResource("/" + fileName);
                }
                if (u != null) {
                    BufferedImage img = ImageIO.read(u);
                    if (img != null) {
                        int maxH = 180;
                        double scale = Math.min(1.0, (double)maxH / img.getHeight());
                        int nw = (int)(img.getWidth() * scale);
                        int nh = (int)(img.getHeight() * scale);
                        Image scaled = img.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
                        JLabel imgLabel = new JLabel(new ImageIcon(scaled));
                        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        JPanel imgPanel = new JPanel(new BorderLayout());
                        imgPanel.setOpaque(false);
                        imgPanel.add(imgLabel, BorderLayout.CENTER);
                        p.add(imgPanel, BorderLayout.SOUTH);
                    }
                }
            } catch (Exception ignored) {
                
            }
        }

        pagesContainer.add(p, "page" + pageCount);
        pageCount++;
    }

    public void addPage(String text) { addPage(text, null); }

    public void next() {
        if (currentPage < pageCount - 1) {
            currentPage++;
            cardLayout.show(pagesContainer, "page" + currentPage);
        }
    }

    public void prev() {
        if (currentPage > 0) {
            currentPage--;
            cardLayout.show(pagesContainer, "page" + currentPage);
        }
    }

    public int getPageCount() { return pageCount; }
    public int getCurrentPage() { return currentPage; }
    public boolean canNext() { return currentPage < pageCount - 1; }
    public boolean canPrev() { return currentPage > 0; }
}
