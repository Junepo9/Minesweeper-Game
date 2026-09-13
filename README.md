# Minesweeper Game 🎮

A classic Minesweeper game built in Java with a graphical user interface (GUI).

## Features ✨

- 10x10 grid with 15 randomly placed mines
- Left-click to reveal cells
- Right-click to place/remove flags
- Automatic reveal of adjacent cells when clicking on a cell with no nearby mines
- Win condition: Reveal all non-mine cells
- Lose condition: Click on a mine
- Restart game after winning or losing
- Color-coded numbers showing adjacent mines

## Game Rules 📋

1. **Reveal Cells**: Left-click on unrevealed cells to reveal them
2. **Flag Suspicious Cells**: Right-click to flag cells you think contain mines
3. **Numbers**: Show how many mines are adjacent to that cell (0-8)
4. **Win**: Reveal all cells that don't contain mines
5. **Lose**: Click on a cell with a mine
6. **Cascade Reveal**: If you click a cell with 0 adjacent mines, all nearby cells auto-reveal

## How to Run 🚀

### Requirements
- Java 8 or higher
- A Java IDE (Eclipse, IntelliJ, NetBeans) OR command line

### Option 1: Using Command Line
```bash
# Navigate to the repository folder
cd Minesweeper-Game

# Compile the Java file
javac Minesweeper.java

# Run the game
java Minesweeper
```

### Option 2: Using an IDE
1. Open your IDE (Eclipse, IntelliJ, etc.)
2. Create a new project
3. Copy `Minesweeper.java` into the `src` folder
4. Run the file as a Java Application

## Controls 🎮

| Action | Button |
|--------|--------|
| Reveal Cell | Left Mouse Click |
| Flag/Unflag | Right Mouse Click |
| Restart | Click anywhere after game ends |

## Game Statistics 📊

- **Grid Size**: 10x10 cells
- **Total Cells**: 100
- **Mine Count**: 15
- **Safe Cells**: 85

## Code Structure 📁

- **Minesweeper.java**: Main entry point, creates the game window
- **GamePanel.java**: Handles game logic, rendering, and mouse input
- **Cell.java**: Represents individual cells in the grid

## How It Works 🔧

1. **Initialization**: Game randomly places 15 mines on the grid
2. **Adjacent Mines**: Each non-mine cell calculates how many mines are adjacent
3. **Reveal Logic**: When a cell is clicked, it reveals and checks if it's a mine
4. **Cascade Effect**: If a cell has 0 adjacent mines, all neighbors auto-reveal
5. **Win Condition**: Check if all non-mine cells are revealed
6. **Game Over**: If a mine is clicked, reveal all mines and end game

## Future Enhancements 🎯

- Difficulty levels (Easy, Medium, Hard)
- Custom grid sizes
- Timer for speedrun mode
- Mine counter
- Sound effects
- Highscore tracking

## License 📜

This project is open source and free to use and modify.

---

Enjoy playing! 🎉
