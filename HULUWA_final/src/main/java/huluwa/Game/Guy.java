package huluwa.Game;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Guy extends Creature //小喽啰类
{
    int index;
    public Guy(int x, int y, Field field, int index)
    {
        super(x, y, field);
        this.index = index;
        this.species = Species.GUY;
        this.rank = 1;
        this.group = Group.EVIL;
        URL loc = this.getClass().getClassLoader().getResource("guy.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        livingImage = image;

        loc = this.getClass().getClassLoader().getResource("guy_dead.png");
        iia = new ImageIcon(loc);
        image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        deadImage = image;
        sendImages();
    }
}

