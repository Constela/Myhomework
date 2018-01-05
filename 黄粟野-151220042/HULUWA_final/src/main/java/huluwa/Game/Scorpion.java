package huluwa.Game;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Scorpion extends Creature //蝎子精类
{
    public Scorpion(int x, int y, Field field)
    {
        super(x, y, field);
        this.species = Species.SCORPION;
        this.rank = 7;
        this.group = Group.EVIL;
        URL loc = this.getClass().getClassLoader().getResource("xiezi.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);

        livingImage = image;
        loc = this.getClass().getClassLoader().getResource("xiezi_dead.png");
        iia = new ImageIcon(loc);
        image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        deadImage = image;

        sendImages();
    }

}

