package com.acervusltd.launchtheowl;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;

public class LaunchTheOwlGame extends ApplicationAdapter {
	// Viewport
	private OrthographicCamera camera;
	private Group group;
	public static final float SCREEN_WIDTH = 800;
	public static final float SCREEN_HEIGHT = 480;
	private SpriteBatch batch;

	// Owl stuff
	private Texture owlImage;
	private Sprite owlSprite;
	private Rectangle owl;
	private boolean inFlight;

	// Nest stuff
	private Texture nestImage;
	private Rectangle nest;

	// Background music
	private Music journeyMusic;

	// Coin animation
	private Rectangle coin;
	private static final int        FRAME_COLS = 4;
	private static final int        FRAME_ROWS = 1;
	private Animation coinAnimation;
	private Texture coinSheet;
	private TextureRegion[] coinFrames;
	private TextureRegion currentFrame;
	private float stateTime;
	private int coinFrameWidth;
	private int coinFrameHeight;

	// Coin sound
	private Sound coinSound;

	@Override
	public void create () {
		batch = new SpriteBatch();
		journeyMusic = Gdx.audio.newMusic(Gdx.files.internal("journey.mp3"));
		coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));

		// Owl settings
		owlImage = new Texture("owl.png");
		owlSprite = new Sprite(owlImage);
		owlSprite.rotate(45);
		inFlight = false;

		nestImage = new Texture("nest.png");

		journeyMusic.setLooping(true);
		journeyMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		batch = new SpriteBatch();

		owl = new Rectangle();
		owl.setX(SCREEN_WIDTH/4 - owlSprite.getWidth()/2);
		owl.setWidth(owlSprite.getWidth());
		owl.setHeight(owlSprite.getHeight());

		nest = new Rectangle();
		nest.setX(3*SCREEN_WIDTH/4 - nestImage.getWidth()/2);

		// Coin setup
		coinSheet = new Texture(Gdx.files.internal("coin.png"));
		coinFrameWidth = coinSheet.getWidth()/FRAME_COLS;
		coinFrameHeight = coinSheet.getHeight()/FRAME_ROWS;
		TextureRegion[][] tmp = TextureRegion.split(coinSheet, coinFrameWidth, coinFrameHeight);
		coinFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				coinFrames[index++] = tmp[i][j];
			}
		}
		coinAnimation = new Animation(0.1f, coinFrames);
		stateTime = 0f;

		resetActors();
	}

	private void resetActors() {
		float initialY = (float) Math.random()*SCREEN_HEIGHT;
		// Do not clip sprites at bottom
		if (initialY < 40) {
			initialY = 40;
		}
		// Do not clip sprites at top
		else if (initialY > SCREEN_HEIGHT - 40) {
			initialY = SCREEN_HEIGHT - 40;
		}

		owl.setX(SCREEN_WIDTH/4 - owlSprite.getWidth()/2);
		owl.setY(initialY - owlSprite.getHeight()/2);
		nest.setY(initialY - nestImage.getHeight()/2);

		coin = new Rectangle();
		coin.setX(SCREEN_WIDTH/2 - coinFrameWidth/2);
		coin.setY(initialY - coinFrameHeight/2);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = coinAnimation.getKeyFrame(stateTime, true);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(owlSprite, owl.getX(), owl.getY());
		batch.draw(nestImage, nest.getX(), nest.getY());
		if (coin != null) {
			batch.draw(currentFrame, coin.getX(), coin.getY());
		}
		batch.end();

		if(Gdx.input.isTouched() && inFlight == false) {
			inFlight = true;
		} else if (inFlight == true) {
			owl.setX(owl.getX() + 2000 * Gdx.graphics.getDeltaTime());
			if (owl.getX() >= nest.getX()) {
				resetActors();
				inFlight = false;
			}
		}

		if (coin != null && coin.overlaps(owl)) {
			coinSound.play();
			coin = null;
		}

		/*
		if(owl.getX() < 0)
			owl.x = 0;
		if(owl.getX() > SCREEN_WIDTH - owlSprite.getWidth())
			owl.x = SCREEN_WIDTH - owlSprite.getWidth();

		if(owl.getY() < 0)
			owl.y = 0;
		if(owl.getY() > SCREEN_HEIGHT - owlSprite.getHeight())
			owl.y = SCREEN_HEIGHT - owlSprite.getHeight();
			*/
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		owlImage.dispose();
		journeyMusic.dispose();
	}
}
