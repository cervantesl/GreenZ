package com.mygdx.greenz.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
 * Created by Lucas on 16/05/2017.
 */
public class Hero extends Sprite {

    public enum State{SALTANDO, ENPIE, CORRIENDO};

    public State estadoActual;
    public State estadoPrevio;

    private Animation heroRun;
    private Animation heroJump;

    private float tiempoEstado;

    public boolean corriendoDerecha = false;

    public World world;
    public Body b2body;

    private TextureRegion region;

    public boolean puedeSaltar = true;

    public Fixture fixture;

    public Hero(World world, GameScreen gameScreen) {
        super(gameScreen.getAtlas().findRegion("characters"));
        this.world = world;

        defineHero();

        region = new TextureRegion(getTexture(), 0, 82, 32, 32);

        setBounds(0, 0, 32 / MainGame.PPM, 32 / MainGame.PPM);
        setRegion(region);

        estadoActual = State.ENPIE;
        estadoPrevio = State.ENPIE;
        tiempoEstado = 0;
        corriendoDerecha = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 1; i < 8; i++) {
            frames.add(new TextureRegion(getTexture(), i * 32, 82, 32, 32));
        }
        heroRun = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 6; i < 8; i++) {
            frames.add(new TextureRegion(getTexture(), i * 32, 82, 32, 32));
        }
        heroJump = new Animation(0.2f, frames);
        frames.clear();

    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2) + 0.08f);
        setRegion(getFrame(dt));
    }

    public State getState() {

        if(b2body.getLinearVelocity().x >= 0.5 && b2body.getLinearVelocity().y == 0) {
            corriendoDerecha = true;
            return State.CORRIENDO;
        }
        else if(b2body.getLinearVelocity().x <= -0.5 && b2body.getLinearVelocity().y == 0) {
            corriendoDerecha = false;
            return State.CORRIENDO;
        }
        else if(b2body.getLinearVelocity().y > 0) {
            return State.SALTANDO;
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

            case SALTANDO:
                region = (TextureRegion) heroJump.getKeyFrame(tiempoEstado, false);
                break;

            default:
                region = this.region;
        }

        if(b2body.getLinearVelocity().x <= -0.5 && !corriendoDerecha && !region.isFlipX()) {
            corriendoDerecha = false;
            region.flip(true, false);
        } else if(b2body.getLinearVelocity().x >= 0.5 && corriendoDerecha && region.isFlipX()) {
            region.flip(true, false);
            corriendoDerecha = true;
        }

        tiempoEstado = estadoActual == estadoPrevio ? tiempoEstado + dt : 0;
        estadoPrevio = estadoActual;

        return region;
    }

    private void defineHero() {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(64 / MainGame.PPM, 64 / MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5 / MainGame.PPM, 6 / MainGame.PPM);

        fixtureDef.shape = shape;
        fixture = b2body.createFixture(fixtureDef);

        fixture.setUserData("player");
    }
}
