package net.listerily.NinjaAdventure.ui;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.resources.CachedResources;
import net.listerily.NinjaAdventure.resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class MenuFrame extends AppBaseFrame {
    public MenuFrame(App app) {
        super(app);
        ResourceManager resourceManager = app.getResourceManager();
        CachedResources cachedResources = resourceManager.getCachedResources();
        Font titleFont, textFont;
        try {
            titleFont = cachedResources.readFont(Font.TRUETYPE_FONT, "HUD/Font/ka1.ttf");
            textFont = cachedResources.readFont(Font.TRUETYPE_FONT, "HUD/Font/Gameplay.ttf");
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        Dimension minimumSize = new Dimension();
        minimumSize.height = 720;
        minimumSize.width = 1240;
        setMinimumSize(minimumSize);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1));
        this.add(controlPanel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());
        controlPanel.add(titlePanel);
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel upperTitleLabel = new JLabel("NINJA", JLabel.CENTER);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        upperTitleLabel.setFont(titleFont.deriveFont(70f));
        titlePanel.add(upperTitleLabel, constraints);

        JLabel subTitleLabel = new JLabel("ADVENTURE", JLabel.CENTER);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        subTitleLabel.setFont(titleFont.deriveFont(150f));
        titlePanel.add(subTitleLabel, constraints);

        JLabel authorLabel = new JLabel("BY LISTERILY, ID: 2012930", JLabel.CENTER);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        authorLabel.setFont(textFont.deriveFont(20f));
        titlePanel.add(authorLabel, constraints);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        controlPanel.add(buttonsPanel);

        JButton newGameButton = new JButton("New Game");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        newGameButton.setFont(textFont.deriveFont(50f));
        newGameButton.addActionListener(e -> onNewGameClicked());
        buttonsPanel.add(newGameButton, constraints);

        JButton exitGameButton = new JButton("Exit Game");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        exitGameButton.setFont(textFont.deriveFont(50f));
        exitGameButton.addActionListener(e -> onExitClicked());
        buttonsPanel.add(exitGameButton, constraints);
    }

    private void onNewGameClicked() {
        new GameFrame(app).setVisible(true);
        this.setVisible(false);
    }

    private void onExitClicked() {
        System.exit(0);
    }

    @Override
    protected boolean requireTimedRepaint() {
        return true;
    }

    @Override
    protected boolean shouldExitOnWindowClose() {
        return true;
    }
}
