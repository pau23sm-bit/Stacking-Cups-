import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Tower — esta es la clase principal del simulador de torres.
 *
 * Imagina que tienes una torre donde puedes apilar tazas y tapas.
 * Esta clase te ayuda a crear la torre, agregar o quitar tazas y tapas,
 * ordenarlas, y ver cómo se ve todo.
 *
 * Es como un juego donde apilas cosas, pero con reglas de altura.
 */
public class Tower
{
    // Estos son los datos que guarda la torre
    private int width; // ancho de la torre
    private int height; // altura máxima en bloques
    private ArrayList<Cup> cups; // lista de tazas apiladas

    private Rectangle wall; // la pared de la torre
    private Rectangle base; // la base de la torre
    private ArrayList<Rectangle> marks; // marcas de altura
    private boolean isVisible; // si la torre se ve en pantalla
    private boolean lastOk; // si la última acción funcionó bien
    private int xPosition; // posición en x
    private int yPosition; // posición en y
    private int cupTop; // altura actual de las tazas
    private int blockSize; // tamaño de cada bloque
    private final int towerHeightPixels = 500; // altura en píxeles

    /**
     * Constructor para crear una torre con ancho y altura máxima.
     * @param width ancho de la torre
     * @param maxHeight altura máxima en bloques
     */
    public Tower(int width, int maxHeight){
        this.width = width;
        this.height = maxHeight;

        marks = new ArrayList<>();

        drawTower(); // dibuja la torre

        this.cups = new ArrayList<>();

        isVisible = true;
        lastOk = true;
        cupTop = 0;
        blockSize = towerHeightPixels / height;
        StackingItem.setBlockSize(blockSize);
    }

    /**
     * Otro constructor que crea una torre con n tazas apiladas.
     * @param numCups número de tazas a crear
     */
    public Tower(int cups)
    {
        this.width = cups;
        // altura máxima necesaria (suma de 2i-1)
        this.height = cups * cups;
        this.cups = new ArrayList<>();
        this.marks = new ArrayList<>();

        drawTower();

        isVisible = true;
        lastOk = true;
        cupTop = 0;

        blockSize = towerHeightPixels / this.height;
        StackingItem.setBlockSize(blockSize);
        // tazas sin tapas
        for(int i = 1; i <= cups; i++)
        {
            Cup c = new Cup(i, pickColor(i));
            this.cups.add(c);
        }
        redrawCups();
    }

    private void drawTower(){
        drawWall();
        drawBase();
        drawMarks();
        isVisible = true;
    }

    private void drawWall()
    {
        int towerHeight = 500;
        wall = new Rectangle();
        wall.changeSize(towerHeight, 5);   // alto grande, ancho pequeño
        wall.moveHorizontal(10);
        wall.moveVertical(50);
        wall.changeColor("black");
        wall.makeVisible();
    }

    private void drawBase()
    {
        int towerWidth = 400;
        int towerHeight = 500;
        base = new Rectangle();
        base.changeSize(5, towerWidth);   // alto pequeño, ancho grande
        base.moveHorizontal(10);
        base.moveVertical(50 + towerHeight);
        base.changeColor("black");
        base.makeVisible();
    }

    private void drawMarks()
    {
        int towerHeight = 500;
        int numberOfMarks = height;

        int spacing = towerHeight / numberOfMarks;

        for(int i = 0; i <= numberOfMarks; i++)
        {
            Rectangle mark = new Rectangle();
            mark.changeSize(3, 15); // rayita

            mark.moveHorizontal(0);
            mark.moveVertical(50 + towerHeight - (i * spacing));

            mark.changeColor("black");
            mark.makeVisible();

            marks.add(mark);
        }
    }

    // Mini ciclo 3 visibilad 
    private void redrawCups() {
        for (Cup c : cups) {
            c.makeInvisible();
            if (c.hasLid()) {
                c.getLid().makeInvisible();
            }
        }

        int towerWidthPixels = 400;
        int towerX = 10;
        int baseY = 50 + towerHeightPixels;

        int stackTop = 0;
        int maxTopY = baseY;
        ArrayList<Cup> placed = new ArrayList<>();

        for (Cup c : cups) {
            int cupHeight = c.getHeight();
            int cupHeightPixels = cupHeight * blockSize;
            int cupWidthPixels = c.getWidth() * blockSize;

            Cup parent = findParentCup(c, placed);
            int cupBaseY;
            int cupX;

            if (parent == null) {
                // Apilar en el suelo, torre tendrá altura acumulada de todos los no-anidados
                cupBaseY = baseY - (stackTop * blockSize);
                cupX = towerX + (towerWidthPixels - cupWidthPixels) / 2;

                stackTop += cupHeight;
                if (c.hasLid()) {
                    stackTop += 1;
                }
            } else {
                // Inserción exacta: la base de la pequeña coincide con la base del padre
                int parentBaseY = parent.getY() + parent.getHeight() * blockSize;
                cupBaseY = parentBaseY;
                cupX = parent.getX() + (parent.getWidth() * blockSize - cupWidthPixels) / 2;
            }

            int cupTopY = cupBaseY - cupHeightPixels;
            c.moveTo(cupX, cupTopY);
            if (isVisible) {
                c.makeVisible();
            }

            // Mantener la Y más alta (en coordenadas de pantalla y menores). Para altura de bloque se usa baseY - maxTopY
            if (cupTopY < maxTopY) {
                maxTopY = cupTopY;
            }

            if (c.hasLid()) {
                Lid lid = c.getLid();
                int lidX = cupX;
                int lidY = cupTopY - blockSize;
                lid.moveTo(lidX, lidY);
                if (isVisible) {
                    lid.makeVisible();
                }
                if (lidY < maxTopY) {
                    maxTopY = lidY;
                }
            }

            placed.add(c);
        }

        // recalcula altura lógica para el DAO de la torre (bloques)
        cupTop = (baseY - maxTopY) / blockSize;
    }

    /**
     * Hace visible la torre 
     * establece su posición base para centrarla
     * horizontalmente y apoyarla desde la parte inferior.
     */
    public void makeVisible() {
        isVisible = true;

        drawTower();   // redibuja estructura
        redrawCups();  //  todito correctamente
    }

    /**
     * Oculta la representación gráfica de la torre 
     * No modifica nada mas 
     */
    public void makeInvisible() {
        isVisible = false;
        for (Cup c : cups) {
            c.makeInvisible();

            if (c.hasLid()) {
                c.getLid().makeInvisible(); 
            }
        }
        wall.makeInvisible();
        base.makeInvisible();
        for (Rectangle mark : marks) {
            mark.makeInvisible();
        }
    }

    //Mini ciclo 4 relacionado a tazas 
    /** 
     * Crea y apila una nueva taza en la torre usando su identificador i.
     * El identificador determina la altura de la taza y su color se asigna
     * automáticamente para cumplir el requisito de usabilidad.
     * @param i identificador de la taza (define su altura)
     */
    public void pushCup(int i) {
        pushCupType("normal", i);
    }

    public void pushCupType(String type, int i)
    {
        type = type == null ? "normal" : type.toLowerCase();

        for(Cup c : cups){
            if(c.getNumber() == i){
                if(isVisible){
                    JOptionPane.showMessageDialog(null,"Ya existe una taza con ese número");
                }
                lastOk = false;
                return;
            }
        }

        int newHeight = 2 * i - 1;
        int newWidth = newHeight;

        if(type.equals("fearful")){
            for(Cup c : cups){
                if(c.getWidth() >= newWidth || c.getHeight() >= newHeight){
                    if(isVisible){
                        JOptionPane.showMessageDialog(null,"Fearful cup no entra: hay un objeto igual o mayor");
                    }
                    lastOk = false;
                    return;
                }
            }
        }

        if(type.equals("opener")){
            for(Cup c : cups){
                if(c.hasLid()){
                    if(isVisible){
                        c.getLid().makeInvisible();
                    }
                    c.removeLid();
                }
            }
        }

        String cupColor = pickColor(i);
        Cup cup = new Cup(i, cupColor, type);

        if(type.equals("hierarchical") || type.equals("crazy")) {
            if(cups.isEmpty()) {
                cups.add(cup);
            } else {
                cups.add(0, cup);
            }
            if(type.equals("hierarchical")) {
                cup.setUnremovable(true);
            }
        } else {
            cups.add(cup);
        }

        recalculateCupTop();

        if(isVisible){
            redrawCups();
        }

        lastOk = true;
    }

    /**
     * Remueve la taza ubicada en la parte superior de la torre.
     * Si no hay tazas apiladas no realiza cambios y marca la
     * operación como no exitosa.
     */
    public void popCup() {
        if (cups.isEmpty()) {
            lastOk = false;
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "No hay tazas para remover");
            }
            return;
        }
        Cup c = cups.get(cups.size() - 1);
        if(c.isUnremovable()){
            lastOk = false;
            if (isVisible) {
                JOptionPane.showMessageDialog(null, "La taza jerárquica no se puede quitar");
            }
            return;
        }

        c = cups.remove(cups.size() - 1);
        if (isVisible) {
            c.makeInvisible();
        }
        if (c.hasLid()) {
            if (isVisible) {
                c.getLid().makeInvisible();
            }
            c.removeLid(); // elimina la referencia
        }
        recalculateCupTop(); 
        lastOk = true;
    }

    /**
     * Elimina de la torre la primera taza cuya altura sea igual a i.
     * Después de removerla, recalcula la posición de las tazas restantes
     * para mantener el apilamiento correcto.
     * @param i altura de la taza a remover
     */
    public void removeCup(int i){
        Cup target = null;
        int index = -1;
        for(int k = 0; k < cups.size(); k++){// buscar taza por número
            if(cups.get(k).getNumber() == i){
                target = cups.get(k);
                index = k;
                break;
            }
        }
        if(target == null){
            if(isVisible){
                JOptionPane.showMessageDialog(null,"No existe esa taza");
            }
            lastOk = false;
            return;
        }

        if(target.isUnremovable()){
            if(isVisible){
                JOptionPane.showMessageDialog(null,"La taza jerárquica no se puede quitar");
            }
            lastOk = false;
            return;
        }

        if(isVisible){ // ocultar taza eliminada
            target.makeInvisible();
            if(target.hasLid()){
                target.getLid().makeInvisible();
            }
        }
        cups.remove(index);
        recalculateCupTop();
        
        if(isVisible){
            redrawCups();
        }
        lastOk = true;
    }

    // metodos get 
    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public String getCupType(int number)
    {
        for(Cup c : cups)
        {
            if(c.getNumber() == number)
            {
                return c.getType();
            }
        }
        return null;
    }

    /**
     * Recalcula el total de bloques ocupados por las tazas (incluyendo tapas).
     */
    private void recalculateCupTop()
    {
        int top = 0;
        ArrayList<Cup> placed = new ArrayList<>();

        for(Cup c : cups)
        {
            Cup parent = findParentCup(c, placed);
            if(parent == null)
            {
                top += c.getHeight();
                if(c.hasLid())
                {
                    top += 1;
                }
            }
            placed.add(c);
        }
        cupTop = top;
    }

    /**
     * Comprueba si un padre potencial puede contener a una taza hija.
     */
    private boolean canContain(Cup parent, Cup child)
    {
        // Debe ser estrictamente más grande para evitar insertarse en taza del mismo tamaño.
        // Además, la tapa debe estar ausente para permitir inserción.
        return parent.getWidth() > child.getWidth()
            && parent.getHeight() > child.getHeight()
            && !parent.hasLid();
    }

    /**
     * Devuelve la coordenada de base (bajo el sistema que pide el usuario)
     * de una taza ya colocada.
     */
    public int getCupBaseY(int number)
    {
        for (Cup c : cups) {
            if (c.getNumber() == number) {
                return c.getY() + c.getHeight() * blockSize;
            }
        }
        return -1; // no encontrada
    }

    /**
     * Devuelve la coordenada X de la taza para pruebas de centrado.
     */
    public int getCupX(int number)
    {
        for (Cup c : cups) {
            if (c.getNumber() == number) {
                return c.getX();
            }
        }
        return -1;
    }

    /**
     * Devuelve el blockSize actual para validar coordenadas en tests.
     */
    public int getBlockSize()
    {
        return blockSize;
    }

    /**
     * Busca un contenedor existente para la taza dada.
     */
    private Cup findParentCup(Cup child, ArrayList<Cup> placed)
    {
        Cup parent = null;
        for(Cup p : placed)
        {
            if(canContain(p, child))
            {
                if(parent == null ||
                   p.getWidth() < parent.getWidth() ||
                   (p.getWidth() == parent.getWidth() && p.getHeight() < parent.getHeight()))
                {
                    parent = p;
                }
            }
        }
        return parent;
    }

    /**
     * Indica si la última operación ejecutada fue exitosa.
     *
     * @return true si la última operación se realizó correctamente,
     *         false en caso contrario
     */
    public boolean ok(){
        return lastOk;
    }
    // metodos auxiliares
    /**
     * Selecciona un color de forma cíclica a partir de un índice
     * para asignar colores variados a las tazas.
     * @param i índice de referencia
     * @return nombre del color asignado
     */
    private String pickColor(int i){
        String[] colors = {"red","blue","green","yellow","magenta","orange"};
        return colors[i % colors.length];
    }

    public void pushLid(int i)
    {
        Cup target = null;

        for(Cup c : cups){
            if(c.getNumber() == i){
                target = c;
                break;
            }
        }

        if(target == null){
            if(isVisible){
                JOptionPane.showMessageDialog(null,"No existe esa taza");
            }
            lastOk = false;
            return;
        }

        if(target.hasLid()){
            if(isVisible){
                JOptionPane.showMessageDialog(null,"La taza ya tiene tapa");
            }
            lastOk = false;
            return;
        }

        Lid lid = new Lid(i, pickColor(i));
        target.addLid(lid);

        redrawCups();

        lastOk = true;
    }

    public void pushLidType(String type, int i)
    {
        type = type == null ? "normal" : type.toLowerCase();

        if(type.equals("fearful")){
            if(isVisible){
                JOptionPane.showMessageDialog(null,"Fearful lid no puede ponerse si la taza tiene algun interior mayor");
            }
            lastOk = false;
            return;
        }

        // el resto se comporta igual que normal por ahora
        pushLid(i);
    }

    public void popLid()
    {
        if(cups.isEmpty()){
            if(isVisible){
                JOptionPane.showMessageDialog(null,"No hay tazas");
            }
            lastOk = false;
            return;
        }
        Cup topCup = cups.get(cups.size() - 1);
        if(!topCup.hasLid()){
            if(isVisible){
                JOptionPane.showMessageDialog(null,"La taza superior no tiene tapa");
            }
            lastOk = false;
            return;
        }
        Lid lid = topCup.getLid();
        if(isVisible){
            lid.makeInvisible();
        }
        topCup.addLid(null);
        redrawCups();
        lastOk = true;
    }

    public void removeLid(int i)
    {
        for(Cup c : cups){
            if(c.getNumber() == i && c.hasLid()){
                Lid lid = c.getLid();
                if(isVisible){
                    lid.makeInvisible();
                }
                c.addLid(null);
                redrawCups();
                lastOk = true;
                return;
            }
        }
        if(isVisible){
            JOptionPane.showMessageDialog(null,"No existe esa tapa");
        }
        lastOk = false;
    }

    public void orderTower()
    {
        if(cups.isEmpty())
        {
            if(isVisible){
                JOptionPane.showMessageDialog(null,"No hay tazas para ordenar");
            }
            lastOk = false;
            return;
        }

        cups.sort((a,b) -> b.getHeight() - a.getHeight());

        redrawCups(); 

        lastOk = true;
    }

    public void reverseTower()
    {
        if(cups.isEmpty())
        {
            if(isVisible){
                JOptionPane.showMessageDialog(null,"No hay tazas para invertir");
            }
            lastOk = false;
            return;
        }

        // Invertir el orden de las tazas (de abajo hacia arriba)
        ArrayList<Cup> reversed = new ArrayList<>();
        for(int i = cups.size() - 1; i >= 0; i--)
        {
            reversed.add(cups.get(i));
        }
        cups = reversed;

        // Recalcular altura y volver a dibujar, preservando tapas
        recalculateCupTop();
        redrawCups();

        lastOk = true;
    }
    //consultas
    /**
     * Consultar la altura total de los elementos apilados
     */
    public int height()
    {
        // Altura visible de los elementos apilados
        return cupTop;
    }

    /**
     * Consulta Solo números de tazas que tienen tapa
     */    public int[] liddedCups()
    {
        ArrayList<Integer> numbers = new ArrayList<>();

        for(Cup c : cups)
        {
            if(c.hasLid())
            {
                numbers.add(c.getNumber());
            }
        }

        // Convertir ArrayList<Integer> a int[]
        int[] result = new int[numbers.size()];
        for(int i = 0; i < numbers.size(); i++)
        {
            result[i] = numbers.get(i);
        }

        return result;
    }

    /**
     * Consulta todos los elementos apilados (taza y tapa) con tipo y número
     */
    public String[][] stackingItems()
    {
        // Contar cuántos elementos vamos a incluir (tazas + tapas)
        int count = 0;
        for(Cup c : cups)
        {
            count++; // la taza
            if(c.hasLid()) count++; // la tapa
        }

        String[][] result = new String[count][2];

        int index = 0;
        for(Cup c : cups)
        {
            result[index][0] = "cup";
            result[index][1] = String.valueOf(c.getNumber());
            index++;

            if(c.hasLid())
            {
                result[index][0] = "lid";
                result[index][1] = String.valueOf(c.getNumber());
                index++;
            }
        }

        return result;
    }

    /**
     * Intercambia la posición de dos objetos en la torre.
     * Los objetos se identifican por un arreglo: {"cup","4"} o {"lid","4"}.
     */
    public void swap(String[] o1, String[] o2)
    {
        if(o1 == null || o2 == null || o1.length < 2 || o2.length < 2)
        {
            if(isVisible){
                JOptionPane.showMessageDialog(null,"Datos de intercambio inválidos");
            }
            lastOk = false;
            return;
        }

        String type1 = o1[0];
        String type2 = o2[0];

        int num1;
        int num2;

        try
        {
            num1 = Integer.parseInt(o1[1]);
            num2 = Integer.parseInt(o2[1]);
        }
        catch(NumberFormatException e)
        {
            if(isVisible){
                JOptionPane.showMessageDialog(null,"Número inválido para intercambio");
            }
            lastOk = false;
            return;
        }

        if(type1.equalsIgnoreCase("cup") && type2.equalsIgnoreCase("cup"))
        {
            swapCups(num1, num2);
        }
        else if(type1.equalsIgnoreCase("lid") && type2.equalsIgnoreCase("lid"))
        {
            swapLids(num1, num2);
        }
        else
        {
            // Uno es cup, otro es lid: intercambiar las tapas entre las tazas correspondientes
            if(type1.equalsIgnoreCase("cup") && type2.equalsIgnoreCase("lid"))
            {
                swapCupWithLid(num1, num2);
            }
            else if(type1.equalsIgnoreCase("lid") && type2.equalsIgnoreCase("cup"))
            {
                swapCupWithLid(num2, num1);
            }
            else
            {
                if(isVisible){
                    JOptionPane.showMessageDialog(null,"Tipo de objeto inválido para intercambio");
                }
                lastOk = false;
                return;
            }
        }

        // Recalcular posiciones después del intercambio
        makeVisible();
        lastOk = true;
    }

    private void swapCups(int n1, int n2)
    {
        int i1 = -1, i2 = -1;
        for(int i = 0; i < cups.size(); i++)
        {
            if(cups.get(i).getNumber() == n1) i1 = i;
            if(cups.get(i).getNumber() == n2) i2 = i;
        }

        if(i1 == -1 || i2 == -1)
        {
            if(isVisible){
                JOptionPane.showMessageDialog(null,"No se encontraron las tazas para intercambiar");
            }
            lastOk = false;
            return;
        }

        // Intercambiar taza + tapa juntos
        Cup temp = cups.get(i1);
        cups.set(i1, cups.get(i2));
        cups.set(i2, temp);

        redrawCups();  // recalcula posiciones gráficas
        recalculateCupTop();
    }

    private void swapLids(int n1, int n2)
    {
        Cup c1 = findCup(n1);
        Cup c2 = findCup(n2);

        if(c1 == null || c2 == null || !c1.hasLid() || !c2.hasLid())
        {
            if(isVisible){
                JOptionPane.showMessageDialog(null,"No se pueden intercambiar tapas (alguna no existe)");
            }
            lastOk = false;
            return;
        }

        Lid lid1 = c1.getLid();
        Lid lid2 = c2.getLid();

        c1.addLid(lid2);
        c2.addLid(lid1);

        redrawCups();   
        recalculateCupTop();
    }

    private void swapCupWithLid(int cupNumber, int lidNumber)
    {
        Cup cup = findCup(cupNumber);
        Cup lidCup = findCup(lidNumber);

        if(cup == null || lidCup == null || !lidCup.hasLid())
        {
            if(isVisible){
                JOptionPane.showMessageDialog(null,"No se pueden intercambiar cup/lid (no existe)");
            }
            lastOk = false;
            return;
        }

        Lid lid = lidCup.getLid();
        Lid temp = cup.getLid(); // puede ser null

        cup.addLid(lid);
        lidCup.addLid(temp);

        redrawCups();  // ?actualizar posiciones
        recalculateCupTop();
    }

    private Cup findCup(int number)
    {
        for(Cup c : cups)
        {
            if(c.getNumber() == number)
            {
                return c;
            }
        }
        return null;
    }

    /**
     * Busca un intercambio de dos tazas que reduzca la altura visible de la torre.
     * Retorna el par de objetos a intercambiar, o un arreglo vacío si no hay mejora.
     */
    public String[][] swapToReduce()
    {
        int currentHeight = height();
        int bestHeight = currentHeight;
        int bestA = -1;
        int bestB = -1;

        for(int i = 0; i < cups.size(); i++)
        {
            for(int j = i + 1; j < cups.size(); j++)
            {
                ArrayList<Cup> copy = new ArrayList<>(cups);
                Cup temp = copy.get(i);
                copy.set(i, copy.get(j));
                copy.set(j, temp);

                int newHeight = computeVisibleHeight(copy);
                if(newHeight < bestHeight)
                {
                    bestHeight = newHeight;
                    bestA = cups.get(i).getNumber();
                    bestB = cups.get(j).getNumber();
                }
            }
        }

        if(bestA == -1)
        {
            return new String[0][0];
        }

        return new String[][]{{"cup", String.valueOf(bestA)}, {"cup", String.valueOf(bestB)}};
    }

    private int computeVisibleHeight(ArrayList<Cup> order)
    {
        int visible = 0;
        for(Cup c : order)
        {
            int h = c.getHeight();
            if(c.hasLid()){
                h += 1;
            }
            visible += h;
        }
        return visible;
    }

    /**
     * Pone en el estado visual de "tapada" a todas las tazas que tienen su tapa.
     */
    public void cover()
    {
        for(Cup c : cups)
        {
            if(c.hasLid())
            {
                // Force appearance update (las tapadas lucen diferentes)
                c.makeVisible();
            }
        }
        lastOk = true;
    }

    /**
     * Terminar el simulador
     */
    public void exit()
    {
        makeInvisible(); // Oculta todo
        cups.clear();
        marks.clear();
        // Terminar el programa
        System.exit(0);
    }
}
