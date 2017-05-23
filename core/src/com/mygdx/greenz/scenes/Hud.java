package com.mygdx.greenz.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.greenz.MainGame;
import com.mygdx.greenz.sprites.Hero;

/**
 * Created by Lucas on 15/05/2017.
 */
public class Hud {

    public Stage stage;
    public FitViewport viewPort;

    public TextButton buttonLeft;
    public TextButton buttonRigth;
    public TextButton buttonUp;

    private TextButton.TextButtonStyle textButtonStyle;
    private TextButton.TextButtonStyle textButtonStyle2;
    private TextButton.TextButtonStyle textButtonStyle3;

    private Skin skin;
    private BitmapFont font;

    private TextureAtlas textureAtlas;

    private Table table;
    private Table table2;



    public Hud(SpriteBatch sb) {
        viewPort = new FitViewport(MainGame.V_WIDTH, MainGame.V_HEIGHT, new OrthographicCamera());

        stage = new Stage(viewPort, sb);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle2 = new TextButton.TextButtonStyle();
        textButtonStyle3 = new TextButton.TextButtonStyle();

        textureAtlas = new TextureAtlas("hud.pack");

        font = new BitmapFont();
        skin = new Skin(textureAtlas);

        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("arrow_button_active.");
        buttonLeft = new TextButton("", textButtonStyle);

        textButtonStyle2.font = font;
        textButtonStyle2.up = skin.getDrawable("arrow_button_idle.");
        buttonRigth = new TextButton("", textButtonStyle2);

        textButtonStyle3.font = font;
        textButtonStyle3.up = skin.getDrawable("Magic_button_blue");
        buttonUp = new TextButton("", textButtonStyle3);

        table = new Table(skin);
        table2 = new Table(skin);

        table.setPosition(40, 20);
        table2.setPosition(400, 20);

        table.add(buttonLeft).width(40).height(40);
        table.add(buttonRigth).width(40).height(40);

        table2.add(buttonUp).width(40).height(40);

        stage.addActor(table);
        stage.addActor(table2);

        Gdx.input.setInputProcessor(stage);

        buttonLeft.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("izquierdo");
            }
        });
        buttonRigth.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("derecho");
            }
        });
        buttonUp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("arriba");
            }
        });
    }
}
