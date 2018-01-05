package huluwa.FileManagement;

import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Record {
    private ArrayList<Space> space; //定时记录场景，每隔0.2s显示
    boolean result; //记录大战结果，0为妖精胜，1为葫芦娃胜
    public Record()
    {
        space = new ArrayList<Space>();
        result = false;
    }
    public void addSpace(Space space)
    {
        this.space.add(space);
    }
    public ArrayList<Space> getSpace()
    {
        return space;
    }

    public boolean getResult() {
        return result;
    }

    public void write(DataOutputStream out) throws IOException
    {
        out.writeInt(space.size());//场景数
        out.writeBoolean(result);   //比赛结果
        for (Space s : space)
        {
            s.write(out);
        }
    }
    public void read(DataInputStream in) throws IOException
    {
        int scenesCount = in.readInt();
        result = in.readBoolean();
        Space s;
        for(int i = 0; i < scenesCount; i++)
        {
            s = new Space();
            s.read(in);
            space.add(s);
        }
    }
    public void setResult(boolean result)
    {
        this.result = result;
    }
}
