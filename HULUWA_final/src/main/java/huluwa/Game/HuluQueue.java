package huluwa.Game;
import java.util.ArrayList;
import java.util.Collections;

public class HuluQueue  {
    protected int num;                  //队列人数
    protected Creature creatures[];     //队列中的生物数组
    protected Formation form;           //表示队列当前队形的对象

    public Creature[] getCreatures()
    {
        return creatures;
    }

    public HuluQueue(Field field, Formation form) {
        this.form = form;
        num = 7;
        creatures = new Huluwa[num];
        ArrayList<Integer> array = new ArrayList<>(num);
        int i;

        for (i = 0; i < num; i++)
            array.add(i);
        Collections.shuffle(array);

        for (i = 0; i < num; i++) {
            HuluAttribute huluAttribute = HuluAttribute.values()[i];

            creatures[i] = new Huluwa(form.getPosTuples().get(array.get(i)).getX(), form.getPosTuples().get(array.get(i)).getY(), field, i + 1, huluAttribute);
        }
    }

}


