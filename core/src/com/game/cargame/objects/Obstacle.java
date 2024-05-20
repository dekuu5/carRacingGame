package com.game.cargame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
    private final Texture texture;
    private final Vector2 position;

    public Obstacle(float x, float y) {
        this.texture = new Texture("img.png");
        this.position = new Vector2(x, y);
    }

    public void update(float deltaTime) {
        position.y -= 0 * deltaTime;
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
}