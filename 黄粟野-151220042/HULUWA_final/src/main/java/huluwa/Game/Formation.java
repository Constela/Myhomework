package huluwa.Game;
import java.util.ArrayList;


public class Formation  //队形基类
{
    protected ArrayList<Pos_formation> posTuples = null; //存储队形的位置元组数据
    //protected int length;

    public ArrayList<Pos_formation> getPosTuples()
    {
        return posTuples;
    }
}
