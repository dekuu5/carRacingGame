package com.game.cargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.cargame.objects.Background;
import com.game.cargame.objects.Car;
import com.game.cargame.objects.MovementType;
import com.game.cargame.objects.Road;

public class GameScreen implements Screen {
    final CarGame game;
    OrthographicCamera camera;
    Car blueCar;
    Car redCar;
    Car yellowCar;
    Thread blueCarThread;
    Thread redCarThread;

    Background bg;
    Road road;
    SpriteBatch batch;
    ScreenViewport viewport;
    public GameScreen(CarGame game) {
        this.game = game;
        viewport = new ScreenViewport();
        camera = (OrthographicCamera) viewport.getCamera();

        camera.setToOrtho(false, 1200, 1080);
        camera.update();

        blueCar = new Car("Blue Car", "car1.png", 100, 100, MovementType.WASD);
        redCar = new Car("Red Car", "car2.png", 500, 100, MovementType.RLUD);
//        yellowCar = new Car("Yellow Car", "car3.png", 300, 100);
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

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
//        camera.position.set(blueCar.getPosition(), 0);
       updateCamerared();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        updateAll();
        renderAll();

    }

    @Override
    public void resize(int width, int height) {

    }

    private void updateAll() {
        road.update(Gdx.graphics.getDeltaTime(), camera.position.y, redCar.getSpeedY());

        // Synchronize to ensure thread-safe update and retrieval of car positions
        synchronized (blueCar) {
            blueCar.update(Gdx.graphics.getDeltaTime());
        }
        synchronized (redCar) {
            redCar.update(Gdx.graphics.getDeltaTime());
        }
       // bg.update(Gdx.graphics.getDeltaTime());

    }

    private void renderAll() {
        batch.begin();
        road.render(batch, camera.position.y);


        synchronized (blueCar) {
            blueCar.getSprite().draw(batch);
        }
        synchronized (redCar) {
            redCar.getSprite().draw(batch);
        }
        batch.end();
    }
    private void updateCamerablue(){
        camera.position.set(blueCar.getPosition().x + blueCar.getSprite().getWidth() / 2,
                blueCar.getPosition().y + blueCar.getSprite().getHeight() / 2, 0);
    }
    private void updateCamerared() {
      //  System.out.println("camera y : " + camera.position.y +"camera x" + camera.position.x );

          camera.position.set(camera.position.x, redCar.getPosition().y + redCar.getSprite().getHeight() / 2, 0);

    }


    @Override
    public void pause() { //handle pausing the game

    }

    @Override
    public void resume() { //handle resuming the game

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        blueCar.dispose();
        redCar.dispose();
        yellowCar.dispose();
    }
}
