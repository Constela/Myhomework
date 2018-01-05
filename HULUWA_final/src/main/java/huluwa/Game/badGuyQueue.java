package huluwa.Game;

public class badGuyQueue {
    protected int num;
    protected Creature creatures[];
    protected Formation form;

    public Creature[] getCreatures()
    {
        return creatures;
    }
    public badGuyQueue(Field field, Formation form)
    {
        this.form = form;
        num = 7;
        creatures = new Creature[num];
        creatures[0] = new Scorpion(form.getPosTuples().get(0).getX(),form.getPosTuples().get(0).getY(),field); //蝎子精初始化

        for(int i= 1 ; i < num  ; i++)
        {
            creatures[i] = new Guy(form.getPosTuples().get(i).getX(),form.getPosTuples().get(i).getY(),field,i); //小喽啰初始化
        }
    }
}
