
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class TutorialPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel pagesContainer;
    private int pageCount = 0;
    private int currentPage = 0;

    public TutorialPanel() {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        pagesContainer = new JPanel(cardLayout);


        addPage(
                "Game Rules:\n\n" +
                        "1. The goal is to get a card total as close to 21 as possible without exceeding it.\n" +
                        "2. Card values: 2–10 — face value, face cards (J,Q,K) = 10, Ace = 1 or 11.\n" +
                        "3. The player and dealer each receive two cards (dealer has one face up). " +
                        "Blackjack (Ace + 10) usually pays 3:2 if the dealer does not have blackjack.\n",
                "/resources/tutorial/1.png"
        );

        addPage(
                "Player Actions:\n\n" +
                        "HIT — draw another card.\n" +
                        "STAND — stop drawing cards.\n" +
                        "DOUBLE — double the bet and receive exactly one more card (usually available only on the first two cards).\n" +
                        "SPLIT — divide a pair into two separate hands (if the first two cards have the same value).\n",
                "/resources/tutorial/2.png"
        );

        addPage(
                "Basic Strategy (in short):\n\n" +
                        "Decisions depend on your hand and the dealer's upcard. " +
                        "The strategy minimizes the house edge. For every hand/dealer upcard combination, there is a recommended action.\n" +
                        "A strategy chart is available in the program and can be viewed at any time.\n",
                "/resources/tutorial/3.png"
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
        ta.setFont(new Font("SansSerif", Font.PLAIN, 16));
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
                        
                        int maxW = 600, maxH = 200;
                        int w = img.getWidth(), h = img.getHeight();
                        double scale = Math.min((double)maxW / w, (double)maxH / h);
                        int nw = (int)(w * scale), nh = (int)(h * scale);
                        Image scaled = img.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
                        JLabel imgLabel = new JLabel(new ImageIcon(scaled));
                        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        JPanel imgPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                        imgPanel.setOpaque(false);
                        imgPanel.add(imgLabel);
                        p.add(imgPanel, BorderLayout.SOUTH);
                    }
                }
             } catch (Exception ignored) {
                 
             }
         }

        pagesContainer.add(p, "page" + pageCount);
        pageCount++;
    }

    
    public void addPage(String text) {
        addPage(text, null);
    }

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
