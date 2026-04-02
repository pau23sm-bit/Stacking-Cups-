import org.junit.Test;
import static org.junit.Assert.*;

public class TowerCC4Test
{
    @Test
    public void commonWorkflowShouldBestable() {
        Tower t = new Tower(8, 30);
        t.makeInvisible();

        t.pushCupType("normal", 2);
        t.pushLidType("normal", 2);
        t.pushCupType("opener", 4);

        // opner debe quitar la tapa de 2
        assertFalse(t.getCupType(2) == null && t.getCupType(2).equals(""));

        t.pushCupType("crazy", 6);

        assertTrue(t.ok());
        assertEquals(6, t.height());
    }
}
