package game;

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

public class NumBlock {

	private int x, y;
	private Texture numTex;
	private int size;
	
	public NumBlock(int x, int y, int value) throws FileNotFoundException, IOException
	{
		this.x = x;
		this.y = y;
		size = 100;
		numTex = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/"+ Integer.toString(value) +".png")));
	}

	
	public void drawNum()
	{
		numTex.bind();
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
}
