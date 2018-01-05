
import org.junit.Test;
import huluwa.Game.Field;
import huluwa.FileManagement.recordFile;

import java.io.File;
import java.io.IOException;

public class HuluTest {
    Field field;
    @Test
    public void testisBattleEnds() throws Exception {
        field = new Field();
        field.start();
        assert !field.ifBattleEnds();
    }

    @Test(expected = Exception.class)
    public void testIO()throws IOException
    {
        File file = new File("HULUWA_final.iml");
        recordFile record = new recordFile();
        record.readRecord(file);
    }

    @Test
    public void testisExistBattle()
    {
        field = new Field();
        assert !field.ifBattleExists();
    }

}
