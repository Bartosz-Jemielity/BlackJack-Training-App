import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class StrategyPanel extends JPanel {
    public static final int VARIANT_BASIC = 0;
    public static final int VARIANT_ADVANCED = 1;

    private int variant = VARIANT_BASIC;
    private JLabel imageLabel;
    private JScrollPane imageScroll;
    
    private int displayHeight = 0;

    public StrategyPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageScroll = new JScrollPane(imageLabel);
        imageScroll.setBorder(null);
        imageScroll.getViewport().setOpaque(false);
        imageLabel.setOpaque(false);
        add(imageScroll, BorderLayout.CENTER);
    }

    public void setVariant(int v) {
        this.variant = v;
        reloadContent();
    }

    
    public void setDisplayHeight(int h) { this.displayHeight = h; reloadContent(); }

    private void reloadContent() {
        
        String[] basicNames = new String[]{"basic_table.png","basic-table.png","tabelka_podstawowa.png","tabelka-podstawowa.png","adv1.png","1adv.png","adv_table.png"};
        String[] advNames = new String[]{"advanced_table.png","advanced-table.png","tabelka_rozszerzona.png","tabelka-rozszerzona.png","adv_table.png","adv_chances.png"};
        String[] candidates = variant == VARIANT_BASIC ? basicNames : advNames;
        try {
            BufferedImage img = null;
            URL foundUrl = null;
            for (String fileName : candidates) {
                String[] paths = new String[]{"/resources/strategy/" + fileName, "/resources/" + fileName, "/" + fileName, "/resources/tutorial/" + fileName};
                for (String p : paths) {
                    URL tu = getClass().getResource(p);
                    if (tu != null) {
                        try { img = ImageIO.read(tu); } catch (Exception ex) { img = null; }
                        if (img != null) { foundUrl = tu; break; }
                    }
                }
                if (img != null) break;
            }
            if (img != null && foundUrl != null) {
                
                removeAll();
                add(imageScroll, BorderLayout.CENTER);
                int targetH = displayHeight > 0 ? displayHeight : (Toolkit.getDefaultToolkit().getScreenSize().height - 120);
                Image scaled = img.getScaledInstance(-1, targetH, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
                imageLabel.setText("");
                revalidate(); repaint();
                return;
            }
        } catch (Exception ignored) { }

        
        removeAll();
        JPanel msg = new JPanel(new BorderLayout());
        msg.setOpaque(false);
        msg.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        JLabel label = new JLabel("Brak obrazu tabeli strategii. Proszę wgrać plik obrazu do resources/strategy/ z jedną z nazw: basic_table.png, advanced_table.png, tabelka_podstawowa.png, tabelka_rozszerzona.png, adv_table.png, adv1.png, adv_chances.png", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setOpaque(false);
        msg.add(label, BorderLayout.CENTER);
        add(msg, BorderLayout.CENTER);
        revalidate(); repaint();
    }
}
