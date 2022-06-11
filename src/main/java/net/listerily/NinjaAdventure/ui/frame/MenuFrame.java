package net.listerily.NinjaAdventure.ui.frame;

import net.listerily.NinjaAdventure.App;
import net.listerily.NinjaAdventure.resources.CachedResources;
import net.listerily.NinjaAdventure.resources.ResourceManager;
import net.listerily.NinjaAdventure.ui.dialog.JoinDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MenuFrame extends AppBaseFrame {
    public MenuFrame(App app) {
        super(app);

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
        upperTitleLabel.setFont(getTitleFont().deriveFont(70f));
        titlePanel.add(upperTitleLabel, constraints);

        JLabel subTitleLabel = new JLabel("ADVENTURE", JLabel.CENTER);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        subTitleLabel.setFont(getTitleFont().deriveFont(150f));
        titlePanel.add(subTitleLabel, constraints);

        JLabel authorLabel = new JLabel("BY LISTERILY, ID: 2012930", JLabel.CENTER);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        authorLabel.setFont(getTextFont().deriveFont(20f));
        titlePanel.add(authorLabel, constraints);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        controlPanel.add(buttonsPanel);
        constraints.insets = new Insets(15, 0, 15, 0);

        JButton newGameButton = new JButton("New Game");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        newGameButton.setFont(getTextFont().deriveFont(50f));
        newGameButton.setBackground(new Color(52,52,52));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setFocusPainted(false);
        newGameButton.setBorderPainted(false);
        newGameButton.addActionListener(e -> onNewGameClicked());
        buttonsPanel.add(newGameButton, constraints);

        JButton joinGameButton = new JButton("Join Game");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        joinGameButton.setFont(getTextFont().deriveFont(50f));
        joinGameButton.setBackground(new Color(52,52,52));
        joinGameButton.setForeground(Color.WHITE);
        joinGameButton.setFocusPainted(false);
        joinGameButton.setBorderPainted(false);
        joinGameButton.addActionListener(e -> onJoinGameClicked());
        buttonsPanel.add(joinGameButton, constraints);

        JButton exitGameButton = new JButton("Exit Game");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        exitGameButton.setFont(getTextFont().deriveFont(50f));
        exitGameButton.setBackground(new Color(52,52,52));
        exitGameButton.setForeground(Color.WHITE);
        exitGameButton.setFocusPainted(false);
        exitGameButton.setBorderPainted(false);
        exitGameButton.addActionListener(e -> onExitClicked());
        buttonsPanel.add(exitGameButton, constraints);
    }

    private void onNewGameClicked() {
        app.getWindowManager().hideMenuFrame();
        app.getWindowManager().showGameFrame(new GameFrame.GameLaunchOptions(2022));
    }

    private void onJoinGameClicked() {
        JoinDialog joinDialog = new JoinDialog(app);
        joinDialog.setVisible(true);
    }

    private void onExitClicked() {
        System.exit(0);
    }

    @Override
    protected boolean shouldExitOnWindowClose() {
        return true;
    }
}
