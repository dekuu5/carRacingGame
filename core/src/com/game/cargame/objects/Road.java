package com.game.cargame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Road {
    private Texture roadTexture;
    private float roadY1;
    private float roadY2;
    private float speed;
    private float carSpeed;


    public Road() {
        this.roadTexture = new Texture("background-1.png");
        this.roadY1 = 0;
        this.roadY2 = roadTexture.getHeight();
        this.speed = 2; // Adjust the speed as needed
    }

    public void update(float deltaTime) {
        roadY1 -= speed * deltaTime;
        roadY2 -= speed * deltaTime;

        // If the first road texture goes off-screen, reset its position
        if (roadY1 + roadTexture.getHeight() <= 0) {
            roadY1 = roadY2 + roadTexture.getHeight();
        }

        // If the second road texture goes off-screen, reset its position
        if (roadY2 + roadTexture.getHeight() <= 0) {
            roadY2 = roadY1 + roadTexture.getHeight();
        }
    }

    public void render(SpriteBatch batch) {
        // Render the first road texture
        batch.draw(roadTexture, 180, roadY1);

        // Render the second road texture below the first one
        batch.draw(roadTexture, 180, roadY2);
    }

    public void dispose() {
        roadTexture.dispose();
    }
}
