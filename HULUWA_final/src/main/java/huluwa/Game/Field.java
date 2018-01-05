package huluwa.Game;
import huluwa.FileManagement.Element;
import huluwa.FileManagement.recordFile;
import huluwa.FileManagement.Space;
import huluwa.Thing2D;



import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import java.util.ArrayList;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;
import java.util.concurrent.TimeUnit;

enum State
{
    BEGIN,RUNNING,REPLAY,END,REPLAYEND
}

public class Field extends JPanel{
    public static final int N = 11;
    public static final int M = 9;

    private int w = 0;
    private int h = 0;

    private int goodGroupCount = 8;
    private int evilGroupCount = 8;

    private Position [][]positions;

    private final int SPACE = 100;

    private State state = State.BEGIN;
    private boolean battleEnds;

    private Map<Integer, Image> livingImages;
    private Map<Integer, Image> corpseImages;

    public int getBoardWidth() {
        return this.w;
    }
    public int getBoardHeight() {
        return this.h;
    }

    private HuluQueue huluwaqueue = null;
    private badGuyQueue scorpionqueue = null;
    private Grandpa grandpa = null;
    private Snake snake = null;

    private ExecutorService exec;
    private Space space;
    private recordFile recordfile;

    public Field()
    {
        addKeyListener(new Field.TAdapter()); //键盘监视器
        try {initWorld();}
        catch (Exception e)
        {
            System.out.println("Error1");
        }
    }

    public void initWorld()
    {
        goodGroupCount = 8;
        evilGroupCount = 8;

        state = State.BEGIN;
        setFocusable(true);
        Creature.setCountZero();

        livingImages = new HashMap<>();
        corpseImages = new HashMap<>();
        w = SPACE * N;
        h = SPACE * M;
        positions = new Position[N][M];

        int i, j;
        for(i = 0; i < N; i++) {
            for (j = 0; j < M; j++) {
                positions[i][j] = new Position(i,j);
            }
        }

        huluwaqueue = new HuluQueue(this, new LongSnake());    //葫芦娃
        scorpionqueue = new badGuyQueue(this,new SwordForm()); //蝎子精和喽啰
        snake = new Snake(9, 4,this);
        grandpa = new Grandpa(1,4,this);

        recordfile = new recordFile();
    }


    public void buildWorld(Graphics g)
    {
        g.setColor(new Color(250, 240, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        if(state == State.BEGIN)
        {
            Thing2D bgs = new Thing2D("bg1.png");
            g.drawImage(bgs.getImage(), 0, 0,this.getBoardWidth(),this.getBoardHeight()-200, this);
        }
        else if(state == State.RUNNING)
            drawSituation(g);
        else if(state == State.END)
        {
            drawSituation(g);

            if(goodGroupCount == 0)
            {
                Thing2D mw = new Thing2D("yjWin1.png");
                g.drawImage(mw.getImage(), 0, 0, this);
            }
            else
            {
                Thing2D hw = new Thing2D("huluWin1.png");
                g.drawImage(hw.getImage(), 0, 0, this);
            }
            Thing2D mw1 = new Thing2D("save.png");
            g.drawImage(mw1.getImage(), 0, this.getBoardHeight()/3-80, this);
            Thing2D mw2 = new Thing2D("space.png");
            g.drawImage(mw2.getImage(), 0, this.getBoardHeight()/3-50, this);
        }
        else if(state == State.REPLAY)
        {
            Thing2D bg = new Thing2D("bg2.png");
            g.drawImage(bg.getImage(), 0, 0, this.getBoardWidth(),this.getBoardHeight(),this);
            drawRecord(g);
        }
        else if(state == State.REPLAYEND)
        {
            drawRecord(g);
            if(recordfile.getRecord().getResult())
            {
                Thing2D hw = new Thing2D("huluReplay.png");
                g.drawImage(hw.getImage(), 0, 0, this);
            }
            else
            {
                Thing2D mw = new Thing2D("yjReplay.png");
                g.drawImage(mw.getImage(), 0, 0, this);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        buildWorld(g);
    }

    void addImages(int creatureNo, Image livingImage, Image deadImage)
    {
        this.livingImages.put(creatureNo, livingImage);
        this.corpseImages.put(creatureNo,deadImage);
    }

    private void drawSituation(Graphics g)
    {
        Thing2D bgr = new Thing2D("bg2.png");
        g.drawImage(bgr.getImage(),  0,0,this.getBoardWidth(),this.getBoardHeight(),this);

        space = new Space();
        for(int i=0;i<N;i++)
        {
            for(int j=0;j<M;j++)
            {
                if (!positions[i][j].corpseImagesNo.isEmpty()) {
                    for (Integer no : positions[i][j].corpseImagesNo) {
                        g.drawImage(corpseImages.get(no), i * SPACE, j * SPACE, this); //绘制尸体图片
                        this.space.addElement(no,false,i,j);
                    }
                }
                if (!positions[i][j].getIs_empty()) {
                    g.drawImage(livingImages.get(positions[i][j].getHolder().getCreatureNo()), i * SPACE, j * SPACE, this);
                    space.addElement(positions[i][j].getHolder().getCreatureNo(),true,i,j);
                }
            }
        }
        recordfile.writeRecord(space);
    }

    private void drawRecord(Graphics g)
    {
        ArrayList<Element>elements = space.getElements();
        for(Element element: elements)
        {
            if(element.getIsAlive())
                g.drawImage(livingImages.get(element.getCreatureNo()), element.getX() * SPACE, element.getY() * SPACE, this);
            else
                g.drawImage(corpseImages.get(element.getCreatureNo()), element.getX() * SPACE, element.getY() * SPACE, this);
        }

    }

    public Position getPosition(int x, int y)
    {
        if(!ifPositionLegal(x, y))
            return null;
        return positions[x][y];
    }

    private boolean ifPositionLegal(int x, int y)
    {
        return x >= 0 && x < 11 && y >= 0 && y < 9;
    }

    public synchronized boolean ifBattleEnds()
    {
        return battleEnds;
    }

    private synchronized void finishBattle(int flag)
    {
        battleEnds = true;
        if (flag == 0)
            System.out.println("葫芦娃胜利");
        else
            System.out.println("蛇精帮胜利");
    }

    public int[][] getCurrentSituation() //当前局势：0为空，1为葫芦娃方，-1为蛇精方
    {
        int[][] s = new int [Field.N][Field.M];
        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < M; j++)
            {
                try{
                    if (!positions[i][j].getIs_empty()) {
                        if (positions[i][j].getHolder().group == Group.GOOD)
                            s[i][j] = 1;
                        else s[i][j] = -1;
                    } else {
                        s[i][j] = 0;
                    }
                }
                catch (NullPointerException e)
                {
                    s[i][j] = 0;
                }
            }
        }
        return s;
    }

    public void start() throws Exception
    {
        state = State.RUNNING;
        exec = Executors.newCachedThreadPool();
        for(Creature c :huluwaqueue.getCreatures())
            exec.execute(c);
        for(Creature c: scorpionqueue.getCreatures())
            exec.execute(c);
        exec.execute(snake);//蛇精
        exec.execute(grandpa); //老爷爷

        exec.execute(()->{
            while(!ifBattleEnds() && !interrupted()) {
                repaint();
                try {
                    sleep(200);
                    ifBattleExists();
                } catch(Exception e)
                {
                    System.out.println("error3");
                }
            }
            //drawSituation(g);
            repaint();
            state = State.END;  //大战结束
            //repaint();
        });
    }

    private void replay()
    {
        ArrayList<Space> scenes = recordfile.getRecord().getSpace();
        Iterator it = scenes.iterator();
        exec = Executors.newCachedThreadPool();
        exec.execute(           //刷新屏幕线程，可以让它有运行过程，减缓刷新速度
                ()->
                {
                    try {
                        while (it.hasNext())
                        {
                            space = (Space) it.next();
                            repaint();
                            sleep(100);
                        }
                        repaint();
                        state = State.REPLAYEND;
                        repaint();
                    } catch (InterruptedException ie) {
                        System.out.println("replay: interrupt");
                    }
                });
    }


    private synchronized void battle(Creature good, Creature evil)
    {
        Random random = new Random();
        int rand = random.nextInt(200) + 1; //生成1-100之间的随机数
        int p = 100 + (good.rank - evil.rank) * 5; //决定胜负

        if (rand<=p) //葫芦娃赢
        {
            evil.die();
            evilGroupCount--;
            System.out.println("妖精："+ evilGroupCount);
            if (evilGroupCount == 0) finishBattle(0);
        }
        else //妖精赢
        {
            good.die();
            goodGroupCount--;
            System.out.println("好人："+ goodGroupCount);
            if (goodGroupCount == 0) finishBattle(1);
        }
    }

    public synchronized boolean ifBattleExists()
    {
        boolean flag = false;
        for(Creature c: huluwaqueue.getCreatures())
        {
            if (c.isAlive())
            {
                if (ifMeetEnemy(c))
                    flag = true;
            }
        }
        if(grandpa.isAlive() && ifMeetEnemy(grandpa))
            flag = true;

        return flag;
    }
    private synchronized boolean ifMeetEnemy(Creature c)
    {
        Creature holder;

        int x = c.getPosition().getX();
        int y = c.getPosition().getY();
        if(ifPositionLegal(x+1, y) && !getPosition(x+1, y).getIs_empty())
        {
            holder = getPosition(x+1,y).getHolder();
            if(holder.group != c.group) //前方有敌人
            {
                battle(c, holder);
                return true;
            }
        }
        if(ifPositionLegal(x-1, y) && !getPosition(x-1, y).getIs_empty())
        {
            holder = getPosition(x-1,y).getHolder();
            if(holder.group != c.group) //后方有敌人
            {
                battle(c, holder);
                return true;
            }

        }

        if(ifPositionLegal(x, y+1) && !getPosition(x, y+1).getIs_empty())
        {
            holder = getPosition(x,y+1).getHolder();
            if(holder.group != c.group) //下方有敌人
            {
                battle(c, holder);
                return true;
            }

        }

        if(ifPositionLegal(x, y-1) && !getPosition(x, y-1).getIs_empty())
        {
            holder = getPosition(x,y-1).getHolder();
            if(holder.group != c.group) //上方有敌人
            {
                battle(c, holder);
                return true;
            }

        }
        return false;
    }

    class TAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_SPACE:
                    if (state == State.BEGIN) {
                        try {
                            start();
                        } catch (Exception ex) {
                            System.out.println("error2");
                        }
                    }
                    else if (state == State.END) {
                        exec.shutdownNow();
                        repaint();
                        try {
                            sleep(1000);
                        } catch (InterruptedException ie) {
                            System.out.println(" interrupt");
                        }
                        try {
                            initWorld();
                        } catch (Exception ex) {
                            System.out.println("Error initWorld");
                        }
                        state = State.BEGIN;
                        repaint();
                    }
                    else if (state == State.REPLAYEND) {
                        try {
                            initWorld();
                        } catch (Exception ex) {
                            System.out.println("Error initWorld");
                        }
                        state = State.BEGIN;
                        repaint();
                    }
                    break;
                case KeyEvent.VK_S:
                    if (state == State.END) {
                        recordfile.writeFile(goodGroupCount != 0);
                        JOptionPane.showMessageDialog(null, "保存成功！");
                    }
                    break;
                case KeyEvent.VK_L:
                    if(state==State.BEGIN)
                    {
                        JFileChooser fd = new JFileChooser();
                        fd.showOpenDialog(null);
                        File file = fd.getSelectedFile();

                        if (file != null) {
                            try {
                                recordfile.readRecord(file);
                            } catch (IOException ioe) {
                                JOptionPane.showMessageDialog(null, "读取出错", "文件读取失败！", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            System.out.println("文件读取成功");
                            state = State.REPLAY;
                            replay();
                        }
                    }
                    break;
                    default:break;
            }
        }
    }
}
