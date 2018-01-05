package huluwa.Game;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Grandpa extends Creature //生物：老爷爷
{
    public Grandpa(int x, int y, Field field)
    {
        super(x, y, field);
        this.species = Species.GRANDPA;
        this.rank = 2;
        this.group = Group.GOOD;
        URL loc = this.getClass().getClassLoader().getResource("ye.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        livingImage = image;

        loc = this.getClass().getClassLoader().getResource("ye_dead.png");
        iia = new ImageIcon(loc);
        image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        deadImage = image;
        sendImages();
    }

}

