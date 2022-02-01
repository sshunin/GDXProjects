package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class PuzzleOperator {
    private Array<Puzzle> puzzles;
    private int screenWidth;
    private int screenHeight;
    private TakedFields firstPlayerItems;
    private TakedFields secondPlayerItems;
    private int turnOrder;
    private Array<Puzzle> takedLine;
    private Array<Texture> puzzleImages;
    private BitmapFont font;

    public void init(int Amount, Array<Texture> images, int screenH, int screenW){
        font = new BitmapFont();
        puzzles = new Array<Puzzle>();

        //set turn for first player
        turnOrder = 1;

        //init taked items for players
        firstPlayerItems = new TakedFields(images);
        secondPlayerItems = new TakedFields(images);

        //touched puzlles
        takedLine = new Array<Puzzle>();

        //load images
        puzzleImages = images;

        //default screen size
        this.screenWidth = screenW;
        this.screenHeight = screenH;
        //randomizer
        final Random random = new Random();

        int num = 0;
        int firstPlayerType = -1;
        int secondPlayerType = -1;

        for(int i = 0; i < Amount; i++) {
            for(int j = 0; j < Amount; j++) {
                Puzzle crossPuzzle = new Puzzle();

                crossPuzzle.init();
                crossPuzzle.setPuzzleNum(num);
                crossPuzzle.width = screenWidth/Amount;
                crossPuzzle.height = screenHeight/Amount;

                crossPuzzle.x = i * crossPuzzle.width + 200;
                crossPuzzle.y = j *crossPuzzle.height + 50;
                //set random type of element
                crossPuzzle.setPuzzleType(random.nextInt(5));
                setTexture(crossPuzzle);

                puzzles.add(crossPuzzle);

                if( i == 0 && j ==0) {
                    firstPlayerType = crossPuzzle.getPuzzleType();
                    //takedLine.add(crossPuzzle);
                    firstPlayerItems.setFieldstype(firstPlayerType);
                    secondPlayerItems.setRestrictedtype(firstPlayerType);
                    firstPlayerItems.insertTouchedFields(crossPuzzle, secondPlayerItems);
                }

                if(i == (Amount-1) && (j == Amount-1)){
                    secondPlayerType = crossPuzzle.getPuzzleType();

                    //check that head elements of players have different types
                    while (secondPlayerType == firstPlayerType) {
                        secondPlayerType = random.nextInt(5);
                    }

                    crossPuzzle.setPuzzleType(secondPlayerType);
                    setTexture(crossPuzzle);
                    firstPlayerItems.setRestrictedtype(secondPlayerType);
                    secondPlayerItems.setFieldstype(secondPlayerType);
                    secondPlayerItems.insertTouchedFields(crossPuzzle, firstPlayerItems);


                }

                num = num + 1;

                //set neighbors
                if(j > 0) {
                    Puzzle leftNeighbor = findPuzzleByNumber((j-1)+i*Amount);
                    crossPuzzle.setLeftNeighbor(leftNeighbor);
                    if(leftNeighbor != null)
                        leftNeighbor.setRightNeighbor(crossPuzzle);
                }
                if(i >0){
                    Puzzle upNeighbor = findPuzzleByNumber(j+(i-1)*Amount);
                    crossPuzzle.setUpNeighbor(upNeighbor);
                    if(upNeighbor != null)
                        upNeighbor.setDownNeighbor(crossPuzzle);
                }
            }
        }
    }

    public void setTexture(Puzzle crossPuzzle) {
        int type = crossPuzzle.getPuzzleType();
        switch (type){
            case 0:
                crossPuzzle.setTexture(puzzleImages.get(0), type);
                break;
            case 1:
                crossPuzzle.setTexture(puzzleImages.get(1), type);
                break;
            case 2:
                crossPuzzle.setTexture(puzzleImages.get(2), type);
                break;
            case 3:
                crossPuzzle.setTexture(puzzleImages.get(3), type);
                break;
            case 4:
                crossPuzzle.setTexture(puzzleImages.get(4), type);
                break;
        }

    }
    public void takeNeighbors(Puzzle touched, int restrictionsType){
        int touchedType = 0;
        if(touched !=null) {
            if (turnOrder == 1) {
                if(touched.getPuzzleType() != firstPlayerItems.getRestrictedtype()) {
                    firstPlayerItems.insertTouchedFields(touched, secondPlayerItems);
                    turnOrder = 2;
                }
            } else {
                if(touched.getPuzzleType() != secondPlayerItems.getRestrictedtype()) {
                    secondPlayerItems.insertTouchedFields(touched, firstPlayerItems);
                    turnOrder = 1;
                }
            }
        }


    }

    public Puzzle findPuzzleByNumber(int num) {
        for(Puzzle puzz: puzzles)
        {
            if(puzz.getPuzzleNum() == num)
                return puzz;
        }

        return null;
    }

    public void drawAll(SpriteBatch batch) {
        for(Puzzle puzz: puzzles) {
            puzz.drawPuzzle(batch);
        }

        firstPlayerItems.drawAll(batch);
        secondPlayerItems.drawAll(batch);

        //draw count
        String counterTxtOne = "Score of Player One: "+ Integer.toString(firstPlayerItems.getTouchedCount());
        String counterTxtTwo = "Score of Player Two: "+ Integer.toString(secondPlayerItems.getTouchedCount());

        font.setColor(0f,0f,0f, 1);
        font.draw(batch, counterTxtOne, 200, screenHeight + 100);
        font.draw(batch, counterTxtTwo, screenWidth, screenHeight + 100);

    }

    public Puzzle findTouchedPuzzle(int x, int y) {
        for(Puzzle puzz: puzzles) {
            if(x >= puzz.x &&  x <= puzz.x + puzz.getWidth())
            {
                if(y >= puzz.y && y <= puzz.y + puzz.getHeight())
                {
                    return puzz;
                }
            }
        }

        return null;
    }

    public void invertType(int type) {
        for(Puzzle takedItem: takedLine) {
            takedItem.setPuzzleType(type);
            setTexture(takedItem);
        }
    }

    public boolean inTakedLine(Puzzle item){
        for(Puzzle elem: takedLine){
            if(elem == item) {
                return true;
            }
        }

        return false;
    }

}
