import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
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
    int xVelocity = -4;
    int yVelocity = 0;
    int gravity = 1;

    Bird bird;
    Timer gameLoopTimer;

    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    ArrayList<Pipe> pipes;
    Timer placePipeTimer;

    boolean gameOver = false;
    double score = 0;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

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
        pipes = new ArrayList<Pipe>();
        Random rand = new Random();

        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipeTimer.start();

        gameLoopTimer = new Timer(1000/60, this);
        gameLoopTimer.start();

    }

    public void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void move(){
        yVelocity += gravity;
        bird.y += yVelocity;
        bird.y = Math.max(bird.y, 0);

        for (int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += xVelocity;

            if (!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += .5;

            }

            if (collision(bird, pipe)){
                gameOver = true;
            }
        }

        if (bird.y > boardHeight){
            gameOver = true;
        }
    }

    public boolean collision(Bird bird, Pipe pipe){
        return bird.x < pipe.x + pipe.width &&
        bird.x + bird.width > pipe.x &&
        bird.y < pipe.y + pipe.height &&
        bird.y + bird.height > pipe.y;
    }

    public void draw(Graphics g){
        g.drawImage(bgImg, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
        //System.out.println("hi");  

        for (int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
            
        } else{
            g.drawString(String.valueOf((int) score ), 10, 35);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    @Override //ew #VEX reference lololol
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if (gameOver){
            placePipeTimer.stop();
            gameLoopTimer.stop();
        }
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
