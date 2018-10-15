package main;

import java.io.Serializable;

/**
 *
 * @author deb kanti
 */
public class Location implements Serializable
{
    public double x;
    public double y;
    private double z;
    
    /**
     * @return the x
     */
   public Location(double x, double y, double z)
   {
       this.x = x;
       this.y = y;
       this.z = z;               
   }

   public Location()
   {
       this.x=0;
       this.y=0;
       this.z = 0;
   }
    
//   public boolean isNull()
//    {
//	return (x==null) && (y==null);
//    }
   
   
    public double getX()
    {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the z
     */
    public double getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(double z) {
        this.z = z;
    }
    
}
