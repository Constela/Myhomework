package huluwa.Game;
import java.util.ArrayList;
public class SwordForm extends Formation {
    public SwordForm()
    {
        posTuples  = new ArrayList<>(7);

        posTuples.add(new Pos_formation(6,4));
        posTuples.add(new Pos_formation(6,3));
        posTuples.add(new Pos_formation(6,5));

        posTuples.add(new Pos_formation(7,2));
        posTuples.add(new Pos_formation(8,1));
        posTuples.add(new Pos_formation(7,6));
        posTuples.add(new Pos_formation(8,7));
    }
}
