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
    Image loseImg;

    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;
    int xVelocity = 0;
    int yVelocity = 0;
    int gravity = 0;

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
    boolean gameStarted = false;

    JButton playAgain = new JButton("Play again?");


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
        loseImg = new ImageIcon(getClass().getResource("./lose screen.png")).getImage();

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
        gameLoopTimer = new Timer(1000/60, this);

        playAgain.addActionListener(e -> {
            if (!gameStarted){
                gameStarted = true;
                System.out.println(".()");
                playAgain.setVisible(false);
                score = 0;
                pipes.clear();
                bird.x = birdX;
                bird.y = birdY;
                start();
            }
        });

        setLayout(null);
        playAgain.setBounds(120, 250, 120, 40);
        add(playAgain);
        playAgain.setVisible(false);
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
                gameStarted = false;
            }
        }

        if (bird.y > boardHeight){
            gameOver = true;
            gameStarted = false;
        }
    }

    public void start(){
        xVelocity = -4;
        yVelocity = 0;
        gravity = 1;
        gameOver = false;
        placePipeTimer.start();
        gameLoopTimer.start();
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
            g.drawImage(loseImg, 0, 0, boardWidth, boardHeight, null);
            g.setFont(new Font("Arial", Font.PLAIN, 28));
            g.drawString("Game Over: " + String.valueOf((int) score), 100, 150);
            
        } else{
            g.drawString(String.valueOf((int) score ), 10, 35);
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    @Override //ew #VEX reference lololol
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if (gameOver){
            System.out.println("death");
            placePipeTimer.stop();
            gameLoopTimer.stop();
            playAgain.setVisible(true);

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver){
            if (e.getKeyCode() == KeyEvent.VK_SPACE){
                if(!gameStarted){
                    gameStarted = true;
                    start();
                }
                yVelocity =  -9;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
