package game;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;







import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.*;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Game {
	
	private static UnicodeFont font;
	private List<MxBlock> blocks = new ArrayList<MxBlock>();
	private NumBlock[] nums = new NumBlock[25];//N i kullanýcýdan almaya baþlayýnca içeride initialize et!!!
	private int p1score, p2score;
	private int turn = 1;
	private int selected = 0;
	private int i = 3;
	private int j = 3;
	private long start;
	private boolean musicOn = true;// for music on off button
	private boolean justClicked = false;// to realize if user just clicked on button or not
	private int[][] remaining = new int[5][5];
	private Texture winner1;
	private Texture winner2;
	private Texture nowinner;
	private Texture turnTex1;
	private Texture turnTex2;
	private Texture logo;
	private Texture on1;
	private Texture on2;
	private Texture off1;
	private Texture off2;
	
	DisplayMode displayMode;
	private GButton exit;
	private volatile boolean cooldown = false;
	private GButton play;
	private GButton again;
	private GButton back;
	private GButton ins1;
	private GButton ins2;
	
	
	private static enum State
	{
		INTRO, MAIN_MENU, INSTR, GAME;
	}
	
	private State state = State.MAIN_MENU;
	
	
	public Game() throws FileNotFoundException, IOException{
		try {
				displayMode = null;
		     DisplayMode[] modes = Display.getAvailableDisplayModes();
		        for (int i = 0; i < modes.length; i++)
		         {
		             if(modes[i].isFullscreenCapable())
		               {
		                    displayMode = modes[i];
		               }
		         }
		        
		    Display.setDisplayMode(displayMode);
			Display.setFullscreen(true);
//			Display.setIcon(new ByteBuffer[]{
//					new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/maxiticon.png")),false,false,null),
//					new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/maxiticon.png")),false,false,null)
//			});
			Display.setTitle("MaxIt!");
			Display.create();
		} catch (LWJGLException e) {
			
			e.printStackTrace();
		}
		
		//Initialization code for OpenGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		//IMPORTANT - arkaplaný çizerken disable etmeyi unutma
			glEnable(GL_BLEND);// FOR THE TRANSPARENCY!!!!!!!!!!
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);// FOR THE TRANSPARENCY!!!!!!!!!!
			again = new GButton(570,645,"ag11","ag12","ag13","ag21","ag22","ag23",64);
			exit = new GButton(950,700,"ex11","ex12","ex13","ex21","ex22","ex23",64);
			play = new GButton(550,300,"p11","p12","p13","p21","p22","p23",64);
			back = new GButton(150,645,"b11","b12","b13","b21","b22","b23",64);
			ins1 = new GButton(450,400,"ins11","ins12","ins13","ins21","ins22","ins23",64);
			ins2 = new GButton(642,400,"ins14","ins15","ins16","ins24","ins25","ins26",64);
		setUpFonts();
		preNxNBoard();
		//on1 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/on1.png")));
		//on2 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/on2.png")));
		//off1 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/off1.png")));
		//off2 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/off2.png")));
		winner1 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/p1win.png")));
		winner2 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/p2win.png")));
		nowinner = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/tie.png")));
		turnTex1 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/turn1.png")));
		turnTex2 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/turn2.png")));
		logo = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/maxlogo.png")));
		start = (Sys.getTime())/ Sys.getTimerResolution();
		playSound("ba.wav");
		while(!Display.isCloseRequested())
		{
			//render
			glClear(GL_COLOR_BUFFER_BIT);
			
			if((((Sys.getTime())/ Sys.getTimerResolution() - start >= 72)&& musicOn)
					|| justClicked)//72 arkaplan müziðinin saniye cinsinden uzunluðu
			{
				start = (Sys.getTime())/ Sys.getTimerResolution();
				playSound("ba.wav");
				justClicked = false;
			}
			
			//System.out.println("x: " + Mouse.getX() + ", y: " + -Mouse.getY());
			if(state == State.MAIN_MENU)
				renderMenu();
			else if(state == State.GAME)
				runGame();
			else if(state == State.INSTR)
				renderInstr();
			
			
			
			
			Display.update();
			Display.sync(60);
		}
		 
		Display.destroy();
	}
	
	public void preNxNBoard() throws FileNotFoundException, IOException//N e N boardý hazýrlayacak (5x5 lik þuan)
	{
		int x = 400;
		int y = 0;
		
		int z = 1;
		int t = 0;
		for(int i = 1; i <= 5; i++)
		{
			z=1;
			t++;
			x = 400+1;
			y += 101;//bloklarýn boyutundan 1 fazla ya da aralarýndaki mesafeyi ne kadar istiyorsan o kadar fazla
			for(int j = 1; j <= 5; j++)//<= n
			{
				if(z == 3 && t == 3)//n/2
					blocks.add(new MxBlock(x,y,z,t,true));
				else
					blocks.add(new MxBlock(x,y,z,t,false));
				
				remaining[i-1][j-1] = 1;
				
				x += 101;//bloklarýn boyutundan 1 fazla ya da aralarýndaki mesafeyi ne kadar istiyorsan o kadar fazla
				z++;
			}
		}
		
	}
	
	public void resetBoard()
	{
		int x = 400;
		int y = 0;
		
		int z = 1;
		int t = 0;
		int counter = 0;
		for(int i = 1; i <= 5; i++)
		{
			z=1;
			t++;
			x = 400+1;
			y += 101;//bloklarýn boyutundan 1 fazla ya da aralarýndaki mesafeyi ne kadar istiyorsan o kadar fazla
			for(int j = 1; j <= 5; j++)//<= n
			{
				blocks.get(counter).setX(x);;
				blocks.get(counter).setY(y);;
				blocks.get(counter).setValue((int)(new Random().nextFloat() * 10) + 1);
				nums[counter] = null;
				remaining[i-1][j-1] = 1;
				
				x += 101;//bloklarýn boyutundan 1 fazla ya da aralarýndaki mesafeyi ne kadar istiyorsan o kadar fazla
				z++;
				counter++;
			}
		}
		
	}
	
	public void runGame() throws FileNotFoundException, IOException
	{
		exit.setX(950);
		exit.setY(645);
		font.drawString(1000, 300, "Scores\nPlayer 1: " + p1score + "\nPlayer 2: " + p2score);
		
		drawTexture(logo, 50, 0,256);
		if(exit.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()))
		{
			exit.shine();
		}
		else
		{
			exit.draw();
		}
		
		if((exit.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY())) 
				&& Mouse.isButtonDown(0))
		{
			playSound("click.wav");
			Display.destroy();
			System.exit(0);
		}
		
		if(back.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()))
		{
			back.shine();
		}
		else
		{
			back.draw();
		}
		
		if((back.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY())) 
				&& Mouse.isButtonDown(0))
		{
			playSound("click.wav");
			state = State.MAIN_MENU;
		}
		
		
		
		
		for(MxBlock m: blocks)
		{
			if((m.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()) && (i == m.getI() || j == m.getJ()))
					||(m.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()) && anyPoss()) )
			{
				m.sh();
			}
			else if((m.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()) && !(i == m.getI() || j == m.getJ()))
					||(m.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()) && !anyPoss()) )
			{
					m.wrsh();
			}
			else
				m.draw();
			
			
			if(Mouse.isButtonDown(0) && m.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()) && (i == m.getI() || j == m.getJ()))
			{
				playSound("click.wav");
				nums[selected] = new NumBlock( m.getX(), m.getY(), m.getValue());
				m.setX(1000);
				m.setY(1000);
				selected++;
				i = m.getI();
				j = m.getJ();
				remaining[i-1][j-1]=0;
				if(turn == 1){
					p1score += m.getValue();
					turn = 2;
				}
				else{
					p2score += m.getValue();
					turn = 1;
				}
			}
			else if(Mouse.isButtonDown(0) && m.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()) && anyPoss())
			{
				playSound("click.wav");
				nums[selected] = new NumBlock( m.getX(), m.getY(), m.getValue());
				m.setX(1000);
				m.setY(1000);
				selected++;
				i = m.getI();
				j = m.getJ();
				remaining[i-1][j-1]=0;
				if(turn == 1){
					p1score += m.getValue();
					turn = 2;
				}
				else{
					p2score += m.getValue();
					turn = 1;
				}
			}
			else if(Mouse.isButtonDown(0) && m.inBounds(Mouse.getX(), 480 - Mouse.getY()))
			{
				
			}
			
		}
		for(int i = 0; i < selected; i++)
		{
			nums[i].drawNum();
		}
		
		if(turn == 1 && selected < 25)
		{
			drawTexture(turnTex1,100, 250, 256);;
		}
		else if(turn == 2 && selected < 25)
		{
			drawTexture(turnTex2,100, 250, 256);
		}
		if(selected >= 25)
		{
			//play.setX(570); 
			//(play.setY(590);
			if(again.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()))
			{
				again.shine();
			}
			else
			{
				again.draw();
			}
			
			if(again.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY())
					&& Mouse.isButtonDown(0))
			{
				playSound("click.wav");
				i = 3;
				j = 3;
				p1score = 0;
				p2score = 0;
				selected = 0;
				turn = 1;
				resetBoard();
			}
			
			if(p1score > p2score)
				{
					drawTexture(winner1,550,240, 256);
				}
			else if(p2score > p1score)
				{
					drawTexture(winner2,550,240, 256);
				}
			else
				{
					drawTexture(nowinner,550,240, 256);
				}
			
		}
		
		
		
	}
	
	
	
	
	public void renderInstr()
	{
		font.drawString(0, 0, "\n\nHow To Play:\n       Two players alternate turns.\n       "
	+ "At each turn,  a player must select a grid element\n" + 
				"       in the current row or column.\n" + 
	"       The block with star is designated as the initial current position.\n" + 
	"       The value of the selected position is added to the player’s score,\n" + 
	"       and that position becomes the current\n" + 
	"       position and cannot be selected again.\n"  +  
	"       Players alternate until all grid elements\n" +  
	"       in the current row and column are already selected,\n" + 
	"       at which point the game ends and\n" + "       the player with the higher score wins.");
		if(back.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()))
		{
			back.shine();
		}
		else
		{
			back.draw();
		}
		
		if((back.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY())) 
				&& Mouse.isButtonDown(0))
		{
			playSound("click.wav");
			state = State.MAIN_MENU;
		}
	}
	
	
	public void renderMenu()
	{
		play.setX(550);
		play.setY(300);
		exit.setX(550);
		exit.setY(500);
		drawTexture(logo,512,0, 256);
		
		if(ins1.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY())
				||ins2.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()))
		{
			ins1.shine();
			ins2.shine();
		}
		else
		{
			ins1.draw();
			ins2.draw();
		}
		
		if(((ins1.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY())
				|| ins2.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()))) 
				&& Mouse.isButtonDown(0))
		{
			playSound("click.wav");
			state = State.INSTR;
		}
		if(play.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()))
		{
			play.shine();
		}
		else
		{
			play.draw();
		}
		
		if((play.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY())) 
				&& Mouse.isButtonDown(0))
		{
			
			playSound("click.wav");
			
			cooldown = true;
			new Thread(new Runnable(){
				
				@Override
				public void run() {
					try {
						Thread.sleep(200);//Main menü den sonra bloklara týklýyordu biraz yavaþlattým
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally{
						cooldown = false;
					}
					
				}
				
			}).run();
			state = State.GAME;
		}
		if(exit.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY()))
		{
			exit.shine();
		}
		else
		{
			exit.draw();
		}
		
		if((exit.inBounds(Mouse.getX(), Display.getHeight() - Mouse.getY())) 
				&& Mouse.isButtonDown(0))
		{
			playSound("click.wav");
			Display.destroy();
			System.exit(0);
		}
	}
	
	
	public void drawTexture(Texture texture, int x, int y,int size)
	{
		texture.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x, y); //Upper left corner
			glTexCoord2f(1, 0);
			glVertex2f(x + size, y); //Upper right corner
			glTexCoord2f(1, 1);
			glVertex2f(x + size, y+256); //Bottom right corner
			glTexCoord2f(0, 1);
			glVertex2f(x, y+size); //Bottom left corner
		glEnd();
	}
	
	public boolean anyPoss()
	{
		int counter = 0;
		for(int x = 0; x < 5; x++)
		{
			if(remaining[i-1][x] == 1)
				counter++;
			if(remaining[x][j-1] == 1)
				counter++;
		}
		if(counter == 0)
			return true;
		else
			return false;
	}
	
	
	private static void setUpFonts()
	{
		java.awt.Font awtfont = new java.awt.Font("Showcard Gothic", java.awt.Font.BOLD, 30);
		font = new UnicodeFont(awtfont);
		font.getEffects().add(new ColorEffect(java.awt.Color.yellow));
		font.addAsciiGlyphs();
		
		try{
			font.loadGlyphs();
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static synchronized void playSound(final String url) {
		  new Thread(new Runnable() {
		  // The wrapper thread is unnecessary, unless it blocks on the
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("res/" + url));
		        clip.open(inputStream);
		        clip.start(); 
		      } catch (Exception e) {
		        System.err.println(e.getMessage());
		      }
		    }
		  }).start();
		}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		new Game();
	}
	
	

}
