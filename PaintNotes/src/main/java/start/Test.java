package start;

import java.awt.Color;

import model.objects.painting.Picture;
import model.objects.pen.normal.BallPen;
import model.settings.Constants;
import model.settings.Settings;
import model.util.DPoint;
import view.forms.Page;

/**
 * Test class.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class Test {

    
    /**
     * load and transform image.
     * @param _control
     */
    public static final void testLoadSomething() {

        loadPicture();
        Picture.getInstance().transformWhiteToAlpha();
        
        Picture.getInstance().saveIMAGE(
                Settings.getWsLocation() + "/result.png");
    }
    
    
    /**
     * Just for testing purpose.
     * @param _args param.
     */
    public static void main(final String[] _args) {

        loadPicture();
  /*      Picture.getInstance().load(
                "C:\\Users\\juliu_000\\Desktop\\in.png");
    
        Picture.getInstance().whiteToAlpha();
        
        Picture.getInstance().savePNG(
                "C:\\Users\\juliu_000\\Desktop\\out.png");   */
    }

    
    /**
     * .
     */
    public static final void loadPicture() {
    
        DPoint newSize = Picture.getInstance().load(
                "/home/juli/Arbeitsfläche/input.jpg");
    
        //change size
        Page.getInstance().setSize((int) newSize.getX(), (int) newSize.getY());
        Picture.getInstance().transformWhiteToAlpha();
        
        Picture.getInstance().saveIMAGE("/home/juli/Arbeitsfläche/output.png");
        
    }

    
    /**
     * Test for mathematics.
     */
    public static final void testPaintMathematics() {
        new Thread() {

            public void run() {

                final int time = 50;
                BallPen pm = new BallPen(
                        Constants.PEN_ID_MATHS, 1, Color.green);
                Picture.getInstance().changePen(pm);
                
                
                Picture.getInstance().addPaintObjectWrinting();

                final int fifety = 50;
                
                try {
                    
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Picture.getInstance().changePaintObject(
                        new DPoint(fifety * 2, fifety * 2));

                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Picture.getInstance().changePaintObject(
                        new DPoint(fifety * (2 + 1), fifety * (2 + 2)));
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Picture.getInstance().changePaintObject(
                        new DPoint(fifety * (2 + 2), fifety * 2));
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Picture.getInstance().changePaintObject(
                        new DPoint(fifety * (2 + 1), 0));
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Picture.getInstance().changePaintObject(
                        new DPoint(fifety * (2), fifety * (2)));
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Picture.getInstance().finish();
                
                BallPen pp = new BallPen(
                        Constants.PEN_ID_POINT, 2, Color.black);
                pp.setClr_foreground(Color.black);
                pp.setThickness(2 + 2);
                Picture.getInstance().changePen(pp);

                Picture.getInstance().addPaintObjectWrinting();
                Picture.getInstance().changePaintObject(
                        new DPoint(2 + 2 + 1, fifety * 2));
                Picture.getInstance().changePaintObject(new DPoint(
                        fifety * 2, 2 + 2 + 2));
                Picture.getInstance().changePaintObject(new DPoint(
                        fifety * (2 + 1), fifety * 2 + (2 + 2 + 1) * (2 + 2)));
                Picture.getInstance().changePaintObject(new DPoint(
                        fifety * (2 + 2 + 1), fifety + (2 + 2 + 1) * 2));
                Picture.getInstance().finish();
                
                pm = new BallPen(Constants.PEN_ID_MATHS, 1, Color.black);
                pm.setClr_foreground(Color.orange);
                pm.setThickness(2);
                Picture.getInstance().changePen(pm);
            }
        } .start();
    }
    

    /**
     * test all eventualities.
     * @param _p1 point of triangle
     * @param _p2 point of triangle
     * @param _p3 point of triangle
     * @param _pic the picture.
     */
    public final void testAlle(final DPoint _p1, 
            final DPoint _p2, final DPoint _p3, final Picture _pic) {

        test(_p1, _p2, _p3, _pic);
        test(_p1, _p3, _p2, _pic);

        test(_p2, _p1, _p3, _pic);
        test(_p2, _p3, _p1, _pic);

        test(_p3, _p2, _p1, _pic);
        test(_p3, _p1, _p2, _pic);
        
    }
    
    
    /**
     * test a single rectangle.
     * @param _p1 point of triangle
     * @param _p2 point of triangle
     * @param _p3 point of triangle
     * @param _pic the picture
     */
    private void test(final DPoint _p1, final DPoint _p2, final DPoint _p3, 
            final Picture _pic) {

        final int time = 5000;
        
        _pic.addPaintObjectWrinting();
        _pic.changePaintObject(_p1);
        _pic.changePaintObject(_p2);
        _pic.changePaintObject(_p3);
        _pic.finish();

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * test a picture.
     * @param _pic the picture.
     */
    public final void test(final Picture _pic) {

        final int fifety = 50;
        final int twenty = 20;
        
        _pic.addPaintObjectWrinting();
        _pic.changePaintObject(new DPoint(fifety, fifety * 2 + twenty));
        _pic.changePaintObject(new DPoint(fifety * 2, fifety * (2 + 2)));
        _pic.changePaintObject(new DPoint(
                fifety * (2 + 2), fifety * (2 + 2 + 2)));
        _pic.changePaintObject(new DPoint(
                fifety * (2 + 1), fifety * (twenty / 2)));
        _pic.changePaintObject(new DPoint(fifety * (2 + 2 + 2), 
                fifety * (2 + 2 + 2 + 2) + twenty + 2 + 2 + 1));
        _pic.finish();

        final int time = 5000;
        
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
