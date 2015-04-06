package control.forms;

import javax.swing.JFrame;

import view.forms.Loading;

public class CLoading {

	
	private Thread t_load;
	private Loading load;
	
	public CLoading(Loading _load) {
		this.load = _load;
		load.setVisible(false);
	}
	
	public void load(final JFrame _jf) {
		if (t_load != null) {
			t_load.interrupt();
			
		}
		t_load = new Thread(){
			public void run(){
				System.out.println("hier");
				int c = 0;
				load.setVisible(true);
				_jf.setEnabled(false);
				
				try{
					while(!isInterrupted()) {
						Thread.sleep(100);
						load.nextIcon(c);
						c++;
						load.repaint();
							
					}
				}catch(Exception e) {
					System.out.println("interrupt");
					e.printStackTrace();
					interrupt();
				}
				load.setVisible(false);
				_jf.setEnabled(true);
				
			}
		};
		t_load.start();
	}
}
