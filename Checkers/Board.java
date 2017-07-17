import greenfoot.*;
import java.awt.Color;
import java.io.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
public class Board extends World
{
    private CheckersLogic logic_;
    private boolean hasClicked;
    private boolean hasClickedTwice;
    private MouseInfo click;
    private int oldX, oldY, newX, newY;
    private Label victoryText;
    private Label turnText;
    private final static String RED_WINS = "Red Wins!", BLACK_WINS = "Black wins!";
    private final static String RED = "Red's Turn", BLACK = "Black's Turn";
    private final static int COLUMNS = 8, ROWS = 8, PIECE_ROWS = 3, CELL_SIZE = 50;
    public Board()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(COLUMNS, ROWS+2, CELL_SIZE);
        logic_ = new CheckersLogic(COLUMNS, ROWS, PIECE_ROWS);
        this.setPaintOrder(Highlight.class, Pieces.class);
        GreenfootImage background = this.getBackground();
        background.setColor(Color.MAGENTA);
        background.fillRect(0, (getHeight() - 2)*CELL_SIZE, getWidth()*CELL_SIZE, 2*CELL_SIZE);
        hasClicked = false;
        hasClickedTwice = false;
        this.addObject(new SaveGame(logic_), 0, ROWS);
        this.addObject(new LoadGame(logic_), 0, ROWS+1);    
    }
    public void act()
    {
        logic_.forceJump();
        updateBoard();
        displayTurn();
        if(Greenfoot.mouseClicked(null))
        {
            mouseClickManager();
        }
        victory();
    }
    public void displayTurn()
    {
        if(turnText != null)
        {
            this.removeObject(turnText);
        }
        if(logic_.getPlayer() == CheckersLogic.RED)
            turnText = new Label(RED);
        else
            turnText = new Label(BLACK);
        turnText.setTextColor(Color.BLACK);
        this.addObject(turnText, COLUMNS/2-1, ROWS+1);    
    }
    public void updateBoard()
    {
        int rows = logic_.getRows();
        int columns = logic_.getColumns();
        for(int x = 0; x < rows; x++)
        {
            for(int y = 0; y < columns; y++)
            {
                removeObjects(getObjectsAt(x,y, Pieces.class));
                int status = logic_.getCellStatus(x,y);
                if(status == CheckersLogic.RED)
                    addObject(new Red(), x,y);
                else if(status == CheckersLogic.BLACK)
                    addObject(new Black(), x,y);
                else if(status == CheckersLogic.RED_KING)
                    addObject(new RedKing(), x,y);
                else if(status == CheckersLogic.BLACK_KING)
                    addObject(new BlackKing(), x,y);                
            }
        }
    }
    public void pieceInteraction()
    {
       if((logic_.isHop(oldX, oldY, newX, newY) && logic_.canHop(oldX, oldY, CheckersLogic.getMovementDirection(oldX, oldY, newX, newY)) || (logic_.isJump(oldX, oldY, newX, newY) && logic_.canJump(oldX, oldY, CheckersLogic.getMovementDirection(oldX, oldY, newX, newY)))))
       {
           logic_.movePiece(oldX, oldY, newX, newY);
           logic_.changeTurn();
       }
       hasClicked = false;
       hasClickedTwice = false;
       clearPosMoves();
    }
    public void mouseClickManager()
    {
        click = Greenfoot.getMouseInfo();
        int x = click.getClickCount();
        if(x%2 == 1 && !hasClicked)
        {
            hasClicked = true;
            oldX = click.getX();
            oldY = click.getY();
            displayPosMoves(oldX, oldY);
        }
        else if(!hasClickedTwice && hasClicked)
        {
            hasClickedTwice = true;
            newX = click.getX();
            newY = click.getY();
        }
        if(hasClicked && hasClickedTwice)
        {
            pieceInteraction();
        }
    }
    public void displayPosMoves(int x, int y)
    {
        if(hasClicked && logic_.canMove(x, y))
        {
            boolean posMoves[][] = logic_.moves(x, y);
            for(int i = 0; i<COLUMNS; i++)
            {
                for(int j = 0; j<ROWS; j++)
                {
                    removeObjects(getObjectsAt(i,j, Highlight.class));
                    if(posMoves[i][j])
                    {
                        addObject(new Highlight(), i, j);
                    }
                }
            }
        }
    }
    public void clearPosMoves()
    {
        for(int i = 0; i<COLUMNS; i++)
        {
            for(int j = 0; j<ROWS; j++)
            {
                removeObjects(getObjectsAt(i,j, Highlight.class));
            }
        }
    }
    public void victory()
    {
        if(logic_.gameOver())
        {
            if(logic_.didRedWin())
            {
                victoryText = new Label(RED_WINS);
            }
            else
            {
                victoryText = new Label(BLACK_WINS);
            }
            this.addObject(victoryText, ROWS/2, COLUMNS/2);
        }
    }
}
