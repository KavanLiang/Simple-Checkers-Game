/**
 * @Author Kavan
 * v1.0
 */ 
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
public class CheckersLogic  
{
    public static final int EMPTY = 0;
    public static final int RED = 1;
    public static final int BLACK = 2;
    public static final int RED_KING = 3;
    public static final int BLACK_KING = 4;
    public static final int ERROR = -1;
    public static final int PLAYER1 = 0, PLAYER2 = 1;
    public static final int TOP_RIGHT = 0, TOP_LEFT = 1, BOTTOM_LEFT = 2, BOTTOM_RIGHT = 3;
    public final static int BUFFER = 1, BUFFER_A = 2;
    
    
    private int[][] status_;
    private int x_, y_;
    private int player_;
    private int offsetMod;
    private int cellStatus;
    private boolean hasJumped;
    private Scanner sc;
    public CheckersLogic(int x, int y, int xPieces)//Sets up the Board when the constructor is called
    {
        x_ = x;
        y_ = y;
        offsetMod = 0;
        status_ = new int[x_][y_];
        player_ = BLACK;
        hasJumped = false;
        sc = new Scanner(System.in);
        for(int i = 0; i<y_; i++)
        {
            if(i<xPieces)
            {
                for(int j = 0; j<x_; j++)
                {
                    if(j%BUFFER_A == 0 && offsetMod%BUFFER_A == 0)
                        status_[j][i] = RED;
                    else if(j%BUFFER_A != 0 && offsetMod%BUFFER_A == BUFFER)
                        status_[j][i] = RED;
                }
            }
            else if(i>=y_-xPieces)
            {
               for(int j = 0; j<x_; j++)
                {
                    if(j%BUFFER_A == 0 && offsetMod%BUFFER_A == 0)
                        status_[j][i] = BLACK;
                    else if(j%BUFFER_A != 0 && offsetMod%BUFFER_A == BUFFER)
                        status_[j][i] = BLACK;
                }
            }
            offsetMod++;
        }
    }
    
    public void changeTurn()//changes the turn to the other player
    {
        if(player_ == RED)
            player_ = BLACK;
        else
            player_ = RED;
    }
    
    public int getRows()
    {
        return x_;
    }
    
    public int getColumns()
    {
        return y_;
    }
    
    public int getPlayer()//gets current player
    {
        return player_;
    }
    
    public int getCellStatus(int x, int y)//returns the int value for whatever is in a cell
    {
        if(x < 0 || x >= x_ || y < 0 || y >= y_)
            return ERROR;
        else
            return status_[x][y];
    }
    
    public void movePiece(int oldRow, int oldColumn, int newRow, int newColumn)//hops and completes chains if possible
    {
        if(canMove(oldRow, oldColumn) && getCellStatus(newRow, newColumn) != ERROR && getCellStatus(oldRow, oldColumn) != ERROR)
            {
            int piece = getCellStatus(oldRow, oldColumn);
            status_[oldRow][oldColumn] = EMPTY;
            status_[newRow][newColumn] = piece;
            int x = getMovementDirection(oldRow, oldColumn, newRow, newColumn);
            if(isJump(oldRow, oldColumn, newRow, newColumn))
            {
                if(x == TOP_LEFT)
                    status_[oldRow-BUFFER][oldColumn-BUFFER] = EMPTY;
                else if(x == TOP_RIGHT)
                    status_[oldRow+BUFFER][oldColumn-BUFFER] = EMPTY;
                else if(x == BOTTOM_LEFT)
                    status_[oldRow-BUFFER][oldColumn+BUFFER] = EMPTY;
                else if(x == BOTTOM_RIGHT)
                    status_[oldRow+BUFFER][oldColumn+BUFFER] = EMPTY;
                if(piece == BLACK_KING || piece == RED_KING)
                {
                    if(canJump(newRow, newColumn, TOP_LEFT))
                        movePiece(newRow, newColumn, newRow-BUFFER_A, newColumn-BUFFER_A);
                    else if(canJump(newRow, newColumn, TOP_RIGHT))
                        movePiece(newRow, newColumn, newRow+BUFFER_A, newColumn-BUFFER_A);
                    else if(canJump(newRow, newColumn, BOTTOM_LEFT))
                        movePiece(newRow, newColumn, newRow-BUFFER_A, newColumn+BUFFER_A);
                    else if(canJump(newRow, newColumn, BOTTOM_RIGHT))
                        movePiece(newRow, newColumn, newRow+BUFFER_A, newColumn+BUFFER_A);
                }
                else if(piece == RED)
                {
                    if(canJump(newRow, newColumn, BOTTOM_LEFT))
                        movePiece(newRow, newColumn, newRow-BUFFER_A, newColumn+BUFFER_A);
                    else if(canJump(newRow, newColumn, BOTTOM_RIGHT))
                        movePiece(newRow, newColumn, newRow+BUFFER_A, newColumn+BUFFER_A);
                }
                else if(piece == BLACK)
                {
                     if(canJump(newRow, newColumn, TOP_LEFT))
                        movePiece(newRow, newColumn, newRow-BUFFER_A, newColumn-BUFFER_A);
                    else if(canJump(newRow, newColumn, TOP_RIGHT))
                        movePiece(newRow, newColumn, newRow+BUFFER_A, newColumn-BUFFER_A);
                }
            }
            checkForNewKings();
        }
    }
    
    public boolean isHop(int oldX, int oldY, int newX, int newY)//returns true if the move is 1 diagonal space
    {
        int dx = oldX - newX;
        int dy = oldY - newY;
        if(Math.abs(dx) == BUFFER && Math.abs(dy) == BUFFER)
            return true;
        else
            return false;
    }
    
    public boolean isJump(int oldX, int oldY, int newX, int newY)//checks if a move is a jump or not
    {
        int dx = oldX - newX;
        int dy = oldY - newY;
        if(Math.abs(dx) == BUFFER_A && Math.abs(dy) == BUFFER_A)
            return true;
        else
            return false;
    }
    
    public boolean canHop(int oldRow, int oldColumn, int direction)//checks if a piece can move 1 diagonal
    {
        int piece = getCellStatus(oldRow, oldColumn);
        if(canMove(oldRow, oldColumn))
            {
            if(piece == RED_KING || piece == BLACK_KING)
            {
                if(direction == BOTTOM_LEFT)
                    return isEmpty(oldRow-BUFFER, oldColumn+BUFFER);
                else if(direction == BOTTOM_RIGHT)
                    return isEmpty(oldRow+BUFFER, oldColumn+BUFFER);
                else if(direction == TOP_LEFT)
                    return isEmpty(oldRow-BUFFER, oldColumn-BUFFER);
                else if(direction == TOP_RIGHT)
                    return isEmpty(oldRow+BUFFER, oldColumn-BUFFER);
            }
            else if(piece == RED)
            {
                if(direction == BOTTOM_LEFT)
                    return isEmpty(oldRow-BUFFER, oldColumn+BUFFER);
                else if(direction == BOTTOM_RIGHT)
                    return isEmpty(oldRow+BUFFER, oldColumn+BUFFER);
            }
            else if(piece == BLACK)
            {
                 if(direction == TOP_LEFT)
                    return isEmpty(oldRow-BUFFER, oldColumn-BUFFER);
                else if(direction == TOP_RIGHT)
                    return isEmpty(oldRow+BUFFER, oldColumn-BUFFER);
            }
        }
        return false;
    }
    
    public boolean isEmpty(int x, int y)//returns true if a cell is empty
    {
        if(getCellStatus(x, y) != 0)
            return false;
        else
            return true;
    }
    
    public boolean hasOpposingColor(int x, int y)//returns true if a cell houses the opposite color
    {
        if(player_ == RED)
        {
            if(BLACK == getCellStatus(x, y) || BLACK_KING == getCellStatus(x, y))
                return true;
        }
        else
        {
            if(RED == getCellStatus(x, y) || RED_KING == getCellStatus(x, y))
                return true;
        }
        return false;
    }
    
    public boolean canJump(int oldRow, int oldColumn, int direction)//returns true if a piece can take a piece
    {
        int piece = getCellStatus(oldRow, oldColumn);
        if(canMove(oldRow, oldColumn))
        {
            if(piece == RED_KING || piece == BLACK_KING)
            {
                if(direction == TOP_LEFT)
                {
                     if(hasOpposingColor(oldRow-BUFFER, oldColumn-BUFFER) && isEmpty(oldRow-BUFFER_A, oldColumn-BUFFER_A))
                        return true;
                }
                else if(direction == TOP_RIGHT)
                {
                     if(hasOpposingColor(oldRow+BUFFER, oldColumn-BUFFER) && isEmpty(oldRow+BUFFER_A, oldColumn-BUFFER_A))
                        return true;
                }
                else if(direction == BOTTOM_LEFT)
                {
                     if(hasOpposingColor(oldRow-BUFFER, oldColumn+BUFFER) && isEmpty(oldRow-BUFFER_A, oldColumn+BUFFER_A))
                        return true;
                }
                else if(direction == BOTTOM_RIGHT)
                {
                     if(hasOpposingColor(oldRow+BUFFER, oldColumn+BUFFER) && isEmpty(oldRow+BUFFER_A, oldColumn+BUFFER_A))
                        return true;
                }
            }
            else if(piece == BLACK)
            {
                if(direction == TOP_LEFT)
                {
                     if(hasOpposingColor(oldRow-BUFFER, oldColumn-BUFFER) && isEmpty(oldRow-BUFFER_A, oldColumn-BUFFER_A))
                        return true;
                }
                else if(direction == TOP_RIGHT)
                {
                     if(hasOpposingColor(oldRow+BUFFER, oldColumn-BUFFER) && isEmpty(oldRow+BUFFER_A, oldColumn-BUFFER_A))
                        return true;
                }
            }
            else if(piece == RED)
            {
                if(direction == BOTTOM_LEFT)
                {
                     if(hasOpposingColor(oldRow-BUFFER, oldColumn+BUFFER) && isEmpty(oldRow-BUFFER_A, oldColumn+BUFFER_A))
                        return true;
                }
                else if(direction == BOTTOM_RIGHT)
                {
                     if(hasOpposingColor(oldRow+BUFFER, oldColumn+BUFFER) && isEmpty(oldRow+BUFFER_A, oldColumn+BUFFER_A))
                        return true;
                }
            }
        }
        return false;
    }
    
    public boolean canMove(int x, int y)//checks if a piece is controllable by the player
    {
        cellStatus = getCellStatus(x, y);
        if(player_ == RED && (getCellStatus(x, y) == RED || getCellStatus(x, y) == RED_KING))
            return true;
        else if(player_ == BLACK && (getCellStatus(x, y) == BLACK || getCellStatus(x, y) == BLACK_KING))
            return true;
        else
            return false;
    }
    
    public void checkForNewKings()//swaps pieces that have made it to the opposite ends for kings
    {
        if(player_ == RED)
        {
            for(int x = 0; x<y_; x++)
            {
                if(getCellStatus(x, y_-BUFFER) == RED)
                    status_[x][y_-BUFFER] = RED_KING;
            }
        }
        else
        {
             for(int x = 0; x<y_; x++)
            {
                if(getCellStatus(x, 0) == BLACK)
                    status_[x][0] = BLACK_KING;
            }
        }
    }
    public static int getMovementDirection(int x, int y, int newX, int newY)//returns an integer that represents a direction
    {
        int dx = x-newX;
        int dy = y-newY;
        if(dx>0 && dy>0)
            return TOP_LEFT;
        else if(dx>0 && dy<0)
            return BOTTOM_LEFT;
        else if(dx<0 && dy>0)
            return TOP_RIGHT;
        else if(dx<0 && dy<0)
            return BOTTOM_RIGHT;
        else
            return ERROR;
    }
    public boolean gameOver()//checks if the game is over
    {
        int redCount = 0, blackCount = 0;
        for(int x = 0; x<x_ ; x++)
        {
            for(int y = 0; y<y_; y++)
            {
                if(getCellStatus(x, y) == RED || RED_KING == getCellStatus(x,y))
                    redCount++;
                else if(getCellStatus(x, y) == BLACK || getCellStatus(x, y) == BLACK_KING)
                    blackCount++;
            }
        }
        if(redCount>0 && blackCount>0)
            return false;
        else
            return true;
    }
    public boolean didRedWin()//returns true if red won
    {
        boolean redWin = true;
        for(int x = 0; x<x_ ; x++)
        {
            for(int y = 0; y<y_; y++)
            {
                if(getCellStatus(x,y) == BLACK || getCellStatus(x,y) == BLACK_KING)
                {
                    redWin = false;
                }
            }
        }
        return redWin;
    }
    public void forceJump()//forces jumps if possible
    {
        for(int x = 0; x<x_; x++)
        {
            for(int y = 0; y<y_; y++)
            {
                if(canMove(x, y))
                {
                    if(canJump(x, y, TOP_LEFT))
                    {
                        movePiece(x, y, x-BUFFER_A, y-BUFFER_A);
                        changeTurn();
                    }
                    else if(canJump(x,y, TOP_RIGHT))
                    {
                        movePiece(x, y, x+BUFFER_A, y-BUFFER_A);
                        changeTurn();
                    }
                    else if(canJump(x,y, BOTTOM_LEFT))
                    {
                        movePiece(x, y, x-BUFFER_A, y+BUFFER_A);
                        changeTurn();
                    }
                    else if(canJump(x,y, BOTTOM_RIGHT))
                    {
                        movePiece(x, y, x+BUFFER_A, y+BUFFER_A);
                        changeTurn();
                    }
                }
            }
        }
    }
    public boolean[][] moves(int x, int y)//returns an array of possible moves given a piece
    {
        boolean posMoves[][] = new boolean[x_][y_];
        int movementD;
        if(canMove(x,y))
        {
            for(int i = 0; i<x_; i++)
            {
                for(int j = 0; j<y_; j++)
                {
                    if((isHop(x, y, i, j) || isJump(x, y, i, j)))
                    {
                        movementD = getMovementDirection(x, y, i, j);
                        if(canHop(x, y, movementD) && isHop(x, y, i ,j) || canJump(x, y, movementD) && isJump(x, y, i, j))
                        {
                            posMoves[i][j] = true;
                        }
                    }
                }
            }
        }
        return posMoves;
    }
    public void saveState(File save)
    {
        try
        {
            PrintWriter writer = new PrintWriter(save);
            writer.println(x_ + "\t" + y_);
            for(int x = 0; x<x_; x++)
            {
                for(int y = 0; y<y_; y++)
                {
                    writer.print(status_[y][x]+"\t");
                }
                writer.println();
            }
            writer.println(getPlayer());
            writer.close();
        }
        catch(Exception e)
        {
            JOptionPane optionPane = new JOptionPane(e.toString(), JOptionPane.ERROR_MESSAGE);  
            JDialog dialog = optionPane.createDialog("Failed to save file");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);    
        }
    }
    public void readSave(File save)
    {
        try
        {
            Scanner read = new Scanner(save);
            x_ = read.nextInt();
            y_ = read.nextInt();
            status_ = new int[x_][y_];
            for(int x = 0; x<x_; x++)
            {
                for(int y = 0; y<y_; y++)
                {
                    status_[y][x] = read.nextInt();
                }
            }
            player_ = read.nextInt();
            read.close();
        }
        catch(Exception e)
        {
            JOptionPane optionPane = new JOptionPane(e.toString(), JOptionPane.ERROR_MESSAGE);  
            JDialog dialog = optionPane.createDialog("Failed to open file");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
        }
    }
}
