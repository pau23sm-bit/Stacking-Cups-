import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para los requisitos del Ciclo 2.
 *
 * Las pruebas se ejecutan en modo invisible (makeInvisible) para no mostrar
 * la ventana gráfica durante la ejecución.
 */
public class TowerC2Test
{
    @Test
    public void accordingMAShouldPushAndPopCups() {
        Tower t = new Tower(5, 20);
        t.makeInvisible();

        t.pushCup(1);
        t.pushCup(2);

        // Las alturas de las tazas son 1 y 3 (total 4)
        assertEquals(4, t.height());

        t.popCup();
        // Queda solo la taza 1 (altura 1)
        assertEquals(1, t.height());

        t.popCup();
        assertEquals(0, t.height());
    }

    @Test
    public void accordingMBShouldSwapTwoCups() {
        Tower t = new Tower(4, 30);
        t.makeInvisible();

        // Guardamos la altura antes del swap
        int heightBefore = t.height();

        // Hacemos el intercambio entre taza 1 y 4.
        t.swap(new String[]{"cup","1"}, new String[]{"cup","4"});

        // Después del swap, la altura no debe cambiar.
        assertEquals(heightBefore, t.height());
    }

    @Test
    public void accordingMCShouldFindSwapToReduce() {
        Tower t = new Tower(5, 5);
        t.makeInvisible();

        // Con altura limitada, solo cabe taza 1 (altura 1)
        // La solucion de swapToReduce debería devolver arreglo vacío (no mejora posible)
        String[][] swap = t.swapToReduce();
        assertEquals(0, swap.length);

        // Con más altura, debe proponer intercambio
        Tower t2 = new Tower(5, 10);
        t2.makeInvisible();
        String[][] swap2 = t2.swapToReduce();
        assertNotNull(swap2);
    }
    
    @Test
    public void shouldReportStackingItemsCorrectly() {
        Tower t = new Tower(3, 20);
        t.makeInvisible();

        // Inicialmente debe estar vacío
        String[][] items = t.stackingItems();
        assertEquals(0, items.length);

        // Agregar tazas
        t.pushCup(1);
        t.pushCup(2);
        items = t.stackingItems();
        assertEquals(2, items.length);
        assertEquals("cup", items[0][0]);
        assertEquals("1", items[0][1]);
        assertEquals("cup", items[1][0]);
        assertEquals("2", items[1][1]);

        // Agregar tapa a la taza 2
        t.pushLid(2);
        items = t.stackingItems();
        assertEquals(3, items.length);
        assertEquals("lid", items[2][0]);
        assertEquals("2", items[2][1]);
    }

    @Test
    public void shouldNestSmallerCupsInsideLarger() {
        Tower t = new Tower(4, 9);
        t.makeInvisible();

        // El cup 4 tiene altura 7, cup 2 altura 3 y cup 1 altura 1
        t.pushCup(4);
        assertTrue(t.ok());
        t.pushCup(2);
        assertTrue(t.ok());
        t.pushCup(1);
        assertTrue(t.ok());

        // Altura visible con nesting esperado: 7
        assertEquals(7, t.height());
    }

    @Test
    public void reversePreservesNestingAndBase() {
        Tower t = new Tower(10, 20);
        t.makeInvisible();

        t.pushCup(7);
        t.pushCup(2);
        t.pushCup(1);

        assertEquals(7, t.height());

        t.reverseTower();
        assertEquals(7, t.height());
    }

    @Test
    public void nestedCupBaseCoordinatesAndCentering() {
        Tower t = new Tower(10, 20);
        t.makeInvisible();

        t.pushCup(7);
        t.pushCup(2);
        t.pushCup(1);

        int base7 = t.getCupBaseY(7);
        int base2 = t.getCupBaseY(2);
        int base1 = t.getCupBaseY(1);

        assertEquals(base7, base2);
        assertEquals(base2, base1);

        int x7 = t.getCupX(7);
        int x2 = t.getCupX(2);
        int x1 = t.getCupX(1);

        int bs = t.getBlockSize();

        int expectedX2 = x7 + ((7 * bs) - (2 * bs)) / 2;
        int expectedX1 = x2 + ((2 * bs) - (1 * bs)) / 2;

        assertEquals(expectedX2, x2);
        assertEquals(expectedX1, x1);
    }

    @Test
    public void accordingMDShouldCoverUpdatesAppearanceWithoutError() {
        Tower t = new Tower(3, 20);
        t.makeInvisible();

        t.pushCup(1);
        t.pushCup(2);
        t.pushLid(2);
        t.cover();

        // Debe seguir reportando la taza 2 como tapada
        int[] lidded = t.liddedCups();
        assertEquals(1, lidded.length);
        assertEquals(2, lidded[0]);
    }
}
