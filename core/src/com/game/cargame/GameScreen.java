package com.game.cargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.game.cargame.objects.Background;
import com.game.cargame.objects.Car;
import com.game.cargame.objects.Road;

public class GameScreen implements Screen {
    final CarGame game;

    OrthographicCamera camera;
    Car blueCar;
    Car redCar;
    Car yellowCar;

    Background bg;
    Road road;

    SpriteBatch batch;

    public GameScreen(CarGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 1080);

        blueCar = new Car("Blue Car", "car1.png", 100, 100);
        redCar = new Car("Red Car", "car2.png", 200, 100);
        yellowCar = new Car("Yellow Car", "car3.png", 300, 100);

        bg = new Background();
        road = new Road();

        batch = game.batch;

        blueCar.start();
        redCar.start();
        yellowCar.start();
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

        updateAll(delta);
        renderAll();
    }

    @Override
    public void resize(int width, int height) {

    }

    private void updateAll(float delta) {
//        bg.update(delta);
//        road.update(delta);
//        blueCar.update(delta);
//        redCar.update(delta);
//        yellowCar.update(delta);
    }

    private void renderAll() {
        batch.begin();
//        bg.render(batch);
//        road.render(batch);
        blueCar.render(batch);
        redCar.render(batch);
        yellowCar.render(batch);
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
