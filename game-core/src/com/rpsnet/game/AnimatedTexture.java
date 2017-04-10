package com.rpsnet.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class AnimatedTexture implements Disposable
{
    private int columns;
    private int rows;

    private Animation<TextureRegion> animation;
    private Texture spriteSheet;
    private float stateTime;

    public AnimatedTexture(String path, int colCount, int rowCount, float frameInterval)
    {
        //Assign column and row values
        columns = colCount;
        rows = rowCount;

        //Load the sprite sheet as a texture
        spriteSheet = new Texture(Gdx.files.internal(path));

        //Split the sheet into a 2D array of textures for animation
        TextureRegion[][] tempFrames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / columns, spriteSheet.getHeight() / columns);

        //Place the regions into a 1D array in order
        TextureRegion[] frames = new TextureRegion[columns * rows];
        int index = 0;
        for(int y = 0; y < rows; y++)
        {
            for(int x = 0; x < columns; x++)
            {
                frames[index++] = tempFrames[y][x];
            }
        }

        //Create the animation with the provided frames and frame interval
        animation = new Animation<TextureRegion>(frameInterval, frames);

        //Assign the state time
        stateTime = 0;
    }

    public void updateFrames(float delta)
    {
        stateTime += delta;
    }

    public TextureRegion getCurrentFrames()
    {
        return animation.getKeyFrame(stateTime, true);
    }

    @Override
    public void dispose()
    {
        spriteSheet.dispose();
    }
}
