package src;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


public class TilingFrame extends JFrame {
	private static final long serialVersionUID = -360885512080963508L;
	private FieldCanvas fieldCanvas;

	public TilingFrame(Field field, int scale){
		fieldCanvas = new FieldCanvas(field, scale);
		setTitle("Heuristics 2016 - Tiling");
		setLayout(new BorderLayout());
		add(this.fieldCanvas, BorderLayout.CENTER);
		
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
		
		this.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					System.exit(0);
				}
			}
			
		});
		
		pack();
		
		setVisible(true);
	}
	
	public void redraw(int delay){
		repaint();
		invalidate();
		validate();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}