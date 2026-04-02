import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Casos de prueba comunes para la clase TowerContest (Ciclo 3).
 *
 * Este archivo es de creación colectiva con tests específicos
 * que validan el algoritmo Greedy de la maratón.
 */
public class TowerContestCTest
{
    @Test
    public void greedyAlgorithmWithFourCupsAndHeight9() {
        TowerContest tc = new TowerContest();
        // Con 4 tazas disponibles (1,2,3,4) y altura máxima 9:
        // Taza 4: altura 7, suma = 7 ✓
        // Taza 3: altura 5, suma = 12 > 9 ✗
        // Taza 2: altura 3, suma = 10 > 9 ✗
        // Taza 1: altura 1, suma = 8 ✓
        // Resultado: tazas 4 y 1
        String result = tc.solve(4, 9);
        assertEquals("CUPS: 4 1", result);
    }

    @Test
    public void greedyAlgorithmWithMultipleCups() {
        TowerContest tc = new TowerContest();
        // Con 6 tazas y altura máxima 20:
        // Taza 6: altura 11, suma = 11 ✓
        // Taza 5: altura 9, suma = 20 ✓
        // Taza 4: altura 7, suma = 27 > 20 ✗
        // Taza 3: altura 5, suma = 25 > 20 ✗
        // Taza 2: altura 3, suma = 23 > 20 ✗
        // Taza 1: altura 1, suma = 21 > 20 ✗
        // Resultado: tazas 6 y 5
        String result = tc.solve(6, 20);
        assertEquals("CUPS: 6 5", result);
    }
}
