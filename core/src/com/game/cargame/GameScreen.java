    package com.game.cargame;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.Screen;
    import com.badlogic.gdx.audio.Sound;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.math.Rectangle;
    import com.badlogic.gdx.utils.Array;
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

            blueCar = new Car();
            redCar = new Car();
            yellowCar = new Car();

            bg = new Background();

            road = new Road();

            batch = game.batch;



        }

        @Override
        public void show() {

        }

        @Override
        public void render(float delta) {
            ScreenUtils.clear(0, 0, 0, 1);

            camera.update();
            batch.setProjectionMatrix(camera.combined);

            updateAll(delta);

            renderAll();
        }



        @Override
        public void resize(int width, int height) {

        }

        private void updateAll(float delta) {

            bg.update(delta);
            road.update(delta);
            yellowCar.update(delta);
            redCar.update(delta);
            blueCar.update(delta);
        }

        private void renderAll() {
            batch.begin();
            bg.render();
            road.render();;
            yellowCar.render();
            redCar.render();
            blueCar.render();
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

        }
    }
