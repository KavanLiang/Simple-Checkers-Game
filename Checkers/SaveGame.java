import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JDialog;

/**
 * Write a description of class SaveGame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SaveGame extends Button
{
    private CheckersLogic state_;
    public SaveGame(CheckersLogic state)
    {
        super("Save");
        state_ = state;
    }
    
    public void onClick()
    {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            state_.saveState(f);
        }
    }
}
