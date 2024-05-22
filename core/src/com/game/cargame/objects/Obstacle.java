package com.game.cargame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
    private final Texture texture;
    private final Vector2 position;
    private final float roadStartX;
    private final float roadEndX;

    public Obstacle(float x, float y, float roadStartX, float roadEndX) {
        this.texture = new Texture("img.png");
        this.position = new Vector2(x, y);
        this.roadStartX = roadStartX;
        this.roadEndX = roadEndX;
    }

    public void update(float deltaTime, float cameraY, float screenHeight) {
        // Update the obstacle's position based on time and speed
        position.y -= 0 * deltaTime;

        // Reposition the obstacle if it moves out of view
        if (position.y + texture.getHeight() < cameraY - screenHeight / 2) {
            reposition(cameraY + screenHeight / 2);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void dispose() {
        texture.dispose();
    }

    private void reposition(float newY) {
        // Reposition the obstacle to a new position above the current view
        float x = roadStartX + (float) (Math.random() * (roadEndX - roadStartX - texture.getWidth()));
        position.set(x, newY + texture.getHeight());
    }
}
