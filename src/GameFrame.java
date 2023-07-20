import javax.swing.*;

public class GameFrame extends JFrame
{
    private String snakeTitle = "Snake";
    public GameFrame()
    {
        this.add(new GameLogic());
        this.setTitle(snakeTitle);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
