package com.game.cargame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Function;

public class Car implements Runnable {

    private String name;
    private Texture img;
    private Sprite sprite;
    private Rectangle rect;
    private Vector2 position;
    private Vector2 velocity;
    private float speedX;
    private float speedY;
    private float acceleration;
    private Function


    public Car(String name, String texturePath, float startX, float startY, String type) {
        this.name = name;
        this.img = new Texture(Gdx.files.internal(texturePath));
        this.sprite = new Sprite(img);
        this.position = new Vector2(startX, startY);
        this.velocity = new Vector2(0, 0);
        this.speedX = 0;
        this.speedY = 0;
        this.acceleration = 50; // Example acceleration value
        this.rect = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());

    }


    @Override
    public void run() {
        while (true) {
            float delta = Gdx.graphics.getDeltaTime();
            update(delta);
        }
    }

    public void update(float delta) {
        // Update position based on speed and direction
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        rect.setPosition(position);

        // Update speed based on input
        updateSpeed(delta);

        // Update velocity based on speed and direction
        updateVelocity();

        // Update the sprite's position
        sprite.setPosition(position.x, position.y);
        System.out.println(name + "x : " + position.x + " y : " + position.y);
    }

    private void updateSpeed(float delta) {
        // Vertical movement
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            speedY += acceleration * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            speedY -= acceleration * delta;
        } else {
            speedY = 0;
        }

        // Horizontal movement
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            speedX += acceleration * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            speedX -= acceleration * delta;
        }else {
            speedX = 0;
        }
    }

    private void updateVelocity() {
        velocity.set(speedX, speedY);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void dispose() {
        img.dispose();
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        rect.setPosition(position);
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }
}
