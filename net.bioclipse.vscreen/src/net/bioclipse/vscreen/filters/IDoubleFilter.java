package net.bioclipse.vscreen.filters;


/**
 * 
 * @author ola
 *
 */
public interface IDoubleFilter extends IFilter{

    public void setThreshold(double threshold);
    public double getThreshold();

    public void setOperator(String operator);
    public int getOperator();

}
