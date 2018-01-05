package huluwa.Game;
import javax.swing.*;

public class Ground extends JFrame {
    public Ground() {
        InitUI();
    }

    public void InitUI() {
        Field field = new Field();
        add(field);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(field.getBoardWidth() + 15,
                field.getBoardHeight() + 55);
        setLocationRelativeTo(null);
        setTitle("葫芦娃大战妖精");
    }
}
