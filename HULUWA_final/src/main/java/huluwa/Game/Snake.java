package huluwa.Game;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Snake extends Creature //生物：蛇精
{
    public Snake(int x, int y, Field field)
    {
        super(x, y, field);
        this.species = Species.SNAKE;
        this.rank = 8;
        this.group = Group.EVIL;
        URL loc = this.getClass().getClassLoader().getResource("she.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        livingImage = image;

        loc = this.getClass().getClassLoader().getResource("she_dead.png");
        iia = new ImageIcon(loc);
        image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        deadImage = image;
        sendImages();
    }
}