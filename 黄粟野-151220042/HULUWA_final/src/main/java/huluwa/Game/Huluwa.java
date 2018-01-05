package huluwa.Game;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Huluwa extends Creature {
    private HuluAttribute huluAttribute;
    private int index;

    public Huluwa(int x, int y, Field field, int index, HuluAttribute huluAttribute)
    {
        super(x,y,field);
        this.species = Species.HULUWA;
        this.index = index;
        this.rank = 9 - index;
        this.group = Group.GOOD;
        this.huluAttribute = huluAttribute;

        URL loc = this.getClass().getClassLoader().getResource("hulu" + index +".png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        livingImage = image;

        loc = this.getClass().getClassLoader().getResource("hulu" + index +"_dead.png");
        iia = new ImageIcon(loc);
        image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        deadImage = image;

        sendImages();
    }
}

enum HuluAttribute {
    RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE;
}