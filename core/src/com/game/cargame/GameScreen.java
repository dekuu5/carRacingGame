package com.game.cargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
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

    public GameScreen(CarGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 1080);

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
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        updateAll();
        renderAll();
    }

    @Override
    public void resize(int width, int height) {

    }

    private void updateAll() {
        // Synchronize to ensure thread-safe update and retrieval of car positions
        synchronized (blueCar) {
            blueCar.update(Gdx.graphics.getDeltaTime());
        }
        synchronized (redCar) {
            redCar.update(Gdx.graphics.getDeltaTime());
        }
    }

    private void renderAll() {
        batch.begin();
        synchronized (blueCar) {
            blueCar.getSprite().draw(batch);
        }
        synchronized (redCar) {
            redCar.getSprite().draw(batch);
        }
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

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
