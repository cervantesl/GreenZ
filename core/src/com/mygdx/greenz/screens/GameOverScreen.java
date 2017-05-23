package com.mygdx.greenz.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.greenz.MainGame;

/**
 * Created by Lucas on 22/05/2017.
 */
public class GameOverScreen extends BaseScreen {

    private Stage stage;

    private TextButton retry, menu;
    private TextButton.TextButtonStyle textButtonStyle;

    private TextureAtlas textureAtlas;

    private Skin skin;

    private BitmapFont font;

    public GameOverScreen(final MainGame mainGame) {
        super(mainGame);

        stage = new Stage(new FitViewport(640, 360));

        textureAtlas = new TextureAtlas("hud.pack");

        font = new BitmapFont();

        textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.font = font;

        skin = new Skin(textureAtlas);

        textButtonStyle.up = skin.getDrawable("arrow_button_active.");

        retry = new TextButton("Retry", textButtonStyle);

        retry.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                // Here I go to the game screen again.
                mainGame.setScreen(new GameScreen(mainGame));
            }
        });

        retry.setPosition(60, 50);
        stage.addActor(retry);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}
