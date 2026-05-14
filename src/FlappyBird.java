import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    Image bgImg;
    Image birdyImg;
    Image topPipeImg;
    Image bottomPipeImg;

    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    int yVelocity = 0;
    int gravity = 1;

    Bird bird;
    Timer gameLoopTimer;

    public class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
        
    }

    FlappyBird(){
        bgImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdyImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.green);
        setFocusable(true);
        addKeyListener(this);

        bird = new Bird(birdyImg);
        gameLoopTimer = new Timer(1000/60, this);
        gameLoopTimer.start();

    }

    public void move(){
        yVelocity += gravity;
        bird.y += yVelocity;
        bird.y = Math.max(bird.y, 0);
    }

    public void draw(Graphics g) {
        g.drawImage(bgImg, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
        //System.out.println("hi");
    }

    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    @Override //ew #VEX reference lololol
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            yVelocity =  -9;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

}
