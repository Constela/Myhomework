package huluwa.FileManagement;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class recordFile {
    private Record record;
    private File file;
    private int count=0;
    public recordFile()
    {
        record = new Record();
    }

    public void readRecord(File file) throws IOException//读文件 存入record
    {
        this.file = file;
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        record.read(in);
    }
    public void writeRecord(Space space)//每次刷新时调用，向文件中添加一个场景
    {
        record.addSpace(space);
    }

    public void writeFile(boolean result)
    {
        record.setResult(result);
        //SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        //String fileName = "HULUWA"+ df.format(new Date()) + ".dat";
        count += 1;
        String fileName = "HULUWA"+ count + ".dat";
        file = new File(fileName);

        try {
            if (!file.exists())
                file.createNewFile();
        }catch(Exception e)
        {
            System.out.println("文件创建失败");
        }
        try
        {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            record.write(out);

            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Record getRecord()
    {
        return record;
    }
}
