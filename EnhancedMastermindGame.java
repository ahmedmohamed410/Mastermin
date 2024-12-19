import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class EnhancedMastermindGame {
    private static final String[] COLORS = {"Red", "Green", "Blue", "Yellow", "Purple", "Orange"};
    private static final int CODE_LENGTH = 4;
    private static final int MAX_ATTEMPTS = 10;

    private String[] secretCode;
    private int attempts;

    private JFrame frame;
    private JLabel feedbackLabel;
    private JButton submitButton;
    private JComboBox<String>[] colorSelectors;

    public EnhancedMastermindGame() {
        secretCode = generateSecretCode();
        attempts = 0;
        createAndShowGUI();
    }

    private String[] generateSecretCode() {
        Random random = new Random();
        String[] code = new String[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            code[i] = COLORS[random.nextInt(COLORS.length)];
        }
        return code;
    }

    private void createAndShowGUI() {
        frame = new JFrame("Mastermind Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(40, 42, 54));

        // Title
        JLabel titleLabel = new JLabel("Mastermind", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        frame.add(titleLabel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(40, 42, 54));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Instructions
        JLabel instructions = new JLabel("Choose 4 colors to guess. You have 10 attempts!");
        instructions.setFont(new Font("Arial", Font.PLAIN, 18));
        instructions.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(instructions, gbc);

        // Color Selectors
        JPanel colorPanel = new JPanel(new GridLayout(1, CODE_LENGTH, 10, 10));
        colorPanel.setBackground(new Color(40, 42, 54));
        colorSelectors = new JComboBox[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            colorSelectors[i] = new JComboBox<>(COLORS);
            colorSelectors[i].setFont(new Font("Arial", Font.PLAIN, 14));
            colorSelectors[i].setBackground(Color.WHITE);
            colorSelectors[i].setForeground(Color.BLACK);
            colorPanel.add(colorSelectors[i]);
        }
        gbc.gridy = 1;
        mainPanel.add(colorPanel, gbc);

        // Submit Button
        submitButton = new JButton("Submit Guess");
        submitButton.setFont(new Font("Arial", Font.BOLD, 18));
        submitButton.setBackground(new Color(46, 139, 87));
        submitButton.setForeground(Color.WHITE);
        gbc.gridy = 2;
        mainPanel.add(submitButton, gbc);

        // Feedback Label
        feedbackLabel = new JLabel("", JLabel.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        feedbackLabel.setForeground(Color.WHITE);
        gbc.gridy = 3;
        mainPanel.add(feedbackLabel, gbc);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Add action listener to submit button
        submitButton.addActionListener(e -> checkGuess());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void checkGuess() {
        String[] guess = new String[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            guess[i] = (String) colorSelectors[i].getSelectedItem();
        }

        attempts++;
        int correctPosition = 0;
        int correctColor = 0;

        boolean[] codeUsed = new boolean[CODE_LENGTH];
        boolean[] guessUsed = new boolean[CODE_LENGTH];

        // Check correct positions
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guess[i].equals(secretCode[i])) {
                correctPosition++;
                codeUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Check correct colors
        for (int i = 0; i < CODE_LENGTH; i++) {
            for (int j = 0; j < CODE_LENGTH; j++) {
                if (!codeUsed[j] && !guessUsed[i] && guess[i].equals(secretCode[j])) {
                    correctColor++;
                    codeUsed[j] = true;
                    guessUsed[i] = true;
                    break;
                }
            }
        }

        // Display guess result
        if (correctPosition == CODE_LENGTH) {
            endGame(true);
        } else if (attempts >= MAX_ATTEMPTS) {
            endGame(false);
        } else {
            feedbackLabel.setText(String.format("Correct colors in right position: %d, Correct colors in wrong position: %d",
                    correctPosition, correctColor));
        }
    }

    private void endGame(boolean won) {
        submitButton.setEnabled(false);

        if (won) {
            feedbackLabel.setText("Congratulations! You guessed the secret code!");
            feedbackLabel.setForeground(Color.GREEN);
        } else {
            feedbackLabel.setText("Game Over! The secret code was: " + String.join(", ", secretCode));
            feedbackLabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EnhancedMastermindGame::new);
    }
}
