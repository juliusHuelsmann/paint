package view.util.mega;


/**
 * Interface for view classes (which add the possibility to rotate
 * the components (by overwriting paintComponents(Graphics g) in container 
 * classes JPanel and JFrame) or by overwriting the method paintComponent(
 * Graphics g) for rotating the images and the icons.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public interface Mega {


    /**
     * Turn the owned components.
     */
    void turn();
}
