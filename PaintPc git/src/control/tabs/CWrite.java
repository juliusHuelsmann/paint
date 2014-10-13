//package declaration
package control.tabs;

//import declarations
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.tabs.Paint;
import view.tabs.Write;
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

    /*
     * Singleton
     */
    
    /**
     * The only instance of this class.
     */
    private static CWrite instance;

    /*
     * Identifiers
     */
    
    /**
     * 
     */
    private static final Color 
    CLR_GRAY = new Color(160, 160, 160),
    CLR_GREEN = new Color(64, 197, 153),
    CLR_PINK = new Color(255, 153, 254),
    CLR_PINK_2 = new Color(133, 33, 134),
    CLR_BLUE = new Color(153, 162, 255),
    CLR_BLUE_2 = new Color(112, 146, 190);
    
//    /**
//     * Headline identifiers.
//     */
//    private static final int
//    HEADLINE_1 = 0,
//    HEADLINE_2 = 1,
//    HEADLINE_3 = 2;
    
    /**
     * The sizes of the headlines.
     */
    private static final int SIZE_H1 = 8, SIZE_H2 = 6, SIZE_H3 = 4;
    
    /**
     * The pens.
     */
    public static final Pen 
    PEN_THEOREM_1 = new Pencil(Constants.PEN_ID_LINES, 2, CLR_GRAY),
    PEN_THEOREM_2 = new BallPen(Constants.PEN_ID_LINES, 2, CLR_GREEN),
    PEN_PROOF_1 = new Pencil(Constants.PEN_ID_LINES, 2, CLR_BLUE), 
    PEN_PROOF_2 = new BallPen(Constants.PEN_ID_LINES, 2, CLR_PINK_2), 
    PEN_EXMPL_1 = new Pencil(Constants.PEN_ID_LINES, 2, CLR_PINK), 
    PEN_EXMPL_2 = new BallPen(Constants.PEN_ID_LINES, 2, CLR_BLUE_2),
    PEN_CMMNT_1 = new Pencil(Constants.PEN_ID_LINES, 2, CLR_GRAY),
    PEN_CMMNT_2 = new BallPen(Constants.PEN_ID_LINES, 2, CLR_PINK_2),
    PEN_HEADLINE_1_1 = new Pencil(Constants.PEN_ID_LINES, SIZE_H1, CLR_GRAY),
    PEN_HEADLINE_1_2 = new BallPen(Constants.PEN_ID_LINES, SIZE_H1, 
            CLR_PINK_2), 
    PEN_HEADLINE_2_1 = new Pencil(Constants.PEN_ID_LINES, SIZE_H2, CLR_GRAY),
    PEN_HEADLINE_2_2 = new BallPen(Constants.PEN_ID_LINES, SIZE_H2,
            CLR_PINK_2), 
    PEN_HEADLINE_3_1 = new Pencil(Constants.PEN_ID_LINES, SIZE_H3, CLR_GRAY),
    PEN_HEADLINE_3_2 = new BallPen(Constants.PEN_ID_LINES, SIZE_H3, CLR_PINK_2);
    
    /*
     * Constructor
     */
    
    /**
     * Empty utility class constructor.
     */
    private CWrite() { }

    /*
     * ActionListener
     */
    
    /**
     * {@inheritDoc}
     */
    @Override public void actionPerformed(final ActionEvent _event) {

        if (_event.getSource().equals(Write.getInstance()
                .getTb_beispiel().getActionCause())) {
            deactivate();
            Paint.getInstance().getJbtn_color1().setBackground(
                    PEN_EXMPL_1.getClr_foreground());
            Paint.getInstance().getJbtn_color2().setBackground(
                    PEN_EXMPL_2.getClr_foreground());
            Status.setPenSelected1(Pen.clonePen(PEN_EXMPL_1));
            Status.setPenSelected2(Pen.clonePen(PEN_EXMPL_2));
            
            System.out.println(PEN_EXMPL_1.getClr_foreground());
            System.out.println(Status.getPenSelected1().getClr_foreground());

        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_bemerkung().getActionCause())) {
            deactivate();
            Paint.getInstance().getJbtn_color1().setBackground(
                    PEN_CMMNT_1.getClr_foreground());
            Paint.getInstance().getJbtn_color2().setBackground(
                    PEN_CMMNT_2.getClr_foreground());
            Status.setPenSelected1(Pen.clonePen(PEN_CMMNT_1));
            Status.setPenSelected2(Pen.clonePen(PEN_CMMNT_2));

        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_beweis().getActionCause())) {
            deactivate();
            Paint.getInstance().getJbtn_color1().setBackground(
                    PEN_PROOF_1.getClr_foreground());
            Paint.getInstance().getJbtn_color2().setBackground(
                    PEN_PROOF_2.getClr_foreground());
            Status.setPenSelected1(Pen.clonePen(PEN_PROOF_1));
            Status.setPenSelected2(Pen.clonePen(PEN_PROOF_2));
        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_headline1().getActionCause())) {
            deactivate();
            Paint.getInstance().getJbtn_color1().setBackground(
                    PEN_HEADLINE_1_1.getClr_foreground());
            Paint.getInstance().getJbtn_color2().setBackground(
                    PEN_HEADLINE_1_2.getClr_foreground());
            Status.setPenSelected1(Pen.clonePen(PEN_HEADLINE_1_1));
            Status.setPenSelected2(Pen.clonePen(PEN_HEADLINE_1_2));
            //TODO: update paint gui.
        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_headline2().getActionCause())) {
            deactivate();
            Paint.getInstance().getJbtn_color1().setBackground(
                    PEN_HEADLINE_2_1.getClr_foreground());
            Paint.getInstance().getJbtn_color2().setBackground(
                    PEN_HEADLINE_2_2.getClr_foreground());
            Status.setPenSelected1(Pen.clonePen(PEN_HEADLINE_2_1));
            Status.setPenSelected2(Pen.clonePen(PEN_HEADLINE_2_2));
            //TODO: update paint gui.
        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_headline3().getActionCause())) {
            deactivate();
            Paint.getInstance().getJbtn_color1().setBackground(
                    PEN_HEADLINE_3_1.getClr_foreground());
            Paint.getInstance().getJbtn_color2().setBackground(
                    PEN_HEADLINE_3_2.getClr_foreground());
            Status.setPenSelected1(Pen.clonePen(PEN_HEADLINE_3_1));
            Status.setPenSelected2(Pen.clonePen(PEN_HEADLINE_3_2));
            //TODO: update paint gui.
        } else if (_event.getSource().equals(Write.getInstance()
                .getTb_satz().getActionCause())) {
            deactivate();
            Paint.getInstance().getJbtn_color1().setBackground(
                    PEN_THEOREM_1.getClr_foreground());
            Paint.getInstance().getJbtn_color2().setBackground(
                    PEN_THEOREM_2.getClr_foreground());
            Status.setPenSelected1(Pen.clonePen(PEN_THEOREM_1));
            Status.setPenSelected2(Pen.clonePen(PEN_THEOREM_2));
            //TODO: update paint gui.
        }

        Paint.getInstance().getIt_stift1().setIcon(
                Status.getPenSelected1().getIconPath());
        Paint.getInstance().getIt_stift2().setIcon(
                Status.getPenSelected2().getIconPath());

        Status.setIndexOperation(Constants.CONTROL_PAINTING_INDEX_PAINT_1);
        CPaintStatus.getInstance().deactivate();
        Paint.getInstance().getIt_stift1().getTb_open().setActivated(true);
        Paint.getInstance().getTb_color1().setActivated(true);
    }
    
    
    
//    /**
//     * Insert a headline somewhere (into the selected items) after releasing
//     * selection if necessary.
//     * 
//     * @param _importance the importance.
//     */
//    private void insertHeadline(final int _importance) {
//        switch (_importance) {
//        case HEADLINE_1:
//            break;
//        case HEADLINE_2:
//            break;
//        case HEADLINE_3:
//            break;
//        default:
//            Status.getLogger().severe("Wrong kind of headline");
//            break;
//        }
//    }
    
    
    /**
     * Deactivate all graphical user interface items.
     */
    public void deactivate() {
        Write.getInstance().getTb_beispiel().setActivated(false);
        Write.getInstance().getTb_bemerkung().setActivated(false);
        Write.getInstance().getTb_beweis().setActivated(false);
        Write.getInstance().getTb_satz().setActivated(false);
        Write.getInstance().getTb_headline1().setActivated(false);
        Write.getInstance().getTb_headline2().setActivated(false);
        Write.getInstance().getTb_headline3().setActivated(false);
    }
    
    
    /**
     * If the pen in Status changed, check whether the current pen settings
     * match with the current ones. The equals method is implemented inside 
     * Pen.
     */
    public void penChanged() {
        deactivate();
        if (Status.getPenSelected1().equals(PEN_THEOREM_1)
                && Status.getPenSelected2().equals(PEN_THEOREM_2)) {
            Write.getInstance().getTb_satz().setActivated(true);
        } else if (Status.getPenSelected1().equals(PEN_PROOF_1)
                && Status.getPenSelected2().equals(PEN_PROOF_2)) {
            Write.getInstance().getTb_beweis().setActivated(true);
        } else if (Status.getPenSelected1().equals(PEN_EXMPL_1)
                && Status.getPenSelected2().equals(PEN_EXMPL_2)) {
            Write.getInstance().getTb_beispiel().setActivated(true);
        } else if (Status.getPenSelected1().equals(PEN_CMMNT_1)
                && Status.getPenSelected2().equals(PEN_CMMNT_2)) {
            Write.getInstance().getTb_bemerkung().setActivated(true);
        } else if (Status.getPenSelected1().equals(PEN_HEADLINE_1_1)
                && Status.getPenSelected2().equals(PEN_HEADLINE_1_2)) {
            Write.getInstance().getTb_headline1().setActivated(true);
        } else if (Status.getPenSelected1().equals(PEN_HEADLINE_2_1)
                && Status.getPenSelected2().equals(PEN_HEADLINE_2_2)) {
            Write.getInstance().getTb_headline2().setActivated(true);
        } else if (Status.getPenSelected1().equals(PEN_HEADLINE_3_1)
                && Status.getPenSelected2().equals(PEN_HEADLINE_3_2)) {
            Write.getInstance().getTb_headline3().setActivated(true);
        } 
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
