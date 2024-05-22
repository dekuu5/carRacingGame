package com.game.cargame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Car implements Runnable {

    private interface MovementStrategy {
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
    private float maxSpeed;
    private boolean isStopped;
    private MovementStrategy movementStrategy;
    private Obstacle[] obstacles;

    public Car(String name, String texturePath, float startX, float startY, MovementType type) {
        this.name = name;
        this.img = new Texture(Gdx.files.internal(texturePath));
        this.sprite = new Sprite(img);
        this.position = new Vector2(startX, startY);
        this.velocity = new Vector2(0, 0);
        this.speedX = 0;
        this.speedY = 0;
        this.acceleration = 10; // Example acceleration value
        this.maxSpeed = 300; // Maximum speed limit
        this.rect = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
        this.isStopped = false;

        if (type == MovementType.RLUD) {
            movementStrategy = this::updateSpeedLRUD;
        } else if (type == MovementType.WASD) {
            movementStrategy = this::updateSpeedWASD;
        } else if (type == MovementType.AI) {
            movementStrategy = this::updateSpeedAI;
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
        if (isStopped) {
            return;
        }

        // Update position based on speed and direction
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        rect.setPosition(position);

        // Update speed based on input
        movementStrategy.updateSpeed(delta);

        // Apply speed limit
        speedY = Math.min(speedY, maxSpeed);
        speedX = Math.min(speedX, maxSpeed);

        // Update velocity based on speed and direction
        updateVelocity();

        // Update the sprite's position
        sprite.setPosition(position.x, position.y);
    }

    private void updateSpeedWASD(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            speedY += acceleration * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            speedY -= acceleration * delta;
        } else {
            speedY = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            speedX += acceleration * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            speedX -= acceleration * delta;
        } else {
            speedX = 0;
        }
    }

    private void updateSpeedLRUD(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            speedY += acceleration * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            speedY -= acceleration * delta;
        } else {
            speedY = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            speedX += acceleration * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            speedX -= acceleration * delta;
        } else {
            speedX = 0;
        }
    }

    private void updateSpeedAI(float delta) {
        // AI logic for automatic movement and obstacle avoidance
        speedY += acceleration * delta;

        boolean obstacleDetected = false;
        for (Obstacle obstacle : obstacles) {
            if (rect.overlaps(obstacle.getBounds())) {
                obstacleDetected = true;
                break;
            }
        }

        if (obstacleDetected) {
            if (position.x > 400) {
                speedX = -acceleration * delta; // Move left
            } else {
                speedX = acceleration * delta; // Move right
            }
        } else {
            speedX = 0; // Keep moving straight
        }
    }

    private void updateVelocity() {
        velocity.set(speedX, speedY);
    }

    public void stopCar() {
        isStopped = true;
        speedX = 0;
        speedY = 0;
        velocity.set(0, 0);
    }

    public void startCar() {
        isStopped = false;
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

    public void setObstacles(Obstacle[] obstacles) {
        this.obstacles = obstacles;
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

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

}
