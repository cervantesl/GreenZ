package com.mygdx.greenz.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.greenz.MainGame;
import com.mygdx.greenz.scenes.Hud;
import com.mygdx.greenz.sprites.Enemy;
import com.mygdx.greenz.sprites.Hero;
import com.mygdx.greenz.tools.B2WorldCreator;

import java.util.ArrayList;

/**
 * Created by Lucas on 12/05/2017.
 */
public class GameScreen extends com.mygdx.greenz.screens.BaseScreen {

    private OrthographicCamera camera;
    private FitViewport gamePort;

    private Hud hud;

    private Hero hero;

    private ArrayList<Enemy> enemies;

    private TextureAtlas atlas;

    //TILED
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //BOX2D
    private Box2DDebugRenderer box2DDebugRenderer;
    private World world;

    private B2WorldCreator b2WorldCreator;


    public GameScreen(MainGame mainGame) {
        super(mainGame);

    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        gamePort = new FitViewport(MainGame.V_WIDTH / MainGame.PPM, MainGame.V_HEIGHT / MainGame.PPM, camera);

        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        map = new TiledMap();
        mapLoader = new TmxMapLoader();

        map = mapLoader.load("level1_p2.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / MainGame.PPM);

        box2DDebugRenderer = new Box2DDebugRenderer();

        world = new World(new Vector2(0, -10), true);

        b2WorldCreator = new B2WorldCreator(world, map);

        hud = new Hud(mainGame.batch);

        atlas = new TextureAtlas("personajes.pack");

        hero = new Hero(world, this);

        enemies = new ArrayList<Enemy>();

        enemies.add(new Enemy(world, this, 3, .64f));
        enemies.add(new Enemy(world, this, 6.2f, .80f));
        enemies.add(new Enemy(world, this, 10, .64f));
        enemies.add(new Enemy(world, this, 30, .64f));
        enemies.add(new Enemy(world, this, 33, .64f));
        enemies.add(new Enemy(world, this, 36, .64f));

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                //CONTACTO SUELO Y HERO
                for (int i = 0; i < b2WorldCreator.fixture.length;i++) {
                    if(fixtureA == hero.fixture && fixtureB == b2WorldCreator.fixture[i]) {
                        System.out.println("contacto");
                        hero.puedeSaltar = true;
                    }
                    if(fixtureB == hero.fixture && fixtureA == b2WorldCreator.fixture[i]) {
                        System.out.println("contacto");
                        hero.puedeSaltar = true;
                    }
                }

                //CONTACTO AGUA Y HERO
                for (int i = 0; i < b2WorldCreator.fixtureMuerte.length;i++) {
                    if(fixtureA == hero.fixture && fixtureB == b2WorldCreator.fixtureMuerte[i]) {
                        mainGame.setScreen(new GameOverScreen(mainGame));
                    }
                    if(fixtureB == hero.fixture && fixtureA == b2WorldCreator.fixtureMuerte[i]) {
                        mainGame.setScreen(new GameOverScreen(mainGame));
                    }
                }

                for (Enemy enemy : enemies) {
                    //CONTACTO CABEZA ENEMIGO Y HERO
                    if(fixtureA == hero.fixture && fixtureB == enemy.fixture) {
                        enemy.muerto = true;
                        System.out.println("contacto cabeza");
                    }
                    if(fixtureB == hero.fixture && fixtureA == enemy.fixture) {
                        enemy.muerto = true;
                        System.out.println("contacto cabeza");
                    }

                    if(!enemy.muerto) {
                        //CONTACTO ENEMIGO Y HERO
                        if(fixtureA == hero.fixture && fixtureB == enemy.fixtureBody) {
                            System.out.println("contacto enemigo");

                            mainGame.setScreen(new GameOverScreen(mainGame));

                        }
                        if(fixtureB == hero.fixture && fixtureA == enemy.fixtureBody) {
                            System.out.println("contacto enemigo");
                            mainGame.setScreen(new GameOverScreen(mainGame));
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                //CONTACTO SUELO Y HERO
                for (int i = 0; i < b2WorldCreator.fixture.length;i++) {
                    if(fixtureA == hero.fixture && fixtureB == b2WorldCreator.fixture[i]) {
                        System.out.println("contacto");
                        hero.puedeSaltar = false;
                    }
                    if(fixtureB == hero.fixture && fixtureA == b2WorldCreator.fixture[i]) {
                        System.out.println("contacto");
                        hero.puedeSaltar = false;
                    }
                }

                for (Enemy enemy : enemies) {
                    //CONTACTO CABEZA ENEMIGO Y HERO
                    if(fixtureA == hero.fixture && fixtureB == enemy.fixture) {
                        enemy.muerto = true;
                        System.out.println("contacto cabeza");
                    }
                    if(fixtureB == hero.fixture && fixtureA == enemy.fixture) {
                        enemy.muerto = true;
                        System.out.println("contacto cabeza");
                    }

                    if(!enemy.muerto) {
                        //CONTACTO ENEMIGO Y HERO
                        if(fixtureA == hero.fixture && fixtureB == enemy.fixtureBody) {
                            System.out.println("contacto enemigo");
                        }
                        if(fixtureB == hero.fixture && fixtureA == enemy.fixtureBody) {
                            System.out.println("contacto enemigo");
                        }
                    }
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);

        hero.update(delta);

        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }

        if(hero.b2body.getPosition().x > 2.4f && hero.b2body.getPosition().x < 35) {
            camera.position.x = hero.b2body.getPosition().x;
        }


        camera.update();
        renderer.setView(camera);

        hud.stage.act(delta);

        //MOBILE
        if (hud.buttonRigth.isPressed() && hero.b2body.getLinearVelocity().x <= 2) {
            hero.b2body.applyLinearImpulse(new Vector2(0.1f, 0), hero.b2body.getWorldCenter(), true);
        }
        if (hud.buttonLeft.isPressed() && hero.b2body.getLinearVelocity().x >= -2) {
            hero.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), hero.b2body.getWorldCenter(), true);
        }

        if (hud.buttonUp.isPressed() && hero.puedeSaltar) {
            hero.b2body.applyForce(0, 235, hero.b2body.getPosition().x, hero.b2body.getPosition().y, true);
            hero.puedeSaltar = false;
        }

        //PC
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && hero.b2body.getLinearVelocity().x <= 2) {
            hero.b2body.applyLinearImpulse(new Vector2(0.1f, 0), hero.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && hero.b2body.getLinearVelocity().x >= -2) {
            hero.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), hero.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && hero.puedeSaltar) { //añadir boolean si esta en el suelo
            hero.b2body.applyForce(0, 235, hero.b2body.getPosition().x, hero.b2body.getPosition().y, true);
            hero.puedeSaltar = false;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.graphics.setTitle("FPS: " + Gdx.graphics.getFramesPerSecond());

        update(delta);

        renderer.render();

        box2DDebugRenderer.render(world, camera.combined);

        mainGame.batch.setProjectionMatrix(camera.combined);

        mainGame.batch.begin();
        hero.draw(mainGame.batch);

        for(Enemy enemy : enemies) {
            enemy.draw(mainGame.batch);
        }

        mainGame.batch.end();

        mainGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        hud.viewPort.update(width, height);
        camera.update();
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
        renderer.dispose();
        map.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
    }

}
