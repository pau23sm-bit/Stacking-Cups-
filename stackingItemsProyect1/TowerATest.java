import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.JOptionPane;

public class TowerATest
{
    @Test
    public void acceptanceTestNestedAndUserConfirmation() throws InterruptedException {
        Tower t = new Tower(10, 40);
        t.makeVisible();

        t.pushCupType("normal", 7);
        t.pushCupType("normal", 2);
        t.pushCupType("normal", 1);

        // Espera para que el usuario vea la animacin en la pantalla
        Thread.sleep(1000);

        assertEquals(7, t.height());

        int dialogResult = JOptionPane.showConfirmDialog(null,
            "¿Acepta el comportamiento de tazas anidadas (misma base + centrado)?",
            "Prueba de Aceptación", JOptionPane.YES_NO_OPTION);

        assertEquals(JOptionPane.YES_OPTION, dialogResult);
    }
}
