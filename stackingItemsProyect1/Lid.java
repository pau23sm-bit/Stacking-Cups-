public class Lid extends StackingItem
{
    private Rectangle top;

    private int widthBlocks; // 2i-1

    public static void setBlockSize(int size)
    {
        StackingItem.setBlockSize(size);
    }

    public Lid(int i, String color)
    {
        super(i, color);
        this.widthBlocks = 2 * i - 1;

        top = new Rectangle();
        top.changeSize(blockSize, widthBlocks * blockSize); // 1 bloque alto
        top.changeColor(color);
    }

    @Override
    public void makeVisible(){
        top.makeVisible();
        isVisible = true;
    }

    @Override
    public void makeInvisible(){
        top.makeInvisible();
        isVisible = false;
    }

    @Override
    public void moveTo(int newX, int newY){
        int dx = newX - xPosition;
        int dy = newY - yPosition;

        top.moveHorizontal(dx);
        top.moveVertical(dy);

        xPosition = newX;
        yPosition = newY;
    }

    @Override
    public int getHeight(){
        return 1; // 1 bloque de alto
    }

    @Override
    public int getWidth(){
        return widthBlocks;
    }
}