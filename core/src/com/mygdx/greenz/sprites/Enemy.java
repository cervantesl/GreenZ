package com.mygdx.greenz.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.greenz.MainGame;
import com.mygdx.greenz.screens.GameScreen;

/**
 * Created by Lucas on 19/05/2017.
 */
public class Enemy extends Sprite {
    public enum State{ENPIE, CORRIENDO};

    public State estadoActual;
    public State estadoPrevio;

    private Animation heroRun;


    private float tiempoEstado;

    public boolean corriendoDerecha = false;

    public World world;
    public Body b2body;

    private TextureRegion region;

    public Fixture fixture;
    public Fixture fixtureBody;

    public boolean muerto = false;
    private boolean destruido = false;

    float posX;
    float posY;

    boolean topeDerecha = false;
    boolean topeIzquierda = true;

    public Enemy(World world, GameScreen gameScreen, float posX, float posY) {
        super(gameScreen.getAtlas().findRegion("characters"));
        this.world = world;

        this.posX = posX;
        this.posY = posY;

        defineEnemy();

        region = new TextureRegion(getTexture(), 786, 38, 38, 35);

        setBounds(0, 0, 24 / MainGame.PPM, 24 / MainGame.PPM);
        setRegion(region);

        estadoActual = State.ENPIE;
        estadoPrevio = State.ENPIE;
        tiempoEstado = 0;
        corriendoDerecha = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(getTexture(), 740, 38, 38, 35));
        frames.add(new TextureRegion(getTexture(), 786, 38, 38, 35));
        frames.add(new TextureRegion(getTexture(), 824, 38, 38, 35));

        heroRun = new Animation(0.2f, frames);
        frames.clear();
    }

    public void update(float dt) {
        if(muerto && !destruido) {
            world.destroyBody(b2body);
            destruido = true;
        } else {
            setPosition(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2) + 0.05f);
            setRegion(getFrame(dt));

            move();
        }
    }

    public State getState() {

        if(b2body.getLinearVelocity().x >= 0.1 && b2body.getLinearVelocity().y == 0) {
            corriendoDerecha = true;
            return State.CORRIENDO;
        }
        else if(b2body.getLinearVelocity().x <= -0.1 && b2body.getLinearVelocity().y == 0) {
            corriendoDerecha = false;
            return State.CORRIENDO;
        }
        else {
            return State.ENPIE;
        }
    }

    public TextureRegion getFrame(float dt) {
        estadoActual = getState();

        TextureRegion region = null;

        switch (estadoActual) {
            case CORRIENDO:
                region = (TextureRegion)heroRun.getKeyFrame(tiempoEstado, true);
                break;

            case ENPIE:
                region = this.region;
                break;

            default:
                region = this.region;
        }

        if(b2body.getLinearVelocity().x <= -0.1 && !corriendoDerecha && !region.isFlipX()) {
            corriendoDerecha = false;
            region.flip(true, false);
        } else if(b2body.getLinearVelocity().x >= 0.1 && corriendoDerecha && region.isFlipX()) {
            region.flip(true, false);
            corriendoDerecha = true;
        }

        tiempoEstado = estadoActual == estadoPrevio ? tiempoEstado + dt : 0;
        estadoPrevio = estadoActual;

        return region;
    }

    private void defineEnemy() {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(posX, posY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5 / MainGame.PPM, 6 / MainGame.PPM);

        fixtureDef.shape = shape;

        fixtureBody = b2body.createFixture(fixtureDef);

        fixtureBody.setUserData("enemy");

        //head
        FixtureDef fixtureDef2 = new FixtureDef();

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5.5f, 12).scl(1 / MainGame.PPM);
        vertice[1] = new Vector2(5.5f, 12).scl(1 / MainGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / MainGame.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / MainGame.PPM);
        head.set(vertice);

        fixtureDef2.shape = head;
        fixtureDef2.restitution = 0.5f;
        fixture = b2body.createFixture(fixtureDef2);
        fixture.setUserData("head");

    }

    private void move() {

        if(b2body.getPosition().x <= posX + 0.9f && !topeDerecha && topeIzquierda) {
            b2body.applyLinearImpulse(new Vector2(0.04f, 0), b2body.getWorldCenter(), true);
        }
        if(b2body.getPosition().x >= posX + 0.8f) {
            topeDerecha = true;
            topeIzquierda = false;
        }

        if(b2body.getPosition().x >= posX && topeDerecha && !topeIzquierda) {
            b2body.applyLinearImpulse(new Vector2(-0.04f, 0), b2body.getWorldCenter(), true);
        }

        if(b2body.getPosition().x <= posX) {
            topeIzquierda = true;
            topeDerecha = false;
        }
    }

    @Override
    public void draw(Batch batch) {
        if(!muerto) {
            super.draw(batch);
        }
    }
}
