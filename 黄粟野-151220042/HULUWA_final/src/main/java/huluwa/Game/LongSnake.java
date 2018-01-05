package huluwa.Game;
import java.util.ArrayList;
public class LongSnake extends Formation {
    public LongSnake()
    {
        posTuples = new ArrayList<>(7);
        for(int i = 0; i < 7; i++)
            posTuples.add(new Pos_formation(2, 1+i));

    }
}
