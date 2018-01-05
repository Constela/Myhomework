package huluwa.FileManagement;
import huluwa.FileManagement.Element;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Space {
    private ArrayList<Element> elements;  //此时所有显示的元素，注意尸体图片需要排在活着的生物图片之前
    public Space()
    {
        elements = new ArrayList<Element>();
    }

    public ArrayList<Element> getElements()
    {
        return elements;
    }

    public void addElement(int creatureNo, boolean isAlive, int x, int y)
    {
        Element e = new Element(creatureNo,isAlive, x, y);
        elements.add(e);
    }

    public void write(DataOutputStream out) throws IOException
    {
        out.writeInt(elements.size());
        for (Element element : elements)
        {
            out.writeInt(element.getCreatureNo());
            out.writeBoolean(element.getIsAlive());
            out.writeInt(element.getX());
            out.writeInt(element.getY());
        }
    }
    public void read(DataInputStream in) throws IOException
    {
        int elementsSize = in.readInt();
        int creatureNo, x, y;
        boolean isAlive;
        for(int i = 0; i < elementsSize; i++)
        {
            creatureNo = in.readInt();
            isAlive = in.readBoolean();
            x = in.readInt();
            y = in.readInt();
            elements.add(new Element(creatureNo, isAlive, x, y));
        }
    }

}
