package com.game.cargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.cargame.objects.Car;
import com.game.cargame.objects.MovementType;
import com.game.cargame.objects.Obstacle;
import com.game.cargame.objects.Road;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameScreen implements Screen {
    final CarGame game;
    int blueCarScore = 0;
    int blueCarHealth = 4;
    OrthographicCamera camera;
    Camera playerOneCamera;
    Camera playerTwoCamera;
    BitmapFont font;
    final Car blueCar;
    final Car redCar;

    Road road;
    SpriteBatch batch;
    FillViewport viewport;
    ScreenViewport playerOneViewport;
    ScreenViewport playerTwoViewport;
    Obstacle[] obstacles;
    boolean isBlueCarMoving = false;

    public GameScreen(CarGame game) {
        this.game = game;

        font = new BitmapFont();
        font.setColor(Color.YELLOW);
        font.getData().setScale(2);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 800);
        camera.position.set(1200 / 2f, 350, 0);
        camera.update();
        playerOneCamera = new OrthographicCamera();
        playerTwoCamera = new OrthographicCamera();
        viewport = new FillViewport(800, 800, camera);
        playerOneViewport = new ScreenViewport(playerOneCamera);
        playerTwoViewport = new ScreenViewport(playerTwoCamera);

        blueCar = new Car("Blue Car", "car1.png", 700, 0, MovementType.WASD);
        redCar = new Car("Red Car", "car2.png", 500, 0, MovementType.AI);

        road = new Road();
        batch = game.batch;

        batch = new SpriteBatch();

        int numberOfObstacles = 3;
        obstacles = new Obstacle[numberOfObstacles];

        for (int i = 0; i < numberOfObstacles; i++) {
            float x = (float) (Math.random() * (800 - 64));
            float y = (float) (Math.random() * 800);
            obstacles[i] = new Obstacle(x, y, 350, 800);
        }

        redCar.setObstacles(obstacles); // Set obstacles for the red car AI
    }

    @Override
    public void show() {
        // Initialization if needed
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.apply();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), 800);
        updateAll(delta);
        if (blueCar.getSpeedY() != 0) {
            updateCamera(camera, blueCar);
        }
        batch.setProjectionMatrix(camera.combined);

        if (blueCar.getSpeedY() != 0) {
            isBlueCarMoving = true;
            blueCarScore++;
        } else {
            isBlueCarMoving = false;
        }

        batch.begin();
        if (isBlueCarMoving) {
            font.draw(batch, "Blue Car Score: " + blueCarScore, Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() - 20);
        }
        font.draw(batch, "Health: " + blueCarHealth, Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() - 50);
        batch.end();

        renderAll();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void updateAll(float delta) {
        synchronized (blueCar) {
            blueCar.update(delta);
            checkCollision(blueCar, delta);
        }
        synchronized (redCar) {
            redCar.update(delta);
            checkCollision(redCar, delta);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.update(delta, camera.position.y, Gdx.graphics.getHeight());
        }
        road.update(delta, camera.position.y, blueCar.getSpeedY());
    }

    private void renderAll() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        road.render(batch, camera.position.y);
        for (Obstacle obstacle : obstacles) {
            obstacle.render(batch);
        }
        synchronized (blueCar) {
            blueCar.getSprite().draw(batch);
        }
        synchronized (redCar) {
            redCar.getSprite().draw(batch);
        }
        batch.end();
    }

    private void updateCamera(Camera camera, Car car) {
        camera.position.set(camera.position.x, car.getPosition().y + car.getSprite().getHeight() / 2, 0);
        camera.update();
    }

    private void checkCollision(Car car, float delta) {
        boolean collisionDetected = false;
        for (Obstacle obstacle : obstacles) {
            if (car.getRectangle().overlaps(obstacle.getBounds())) {
                car.stopCar();
                collisionDetected = true;
                if (car == blueCar) {
                    blueCarHealth--;
                    if (blueCarHealth <= 0) {
                        handleGameOver();
                    }
                }
                break;
            }
        }

        if (!collisionDetected && car.getSpeedY() == 0) {
            car.startCar();
            car.setSpeedY(car.getAcceleration() * delta);
        }
    }

    private void handleGameOver() {
        System.out.println("Game Over!");
        blueCarHealth = 4;
        blueCarScore = 0;
    }

    @Override
    public void pause() {
        // Handle pause
    }

    @Override
    public void resume() {
        // Handle resume
    }

    @Override
    public void hide() {
        // Handle hide
    }

    @Override
    public void dispose() {
        blueCar.dispose();
        redCar.dispose();
        batch.dispose();
        for (Obstacle obstacle : obstacles) {
            obstacle.dispose();
        }
        font.dispose();
    }
}
