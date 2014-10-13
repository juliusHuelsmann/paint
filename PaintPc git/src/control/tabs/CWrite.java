package control.tabs;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.forms.Page;
import view.forms.tabs.Look;
import view.forms.tabs.Write;
import model.objects.pen.Pen;
import model.objects.pen.normal.BallPen;
import model.objects.pen.normal.Pencil;
import model.settings.Constants;
import model.settings.Status;



/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public final class CWrite implements ActionListener {

    /**
     * Empty utility class constructor.
     */
    private CWrite() { }

    
    private final static Color 
    clr_green = new Color(2 * 2 * 2 * 2 * 2 * 2, 
            (int) (Math.pow(2, 2 + 2 + 2 + 1) 
                    + Math.pow(2, 2 + 2 + 2) + 2 + 2 + 1), 
            (2 + 1) * (2 + 1) * (2 + 1) + (2 + 2 + 2 + 1) 
            * (2 + 2 + 2 + 2 + 2)),
            clr_pink = new Color(255, 153, 254),
            clr_blue = new Color(255, 100, 100),
            clr_darkBlue = new Color(112, 146, 190);
    
    /**
     * 
     */
    public final static Pen 
    penWrite1 = new Pencil(Constants.PEN_ID_LINES, 2, Color.gray),
    penWrite2 = new BallPen(Constants.PEN_ID_LINES, 2, clr_green),
    penProof1 = new Pencil(Constants.PEN_ID_LINES, 2, clr_blue), 
    penProof2 = new BallPen(Constants.PEN_ID_LINES, 2, Color.gray), 
    penExmpl1 = new Pencil(Constants.PEN_ID_LINES, 2, clr_pink), 
    penExmpl2 = new BallPen(Constants.PEN_ID_LINES, 2, clr_darkBlue),
    penCmmnt1 = new Pencil(Constants.PEN_ID_LINES, 2, Color.gray),
    penCmmnt2 = new BallPen(Constants.PEN_ID_LINES, 2, clr_pink);
    
    
    
    /**
     * The only instance of this class.
     */
    private static CWrite instance;
    
    
    @Override public void actionPerformed(final ActionEvent _event) {

        if (_event.getSource().equals(Write.getInstance()
                .getTb_beispiel())) {
            deactivate();
            Status.setPenSelected1(Pen.clonePen(penExmpl1));
            Status.setPenSelected2(Pen.clonePen(penExmpl2));
        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_bemerkung())) {
            deactivate();
            Status.setPenSelected1(Pen.clonePen(penCmmnt1));
            Status.setPenSelected2(Pen.clonePen(penCmmnt2));
        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_beweis())) {
            deactivate();
            Status.setPenSelected1(Pen.clonePen(penProof1));
            Status.setPenSelected2(Pen.clonePen(penProof2));
        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_headline1())) {
                
        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_headline2())) {
                
        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_headline3())) {
                
        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_satz())) {
            deactivate();
            Status.setPenSelected1(Pen.clonePen(penWrite1));
            Status.setPenSelected2(Pen.clonePen(penWrite2));
            //TODO: update paint gui.
        }
    }
    
    
    public void deactivate() {
        Write.getInstance().getTb_beispiel().setActivated(false);
        Write.getInstance().getTb_bemerkung().setActivated(false);
        Write.getInstance().getTb_beweis().setActivated(false);
        Write.getInstance().getTb_satz().setActivated(false);
    }
    
    
    /**
     * Return the only instance of this class.
     * @return the only instance of this class.
     */
    public static CWrite getInstance() {
        if (instance == null) {
            instance = new CWrite();
        }
        return instance;
    }

}
