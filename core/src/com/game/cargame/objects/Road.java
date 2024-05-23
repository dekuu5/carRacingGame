package com.game.cargame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Road {
    private Texture roadTexture;
    private float roadY1;
    private float roadY2;
    private float roadY3;
    private float maxSpeed;

    public Road() {
        this.roadTexture = new Texture("background-1.png");
        this.roadY1 = -540;
        this.roadY2 = roadTexture.getHeight() - 540;
        this.roadY3 = roadTexture.getHeight() * 2 - 540;
        this.maxSpeed = 300; // Maximum speed limit for the road
    }

    public void update(float deltaTime, float cameraY, float speed) {
        // Apply speed limit
        speed = Math.min(speed, maxSpeed);

        roadY1 -= speed * deltaTime;
        roadY2 -= speed * deltaTime;
        roadY3 -= speed * deltaTime;

        if (roadY1 + roadTexture.getHeight() <= cameraY - 1080 / 2.0) {
            roadY1 = roadY3 + roadTexture.getHeight();
        }
        if (roadY2 + roadTexture.getHeight() <= cameraY - 1080 / 2.0) {
            roadY2 = roadY1 + roadTexture.getHeight();
        }
        if (roadY3 + roadTexture.getHeight() <= cameraY - 1080 / 2.0) {
            roadY3 = roadY2 + roadTexture.getHeight();
        }
    }

    public void render(SpriteBatch batch, float cameraY) {
        batch.draw(roadTexture, 180, roadY1);
        batch.draw(roadTexture, 180, roadY2);
        batch.draw(roadTexture, 180, roadY3);
    }

    public void dispose() {
        roadTexture.dispose();
    }
}