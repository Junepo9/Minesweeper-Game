import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Minesweeper extends JFrame {
    private GamePanel gamePanel;

    public Minesweeper() {
        setTitle("Minesweeper Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        gamePanel = new GamePanel();
        add(gamePanel);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Minesweeper());
    }
}

class GamePanel extends JPanel {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int MINES = 15;
    private static final int CELL_SIZE = 40;
    
    private Cell[][] grid;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private boolean gameOver;
    private boolean won;
    private int revealedCount;

    public GamePanel() {
        setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE + 60));
        setBackground(new Color(200, 200, 200));
        setFocusable(true);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseClick(e);
            }
        });
        
        initializeGame();
    }

    private void initializeGame() {
        grid = new Cell[ROWS][COLS];
        revealed = new boolean[ROWS][COLS];
        flagged = new boolean[ROWS][COLS];
        gameOver = false;
        won = false;
        revealedCount = 0;
        
        // Initialize all cells as empty
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = new Cell(false);
            }
        }
        
        // Place mines randomly
        Random rand = new Random();
        int minesPlaced = 0;
        while (minesPlaced < MINES) {
            int row = rand.nextInt(ROWS);
            int col = rand.nextInt(COLS);
            if (!grid[row][col].isMine()) {
                grid[row][col].setMine(true);
                minesPlaced++;
            }
        }
        
        // Calculate adjacent mine counts
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (!grid[i][j].isMine()) {
                    grid[i][j].setAdjacentMines(countAdjacentMines(i, j));
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < ROWS && j >= 0 && j < COLS && grid[i][j].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void handleMouseClick(MouseEvent e) {
        if (gameOver || won) {
            if (e.getY() > ROWS * CELL_SIZE) {
                initializeGame();
                repaint();
            }
            return;
        }
        
        int col = e.getX() / CELL_SIZE;
        int row = e.getY() / CELL_SIZE;
        
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return;
        
        if (SwingUtilities.isRightMouseButton(e)) {
            // Right click - toggle flag
            if (!revealed[row][col]) {
                flagged[row][col] = !flagged[row][col];
            }
        } else {
            // Left click - reveal cell
            if (!flagged[row][col] && !revealed[row][col]) {
                revealCell(row, col);
            }
        }
        
        repaint();
    }

    private void revealCell(int row, int col) {
        if (revealed[row][col]) return;
        
        revealed[row][col] = true;
        revealedCount++;
        
        if (grid[row][col].isMine()) {
            gameOver = true;
            revealAllMines();
            return;
        }
        
        // Check win condition
        if (revealedCount == ROWS * COLS - MINES) {
            won = true;
            return;
        }
        
        // If no adjacent mines, reveal all adjacent cells
        if (grid[row][col].getAdjacentMines() == 0) {
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (i >= 0 && i < ROWS && j >= 0 && j < COLS && !revealed[i][j]) {
                        revealCell(i, j);
                    }
                }
            }
        }
    }

    private void revealAllMines() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (grid[i][j].isMine()) {
                    revealed[i][j] = true;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw grid
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                drawCell(g2d, i, j);
            }
        }
        
        // Draw game status
        drawStatus(g2d);
    }

    private void drawCell(Graphics2D g, int row, int col) {
        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;
        
        if (revealed[row][col]) {
            // Revealed cell
            g.setColor(new Color(220, 220, 220));
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1));
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            
            if (grid[row][col].isMine()) {
                g.setColor(Color.RED);
                g.fillOval(x + 10, y + 10, 20, 20);
            } else if (grid[row][col].getAdjacentMines() > 0) {
                g.setColor(Color.BLUE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString(String.valueOf(grid[row][col].getAdjacentMines()), x + 14, y + 28);
            }
        } else {
            // Unrevealed cell
            g.setColor(new Color(150, 150, 150));
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2));
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            
            if (flagged[row][col]) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawString("🚩", x + 12, y + 28);
            }
        }
    }

    private void drawStatus(Graphics2D g) {
        int statusY = ROWS * CELL_SIZE + 20;
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("GAME OVER! You hit a mine!", 20, statusY);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Click to restart", 20, statusY + 25);
        } else if (won) {
            g.setColor(Color.GREEN);
            g.drawString("YOU WON! Congratulations!", 20, statusY);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Click to play again", 20, statusY + 25);
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Left Click: Reveal | Right Click: Flag", 20, statusY);
            g.drawString("Mines: " + MINES + " | Revealed: " + revealedCount, 20, statusY + 25);
        }
    }
}

class Cell {
    private boolean isMine;
    private int adjacentMines;

    public Cell(boolean isMine) {
        this.isMine = isMine;
        this.adjacentMines = 0;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int count) {
        adjacentMines = count;
    }
}
