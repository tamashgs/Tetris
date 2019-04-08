import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main implements ActionListener{
	
	private JFrame window;
	private Board board; //JPanel
	private JButton startbutton;
	private JButton restartbutton;
	private JButton exitbutton;
	private Score score;
	private ImageIcon img;
	private JLabel background;
	
	public Main() {
		
		window = new JFrame("Tetris 1.2");
		window.setLayout(null);
		
		startbutton = new JButton("Start");
		startbutton.addActionListener(this);
		startbutton.setBounds(225, 150, 100, 50);
		
		restartbutton = new JButton("Restart");
		restartbutton.addActionListener(this);
		restartbutton.setBounds(380, 150, 100, 50);
		
		exitbutton = new JButton("Exit");
		exitbutton.addActionListener(this);
		exitbutton.setBounds(380, 250, 100, 50);
		
		window.add(startbutton);

		img = new ImageIcon("background.jpg");
		background = new JLabel("",img,JLabel.CENTER);
		background.setBounds(0,0, 550, 630);

		window.add(background);
		
		window.setResizable(false);
		window.setVisible(true);
		window.setBounds(700, 200, 550, 630);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	public static void main(String s[]) {
		new Main();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource() == startbutton) {
			
			createNewBoard();
		}
		if (ae.getSource() == restartbutton) {
			
			board.stop();
			window.remove(board);
			window.remove(score);
			createNewBoard();
		}
		if (ae.getSource() == exitbutton) {
			
			board.stop();
			backToMenu();
		}
	}
	
	public void createNewBoard() {
		
		board = new Board();
		board.setBounds(0, 0, 310, 630);
		
		window.remove(startbutton); //window.getContentPane().remove(startbutton);
		window.add(board);
		window.addKeyListener(board);
		window.add(restartbutton);
		window.add(exitbutton);
		
		board.setOpaque(true);
		
		score = new Score(board);
		score.setBounds(325, 50, 200, 50);
		window.add(score);
		
		window.add(background);
		
		window.revalidate();
		window.requestFocusInWindow();
		window.repaint();
	}
	
	public void backToMenu() {
		
		window.add(startbutton);
		window.remove(board);
		window.remove(restartbutton);
		window.remove(exitbutton);
		window.remove(score);
		
		window.revalidate();
		window.requestFocusInWindow();
		window.repaint();
	}
}
