package model.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public abstract class DragAndDrop implements DropTargetListener {
    
	public final void defaultDropHandler(final java.util.List<File> _files) {


        for (int i = 0; i < _files.size(); i++) {
            System.out.println("FilePath" + _files.get(i).getPath() + "'.");
        }
	}
	
	public abstract void dropHandler(final java.util.List<File> _files);

    @Override
    public void drop(DropTargetDropEvent event) {

        // Accept copy drops
        event.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = event.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        for (DataFlavor flavor : flavors) {

            try {

                // If the drop items are files
                if (flavor.isFlavorJavaFileListType()) {

                    // Get all of the dropped files
                	@SuppressWarnings("unchecked")
					java.util.List<File> files = (java.util.List<File>) transferable.getTransferData(flavor);

                	dropHandler(files);
                	

                }

            } catch (Exception e) {

                // Print out the error stack
                e.printStackTrace();

            }
        }

        // Inform that the drop is complete
        event.dropComplete(true);

    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {
    }

    @Override
    public void dragExit(DropTargetEvent event) {
    }

    @Override
    public void dragOver(DropTargetDragEvent event) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent event) {
    }
    
    
    
    
    
    

    public static void main(final String[] _args) {
    	JFrame jf = new JFrame();
    	jf.setVisible(true);;
    	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	jf.setSize(500, 300);
    	jf.setLocationRelativeTo(null);
    	jf.setLayout(null);

        // Create the label
        final JTextArea jta_target = new JTextArea("2drag.");
        jta_target.setSize(500, 300);
        // Create the drag and drop listener
        DragAndDrop myDragDropListener = new DragAndDrop() {

			@Override
			public void dropHandler(java.util.List<File> _files) {
				jta_target.append("\n" + _files.get(0));
			}};

        // Connect the label with a drag and drop listener
        new DropTarget(jta_target, myDragDropListener);

        // Add the label to the content
        jf.add(jta_target);


    	jf.setVisible(true);
    }

}
