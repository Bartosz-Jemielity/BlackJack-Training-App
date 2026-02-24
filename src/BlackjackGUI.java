import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

public class BlackjackGUI extends JFrame {
    private Mode currentMode = Mode.INTERACTIVE;


    private JButton modeMenuBtn, pauseResumeBtn;

    private JButton tutorialBtn;

    private JButton advTutorialBtn;

    private JButton showStrategyBtn;
    private JPanel mainPanel, tablePanel, dealerPanel, playerHandContainer, bottomContainer;
    private JLabel dealerTitleLabel, statusLabel, statsLabel, countLabel, cardsRemainingLabel;
    private JToggleButton showCountToggle;
    private JSpinner shoeSizeSpinner;
    private JSlider speedSlider;
    private JButton btnDeal, btnHit, btnStand, btnDouble, btnSplit, btnHint, btnProHint;


    private GameEngine engineInteractive;
    private But shoeInteractive;
    private HiLoCounter counterInteractive;

    private SimulationEngine simulationEngine;
    private But shoeExercise;
    private HiLoCounter counterExercise;
    private CountingExercisePanel exercisePanel;

    private int currentShoeSize = 6;
    private int dealerCardsRevealedCount = -1;
    private int simDealerCardsRevealedCount = -1;
    private int playerCardsRevealedCount = -1;

    private Timer animationTimer;
    private BufferedImage cardBackImage;
    private int totalWins = 0, totalLosses = 0;
    private double totalProfit = 0.0;

    private JPanel strategyDock;
    private StrategyPanel strategyPanel;
    private boolean strategyDockVisible = false;

    public BlackjackGUI() {
        setTitle("Blackjack Training App");

        setUndecorated(false);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        shoeInteractive = new But(currentShoeSize);
        counterInteractive = new HiLoCounter();
        engineInteractive = new GameEngine(shoeInteractive, counterInteractive);

        loadResources();
        setupUI();
        updateControlsState(false);
    }

    private void loadResources() {
        try {
            URL imgUrl = getClass().getResource("/resources/cards/back_dark.png");
            if (imgUrl != null) cardBackImage = ImageIO.read(imgUrl);
        } catch (Exception e) {
            System.err.println("Błąd zasobów graficznych.");
        }
    }

    private void setupUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(35, 100, 35));


        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        settingsPanel.setOpaque(false);

        modeMenuBtn = new JButton("Mode: INTERACTIVE ▼");
        modeMenuBtn.addActionListener(e -> showModeMenu(modeMenuBtn));
        settingsPanel.add(modeMenuBtn);

        pauseResumeBtn = new JButton("▶ Start");
        pauseResumeBtn.setVisible(false);
        pauseResumeBtn.addActionListener(e -> togglePauseForMode());
        settingsPanel.add(pauseResumeBtn);

        settingsPanel.add(new JLabel(" Shoe: "));
        shoeSizeSpinner = new JSpinner(new SpinnerNumberModel(6, 1, 8, 1));
        shoeSizeSpinner.addChangeListener(e -> {
            int newSize = (Integer) shoeSizeSpinner.getValue();
            if (newSize != currentShoeSize) {
                currentShoeSize = newSize;
                reshuffleAllShoes();
            }
        });
        settingsPanel.add(shoeSizeSpinner);

        settingsPanel.add(new JLabel(" Speed: "));
        speedSlider = new JSlider(1, 10, 5);
        speedSlider.setOpaque(false);
        speedSlider.addChangeListener(e -> {
            int d = getSpeedDelay();
            if (exercisePanel != null) exercisePanel.setDelay(d);
            if (simulationEngine != null) simulationEngine.setDelay(d);
            if (animationTimer != null) animationTimer.setDelay(d);
        });
        settingsPanel.add(speedSlider);


        tutorialBtn = new JButton("Tutorial");
        tutorialBtn.addActionListener(e -> showTutorial());
        settingsPanel.add(tutorialBtn);


        advTutorialBtn = new JButton("Card Counting");
        advTutorialBtn.addActionListener(e -> showAdvancedTutorial());
        settingsPanel.add(advTutorialBtn);


        showStrategyBtn = new JButton("Show Table");
        showStrategyBtn.addActionListener(e -> {
            toggleStrategyDock();
        });
        settingsPanel.add(showStrategyBtn);


        showCountToggle = new JToggleButton("Hi-Lo");
        showCountToggle.addActionListener(e -> updateCountDisplay());
        settingsPanel.add(showCountToggle);

        cardsRemainingLabel = new JLabel("Cards: 312", SwingConstants.CENTER);
        cardsRemainingLabel.setForeground(Color.WHITE);
        cardsRemainingLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        settingsPanel.add(cardsRemainingLabel);


        countLabel = new JLabel(" ", SwingConstants.CENTER);
        countLabel.setForeground(Color.YELLOW);
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 11));

        statsLabel = new JLabel("Wins: 0 | Losses: 0 | Profit: 0.0");
        statsLabel.setForeground(Color.WHITE);
        statsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        topPanel.add(settingsPanel);
        topPanel.add(statsLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);


        getLayeredPane().add(countLabel, JLayeredPane.POPUP_LAYER);

        tablePanel = new JPanel(new GridBagLayout());
        tablePanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0;

        dealerTitleLabel = new JLabel("DEALER", SwingConstants.CENTER);
        dealerTitleLabel.setForeground(new Color(220, 220, 220));
        dealerTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        dealerPanel.setOpaque(false);

        gbc.gridy = 0; gbc.weighty = 0.4;
        JPanel dZone = new JPanel(new BorderLayout()); dZone.setOpaque(false);
        dZone.add(dealerTitleLabel, BorderLayout.NORTH); dZone.add(dealerPanel, BorderLayout.CENTER);
        tablePanel.add(dZone, gbc);

        gbc.gridy = 1; gbc.weighty = 0.6;
        playerHandContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 0));
        playerHandContainer.setOpaque(false);
        tablePanel.add(playerHandContainer, gbc);

        mainPanel.add(tablePanel, BorderLayout.CENTER);


        strategyDock = new JPanel(new BorderLayout());
        strategyDock.setOpaque(false);
        strategyDock.setPreferredSize(new Dimension(420, 0));

        JPanel topCtrl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6)); topCtrl.setOpaque(false);
        JToggleButton tbasic = new JToggleButton("Podstawowa");
        JToggleButton tadv = new JToggleButton("Rozszerzona");
        ButtonGroup sbg = new ButtonGroup(); sbg.add(tbasic); sbg.add(tadv);
        tbasic.setSelected(true);
        strategyPanel = new StrategyPanel();

        strategyPanel.setDisplayHeight(this.getHeight() - 120);
        tbasic.addActionListener(e -> strategyPanel.setVariant(StrategyPanel.VARIANT_BASIC));
        tadv.addActionListener(e -> strategyPanel.setVariant(StrategyPanel.VARIANT_ADVANCED));

        topCtrl.add(tbasic); topCtrl.add(tadv);

        strategyDock.add(topCtrl, BorderLayout.NORTH);
        strategyDock.add(strategyPanel, BorderLayout.CENTER);
        strategyDock.setVisible(false);

        mainPanel.add(strategyDock, BorderLayout.EAST);



        this.addComponentListener(new ComponentAdapter(){
            @Override public void componentResized(ComponentEvent e) {
                if (strategyPanel != null && strategyDock.isVisible()) {
                    strategyPanel.setDisplayHeight(getHeight() - 120);
                    strategyPanel.revalidate(); strategyPanel.repaint();
                }
            }
        });

        bottomContainer = new JPanel(new BorderLayout());
        JPanel ctrlP = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        ctrlP.setBackground(new Color(15, 45, 15));
        btnDeal = createButton("DEAL", new Color(34, 139, 34), new Dimension(150, 70));
        btnHit = createButton("HIT", Color.WHITE, new Dimension(130, 65));
        btnStand = createButton("STAND", Color.WHITE, new Dimension(130, 65));
        btnDouble = createButton("DOUBLE", new Color(218, 165, 32), new Dimension(130, 65));
        btnSplit = createButton("SPLIT", new Color(70, 130, 180), new Dimension(130, 65));
        btnHint = createButton("Hint", new Color(144, 238, 144), new Dimension(90, 50));
        btnProHint = createButton("Pro", new Color(255, 215, 0), new Dimension(90, 50));

        btnDeal.addActionListener(e -> startRound());
        btnHit.addActionListener(e -> playHit());
        btnStand.addActionListener(e -> playStand());
        btnDouble.addActionListener(e -> playDouble());
        btnSplit.addActionListener(e -> { if(engineInteractive.playerSplit()) updateTable(); });
        btnHint.addActionListener(e -> showHint(false));
        btnProHint.addActionListener(e -> showHint(true));

        ctrlP.add(btnDeal); ctrlP.add(Box.createHorizontalStrut(30));
        ctrlP.add(btnHit); ctrlP.add(btnStand); ctrlP.add(btnDouble); ctrlP.add(btnSplit);
        ctrlP.add(Box.createHorizontalStrut(20));
        ctrlP.add(btnHint); ctrlP.add(btnProHint);
        bottomContainer.add(ctrlP, BorderLayout.CENTER);

        statusLabel = new JLabel("Welcome!", SwingConstants.CENTER);
        statusLabel.setPreferredSize(new Dimension(100, 75));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        statusLabel.setOpaque(true);
        bottomContainer.add(statusLabel, BorderLayout.SOUTH);

        mainPanel.add(bottomContainer, BorderLayout.SOUTH);
        add(mainPanel);
    }


    private void showTutorial() {
        TutorialPanel tp = new TutorialPanel();
        JDialog dlg = new JDialog(this, "Tutorial - Blackjack", true);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setSize(700, 450);
        dlg.setLocationRelativeTo(this);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPrev = new JButton("Back");
        JButton btnNext = new JButton("Next");
        JButton btnClose = new JButton("Close");

        btnPrev.setEnabled(!tp.canPrev() ? false : true);
        btnNext.setEnabled(tp.canNext());

        btnPrev.addActionListener(e -> { tp.prev(); btnPrev.setEnabled(tp.canPrev()); btnNext.setEnabled(tp.canNext()); });
        btnNext.addActionListener(e -> { tp.next(); btnPrev.setEnabled(tp.canPrev()); btnNext.setEnabled(tp.canNext()); });
        btnClose.addActionListener(e -> dlg.dispose());

        btnBar.add(btnPrev); btnBar.add(btnNext); btnBar.add(btnClose);

        dlg.setLayout(new BorderLayout());
        dlg.add(tp, BorderLayout.CENTER);
        dlg.add(btnBar, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }


    private void showAdvancedTutorial() {
        AdvancedTutorialPanel atp = new AdvancedTutorialPanel();
        JDialog dlg = new JDialog(this, "Zaawansowany tutorial - Liczenie kart", true);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setSize(820, 520);
        dlg.setLocationRelativeTo(this);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPrev = new JButton("Back");
        JButton btnNext = new JButton("Next");
        JButton btnClose = new JButton("Close");

        btnPrev.setEnabled(atp.canPrev());
        btnNext.setEnabled(atp.canNext());

        btnPrev.addActionListener(e -> { atp.prev(); btnPrev.setEnabled(atp.canPrev()); btnNext.setEnabled(atp.canNext()); });
        btnNext.addActionListener(e -> { atp.next(); btnPrev.setEnabled(atp.canPrev()); btnNext.setEnabled(atp.canNext()); });
        btnClose.addActionListener(e -> dlg.dispose());

        btnBar.add(btnPrev); btnBar.add(btnNext); btnBar.add(btnClose);

        dlg.setLayout(new BorderLayout());
        dlg.add(atp, BorderLayout.CENTER);
        dlg.add(btnBar, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private void showHint(boolean proMode) {
        if (!engineInteractive.isRoundActive()) {
            statusLabel.setText("Deal first!");
            return;
        }
        Hand playerHand = engineInteractive.getPlayerHands().isEmpty() ? null : engineInteractive.getPlayerHands().get(0);
        Card dealerUp = engineInteractive.getDealerHand().getCards().isEmpty() ? null : engineInteractive.getDealerHand().getCards().get(0);
        if (playerHand == null || dealerUp == null) {
            statusLabel.setText("No hand to analyze");
            return;
        }
        StrategyContext ctx = new StrategyContext();
        ctx.canDouble = playerHand.getCards().size() == 2;
        ctx.canSplit = playerHand.getCards().size() == 2 &&
                playerHand.getCards().get(0).getRank() == playerHand.getCards().get(1).getRank();
        ctx.trueCount = counterInteractive.getTrueCount(shoeInteractive.remainingDecks());

        Action suggestion;
        String prefix;
        if (proMode) {
            AdvancedStrategyImpl advStrategy = new AdvancedStrategyImpl();
            suggestion = advStrategy.decide(playerHand, dealerUp, ctx);
            prefix = "Pro: ";
        } else {
            BasicStrategyImpl basicStrategy = new BasicStrategyImpl();
            suggestion = basicStrategy.decide(playerHand, dealerUp, ctx);
            prefix = "Hint: ";
        }

        String actionText;
        switch (suggestion) {
            case HIT: actionText = "HIT"; break;
            case STAND: actionText = "STAND"; break;
            case DOUBLE: actionText = "DOUBLE"; break;
            case SPLIT: actionText = "SPLIT"; break;
            case SURRENDER: actionText = "SURRENDER"; break;
            default: actionText = "?"; break;
        }
        statusLabel.setText(prefix + actionText);
    }

    private void playHit() {
        RoundOutcome o = engineInteractive.playerHit();
        if (o != null) {
            animateDealerSequence(o);
        } else {
            updateTable();
            checkAutoStand();
            updateControlsState(engineInteractive.isRoundActive());
        }
    }

    private void playStand() {
        RoundOutcome o = engineInteractive.playerStand();
        if (o != null) animateDealerSequence(o); else updateTable();
    }

    private void playDouble() {
        RoundOutcome o = engineInteractive.playerDouble();
        if (o != null) animateDealerSequence(o); else updateTable();
    }

    private void checkAutoStand() {
        if (currentMode == Mode.INTERACTIVE && engineInteractive.isRoundActive()) {
            if (engineInteractive.getActiveHand().bestValue() == 21) {
                playStand();
            }
        }
    }

    private JButton createButton(String t, Color bg, Dimension d) {
        JButton b = new JButton(t); b.setBackground(bg); b.setPreferredSize(d);
        b.setFont(new Font("SansSerif", Font.BOLD, 18));
        b.setFocusPainted(false);
        return b;
    }

    private int getSpeedDelay() {
        int v = speedSlider.getValue();
        return v >= 10 ? 50 : 1600 - (int)((1600 - 100) * ((double)(v - 1) / 9.0));
    }

    private void switchMode(Mode m) {
        if (currentMode == m) return;

        if (animationTimer != null) animationTimer.stop();
        if (simulationEngine != null) simulationEngine.pause();
        if (exercisePanel != null) {
            exercisePanel.pause();
            mainPanel.remove(exercisePanel);
        }

        currentMode = m;
        modeMenuBtn.setText("Mode: " + m.name() + " ▼");
        pauseResumeBtn.setText("▶ Start " + m.name());

        boolean isInteractive = (m == Mode.INTERACTIVE);
        boolean isSim = (m == Mode.SIMULATION);
        boolean isExercise = (m == Mode.EXERCISE);


        bottomContainer.setVisible(isInteractive);
        statsLabel.setVisible(isInteractive);
        tablePanel.setVisible(!isExercise);
        pauseResumeBtn.setVisible(isSim || isExercise);

        if (isSim) initSimulationMode();
        if (isExercise) initExerciseMode();

        updateControlsState(false);


        updateCountDisplay();
        mainPanel.revalidate(); mainPanel.repaint();
    }

    private void initSimulationMode() {
        if (simulationEngine == null) {
            simulationEngine = new SimulationEngine(new But(currentShoeSize), new HiLoCounter(), new SimulationEngine.Listener() {
                @Override public void onRoundStarting() {

                    updateCountDisplay();
                    simDealerCardsRevealedCount = -1;
                }
                @Override public void onStateUpdated() { updateTableForSimulation(); updateCountDisplay(); }
                @Override public void onRoundFinished(RoundOutcome o) {
                    simulationEngine.pause();
                    startSimDealerRevealAnimation(o);
                }
            });

            updateCountDisplay();
        }
    }

    private void initExerciseMode() {
        if (exercisePanel == null) {
            shoeExercise = new But(currentShoeSize);
            counterExercise = new HiLoCounter();
            exercisePanel = new CountingExercisePanel(shoeExercise, counterExercise, cardBackImage);
            exercisePanel.setListener(new CountingExercisePanel.Listener() {
                @Override public void onCardDealt(Card c) { updateCountDisplay(); }
                @Override public void onShuffle() {
                    JOptionPane.showMessageDialog(BlackjackGUI.this, "Shoe Reshuffled!", "Exercise Info", JOptionPane.INFORMATION_MESSAGE);
                    updateCountDisplay();
                }
            });
        }
        mainPanel.add(exercisePanel, BorderLayout.CENTER);
    }

    private void renderEngineState(GameEngine engine, int dRevealed, int pAnim) {
        if (!tablePanel.isVisible()) return;
        dealerPanel.removeAll();
        List<Card> dCards = engine.getDealerHand().getCards();
        if (dCards.isEmpty()) return;

        boolean isPlayerTurn = (dRevealed == -1);
        int dLimit = (pAnim >= 0 && pAnim < 4) ? (pAnim + 1) / 2 : (isPlayerTurn ? 2 : dRevealed);

        if (dLimit > dCards.size()) dLimit = dCards.size();
        for (int i = 0; i < dLimit; i++) {
            if (i == 1 && isPlayerTurn) dealerPanel.add(new JLabel(new ImageIcon(getBackImageScaled())));
            else dealerPanel.add(createCardLabel(dCards.get(i)));
        }

        int visibleLimit = isPlayerTurn ? 1 : dLimit;
        int score = calculateVisibleScore(dCards, visibleLimit);
        dealerTitleLabel.setText("DEALER" + (score > 0 ? " (" + score + ")" : ""));

        playerHandContainer.removeAll();
        int pLimit = (pAnim >= 0 && pAnim < 4) ? (pAnim / 2) + 1 : 99;
        for (int i = 0; i < engine.getPlayerHands().size(); i++) {
            playerHandContainer.add(createPlayerHandPanel(engine.getPlayerHands().get(i), (i == engine.getActiveHandIndex() && isPlayerTurn), i, pLimit));
        }
        dealerPanel.revalidate(); dealerPanel.repaint();
        playerHandContainer.revalidate(); playerHandContainer.repaint();
        updateCountDisplay();
    }

    private int calculateVisibleScore(List<Card> cards, int limit) {
        Hand h = new Hand();
        for (int i = 0; i < Math.min(limit, cards.size()); i++) h.addCard(cards.get(i));
        return h.bestValue();
    }

    private void updateTable() { renderEngineState(engineInteractive, dealerCardsRevealedCount, playerCardsRevealedCount); }
    private void updateTableForSimulation() { renderEngineState(simulationEngine.getEngine(), simDealerCardsRevealedCount, -1); }

    private void startRound() {
        statusLabel.setText("Dealing...");
        statusLabel.setBackground(Color.WHITE);
        statusLabel.setForeground(Color.BLACK);

        if (shoeInteractive.remainingDecks() < 0.5) {
            JOptionPane.showMessageDialog(this, "Shoe Reshuffled!", "Trainer Info", JOptionPane.INFORMATION_MESSAGE);
        }

        engineInteractive.startRound();
        dealerCardsRevealedCount = -1; playerCardsRevealedCount = 0;
        updateControlsState(false);
        if (animationTimer != null) animationTimer.stop();
        animationTimer = new Timer(getSpeedDelay(), e -> {
            playerCardsRevealedCount++; updateTable();
            if (playerCardsRevealedCount >= 4) {
                ((Timer)e.getSource()).stop();
                playerCardsRevealedCount = -1;
                updateTable();
                updateControlsState(true);
                statusLabel.setText("Your Turn");
                checkAutoStand();
            }
        });
        animationTimer.start();
    }

    private void animateDealerSequence(RoundOutcome outcome) {
        updateControlsState(false); dealerCardsRevealedCount = 2; updateTable();
        if (animationTimer != null) animationTimer.stop();
        animationTimer = new Timer(getSpeedDelay(), e -> {
            if (dealerCardsRevealedCount < engineInteractive.getDealerHand().getCards().size()) {
                dealerCardsRevealedCount++; updateTable();
            } else { ((Timer)e.getSource()).stop(); showRoundResult(outcome); }
        });
        animationTimer.start();
    }

    private void startSimDealerRevealAnimation(RoundOutcome o) {
        simDealerCardsRevealedCount = 2; updateTableForSimulation();
        Timer t = new Timer(getSpeedDelay(), e -> {
            if (simDealerCardsRevealedCount < simulationEngine.getEngine().getDealerHand().getCards().size()) {
                simDealerCardsRevealedCount++; updateTableForSimulation();
            } else {
                ((Timer)e.getSource()).stop();
                if (simulationEngine != null && currentMode == Mode.SIMULATION) simulationEngine.start();
            }
        });
        t.start();
    }

    private void updateControlsState(boolean inGame) {
        boolean isInt = (currentMode == Mode.INTERACTIVE);
        btnDeal.setEnabled(!inGame && isInt);
        btnHit.setEnabled(inGame && isInt);
        btnStand.setEnabled(inGame && isInt);
        btnDouble.setEnabled(inGame && isInt && engineInteractive.canDouble());
        btnSplit.setEnabled(inGame && isInt && engineInteractive.canSplit());
        btnHint.setEnabled(inGame && isInt);
        btnProHint.setEnabled(inGame && isInt);
    }

    private JPanel createPlayerHandPanel(Hand h, boolean active, int idx, int limit) {
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false);
        JLabel l = new JLabel("Hand " + (idx+1) + " (" + h.bestValue() + ")", SwingConstants.CENTER);
        l.setForeground(active ? Color.YELLOW : Color.WHITE);
        l.setFont(new Font("SansSerif", Font.BOLD, 18));
        p.add(l, BorderLayout.NORTH);
        JPanel cp = new JPanel(new FlowLayout()); cp.setOpaque(false);
        if (active) cp.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        for (int i = 0; i < Math.min(limit, h.getCards().size()); i++) cp.add(createCardLabel(h.getCards().get(i)));
        p.add(cp, BorderLayout.CENTER);
        return p;
    }

    private JLabel createCardLabel(Card card) {
        JLabel l = new JLabel();
        try {
            String suit = card.getSuit().name().toLowerCase();
            String rank = switch(card.getRank()){case 1->"A";case 11->"J";case 12->"Q";case 13->"K";default->String.valueOf(card.getRank());};
            URL u = getClass().getResource("/resources/cards/" + suit + "_" + rank + ".png");
            if(u!=null) l.setIcon(new ImageIcon(ImageIO.read(u).getScaledInstance(110, 160, Image.SCALE_SMOOTH)));
            else { l.setText(card.toString()); l.setPreferredSize(new Dimension(110, 160)); l.setOpaque(true); l.setBackground(Color.WHITE); }
        } catch(Exception e) { l.setText(card.toString()); }
        return l;
    }

    private Image getBackImageScaled() {
        if (cardBackImage != null) return cardBackImage.getScaledInstance(110, 160, Image.SCALE_SMOOTH);
        BufferedImage b = new BufferedImage(110, 160, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = b.createGraphics(); g.setColor(Color.BLACK); g.fillRect(0,0,110,160); g.dispose();
        return b;
    }

    private void showRoundResult(RoundOutcome o) {
        double p = o.totalPayout(); totalProfit += p;
        if (p > 0) totalWins++; else if (p < 0) totalLosses++;
        statsLabel.setText(String.format("Wins: %d | Losses: %d | Profit: %.1f", totalWins, totalLosses, totalProfit));
        statusLabel.setText((p>0?"WINNER! ":p<0?"DEALER WINS ":"PUSH ") + "(" + (p>=0?"+":"")+p+")");
        statusLabel.setBackground(p>0?Color.GREEN:p<0?Color.RED:Color.GRAY);
        statusLabel.setForeground(Color.WHITE);
        btnDeal.setEnabled(true);
    }

    private void updateCountDisplay() {
        int cardsRemaining = 0;
        double decksRemaining = 1.0;

        if (currentMode == Mode.EXERCISE && shoeExercise != null) {
            cardsRemaining = shoeExercise.remainingCards();
            decksRemaining = shoeExercise.remainingDecks();
        } else if (currentMode == Mode.SIMULATION && simulationEngine != null) {
            cardsRemaining = simulationEngine.getShoe().remainingCards();
            decksRemaining = simulationEngine.getShoe().remainingDecks();
        } else if (shoeInteractive != null) {
            cardsRemaining = shoeInteractive.remainingCards();
            decksRemaining = shoeInteractive.remainingDecks();
        }

        cardsRemainingLabel.setText(String.format("Cards: %d (%.1f decks)", cardsRemaining, decksRemaining));

        if (!showCountToggle.isSelected()) {
            countLabel.setVisible(false);
            return;
        }

        String text = " ";
        if (currentMode == Mode.EXERCISE && counterExercise != null && shoeExercise != null) {
            text = String.format("Running: %d | True: %.1f", counterExercise.getRunningCount(), counterExercise.getTrueCount(shoeExercise.remainingDecks()));
        } else if (currentMode == Mode.SIMULATION && simulationEngine != null) {
            text = String.format("Running: %d | True: %.1f", simulationEngine.getEngine().getRunningCount(), simulationEngine.getEngine().getTrueCount());
        } else if (counterInteractive != null && shoeInteractive != null) {
            text = String.format("Running: %d | True: %.1f", counterInteractive.getRunningCount(), counterInteractive.getTrueCount(shoeInteractive.remainingDecks()));
        }
        countLabel.setText(text);


        if (showCountToggle.isShowing()) {
            try {
                Point btnPos = SwingUtilities.convertPoint(showCountToggle, 0, showCountToggle.getHeight(), getLayeredPane());
                int labelWidth = 180;
                int btnCenter = btnPos.x + showCountToggle.getWidth() / 2;
                int labelX = btnCenter - labelWidth / 2;
                countLabel.setBounds(labelX, btnPos.y, labelWidth, 16);
                countLabel.setVisible(true);
            } catch (Exception ex) {
                countLabel.setVisible(false);
            }
        }
    }

    private void togglePauseForMode() {
        if (currentMode == Mode.SIMULATION && simulationEngine != null) {
            if (simulationEngine.isRunning()) {
                simulationEngine.pause(); pauseResumeBtn.setText("▶ Start SIMULATION");
            } else {
                simulationEngine.setDelay(getSpeedDelay()); simulationEngine.start(); pauseResumeBtn.setText("⏸ Pause SIMULATION");
            }
        } else if (currentMode == Mode.EXERCISE && exercisePanel != null) {
            if (exercisePanel.isRunning()) {
                exercisePanel.pause(); pauseResumeBtn.setText("▶ Start EXERCISE");
            } else {
                exercisePanel.setDelay(getSpeedDelay()); exercisePanel.start(); pauseResumeBtn.setText("⏸ Pause EXERCISE");
            }
        }
    }

    private void reshuffleAllShoes() {
        shoeInteractive = new But(currentShoeSize);
        counterInteractive = new HiLoCounter();
        engineInteractive = new GameEngine(shoeInteractive, counterInteractive);

        if (shoeExercise != null) {
            shoeExercise = new But(currentShoeSize);
            counterExercise = new HiLoCounter();
            if (exercisePanel != null) {
                exercisePanel.setShoeAndCounter(shoeExercise, counterExercise);
            }
        }

        if (simulationEngine != null) {
            simulationEngine = new SimulationEngine(new But(currentShoeSize), new HiLoCounter(), new SimulationEngine.Listener() {
                @Override public void onRoundStarting() { updateCountDisplay(); simDealerCardsRevealedCount = -1; }
                @Override public void onStateUpdated() { updateTableForSimulation(); updateCountDisplay(); }
                @Override public void onRoundFinished(RoundOutcome o) {
                    simulationEngine.pause();
                    startSimDealerRevealAnimation(o);
                }
            });
        }

        updateCountDisplay();
        updateTable();
        JOptionPane.showMessageDialog(this, "Shoe reshuffled with " + currentShoeSize + " decks!", "Reshuffle", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showModeMenu(Component inv) {
        JPopupMenu p = new JPopupMenu();
        for (Mode m : Mode.values()) { JMenuItem i = new JMenuItem(m.name()); i.addActionListener(e -> switchMode(m)); p.add(i); }
        p.show(inv, 0, inv.getHeight());
    }

    private void toggleStrategyDock() {
        strategyDockVisible = !strategyDockVisible;
        strategyDock.setVisible(strategyDockVisible);

        if (strategyDockVisible) {
            strategyPanel.setDisplayHeight(this.getHeight() - 120);
        }
        mainPanel.revalidate(); mainPanel.repaint();
    }


    private void showStrategyFullscreen() {
        StrategyPanel sp = new StrategyPanel();

        sp.setVariant(StrategyPanel.VARIANT_BASIC);


        JDialog dlg = new JDialog(this, "Tabela strategii", true);
        dlg.setUndecorated(true);

        Rectangle bounds = this.getBounds();

        bounds = new Rectangle(bounds.x + 10, bounds.y + 40, Math.max(200, bounds.width - 20), Math.max(200, bounds.height - 80));
        dlg.setBounds(bounds);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JToggleButton tBasic = new JToggleButton("Podstawowa");
        JToggleButton tAdv = new JToggleButton("Rozszerzona");
        ButtonGroup bg = new ButtonGroup(); bg.add(tBasic); bg.add(tAdv);
        tBasic.setSelected(true);
        tBasic.addActionListener(e -> sp.setVariant(StrategyPanel.VARIANT_BASIC));
        tAdv.addActionListener(e -> sp.setVariant(StrategyPanel.VARIANT_ADVANCED));


        sp.setDisplayHeight(bounds.height - 80);

        JButton btnClose = new JButton("Zamknij");
        btnClose.addActionListener(e -> dlg.dispose());

        topBar.add(tBasic); topBar.add(tAdv); topBar.add(Box.createHorizontalStrut(20)); topBar.add(btnClose);

        dlg.setLayout(new BorderLayout());
        dlg.add(topBar, BorderLayout.NORTH);
        dlg.add(sp, BorderLayout.CENTER);


        dlg.getRootPane().registerKeyboardAction(e -> dlg.dispose(), KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);

        dlg.setVisible(true);
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new BlackjackGUI().setVisible(true)); }
}
