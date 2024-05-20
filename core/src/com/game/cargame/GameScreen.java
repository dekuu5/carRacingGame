package com.game.cargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.cargame.objects.Background;
import com.game.cargame.objects.Car;
import com.game.cargame.objects.MovementType;
import com.game.cargame.objects.Road;

public class GameScreen implements Screen {
    final CarGame game;
    OrthographicCamera camera;
    Camera playerOneCamera;
    Camera playerTwoCamera;

    final Car blueCar;
    final Car redCar;
    Car yellowCar;
    Thread blueCarThread;
    Thread redCarThread;

    Background bg;
    Road road;
    SpriteBatch batch;
    FitViewport viewport;
    ScreenViewport playerOneViewport;
    ScreenViewport playerTwoViewport;

    public GameScreen(CarGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 1080);
        camera.position.set(1200 / 2f, 350, 0);
        camera.update();
        playerOneCamera = new OrthographicCamera();
        playerTwoCamera = new OrthographicCamera();
        viewport = new FitViewport(800, 800, camera);
        playerOneViewport = new ScreenViewport(playerOneCamera);
        playerTwoViewport = new ScreenViewport(playerTwoCamera);


        blueCar = new Car("Blue Car", "car1.png", 700, 0, MovementType.WASD);
        redCar = new Car("Red Car", "car2.png", 500, 0, MovementType.RLUD);

        bg = new Background();
        road = new Road();
        batch = game.batch;

        blueCarThread = new Thread(blueCar, "Blue Car Thread");
        redCarThread = new Thread(redCar, "Red Car Thread");

        blueCarThread.start();
        redCarThread.start();
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
        if(blueCar.getSpeedY()!=0){
        updateCamera(camera, blueCar);
        camera.update();}
        batch.setProjectionMatrix(camera.combined);

        updateAll(delta);
        renderAll();
    }

    @Override
    public void resize(int width, int height) {
//        playerOneViewport.update(width / 2, height);
//        playerTwoViewport.update(width, height);
//        playerTwoViewport.setScreenX(width / 2);
    }

    private void updateAll(float delta) {
        synchronized (blueCar) {
            blueCar.update(delta);
        }
        synchronized (redCar) {
            redCar.update(delta);
        }
          road.update(delta, camera.position.y, blueCar.getSpeedY());
//        road.update(delta, playerTwoCamera.position.y);
    }

    private void renderAll() {
     //   updateCamera(camera, blueCar);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        road.render(batch, camera.position.y);
        synchronized (blueCar) {
            blueCar.getSprite().draw(batch);
        }
//        batch.end();
//
//        updateCamera(camera, redCar);
//        batch.setProjectionMatrix(playerTwoCamera.combined);
//        batch.begin();
//        road.render(batch, playerTwoCamera.position.y);
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

    }
}