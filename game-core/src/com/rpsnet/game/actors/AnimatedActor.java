package com.rpsnet.game.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.rpsnet.game.AnimatedTexture;

public class AnimatedActor extends Actor
{
    private AnimatedTexture animatedTexture;

    public AnimatedActor(AnimatedTexture anim)
    {
        animatedTexture = anim;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        TextureRegion currentFrame = animatedTexture.getCurrentFrame();

        float xPos = getX();
        float yPos = getY() - (currentFrame.getRegionHeight() / 2);

        batch.draw(currentFrame, xPos, yPos);
    }

    @Override
    public void act(float delta)
    {
        animatedTexture.updateFrames(delta);
    }
}
