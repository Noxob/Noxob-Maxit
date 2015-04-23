package game;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class MxBlock {
	
	private int value;
	private int x;
	private int y;
	private int i;
	private int j;
	private Texture block;
	private Texture shine;
	private Texture wrsh;
	private int size;
	
	public MxBlock(int x, int y, int i, int j, boolean chosen) throws FileNotFoundException, IOException
	{
		this.x = x;
		this.y = y;
		this.i = i;
		this.j = j;
		size = 100;
		Random randomGenerator = new Random();
		value = (int) ((randomGenerator.nextFloat() * 10) + 1);
		
		
			shine = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/mxshine.png")));
		
		
		
			wrsh = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/mxwrsh.png")));
		
		
		if(chosen)
		{
			try {
				block = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/star.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				block = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/mxblock.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public void draw()
	{
		block.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x, y); //Upper left corner
			glTexCoord2f(1, 0);
			glVertex2f(x + size, y); //Upper right corner
			glTexCoord2f(1, 1);
			glVertex2f(x + size, y + size); //Bottom right corner
			glTexCoord2f(0, 1);
			glVertex2f(x, y + size); //Bottom left corner
		glEnd();
	}
	
	public void sh()
	{
		shine.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x, y); //Upper left corner
			glTexCoord2f(1, 0);
			glVertex2f(x + size, y); //Upper right corner
			glTexCoord2f(1, 1);
			glVertex2f(x + size, y + size); //Bottom right corner
			glTexCoord2f(0, 1);
			glVertex2f(x, y + size); //Bottom left corner
		glEnd();
	}
	
	public void wrsh()
	{
		wrsh.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x, y); //Upper left corner
			glTexCoord2f(1, 0);
			glVertex2f(x + size, y); //Upper right corner
			glTexCoord2f(1, 1);
			glVertex2f(x + size, y + size); //Bottom right corner
			glTexCoord2f(0, 1);
			glVertex2f(x, y + size); //Bottom left corner
		glEnd();
	}
	
	public boolean inBounds(int mousex, int mousey)
	{
		if(mousex > x && mousex < x+size && mousey > y && mousey < y+size) 
		return true;
		
		return false;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	

}
