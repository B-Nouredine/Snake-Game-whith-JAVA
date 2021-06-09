package tp7_SnakeGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Timer;
import javax.swing.border.Border;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Snake extends JFrame
{
	int nbCasesX=40;
	int nbCasesY=20;
	int largeur=30;
	int marge=30;
	int direction=1; //1=right 2=left 3=up 4=down
	ArrayList<Cellule> serpent=null;
	Cellule food;
	ArrayList<Cellule> obstacle = new ArrayList<Snake.Cellule>();
	int freq=100;
	int score=0;
	int stage=1;
	Timer horloge;
	
	//principal panel
	JPanel p = new JPanel() {
		@Override
		public void paint(Graphics g) {
			// TODO Auto-generated method stub
			super.paint(g);
			
			//paint snake, background, food & obstacle
			dessinerBackground(g);
			dessinerSerpent(g);
			dessinerFood(g);
			dessinerObstacle(g);
			
			g.setFont(new Font("Times New Roman", 1, 20));
			g.setColor(new Color(80,20,100));
			g.drawString("SCORE: "+score, nbCasesX*largeur/4, 20);
			g.drawString("STAGE: "+stage, nbCasesX*largeur/3+150 + 200, 20);
		
		}
	};
	
	//panel "game over" and "replay"
	JPanel stop = new JPanel() {@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		setBackground(Color.black);
		JTextField text = new JTextField();
		text.setSize(400, 200);
		text.setLocation(getContentPane().getWidth()/2-text.getWidth()/2, getContentPane().getHeight()/2-text.getHeight());
		text.setText("GAME OVER! \nYour Score is: " + score);
		text.setBackground(Color.red);
		text.setFont(new Font("Arial", 1, 20));
		JButton replay = new JButton("REPLAY");
		replay.setSize(400, 200);
		replay.setLocation(text.getX(), text.getY()+200);
		replay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
				new Snake();
			}
		});
		
		this.add(text);
		this.add(replay);
	}
	};
	
	//methodes
			void dessinerBackground(Graphics g)
			{
				for(int i=0;i<nbCasesX;i++)
				{
					for(int j=0;j<nbCasesY;j++)
					{
						dessinerCelluleSquare(i,j,Color.black, g);
					}
				}
			}
			void initialiserSerpent()
			{
				serpent=new ArrayList<Cellule>();
				serpent.add(new Cellule(6,nbCasesY/2,Color.white));
				serpent.add(new Cellule(2,nbCasesY/2,Color.red));
				serpent.add(new Cellule(3,nbCasesY/2,Color.red));
				serpent.add(new Cellule(4,nbCasesY/2,Color.red));
				serpent.add(new Cellule(5,nbCasesY/2,Color.red));
			}
			void dessinerSerpent(Graphics g)
			{
				for(Cellule c: serpent)
				{
					dessinerCelluleCircle(c, g);
				}
			}
			
			void initialiserFood()
			{
				boolean badPosition=false;
				Random rand = new Random();
				
				do {
				food = new Cellule(rand.nextInt(nbCasesX-1), rand.nextInt(nbCasesY-1), Color.blue);
				
				for(int i=0;i<serpent.size();i++)
				{
					if(food.x==serpent.get(i).x && food.y==serpent.get(i).y) badPosition=true;
				}
				for(Cellule c: obstacle)
				{
					if(food.x==c.x && food.y==c.y) badPosition=true;
				}
				}while(badPosition==true);
			}
			void dessinerFood(Graphics g)
			{
				dessinerCelluleCircle(food, g);
			}
			
			
			void dessinerObstacle(Graphics g)
			{
				for(Cellule c: obstacle)
				{
					dessinerCelluleSquare(c,g);
				}
			}
			void dessinerCelluleSquare(int x, int y, Color c, Graphics g)
			{
				g.setColor(c);
				g.fillRect(marge+x*largeur, marge+y*largeur, largeur, largeur);
			}
			
			void dessinerCelluleSquare(Cellule c, Graphics g)
			{
				dessinerCelluleSquare(c.x, c.y, c.couleur, g);
			}
			
			void dessinerCelluleCircle(int x, int y, Color c, Graphics g)
			{
				g.setColor(c);
				g.fillOval(marge+x*largeur, marge+y*largeur, largeur, largeur);
			}
			
			void dessinerCelluleCircle(Cellule c, Graphics g)
			{
				dessinerCelluleCircle(c.x, c.y, c.couleur, g);
			}
			
			//methode des stages
			void setStage(int s)
			{
					//vider obstacle
					for(int i=0;i<obstacle.size();i++)
					{
						obstacle.remove(i);
					}
					
					//initialiser le serpent
					//initialiserSerpent();
			
				if(s==2)
				{
					stage=2;
					freq=100;
					for(int i=0; i<nbCasesX; i++)
					{
						obstacle.add(new Cellule(i, 0, Color.yellow));
						obstacle.add(new Cellule(i, nbCasesY-1, Color.yellow));
					}
					for(int i=0; i<nbCasesY; i++)
					{
						obstacle.add(new Cellule(0, i, Color.yellow));
						obstacle.add(new Cellule(nbCasesX-1, i, Color.yellow));
					}
				}
			
				if(s==3)
				{
					stage=3;
					freq=90;
					for(int y=4; y<16;y++)
					{
						obstacle.add(new Cellule(10,y,Color.green));
						obstacle.add(new Cellule(30,y,Color.green));
					}
				}
				
				if(s==4)
				{
					stage=4;
					freq=80;
					for(int i=9;i<15;i++)
					{
						obstacle.add(new Cellule(i,4,Color.pink));
						obstacle.add(new Cellule(nbCasesX-i,4,Color.pink));
						obstacle.add(new Cellule(i,nbCasesY-4,Color.pink));
						obstacle.add(new Cellule(nbCasesX-i,nbCasesY-4,Color.pink));
					}
					for(int i=5;i<8;i++)
					{
						obstacle.add(new Cellule(9,i,Color.pink));
						obstacle.add(new Cellule(9,nbCasesY-i,Color.pink));
						obstacle.add(new Cellule(nbCasesX-9,i,Color.pink));
						obstacle.add(new Cellule(nbCasesX-9,nbCasesY-i,Color.pink));
					}
				}
			}
//	//constructeur avec parametres
//	public Snake(int a)
//	{
//		this.setTitle("SnakeGame-START");
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		this.setLocationRelativeTo(null);
//		this.setSize(400, 430);
//		this.setResizable(false);
//		this.setLocation(500, 200);
//		
//		JPanel start = new JPanel()
//		{
//			@Override
//		public void paint(Graphics arg0) {
//			// TODO Auto-generated method stub
//			super.paint(arg0);
//			this.setBackground(Color.black);
//			JButton startBtn = new JButton("START");
//			startBtn.setBackground(Color.green);
//			startBtn.setSize(400, 200);
//			startBtn.setLocation(0, 0);
//			
//			startBtn.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent arg0) {
//					// TODO Auto-generated method stub
//					new Snake();
//					dispose();
//				}
//			});
//			
//			JButton exitBtn = new JButton("EXIT");
//			exitBtn.setBackground(Color.red);
//			exitBtn.setSize(400, 200);
//			exitBtn.setLocation(0, startBtn.getHeight());
//			
//			exitBtn.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent arg0) {
//					// TODO Auto-generated method stub
//					dispose();
//				}
//			});
//			this.add(startBtn);
//			this.add(exitBtn);
//		}};
//		this.setContentPane(start);
//		this.setVisible(true);
//	}
	
	//constructeur sans parametres
	public Snake()
	{
		this.setTitle("SnakeGame 2021-05-19");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		this.setLocationRelativeTo(null);
		this.setSize(largeur*nbCasesX+marge*2, largeur*nbCasesY+marge*2+30);
		this.initialiserSerpent();
		this.initialiserFood();
		horloge = new Timer(freq, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int [] prevX = new int[serpent.size()]; int [] prevY = new int[serpent.size()];
				for(int i=0;i<serpent.size();i++)
				{
					prevX[i]=serpent.get(i).x;
					prevY[i]=serpent.get(i).y;
				}
				if(direction==1) serpent.get(0).x++;
				if(direction==2) serpent.get(0).x--;
				if(direction==3) serpent.get(0).y--;
				if(direction==4) serpent.get(0).y++;
				
				//when snake overtakes borders
				if(serpent.get(0).x==nbCasesX) serpent.get(0).x=0;
				if(serpent.get(0).x==-1) serpent.get(0).x=nbCasesX-1;
				if(serpent.get(0).y==nbCasesY) serpent.get(0).y=0;
				if(serpent.get(0).y==-1) serpent.get(0).y=nbCasesY-1;
				
				//tail follows head
				for(int i=1;i<serpent.size();i++)
				{
					serpent.get(i).x=prevX[i-1];
					serpent.get(i).y=prevY[i-1];
				}
				
				//snake eats food
				if(serpent.get(0).x==food.x && serpent.get(0).y==food.y)
				{
					score+=10;
					Random rand = new Random();
					initialiserFood();
					serpent.add(new Cellule(serpent.get(serpent.size()-1).x, serpent.get(serpent.size()-1).y, Color.red));
				}
				
				//determine stages
				if(score>90 && score<=140) setStage(2);
				else if(score>140 && score<=190) setStage(3);
				else if(score>190) setStage(4);

				//snake touches itself
				for(int i=1;i<serpent.size();i++)
				{
				if(serpent.get(0).x==serpent.get(i).x && serpent.get(0).y==serpent.get(i).y)
				{
					setContentPane(stop);
					validate();
					horloge.stop();
				}
				}
				//snake touches obstacle
				for(Cellule c: obstacle)
				{
					if(serpent.get(0).x==c.x && serpent.get(0).y==c.y)
					{
						setContentPane(stop);
						validate();
						horloge.stop();
					}
				}

				repaint();
			}
		});
		horloge.start();
		
		this.addWindowListener(new WindowAdapter() {
			//what happens when we close the window
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
				horloge.stop();
				if(JOptionPane.showConfirmDialog(null, "DO you want to save?")==0)
				{
					//creer le fichier
					String fichier = new String("/home/nouredineb/Desktop/yes.txt");
					FileWriter f=null;
					try {
						f = new FileWriter(fichier);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					PrintWriter stylo = new PrintWriter(f);
					
					for(Cellule c: serpent)
					{
						stylo.print(c.x); stylo.print("\n"); stylo.print(c.y); stylo.print("\n");
					}
					stylo.print(direction);
					stylo.close();
				}
				else if(JOptionPane.showConfirmDialog(null, "DO you want to save?")==1)
				{
					//vider le fichier pour recommencer le jeux des le debut
					String fichier = new String("/home/nouredineb/Desktop/yes.txt");
					FileWriter f=null;
					try {
						f = new FileWriter(fichier);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					PrintWriter stylo = new PrintWriter(f);
					stylo.print("");
					stylo.close();
				}
			}
			
			//what happens when we open the window
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowOpened(e);
				String fichier = new String("/home/nouredineb/Desktop/yes.txt");
				FileReader f=null;
				try {
					f = new FileReader(fichier);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Scanner buff = new Scanner(f);
				String x = null,y = null;
				serpent = new ArrayList<Cellule>();
				
				//on teste si le fichier est vide ou non
				if(buff.hasNextLine())
				{
					if(buff.hasNextLine())
					{
						x=buff.nextLine();
						y=buff.nextLine();
						serpent.add(new Cellule(Integer.parseInt(x),Integer.parseInt(y),Color.white));
						while(buff.hasNextLine())
						{
							x=buff.nextLine();
						if(buff.hasNextLine())
						{
							y=buff.nextLine();
						}else {direction=Integer.parseInt(x); break; } //la derniere valeur est reservee pour la direction
						//c'est la valeur qui n'a pas de ligne suivante, on break pour ne pas executer la ligne suivante
						//avec x=direction et le y precedent!
						serpent.add(new Cellule(Integer.parseInt(x),Integer.parseInt(y),Color.red));
						}
						buff.close();
					}
				}
				else
				{
					initialiserSerpent();
				}
				
			}
		});
		
		//arrow keys directing
		this.addKeyListener(new KeyAdapter() {@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			super.keyPressed(e);
			if(e.getKeyCode()==KeyEvent.VK_RIGHT && direction!=2) {direction=1;}
			if(e.getKeyCode()==KeyEvent.VK_LEFT && direction!=1) {direction=2;}
			if(e.getKeyCode()==KeyEvent.VK_UP && direction!=4) {direction=3;}
			if(e.getKeyCode()==KeyEvent.VK_DOWN && direction!=3) {direction=4;}
		}});
		
		this.setContentPane(p);
		this.setVisible(true);
	}
	
	
	
	public static void main(String[] args){
		// TODO Auto-generated method stub
		new Snake();
	}
	
	class Cellule
	{
		int x,y;
		Color couleur;

		public Cellule(int x, int y, Color couleur) {
			super();
			this.x = x;
			this.y = y;
			this.couleur = couleur;
		}
	}

}
