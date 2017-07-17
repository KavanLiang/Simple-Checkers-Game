import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

public abstract class Button extends Actor
{
    public Button(String label)
    {
        GreenfootImage text = new GreenfootImage(label,12,Color.BLACK,Color.LIGHT_GRAY);
        int buffer = 4;//four pixel border around text
        
        GreenfootImage btn = new GreenfootImage(text.getWidth()+2*buffer, text.getHeight()+2*buffer);
        
        //Light Gray background
        btn.setColor(Color.LIGHT_GRAY);
        btn.fillRect(0,0,btn.getWidth(),btn.getHeight());
        
        //Black border
        btn.setColor(Color.BLACK);
        btn.drawRect(0,0,btn.getWidth()-1,btn.getHeight()-1);//don't draw full size or the edge will not show
        
        //add the text
        btn.drawImage(text,buffer,buffer);
        setImage(btn);
    }
    /**
     * Act - do whatever the Button wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        if(Greenfoot.mouseClicked(this))
            onClick();
    } 
    
    //to be implemented by subclasses for particular buttons
    public abstract void onClick();
}