import java.util.ArrayList;

/**
 * TowerContest — clase para resolver y simular el problema de la maratón.
 *
 * CICLO 3: Esta clase resuelve el problema de la maratón
 * usando un algoritmo Greedy (de mayor a menor altura).
 * Tower solo simula, NO resuelve el problema.
 */
public class TowerContest
{
    /**
     * Resuelve el problema de la maratón usando algoritmo Greedy.
     *
     * Algoritmo Greedy: Selecciona tazas de mayor a menor altura
     * que logren encajar en la altura máxima, sin excederla.
     *
     * @param n número máximo de tazas disponibles (1 a n)
     * @param h altura máxima permitida
     * @return "CUPS: lista de tazas seleccionadas" o "NO"
     */
    public String solve(int n, int h)
    {
        if(n <= 0 || h <= 0)
        {
            return "NO";
        }

        // Crear lista de tazas disponibles ordenadas de MAYOR a MENOR altura
        ArrayList<Integer> selectedCups = new ArrayList<>();
        int totalHeight = 0;

        // Iterar desde la taza más grande hasta la más pequeña
        for(int i = n; i >= 1; i--)
        {
            int cupHeight = 2 * i - 1; // Formula: altura = 2n-1

            // Si esta taza cabe en el espacio restante, incluirla
            if(totalHeight + cupHeight <= h)
            {
                selectedCups.add(i);
                totalHeight += cupHeight;
            }
        }

        // Si no se seleccionó ninguna taza, retornar NO
        if(selectedCups.isEmpty())
        {
            return "NO";
        }

        // Construir la respuesta ordenando las tazas de mayor a menor
        StringBuilder sb = new StringBuilder();
        sb.append("CUPS:");
        for(int cup : selectedCups)
        {
            sb.append(" ").append(cup);
        }
        return sb.toString();
    }

    /**
     * Simula la solución en la torre gráfica.
     * @param n número de tazas
     * @param h altura máxima
     */
    public void simulate(int n, int h)
    {
        Tower tower = new Tower(n, h);
        String solution = solve(n, h);
        if(solution.equals("NO"))
        {
            // No hay solución, solo muestra la torre vacía
            return;
        }

        // Apila las tazas de la solución
        String[] parts = solution.split(" ");
        for(int i = 1; i < parts.length; i++)
        {
            try
            {
                int cupNumber = Integer.parseInt(parts[i]);
                tower.pushCup(cupNumber);
            }
            catch(NumberFormatException e)
            {
                // Ignora errores
            }
        }
    }
}
