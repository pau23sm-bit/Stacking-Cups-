public abstract class StackingItem
{
    protected int number;
    protected String color;
    protected int xPosition;
    protected int yPosition;
    protected boolean isVisible;

    protected static int blockSize;

    public StackingItem(int number, String color)
    {
        this.number = number;
        this.color = color;
        this.xPosition = 0;
        this.yPosition = 0;
        this.isVisible = false;
    }

    public static void setBlockSize(int size)
    {
        blockSize = size;
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public int getNumber()
    {
        return number;
    }

    public int getX()
    {
        return xPosition;
    }

    public int getY()
    {
        return yPosition;
    }

    public String getColor()
    {
        return color;
    }

    public abstract void makeVisible();
    public abstract void makeInvisible();
    public abstract void moveTo(int x, int y);

    public abstract int getHeight();
    public abstract int getWidth();
}
