import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para TowerContest (ciclo 3).
 * 
 * Ciclo 3: Algoritmo Greedy que selecciona tazas de mayor a menor altura
 * que quepan en la altura máxima.
 */
public class TowerContestTest
{
    @Test
    public void greedyAlgorithmShouldSelectLargeCupsFirst() {
        TowerContest tc = new TowerContest();
        // Test básico: con 5 tazas y altura 9
        // Taza 5 (alt 9) cabe, taza 4 (alt 7) no cabe con taza 5
        // Taza 4 (alt 7) cabe solo, taza 3 (alt 5) + taza 4 (alt 7) = 12 > 9
        // Taza 4 (alt 7), taza 2 (alt 3) no cabe (7+3=10>9)
        // Taza 4 (alt 7), taza 1 (alt 1) = 8, cabe
        String result = tc.solve(5, 9);
        assertTrue(result.contains("CUPS:"));
        assertFalse(result.equals("NO"));
    }

    @Test
    public void shouldReturnNOWhenHeightTooSmall() {
        TowerContest tc = new TowerContest();
        String result = tc.solve(10, 0);
        assertEquals("NO", result);
    }
    
    @Test
    public void shouldSelectOnlyTazaThatFitsWhenHeightExact() {
        TowerContest tc = new TowerContest();
        // Taza 5 tiene altura exacta 9
        String result = tc.solve(5, 9);
        assertEquals("CUPS: 5", result);
    }
    
    @Test
    public void shouldReturnNOWhenNoCupFits() {
        TowerContest tc = new TowerContest();
        // Taza 1 tiene altura 1, pero altura máxima es 0
        String result = tc.solve(1, 0);
        assertEquals("NO", result);
    }
}
