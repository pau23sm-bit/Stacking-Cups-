/**
 * Cup — esta clase representa una taza que se apila en la torre.
 *
 * Una taza es como un recipiente en forma de U, hecha de bloques rectangulares.
 * Cada taza tiene un número que determina su tamaño.
 * Puede tener una tapa encima.
 */
public class Cup extends StackingItem
{
    private int height; // altura en bloques
    private Lid lid; // la tapa, si tiene
    private int size; // tamaño total
    private Rectangle[][] blocks; // los bloques que forman la taza
    private String type; // tipo de taza [normal, opener, hierarchical, fearful, crazy]
    private boolean unremovable; // para tipo hierarchical

    /**
     * Constructor para crear una taza normal.
     * @param number el número de la taza (1,2,3...)
     * @param color el color de la taza
     */
    public Cup(int number, String color)
    {
        this(number, color, "normal");
    }

    /**
     * Constructor para crear una taza con tipo.
     * @param number el número de la taza (1,2,3...)
     * @param color el color de la taza
     * @param type el tipo de la taza
     */
    public Cup(int number, String color, String type)
    {
        super(number, color);

        this.type = type == null ? "normal" : type.toLowerCase();
        this.color = typeColor(color, this.type);
        this.height = 2 * number - 1; // fórmula para la altura
        this.size = this.height;
        this.lid = null; // al principio no tiene tapa
        this.unremovable = false;

        createShape(); // crea la forma de U
    }

    /**
     * Configura el tamaño de los bloques.
     * @param size el tamaño en píxeles
     */
    public static void setBlockSize(int size)
    {
        StackingItem.setBlockSize(size);
    }

    /**
     * Crea la forma de la taza: una U con bloques.
     * Para tazas muy pequeñas (altura 1), dibuja la forma llena.
     * Para tazas más grandes, dibuja solo los bordes (forma de U).
     */
    private void createShape()
    {
        blocks = new Rectangle[size][size];

        for(int row = 0; row < size; row++)
        {
            for(int col = 0; col < size; col++)
            {
                boolean drawBlock = false;
                
                // Para tazas muy pequeñas (height 1), dibuja la forma completa
                if(height == 1)
                {
                    drawBlock = true;
                }
                // Para tazas más grandes, dibuja solo los bordes (forma de U)
                else if(col == 0 || col == size - 1 || row == size - 1)
                {
                    drawBlock = true;
                }
                
                if(drawBlock)
                {
                    Rectangle r = new Rectangle();
                    r.changeSize(blockSize, blockSize);
                    r.changeColor(color);

                    r.moveHorizontal(xPosition + col * blockSize);
                    r.moveVertical(yPosition + row * blockSize);

                    blocks[row][col] = r;
                }
            }
        }
    }

    @Override
    public void makeVisible()
    {
        updateAppearance();

        for(int row = 0; row < size; row++)
        {
            for(int col = 0; col < size; col++)
            {
                if(blocks[row][col] != null)
                {
                    blocks[row][col].makeVisible();
                }
            }
        }
        isVisible = true;
    }

    @Override
    public void makeInvisible()
    {
        for(int row = 0; row < size; row++)
        {
            for(int col = 0; col < size; col++)
            {
                if(blocks[row][col] != null)
                {
                    blocks[row][col].makeInvisible();
                }
            }
        }
        isVisible = false;
    }

    @Override
    public void moveTo(int newX, int newY)
    {
        int dx = newX - xPosition;
        int dy = newY - yPosition;

        for(int row = 0; row < size; row++)
        {
            for(int col = 0; col < size; col++)
            {
                if(blocks[row][col] != null)
                {
                    blocks[row][col].moveHorizontal(dx);
                    blocks[row][col].moveVertical(dy);
                }
            }
        }
        xPosition = newX;
        yPosition = newY;
    }

    public void addLid(Lid lidColor)
    {
        this.lid = lidColor;
        updateAppearance();
    }

    public void removeLid()
    {
        this.lid = null;
        updateAppearance();
    }

    public String getType()
    {
        return type;
    }

    public void setUnremovable(boolean unremovable)
    {
        this.unremovable = unremovable;
    }

    public boolean isUnremovable()
    {
        return unremovable;
    }

    public boolean hasLid()
    {
        return lid != null;
    }

    public Lid getLid()
    {
        return lid;
    }

    /**
     * Actualiza el color de la taza según si está tapada o no.
     * Las tazas tapadas lucen diferentes (requisito de usabilidad).
     */
    private void updateAppearance()
    {
        String displayColor = hasLid() ? "black" : color;

        for(int row = 0; row < size; row++)
        {
            for(int col = 0; col < size; col++)
            {
                if(blocks[row][col] != null)
                {
                    blocks[row][col].changeColor(displayColor);
                }
            }
        }
    }

    @Override

    public int getHeight()
    {
        return height;
    }

    public static String typeColor(String defaultColor, String type)
    {
        switch(type.toLowerCase())
        {
            case "opener":
                return "cyan";
            case "hierarchical":
                return "magenta";
            case "fearful":
                return "orange";
            case "crazy":
                return "red";
            default:
                return defaultColor;
        }
    }

    @Override
    public int getWidth()
    {
        return height;
    }
}
