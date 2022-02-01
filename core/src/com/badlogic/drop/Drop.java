package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

public class Drop extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private Rectangle bucket;
	private Array<Rectangle> raindrops;
	//private Array<Puzzle> puzzles;
	private PuzzleOperator puzzles;
	private long lastDropTime;
	private String counterTxt;
	private int counter;
	Puzzle touchedPuzz;
	private BitmapFont font;
	private int crossSize;
	private int screenWidth;
	private int screenHeight;
	private LeftMenu lMenu;
	private RightMenu rMenu;
	private Array<Texture> imageTypes;
	public SettingsScreen settings;
	private  Preferences prefs;

	@Override
	public void create () {
		//init puzz
        crossSize = 15;
		touchedPuzz = null;

		//init screen size
		screenWidth = 1200;
		screenHeight = 800;

		//init counter in right top corner
		counterTxt = new String("0");
		counter = 0;

		font = new BitmapFont();

		//load images
		imageTypes = new Array<Texture>();
		initTextures();

		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start the playback of the background music immediately
		rainMusic.setLooping(true);
		//rainMusic.play();

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);
		batch = new SpriteBatch();


		//create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.x = screenWidth / 2 - 64 / 2; // center the bucket horizontally
		bucket.y = 20; // bottom left corner of the bucket is 20 pixels above the bottom screen edge
		bucket.width = 64;
		bucket.height = 64;

		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		//spawnRaindrop();

		//init left menu
		lMenu = new LeftMenu(imageTypes);

		//init right menu
		rMenu = new RightMenu(imageTypes);

		//init cross panel
		puzzles = new PuzzleOperator();
		puzzles.init(crossSize, imageTypes, screenHeight - 200, screenWidth - 400);
	}

	@Override
	public void render () {
		//ScreenUtils.clear(1, 0, 0, 1);

		// clear the screen with a dark blue color. The
		// arguments to clear are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		ScreenUtils.clear(255, 255, 255, 1);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		batch.begin();

		//draw a cross lines grid
		puzzles.drawAll(batch);

		//draw menu
		lMenu.drawMenu(batch);
		rMenu.drawMenu(batch);

		//batch.draw(bucketImage, bucket.x, bucket.y);
		//for(Rectangle raindrop: raindrops) {
			//batch.draw(dropImage, raindrop.x, raindrop.y);

		//}

		//draw a counter info
		//counterTxt = "Score: "+ Integer.toString(counter);
		//font.draw(batch, counterTxt, 10*2f, screenHeight-0.5f*10);

		//draw number of touched puzzle
		//drawTouchedInfo();

		batch.end();

		// process user input
		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;   //move bucket horizontally
			bucket.y = touchPos.y - 64 / 2;   //move bucket vertically

			//get num of touched puzzle
			touchedPuzz = null;
			touchedPuzz = puzzles.findTouchedPuzzle((int)touchPos.x, (int)touchPos.y);

			//get touched menu elem
			if(touchedPuzz == null)
				touchedPuzz = lMenu.findTouchedMenu((int)touchPos.x, (int)touchPos.y);

			if(touchedPuzz == null)
				touchedPuzz = rMenu.findTouchedMenu((int)touchPos.x, (int)touchPos.y);

		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		if(Gdx.input.isKeyPressed(Input.Keys.UP)) bucket.y += 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) bucket.y -= 200 * Gdx.graphics.getDeltaTime();


		// make sure the bucket stays within the screen bounds
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > screenWidth - 64) bucket.x = screenWidth - 64;

		if(bucket.y < 0) bucket.y = 0;
		if(bucket.y > screenHeight - 64) bucket.y = screenHeight - 64;

		//take neughbors
		puzzles.takeNeighbors(touchedPuzz, 0);

		// check if we need to create a new raindrop
		//if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the latter case we play back
		// a sound effect as well.
		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) iter.remove();
			if(raindrop.overlaps(bucket)) {
				counter = counter+1;
				dropSound.play();
				iter.remove();
			}
		}
	}
	
	@Override
	public void dispose () {
		//img.dispose();
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, screenWidth-64);
		raindrop.y = screenHeight;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	private void drawTouchedInfo(){
		if(touchedPuzz != null){
			String touchedNumberStr = "Touched cell: " + touchedPuzz.getPuzzleNum();
			Puzzle leftN = touchedPuzz.getLeftNeighbor();
			String touchedPuzzLeftNeighBor = "Left neighbor: null";
			if(leftN != null)
				touchedPuzzLeftNeighBor = "Left neighbor: " + leftN.getPuzzleNum();

			Puzzle rightN = touchedPuzz.getRightNeighbor();
			String touchedPuzzRNeighBor = "Right neighbor: null";
			if(rightN != null)
				touchedPuzzRNeighBor = "Right neighbor: " + rightN.getPuzzleNum();

			Puzzle upN = touchedPuzz.getUpNeighbor();
			String touchedPuzzUpNeighBor = "Up neighbor: null";
			if(upN != null)
				touchedPuzzUpNeighBor = "Up neighbor: " + upN.getPuzzleNum();

			Puzzle downN = touchedPuzz.getDownNeighbor();
			String touchedPuzzDownNeighBor = "Down neighbor: null";
			if(downN != null)
				touchedPuzzDownNeighBor = "Down neighbor: " + downN.getPuzzleNum();


			font.draw(batch, touchedNumberStr, 300*2f, screenHeight-0.5f*10);
			font.draw(batch, touchedPuzzLeftNeighBor, 300*2f, screenHeight-0.5f*50);
			font.draw(batch, touchedPuzzRNeighBor, 300*2f, screenHeight-0.5f*90);
			font.draw(batch, touchedPuzzUpNeighBor, 300*2f, 480-0.5f*130);
			font.draw(batch, touchedPuzzDownNeighBor, 300*2f, screenHeight-0.5f*170);
		}
	}

	public void initTextures(){

		imageTypes.add(new Texture(Gdx.files.internal("air.png")));
		imageTypes.add(new Texture(Gdx.files.internal("earth.png")));
		imageTypes.add(new Texture(Gdx.files.internal("water.png")));
		imageTypes.add(new Texture(Gdx.files.internal("flame.png")));
		imageTypes.add(new Texture(Gdx.files.internal("life.png")));
	}

	public Preferences  getPrefs(){
		return prefs;
	}
}
