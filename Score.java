import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Score extends JPanel implements Runnable{
	
	int score;
	private Thread thread;
	private JLabel label;
	private Board board;
	
	Score(Board b){
		
		board = b;
		score = 0;
		label = new JLabel("0");
		this.add(label);
		repaint();
		Font f = new Font(null, 30, 30);
		label.setFont(f);
		label.setBackground(new Color(0,0,0,0));
		thread = new Thread(this);
		thread.start();
	}
	
	public void addScore(){
		score += 10;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
	}

	@Override
	public void run() {
		
		while (board.getEnd()) {
			String s = "" + board.getScore();
			label.setText(s);
		}
		label.setText("Game Over");
	}
	
}
