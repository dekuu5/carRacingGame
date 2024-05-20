package com.game.cargame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Car implements Runnable {

    private interface keyMovement
    {
        void updateSpeed(float delta);
    }




    private String name;
    private Texture img;



    private Sprite sprite;
    private Rectangle rect;
    private Vector2 position;
    private Vector2 velocity;
    private float speedX;
    private float speedY;
    private float acceleration;
    private keyMovement updateSpeed;


    public Car(String name, String texturePath, float startX, float startY, MovementType type) {
        this.name = name;
        this.img = new Texture(Gdx.files.internal(texturePath));
        this.sprite = new Sprite(img);
        this.position = new Vector2(startX, startY);
        this.velocity = new Vector2(0, 0);
        this.speedX = 0;
        this.speedY = 0;
        this.acceleration = 50; // Example acceleration value
        this.rect = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
        if (type == MovementType.RLUD) {
            updateSpeed = this::updateSpeedLRUD;
        }else if (type == MovementType.WASD) {

            updateSpeed = this::updateSpeedWASD;
        }

    }


    @Override
    public void run() {
        while (true) {
            float delta = Gdx.graphics.getDeltaTime();
            update(delta);
            try {
                Thread.sleep(16); // approximately 60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

            }
        }
    }
    public void update(float delta) {
        // Update position based on speed and direction
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        rect.setPosition(position);

        // Update speed based on input
        updateSpeed.updateSpeed(delta);

        // Update velocity based on speed and direction
        updateVelocity();

        // Update the sprite's position
        sprite.setPosition(position.x, position.y);
   //     System.out.println(name + "x : " + position.x + " y : " + position.y);
    }


    private void updateSpeedWASD(float delta){
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            speedY += acceleration * delta;
        } else {
            speedY -= acceleration * delta;
        }
        // Horizontal movement
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            speedX += acceleration * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            speedX -= acceleration * delta;
        }else {
            speedX = 0;
        }
    }
    private void updateSpeedLRUD(float delta) {
        // Vertical movement
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            speedY += acceleration * delta;
        } else {
            speedY -= acceleration * delta;
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

    public void setPosition(float x, float y) {
        this.position = new Vector2(x, y);
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
    public Sprite getSprite() {
        return sprite;
    }
}
