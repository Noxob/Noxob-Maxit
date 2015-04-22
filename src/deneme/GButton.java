package deneme;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class GButton {
	
	private int x,y;
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

	private Texture g1;
	private Texture g2;
	private Texture g3;
	private Texture s1;
	private Texture s2;
	private Texture s3;
	private int height;
	
	
	public GButton(int x, int y, String f1, String f2, String f3, String s1, String s2, String s3, int height) throws FileNotFoundException, IOException
	{
		this.x = x;
		this.y = y;
		this.height = height;
		g1 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + f1 + ".png")));	
		g2 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + f2 + ".png")));
		g3 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + f3 + ".png")));
		this.s1 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + s1 + ".png")));	
		this.s2 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + s2 + ".png")));
		this.s3 = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + s3 + ".png")));
	}
	
	public void draw()
	{
		g1.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x, y); //Upper left corner
			glTexCoord2f(1, 0);
			glVertex2f(x + height, y); //Upper right corner
			glTexCoord2f(1, 1);
			glVertex2f(x + height, y + height); //Bottom right corner
			glTexCoord2f(0, 1);
			glVertex2f(x, y + height); //Bottom left corner
		glEnd();
		
		g2.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x+height, y); //Upper left corner
			glTexCoord2f(1, 0);
			glVertex2f(x+ height + height, y); //Upper right corner
			glTexCoord2f(1, 1);
			glVertex2f(x + (2 *height), y + height); //Bottom right corner
			glTexCoord2f(0, 1);
			glVertex2f(x + height, y + height); //Bottom left corner
		glEnd();
		
		g3.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x +(2 *height), y); //Upper left corner
			glTexCoord2f(1, 0);
			glVertex2f(x + (3 *height), y); //Upper right corner
			glTexCoord2f(1, 1);
			glVertex2f(x + (3 *height), y + height); //Bottom right corner
			glTexCoord2f(0, 1);
			glVertex2f(x + (2 *height), y + height); //Bottom left corner
		glEnd();
	}
	
	public void shine()
	{
		s1.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x, y); //Upper left corner
			glTexCoord2f(1, 0);
			glVertex2f(x + height, y); //Upper right corner
			glTexCoord2f(1, 1);
			glVertex2f(x + height, y + height); //Bottom right corner
			glTexCoord2f(0, 1);
			glVertex2f(x, y + height); //Bottom left corner
		glEnd();
		
		s2.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x+height, y); //Upper left corner
			glTexCoord2f(1, 0);
			glVertex2f(x+ height + height, y); //Upper right corner
			glTexCoord2f(1, 1);
			glVertex2f(x + (2 *height), y + height); //Bottom right corner
			glTexCoord2f(0, 1);
			glVertex2f(x + height, y + height); //Bottom left corner
		glEnd();
		
		s3.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x +(2 *height), y); //Upper left corner
			glTexCoord2f(1, 0);
			glVertex2f(x + (3 *height), y); //Upper right corner
			glTexCoord2f(1, 1);
			glVertex2f(x + (3 *height), y + height); //Bottom right corner
			glTexCoord2f(0, 1);
			glVertex2f(x + (2 *height), y + height); //Bottom left corner
		glEnd();
	}
	
	public boolean inBounds(int mousex, int mousey)
	{
		if(mousex > x && mousex < x+ (3*height) && mousey > y && mousey < y + height)
		return true;
		
		return false;
	}
	
	
	
	
	
}
