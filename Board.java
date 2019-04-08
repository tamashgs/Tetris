import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
//import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements KeyListener,Runnable{
	
	private Thread boardThread;
	private boolean run = true;
	
	private int blockMeret = 30;
	private int tableMagas = 20;
	private int tableSzele = 10;
	
	//private Timer timer;
	private int fps= 60;
	private int delay = 1000/fps; //60 = fps
	private int scrollSpeed = 50;
	
	private Shape shapes[] = new Shape[7];
	private Shape currentShape;
	private Shape nextShape;
	private int[][] boardMtx;
	private BufferedImage[][] colorMtx;
	private BufferedImage img;
	private BufferedImage back = null;

	int points = 0;
	
	public Board() {
		
		try {
			img = ImageIO.read(new File("block.png"));
			back = ImageIO.read(new File("background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		createShapes();
		
		boardMtx = new int [10][20];
		colorMtx = new BufferedImage [10][20];
		
		for(int i = 0; i < 10; i++)
			for(int j = 0 ; j < 20; j++) {
				boardMtx[i][j] = 0;
				colorMtx[i][j] = null;
			}
		
		boardThread = new Thread(this);
		boardThread.start(); //void run() meghivasa
		
		/*
		timer = new Timer(delay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		timer.start();
		*/
	}
	
	//Létrehozza az alakokat
	public void createShapes() {

		shapes[0] = new Shape(img.getSubimage(0, 0, 30, 30),new int[][] {{1, 1, 1, 1}});
		shapes[1] = new Shape(img.getSubimage(30, 0, 30, 30),new int[][] {{1, 1, 0}, {0, 1, 1}});
		shapes[2] = new Shape(img.getSubimage(60, 0, 30, 30),new int[][] {{0, 1, 1}, {1, 1, 0}});
		shapes[3] = new Shape(img.getSubimage(90, 0, 30, 30),new int[][] {{1, 1, 1}, {0, 0, 1}});
		shapes[4] = new Shape(img.getSubimage(120, 0, 30, 30),new int[][] {{1, 1, 1}, {1, 0, 0}});
		shapes[5] = new Shape(img.getSubimage(150, 0, 30, 30),new int[][] {{1, 1, 1}, {0, 1, 0}});
		shapes[6] = new Shape(img.getSubimage(180, 0, 30, 30),new int[][] {{1, 1}, {1, 1}});
	}
	
	//Teriti az aktualis alakot
	public Shape getCurrentShape(){
		return currentShape;
	}
	
	//Teriti a kovetkezo alakot
		public Shape getNextShape(){
			return nextShape;
		}
	
	//Beallitja az alakot
	public void setShape(){
		Random rand = new Random();
		currentShape = new Shape(shapes[rand.nextInt(7)]);
		nextShape = new Shape(shapes[rand.nextInt(7)]);
		
	}
	
	//General egy kovetkezo alakot, es aktualizalja a regit
	public void nextShape(){
		Random rand = new Random();
		currentShape = nextShape;
		nextShape = new Shape(shapes[rand.nextInt(7)]);
	}
	
	//Jatek futtatása
	public void run () {
		
		//long lastTime = System.currentTimeMillis();
		int time = 0;
		setShape();
		
		while (run) {
			
			try { Thread.sleep(delay); } 
			catch (InterruptedException e) { e.printStackTrace(); }
			
			repaint();
			
			if (time > scrollSpeed) {
				
				if ( (currentShape.getShapeY() + currentShape.getLength() == 20) || checkPlace() ){
					setBoard();
					nextShape();
					checkFullLine();
				}else{
					currentShape.setShapeY(1);
				}
				
				time = 0;
				
			}else {
				time ++;
			}
		}
	}
	
	//Megnézi, hogy az alak koordinatái és a tablában levo alakok erintkeznek e
	//Ha legfelul erintkeznek leallitja a jatekot
	public boolean checkPlace() {
		
		for(int i = 0; i < currentShape.getWeight(); i++)
			for(int j = 0; j < currentShape.getLength(); j++)
				if ( currentShape.getPositions()[i][j] == 1 )
					if( boardMtx[currentShape.getShapeX() + i][currentShape.getShapeY() + j + 1] == 1 ) {
						if (currentShape.getShapeY() == 0) {
							run = false;
						}
						return true;
					}
		return false;
	}
	
	//Lállítja a játékot
	public void stop() {
		run = false;
	}
	
	public boolean getEnd(){
		return run;
	}
	
	//Kirajzolja a lerakott kockákat a boardMtx-nek megfeleloen, majd az aktualos alakot is
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		g.drawImage(back,0,0,null);
		
		for(int i = 0; i < tableMagas; i++ ) {
			g.drawLine(0, i*blockMeret, tableSzele*blockMeret, i*blockMeret);
			g.drawLine(i*blockMeret, 0, i*blockMeret, tableMagas*blockMeret);
			//DESIGN: g.drawLine(i*blockMeret, 0, tableSzele*blockMeret, i*blockMeret);
		}
		
		for(int i = 0; i < 10; i++)
			for(int j = 0 ; j < 20; j++)
				if (boardMtx[i][j] == 1 )
					g.drawImage(colorMtx[i][j],i*blockMeret,j*blockMeret,null);

		currentShape.Paint(g);
		
	}

	//Elmenti az aktualis alakot a tablaban
	public void setBoard() {
		
		int a[][] = currentShape.getPositions();
		
		for(int i = 0; i < a.length; i++)
			for(int j = 0 ; j < a[i].length; j++)
				if (a[i][j] == 1) {
					colorMtx[i+currentShape.getShapeX()][j+currentShape.getShapeY()] = currentShape.getImage();
					boardMtx[i+currentShape.getShapeX()][j+currentShape.getShapeY()] = 1;
				}
	}
	
	//Megnezi, hogy tele van-e egy sor, ha igen kitorli es noveli a pontszamot
	public void checkFullLine() {
		
		int db;
		for(int j = 0; j < 20; j++) {
			db = 0;
			for(int i = 0 ; i < 10; i++)
				if ( boardMtx[i][j] == 1 ) db++;
			if (db == 10 ) {
				
				for(int k = j; k > 1; k--)
					for(int i = 0 ; i < 10; i++)
						boardMtx[i][k] = boardMtx[i][k-1];
				
				points = points + 10;
				//System.out.println("Score:" + points);
			}
		}
		
	}
	
	public void update(){
		
	}
	
	public int[][] getBoard(){
		return boardMtx;
	}
	
	public int getScore() {
		return points;
	}
	
	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
			getCurrentShape().setShapeX(-1);
		}
		if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
			getCurrentShape().setShapeX(1);
		}
		if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
			scrollSpeed = 0;
		}
		if (ke.getKeyCode() == KeyEvent.VK_UP) {
			getCurrentShape().rotate();
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
			scrollSpeed = 50;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
