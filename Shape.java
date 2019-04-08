import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Shape {
	
	private BufferedImage kocka;
	private int[][] helyzet;
	private int meret = 30; // board.getBlockSize()
	private int x;
	private int y;
	
	public Shape(BufferedImage kocka, int[][] helyzet) {
		
		this.kocka = kocka;
		this.helyzet = helyzet;
		x = 4;
		y = 0;
	}
	
	public Shape(Shape s) {
		
		this.kocka = s.kocka;
		int newlength = s.helyzet.length;
		this.helyzet = new int[newlength][newlength];
		System.arraycopy(s.helyzet, 0, this.helyzet, 0, newlength);
		this.x = 4;
		this.y = 0;
	}
	
	public void Paint(Graphics g) {
		
		for(int i = 0; i < helyzet.length; i++)
			for(int j = 0 ; j < helyzet[i].length; j++)
				if(helyzet[i][j] != 0)
					g.drawImage(kocka,i*meret + x*meret,j*meret + y*meret,null);
	}
	
	public void setShapeX(int xx){
		
		if((x + xx + helyzet.length <= 10) && (x + xx >= 0)){
			x += xx;
		}
	}
	
	public void setShapeY(int yy){
		
		if((y + yy + helyzet[0].length - 1 < 20)){
			y += yy;
		}
	}
	
	public int getShapeY(){
		return y;
	}
	
	public int getShapeX(){
		return x;
	}
	
	public int getLength(){
		return helyzet[0].length;
	}
	
	public int getWeight(){
		return helyzet.length;
	}

	public int[][] getPositions() {
		return helyzet;
	}
	
	public BufferedImage getImage() {
		return kocka;
	}
	
	//Mátrix forgatása
	public void rotate(){
		
		//if(collision) return;
		
		int[][] rotatedMatrix = null;
		
		rotatedMatrix = getTranspose(helyzet);
		
		rotatedMatrix = getReverseMatrix(rotatedMatrix);
		
		if(x + rotatedMatrix[0].length > 10 || y + rotatedMatrix.length > 20)
			return;
		
		/*
		for(int row = 0; row < rotatedMatrix.length; row++)
		{
			for(int col = 0; col < rotatedMatrix[0].length; col++){
				
				if(board.getBoard()[y + row][x + col] != 0){
					return;
				}
				
			}
		}
		*/
		helyzet = rotatedMatrix;
		
	}
	
	//Mátrix transzponáltja
	private int[][] getTranspose(int[][] matrix){
		
		int[][] newMatrix = new int[matrix[0].length][matrix.length];
		
		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix[0].length; j++)
				newMatrix[j][i] = matrix[i][j];
		
		return newMatrix;
	}
	
	//Mátrix fordítottja
	private int[][] getReverseMatrix(int[][] matrix){
		
		int middle = matrix.length / 2;
		
		for(int i = 0; i < middle; i++){
			int[] m = matrix[i];
			matrix[i] = matrix[matrix.length - i - 1];
			matrix[matrix.length - i - 1] = m;
		}
		return matrix;
	}
}
