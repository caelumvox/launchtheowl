package com.acervusltd.launchtheowl;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class LaunchTheOwlGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private Texture owlImage;
	private Rectangle owl;
	private Music journeyMusic;

	public static final float SCREEN_WIDTH = 800;
	public static final float SCREEN_HEIGHT = 480;

	@Override
	public void create () {
		batch = new SpriteBatch();
		owlImage = new Texture("owl.png");

		journeyMusic = Gdx.audio.newMusic(Gdx.files.internal("journey.mp3"));
		journeyMusic.setLooping(true);
		journeyMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		batch = new SpriteBatch();

		owl = new Rectangle();
		owl.x = SCREEN_WIDTH/2 - owlImage.getWidth()/2;
		owl.y = 20;
		owl.width = owlImage.getWidth();
		owl.height = owlImage.getHeight();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(owlImage, owl.getX(), owl.getY());
		batch.end();

		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			owl.x = touchPos.x - owlImage.getWidth() / 2;
			owl.y = touchPos.y - owlImage.getHeight() / 2;
		}

		if(owl.x < 0) owl.x = 0;
		if(owl.x > SCREEN_WIDTH - owlImage.getWidth()) owl.x = SCREEN_WIDTH - owlImage.getWidth();

		if(owl.y < 0) owl.y = 0;
		if(owl.y > SCREEN_HEIGHT - owlImage.getHeight()) owl.y = SCREEN_HEIGHT - owlImage.getHeight();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		owlImage.dispose();
		journeyMusic.dispose();
	}
}
