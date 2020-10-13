package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class Snake extends JFrame{
	int x,y,x1,y1 ;
	Direction N=Direction.left;
	boolean game=false;
	boolean nFood=true;
	boolean incL=false;
	final int size=450;
	final int sz=15;
	final int max=size/sz;
	final int bound=max-1;
	final int intialPos[][]=new int[][]{{15,16,17,18},{15,15,15,15}};
	int length=4;
	int position[][]=new int[max][2];
	Random rand = new Random();
	public Board board[][]=new Board[max][max];
	public static void main(String [] args){
		new Snake();
	}

	public Snake(){
		this.setSize(size+6,size+25);
		this.setResizable(false);
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e){
				int key = e.getKeyCode();
				if(game){
					if (key == KeyEvent.VK_LEFT&&N!=Direction.right) {
						N=Direction.left;

					}

					else if (key == KeyEvent.VK_RIGHT&&N!=Direction.left) {
						N=Direction.right;
					}
					else if (key == KeyEvent.VK_UP&&N!=Direction.down){
						N=Direction.up;
					}
					else if (key == KeyEvent.VK_DOWN&&N!=Direction.up){
						N=Direction.down;
					}
				}
				else{
					if(key==KeyEvent.VK_ENTER){
						game=true;
						length=4;
						reset();
					}
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		this.add(new Drawingboard());
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
		executor.scheduleAtFixedRate(new Run(this), 0L, 100L, TimeUnit.MILLISECONDS);
		this.setLocationRelativeTo (null);
		this.setVisible(true);
	}

	void reset(){
		length=4;
		nFood=true;
		N=Direction.left;
		for(int i=0;i<max;i++) {
			for(int j=0;j<max;j++) {
				position[i][0]=-1;
				position[i][1]=-1;
			}
		}
		setupBoard();
	}
	void setupBoard(){
		for(int i=0;i<max;i++) {
			for(int j=0;j<max;j++) {
				board[i][j]=Board.nothing;
			}
		}
		for(int i=0;i<max;i++) {
			board[i][0]=Board.wall;
			board[i][bound]=Board.wall;
			board[0][i]=Board.wall;
			board[bound][i]=Board.wall;
		}
		board[intialPos[0][0]][intialPos[1][0]]=Board.head;
		position[0][0]=intialPos[0][0];
		position[0][1]=intialPos[1][0];
		for(int i=1;i<4;i++){
			board[intialPos[0][i]][intialPos[1][i]]=Board.snake;
			position[i][0]=intialPos[0][i];
			position[i][1]=intialPos[1][i];
		}
	}


	class Run implements Runnable{
		Snake m;
		public Run(Snake n){
			m=n;
		}
		@Override
		public void run() {
			if(game) {
				changePos();
			}
			if(game)m.repaint();

		}
		void changePos(){
			findHead();
			movedirection();
			createFood();
			board[x][y]=Board.head;
			board[x1][y1]=Board.nothing;
			position[0][0]=x;
			position[0][1]=y;
			for(int i=1;i<length;i++){
				x=x1;
				y=y1;
				x1=position[i][0];
				y1=position[i][1];
				board[x][y]=Board.snake;
				board[x1][y1]=Board.nothing;
				position[i][0]=x;
				position[i][1]=y;


			}
			if(incL){
				position[length][0]=x1;
				position[length][1]=y1;
				board[x1][y1]=Board.snake;
				length=length+1;
				incL=false;

			}
		}
		void findHead(){
			for(int i=0;i<max;i++) {
				for(int j=0;j<max;j++) {
					if(board[i][j]==Board.head){
						x=i;
						y=j;
					}
				}
			}
		}
		void movedirection(){
			x1=x;
			y1=y;
			if(N==Direction.down)y=y+1;
			else if(N==Direction.up)y=y-1;
			else if(N==Direction.left)x=x-1;
			else if(N==Direction.right)x=x+1;
			if(board[x][y]==Board.snake)game=false;
			if(x<1||x>bound-1||y<1||y>bound-1)game=false;
			if(board[x][y]==Board.food){
				nFood=true;
				incL=true;

			}

		}
		void createFood(){
			while(nFood){
				int i=rand.nextInt(bound-1)+1;
				int j=rand.nextInt(bound-1)+1;
				if(board[i][j]==Board.nothing){
					board[i][j]=Board.food;
					nFood=false;
				}
			}
		}

	}



	class Drawingboard extends JComponent{
		public Drawingboard() {
			repaint();
		}
		public void paint(Graphics g){
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, size, size);
			for(int i=0;i<max;i++) {
				for(int j=0;j<max;j++) {
					if(board[i][j]==Board.wall)drawWall(i*sz,j*sz,g);
					if(board[i][j]==Board.snake||board[i][j]==Board.head)drawSnake(i*sz,j*sz,g);
					if(board[i][j]==Board.food)drawFood(i*sz,j*sz,g);
				}
			}
		}


		void drawFood(int x, int y, Graphics g) {
			g.setColor(Color.red);
			g.fillRect(x, y, sz, sz);

		}
		void drawWall(int x,int y,Graphics g) {
			g.setColor(Color.black);
			g.fillRect(x, y, sz, sz);
		}
		void drawSnake(int x,int y,Graphics g){
			g.setColor(Color.GREEN);
			g.fillRect(x, y, sz, sz);
		}


	}
}