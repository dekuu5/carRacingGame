package com.game.cargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.cargame.objects.Car;
import com.game.cargame.objects.MovementType;
import com.game.cargame.objects.Obstacle;
import com.game.cargame.objects.Road;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    final CarGame game;
    Thread blueCarThread;
    Thread redCarThread;
    int blueCarScore = 0;
    int blueCarHealth = 4;
    OrthographicCamera camera;
    Camera playerOneCamera;
    Camera playerTwoCamera;
    BitmapFont font;
    final Car blueCar;
    final Car redCar;
    int numberOfObstacles;
    Road road;
    SpriteBatch batch;
    FillViewport viewport;
    ScreenViewport playerOneViewport;
    ScreenViewport playerTwoViewport;
    List <Obstacle> obstacles;
    boolean isBlueCarMoving = false;
    boolean gameOver = false;
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
        blueCarThread = new Thread(blueCar, "Blue Car Thread");
        redCarThread = new Thread(redCar, "Red Car Thread");

        blueCarThread.start();
        redCarThread.start();

        road = new Road();
        batch = game.batch;

        batch = new SpriteBatch();

        numberOfObstacles = 3;
        obstacles = new ArrayList< >(numberOfObstacles);
         for (int i = 0; i < numberOfObstacles; i++) {
            float x = (float) (Math.random() * (800 - 64));
            float y = camera.position.y + (float) (Math.random() * 800);
            obstacles.add(new Obstacle(x, y, 0, 800));}

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

        redCar.setObstacles(obstacles); // Set obstacles for the red car AI
        blueCar.setObstacles(obstacles);

        updateAll(delta);
        if (blueCar.getSpeedY() != 0) {
            updateCamera(camera, blueCar);
        }
        batch.setProjectionMatrix(camera.combined);

        if (blueCar.getSprite().getY() != 0&& (int) blueCar.getSpeedY() != 0){
            isBlueCarMoving = true;
            blueCarScore++;
            System.out.println(blueCar.getSpeedY());
        } else {
            isBlueCarMoving = false;
        }


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
            System.out.println(redCar.getPosition().y);
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
        font.draw(batch, "Blue Car Score: " + blueCarScore, 300 , 0+ camera.position.y +blueCar.getSprite().getHeight()+250);

        font.draw(batch, "Health: " + blueCarHealth, 300 ,  camera.position.y +blueCar.getSprite().getHeight()+200);
        if (gameOver){font.draw(batch, "Game Over!", camera.position.x/2, camera.position.y /2);}

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
                    obstacle.remove();
                    obstacle.update(delta, camera.position.y, Gdx.graphics.getHeight());
                    if (blueCarHealth <= 0) {
                        handleGameOver();
                        break;
                    }

                }

            }
        }

        if (!collisionDetected &&  (int)car.getSpeedY() == 0) {
            car.startCar();
            car.setSpeedY(car.getAcceleration() * delta);
        }
    }


    private void handleGameOver() {
        System.out.println("Game Over!");
        gameOver=true;
//        blueCarHealth = 4;
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