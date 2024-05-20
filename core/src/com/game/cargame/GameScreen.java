package com.game.cargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.cargame.objects.Car;
import com.game.cargame.objects.MovementType;
import com.game.cargame.objects.Obstacle;
import com.game.cargame.objects.Road;

public class GameScreen implements Screen {
    final CarGame game;
    OrthographicCamera camera;
    Camera playerOneCamera;
    Camera playerTwoCamera;

    final Car blueCar;
    final Car redCar;
    Thread blueCarThread;
    Thread redCarThread;

    Road road;
    SpriteBatch batch;
    FillViewport viewport;
    ScreenViewport playerOneViewport;
    ScreenViewport playerTwoViewport;
    Obstacle[] obstacles;

    public GameScreen(CarGame game) {
        this.game = game;

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
        redCar = new Car("Red Car", "car2.png", 500, 0, MovementType.RLUD);

        road = new Road();
        batch = game.batch;

        blueCarThread = new Thread(blueCar, "Blue Car Thread");
        redCarThread = new Thread(redCar, "Red Car Thread");

        blueCarThread.start();
        redCarThread.start();

        batch = new SpriteBatch();

        // Initialize the array of obstacles
        int numberOfObstacles = 3;
        obstacles = new Obstacle[numberOfObstacles];

        // Initialize obstacles at random positions
        for (int i = 0; i < numberOfObstacles; i++) {
            float x = (float) (Math.random() * (800 - 64)); // Adjust 64 to the width of your texture
            float y = (float) (Math.random() * 800); // Adjust 64 to the height of your texture
            obstacles[i] = new Obstacle(x, y);
        }
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
        System.out.println("camera y" + camera.position.y + " camera x" + camera.position.x); ;
        batch.setProjectionMatrix(camera.combined);

        renderAll();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void updateAll(float delta) {
        synchronized (blueCar) {
            blueCar.update(delta);
        }
        synchronized (redCar) {
            redCar.update(delta);
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
    }
}
