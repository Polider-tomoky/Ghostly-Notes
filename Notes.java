package ProjectNotes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Notes extends JFrame {
    private static Point mouseCoords = null;
    private static AboutComponent aboutComponent;
    static JFrame frame = new JFrame();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame.setIconImage(new ImageIcon("src\\ProjectNotes\\ava.png").getImage());

            frame.setUndecorated(true);
            frame.setBackground(new Color(0, 0, 0, 0));
            aboutComponent = new AboutComponent();
            frame.setContentPane(aboutComponent);

            frame.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    mouseCoords = e.getPoint();
                }
            });
            frame.addMouseMotionListener(new MouseAdapter() {
                public void mouseDragged(MouseEvent e) {
                    Point currCoords = e.getLocationOnScreen();
                    frame.setLocation(currCoords.x - mouseCoords.x, currCoords.y - mouseCoords.y);
                }
            });

            frame.pack();
            frame.setSize(2400, 500);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            frame.setAlwaysOnTop(true);

            // Создание редактора
            TextEditWindow textEditWindow = new TextEditWindow(aboutComponent);
            textEditWindow.setVisible(false);
            textEditWindow.setAlwaysOnTop(true);

            // Обработчик нажатия "Р (Редактор)" для вызова редактора
            frame.getRootPane().registerKeyboardAction(e -> {
                textEditWindow.setVisible(true);
                textEditWindow.requestFocus();
            }, KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

            // Выравнивание заметки по центру
            aboutComponent.setAlignmentX(Component.CENTER_ALIGNMENT);
        });
    }
    // Класс для отображения заметки
    static class AboutComponent extends JComponent {
        private String text = "";
        private int fontSize = 25;

        public void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            g.setFont(new Font("Arial", Font.PLAIN, fontSize));
            g.setColor(Color.WHITE);

            String[] lines = text.split("\n");

            int maxWidth = 0;
            for (String line : lines) {
                int textWidth = g.getFontMetrics().stringWidth(line);
                if (textWidth > maxWidth) {
                    maxWidth = textWidth;
                }
            }
            int x = (int) ((screenSize.getWidth() - maxWidth) / 2);
            int y = 100;

            for (String line : lines) {
                g.drawString(line, x, y);
                y += fontSize + 5;
            }
        }
        // Обновление текста и размера шрифта
        public void updateText(String newText, int newFontSize) {
            text = newText;
            fontSize = newFontSize;
            repaint();
        }
    }
    // Окно редактора текста
    static class TextEditWindow extends JFrame {
        public TextEditWindow(AboutComponent aboutComponent) {
            setTitle("Редактор заметки");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new FlowLayout());

            JTextArea textArea = new JTextArea("Введите свою заметку", 5, 20);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            add(new JScrollPane(textArea));

            JTextField fontSizeField = new JTextField("25", 5);
            add(fontSizeField);

            JButton applyButton = new JButton("Сохранить");
            applyButton.addActionListener(e -> {
                String newText = textArea.getText();
                int newFontSize = Integer.parseInt(fontSizeField.getText());
                aboutComponent.updateText(newText, newFontSize);
            });
            add(applyButton);

            pack();
            setLocationRelativeTo(null);
        }
    }
}