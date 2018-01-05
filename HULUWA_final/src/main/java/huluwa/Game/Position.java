package huluwa.Game;
import java.util.ArrayList;

public class Position {
    private int x;
    private int y;
    private Creature holder; //位置的占有者
    private boolean is_empty;

    public ArrayList<Integer> corpseImagesNo;

    public Position(int x, int y)
    {
        super();
        this.x = x;
        this.y = y;
        is_empty = true;
        corpseImagesNo = new ArrayList<>();
    }

    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }

    public synchronized Creature getHolder() {
        return holder;
    }
    public synchronized void setHolder(Creature holder) { this.holder = holder; is_empty=false; }

    public synchronized boolean getIs_empty() { return is_empty; }
    public void setIs_empty(boolean a) { is_empty = a;}
    public synchronized void out() //从位置离开
    {
        this.holder = null;
        is_empty = true;
        notifyAll();
    }
    public synchronized void waitForPos() throws InterruptedException
    {
        while(!getIs_empty())
            wait();
    }

    public synchronized void addCorpseImagesNo(int creatureNo)
    {
        corpseImagesNo.add(new Integer(creatureNo));
    }
}
