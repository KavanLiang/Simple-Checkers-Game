import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import javax.swing.JFileChooser;
import java.io.File;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JDialog;

/**
 * Write a description of class LoadGame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LoadGame extends Button
{
    private CheckersLogic state_;
    public LoadGame(CheckersLogic gameState)
    {
        super("Load");
        state_ = gameState;
    }
    
    public void onClick()
    {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
        {
            File f = fileChooser.getSelectedFile();
            state_.readSave(f);               
            Board board = (Board)getWorld();
            board.updateBoard();
        }        
    }
    
    public static int toInt(String s)
    {
        Integer convert = new Integer(s);
        return convert.intValue();
    }
}
