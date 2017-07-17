import greenfoot.*;
import java.awt.Color;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Highlight here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Highlight extends Actor
{
    private static final int SIZE = 50, TRANSPARENCY = 150;
    public Highlight()
    {
        GreenfootImage highlight = new GreenfootImage(SIZE, SIZE);
        highlight.setColor(Color.BLUE);
        highlight.setTransparency(TRANSPARENCY);
        highlight.fill();
        setImage(highlight);
    }
    public void act() 
    {
        // Add your action code here.
    }    
}
