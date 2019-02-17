package com.dat055.model.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.dat055.controller.MenuController;

public class PauseMenu extends Menu {
    private MenuController controller;
    String ip;
    TextButton resume, settings, menu;
    public PauseMenu(MenuController ctrl) {
        super();

        controller = ctrl;
        createTable(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/18);
    }

    @Override
    public void createTable(int width, int height) {
        initStyles(height);

        resume = createButton("Resume game");
        settings = createButton("Settings");
        menu = createButton("Main menu");

        addListeners();

        layoutTable(width, height);
    }

    private void layoutTable(int butX, int butY) {
        int padL, padS;
        padL = butX/2;
        padS = butY/2;

        Table table = new Table();
        table.setSize(controller.getWidth(),controller.getWidth());

        table.setPosition(0,0);

        table.padTop(200);
        table.add().width(butX).height(butY).padBottom(padS).expandX().row();
        table.add(settings).width(butX).height(butY).padBottom(padL).row();
        table.add(menu).width(butX).height(butY).padBottom(padL).row();


        super.table = table;
    }

    private void initStyles(int height) {
        initTxtBtnStyle(height);
        initLblStyle(height);
    }

    private void initTxtBtnStyle(int height) {
        TextButton.TextButtonStyle txtBtnStyle = new TextButton.TextButtonStyle();

        Skin skin = new Skin(Gdx.files.internal("UI/ui.json"));
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("UI/ui.atlas"));
        skin.addRegions(atlas);

        txtBtnStyle.font = generateFont(height-Gdx.graphics.getHeight()/50);
        txtBtnStyle.fontColor = Color.BLACK;
        txtBtnStyle.downFontColor = Color.WHITE;

        txtBtnStyle.up = skin.getDrawable("but1_pressed");
        txtBtnStyle.down = skin.getDrawable("but1");

        super.txtBtnStyle = txtBtnStyle;
    }

    private void initLblStyle(int height) {
        Label.LabelStyle lblStyle = new Label.LabelStyle();
        lblStyle.font = generateFont(height-Gdx.graphics.getHeight()/60);
        lblStyle.fontColor = Color.WHITE;
        super.lblStyle = lblStyle;
    }

    public BitmapFont fontPad(BitmapFont f) {
        BitmapFont.BitmapFontData fd = f.getData();
        fd.padLeft = -5;
        fd.padRight = -10;
        return f;
    }

    public String getIp() {
        return ip;
    }

    private void addListeners() {
        resume.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Unpause
                super.touchUp(event, x, y, pointer, button);
            }
        });

        settings.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.swapMenu("Settings");
                super.touchUp(event, x, y, pointer, button);
            }

        });

        menu.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.swapMenu("Main");
                super.touchUp(event, x, y, pointer, button);
            }

        });
    }
}