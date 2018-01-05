package huluwa.Game;
import java.awt.*;
import java.util.Random;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.yield;

public class Creature implements Runnable{
    public Species species;      //物种
    protected Position position; //位置
    private Field field;
    private boolean alive;      //是否死亡
    public int rank;            //战斗力
    public Group group;         //阵营
    public Image livingImage;
    public Image deadImage;
    private static int count = 0;
    private final int creatureNo;

    public Creature(int x, int y, Field field) {
        this.field = field;
        alive = true;
        setPosition(field.getPosition(x, y));

        this.creatureNo = count++;
    }

    public void sendImages() {
        field.addImages(creatureNo, livingImage, deadImage);
    }
    public int getCreatureNo() { return creatureNo; }
    public static void setCountZero()
    {
        count = 0;
    }

    public synchronized Position getPosition()
    {
        return this.position;
    }
    public synchronized void setPosition(Position position) //设置位置
    {
        if (position.getIs_empty()) {
            synchronized (position) {
                leavePosition();
                position.setHolder(this);
                this.position = position;
                boolean a = false;
                this.position.setIs_empty(a);
            }
        }else assert (false);
    }
    public synchronized void leavePosition() //离开位置
    {
        if(this.position != null)
        {
            (this.position).out();
            this.position = null;
        }
    }

    public synchronized boolean ifPositionEmpty()
    {
        return this.position == null;
    }

    public synchronized boolean isAlive() { return alive; }

    public synchronized void die() //死亡
    {
        assert this.isAlive();
        assert !this.ifPositionEmpty();
        this.position.addCorpseImagesNo(creatureNo);
        leavePosition();
        this.alive = false;
    }
    private int calculate_distance(int x,int y,int i,int j)
    {
        return (x - i) * (x - i) + (y - j) * (y - j);
    }

    private Pos_formation nearestEnemy(int x, int y, int g, int[][] s)
    {
        int N = Field.N;
        int M = Field.M;

        int minx = x, miny = y;
        int mind = N * N + M * M;

        for(int i = 0; i < N; i++)
        {
            for (int j = 0; j < M; j++) {
                if (s[i][j] == -g) {
                    int d = calculate_distance(x,y,i,j);
                    if (d < mind) {
                        mind = d;
                        minx = i;
                        miny = j;
                    }
                }
            }
        }
        return new Pos_formation(minx, miny);
    }

    private synchronized Position decideNextPos() //决定下一步
    {
        int x = position.getX();
        int y = position.getY();
        int g = this.group == Group.GOOD ? 1 : -1;
        int[][] s = this.field.getCurrentSituation();
        int i;
        //下一步朝前方有敌人的方向前进
        //先看前方，再看后方
        for (i = x + g; i < Field.N && i >= 0; i += g) {
            if (s[i][y] == -g)
                return this.field.getPosition(x + g, y);
        }

        for (i = x - g; i < Field.N && i >= 0; i -= g) {
            if (s[i][y] == -g)
                return this.field.getPosition(x - g, y);
        }

        Pos_formation pos_next = nearestEnemy(x, y, g, s);

        if(pos_next.getY() < y)
            return this.field.getPosition(x,y-1);
        else return this.field.getPosition(x, y+1);
    }

    public void run()
    {
        while (this.isAlive() && !this.field.ifBattleEnds() && !interrupted())
        {
            try {
                Position nextPos = decideNextPos(); //判断下一步移动位置
                synchronized (nextPos) {
                    nextPos.waitForPos();           //如果该位置有人，则等待
                    if (!this.isAlive()) break;     //如果它dead，就结束移动
                    synchronized (this) {
                        setPosition(nextPos);
                    }
                }
                Random rand = new Random();
                Thread.sleep(rand.nextInt(1000) + 800);
            }catch (NullPointerException npe)
            {
                return;
            }
            catch (InterruptedException ie)
            {
                System.err.println(ie);
            }
        }
    }

}

enum Species //物种
{
    HULUWA, GRANDPA, SNAKE, SCORPION, GUY
}
