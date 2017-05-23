package com.mygdx.greenz.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.greenz.MainGame;

/**
 * Created by Lucas on 27/04/2017.
 */
public class B2WorldCreator {

    public Fixture[] fixture;

    public Fixture[] fixtureMuerte;

    public Fixture[] fixturePared;

    public B2WorldCreator(World world, TiledMap map) {

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body = null;
        int i = 1;

        fixture = new Fixture[map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class).size + 1];
        fixtureMuerte = new Fixture[map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class).size + 1];
        fixturePared = new Fixture[map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class).size + 1];

        for (MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox((rect.getWidth() / 2) / MainGame.PPM, (rect.getHeight() / 2) / MainGame.PPM);
            fixtureDef.shape = shape;

            fixture[i]= body.createFixture(fixtureDef);
            fixture[i].setUserData("solidos");

            i++;
        }

        int j = 0;

        for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox((rect.getWidth() / 2) / MainGame.PPM, (rect.getHeight() / 2) / MainGame.PPM);
            fixtureDef.shape = shape;

            fixtureMuerte[j]= body.createFixture(fixtureDef);
            fixtureMuerte[j].setUserData("muertos");

            j++;
        }

        int k = 0;

        for (MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox((rect.getWidth() / 2) / MainGame.PPM, (rect.getHeight() / 2) / MainGame.PPM);
            fixtureDef.shape = shape;

            fixturePared[k]= body.createFixture(fixtureDef);
            fixturePared[k].setUserData("pared");

            k++;
        }
    }
}
