import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MainMenuPanel extends JPanel {
    private GameFrame gameFrame;
    private Image backgroundImage;

    public MainMenuPanel(GameFrame gameFrame, String fileName) {
        this.gameFrame = gameFrame;
        try {
            backgroundImage = new ImageIcon(getClass().getResource(fileName)).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
        initButtonsPanel();
    }



    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    private void initButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(Box.createVerticalStrut(150));

        JLabel titleLabel = new JLabel("Dizzy DODO", JLabel.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 128, 0));
        buttonsPanel.add(titleLabel);
        buttonsPanel.add(Box.createVerticalStrut(20));

        String[] buttonLabels = {"Indian Summer", "Underworld", "Water World", "Champagne Factory", "Scores"};
        for (String label : buttonLabels) {
            JButton button = createRoundedButton(label);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(200, 30));
            if (label.equals("Scores")) {
                button.addActionListener(e -> gameFrame.switchTo("Scores"));
            } else {
                button.addActionListener(e -> gameFrame.switchTo(label.replace(" ", "") + "Level"));
            }
            buttonsPanel.add(button);
            buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        add(buttonsPanel, BorderLayout.CENTER);
    }


    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }

        };
        button.setForeground(Color.BLACK);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        return button;
    }
}
