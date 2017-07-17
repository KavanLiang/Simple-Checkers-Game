import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class Label here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Label extends Actor
{
    private String label_;
    private int fontSize_;
    private Color textColor_;
    private Color bkColor_;
    
    public Label(String text)
    {
        super();//Actor now built
        label_ = text;
        fontSize_ = 24;
        textColor_ = Color.RED;
        bkColor_ = new Color(0,0,0,0);//Transparent with alpha = 0
        
        updateImage();
    }
    
    public void updateImage()
    {
        GreenfootImage img = new GreenfootImage(label_, fontSize_, textColor_, bkColor_);
        setImage(img);
    }
    
    public void setLabel(String text)
    {
        label_ = text;
        updateImage();
    }
    
    public void setFontSize(int fs)
    {
        fontSize_ = fs;
        updateImage();
    }
    
    public void setTextColor(Color c)
    {
        textColor_ = c;
        updateImage();
    }
    
    public void setBackground(Color c)
    {
        bkColor_ = c;
        updateImage();
    }
    
    /**
     * Act - do whatever the Label wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        
    }    
}
