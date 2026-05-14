import java.awt.*;
import javax.swing.*;
public class FlappyBird extends JPanel{
    int boardWidth = 360;
    int boardHeight = 640;

    Image bgImg;
    Image birdyImg;
    Image topPipeImg;
    Image bottomPipeImg;

    FlappyBird(){
        bgImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdyImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.green);

    }

    public void draw(Graphics g) {
        g.drawImage(bgImg, 0, 0, boardWidth, boardHeight, null);
    }

    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

}
