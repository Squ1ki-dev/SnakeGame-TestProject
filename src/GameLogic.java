import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class GameLogic extends JPanel implements ActionListener
{
    public enum Direction
    {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private Timer timer;
    private Random random;

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;

    private static final int UNIT_SIZE = 30;
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

    private static final int DELAY = 75;

    private final int xCoordinate[] = new int[GAME_UNITS];
    private final int yCoordinate[] = new int[GAME_UNITS];

    private int bodyParts = 6;
    private int fruitEaten;
    private int fruitXPos;
    private int fruitYPos;
    private Direction direction;
    private boolean moving = false;

    public GameLogic()
    {
        random = new Random();
        direction = Direction.RIGHT;

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyBind());

        startGame();
    }

    public void startGame()
    {
        newFruit();
        moving = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        drawGraphics(graphics);
    }

    public void drawGraphics(Graphics graphics)
    {
        if(!moving) gameOver(graphics);

        graphics.setColor(Color.red);
        graphics.fillOval(fruitXPos, fruitYPos, UNIT_SIZE, UNIT_SIZE);

        for (int i = 0; i < bodyParts; i++)
        {
            if(i == 0)
                graphics.setColor(Color.green);
            else
                graphics.setColor(new Color(45, 180, 0));

            graphics.fillRect(xCoordinate[i], yCoordinate[i], UNIT_SIZE, UNIT_SIZE);
            scoreText(graphics);
        }

    }

    public void movement()
    {
        for (int i = bodyParts; i > 0; i--)
        {
            xCoordinate[i] = xCoordinate[i-1];
            yCoordinate[i] = yCoordinate[i-1];
        }

        switch (direction)
        {
            case UP:
                yCoordinate[0] = yCoordinate[0] - UNIT_SIZE;
                break;
            case DOWN:
                yCoordinate[0] = yCoordinate[0] + UNIT_SIZE;
                break;
            case LEFT:
                xCoordinate[0] = xCoordinate[0] - UNIT_SIZE;
                break;
            case RIGHT:
                xCoordinate[0] = xCoordinate[0] + UNIT_SIZE;
                break;
        }
    }

    public void newFruit()
    {
        fruitXPos = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        fruitYPos = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void checkFruit()
    {
        if((xCoordinate[0] == fruitXPos) && (yCoordinate[0] == fruitYPos))
        {
            bodyParts++;
            fruitEaten++;
            newFruit();
        }
    }

    public void checkCollision()
    {
        for (int i = bodyParts; i > 0; i--)
        {
            if((xCoordinate[0] == xCoordinate[i]) && (yCoordinate[0] == yCoordinate[i]))
                moving = false;
        }

        if((xCoordinate[0] <= 0) || (xCoordinate[0] > SCREEN_WIDTH) || (yCoordinate[0] < 0) || (yCoordinate[0] > SCREEN_HEIGHT))
            moving = false;

        if(!moving)
            timer.stop();
    }

    private void setText(Graphics graphics, Color color, int fontSize)
    {
        graphics.setColor(color);
        graphics.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
    }

    public void scoreText(Graphics graphics)
    {
        setText(graphics, Color.red, 30);
        FontMetrics scoreFont = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + fruitEaten, (SCREEN_WIDTH - scoreFont.stringWidth("Score: " + fruitEaten)) / 2, graphics.getFont().getSize());
    }

    public void gameOver(Graphics graphics)
    {
        setText(graphics, Color.red, 75);
        FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (SCREEN_WIDTH - fontMetrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(moving)
        {
            movement();
            checkFruit();
            checkCollision();
        }
        repaint();
    }

    public class KeyBind extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent keyEvent)
        {
            switch (keyEvent.getKeyCode())
            {
                case KeyEvent.VK_A:
                    direction = Direction.LEFT;
                    break;
                case KeyEvent.VK_D:
                    direction = Direction.RIGHT;
                    break;
                case KeyEvent.VK_W:
                    direction = Direction.UP;
                    break;
                case KeyEvent.VK_S:
                    direction = Direction.DOWN;
                    break;
            }
        }
    }
}
