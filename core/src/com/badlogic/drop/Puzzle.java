package com.badlogic.drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Puzzle extends Rectangle {
    private int puzzleType;
    private int puzzleNum;
    private Texture puzzleImageType1 = null;
    private Texture puzzleImageType2 = null;
    private Texture puzzleImageType3 = null;
    private Texture puzzleImageType4 = null;
    private Texture puzzleImageType5 = null;

    private Puzzle leftNeighbor;
    private Puzzle rightNeighbor;
    private Puzzle downNeighbor;
    private Puzzle upNeighbor;


    public void init(){
        puzzleType = 0;
        puzzleNum = 0;

        leftNeighbor = null;
        rightNeighbor = null;
        downNeighbor = null;
        upNeighbor = null;
    }

    public int getPuzzleType() {
        return puzzleType;
    }

    public void setPuzzleType(int puzzleType) {
        this.puzzleType = puzzleType;
    }

    public void drawPuzzle(SpriteBatch batch){
        switch (this.puzzleType) {
            case 0:
                batch.draw(puzzleImageType1, this.x, this.y);
                break;
            case 1:
                batch.draw(puzzleImageType2, this.x, this.y);
                break;
            case 2:
                batch.draw(puzzleImageType3, this.x, this.y);
                break;
            case 3:
                batch.draw(puzzleImageType4, this.x, this.y);
                break;
            case 4:
                batch.draw(puzzleImageType5, this.x, this.y);
                break;
        }
    }

    public void setTexture(Texture image, int type){
        switch (type) {
            case 0: {
                if(puzzleImageType1 == null) puzzleImageType1 = image;
                break;
            }
            case 1:{
                if(puzzleImageType2 == null) puzzleImageType2 = image;
                break;
            }
            case 2:{
                if(puzzleImageType3 == null) puzzleImageType3 = image;
                break;
            }
            case 3:{
                if(puzzleImageType4 == null) puzzleImageType4 = image;
                break;
            }
            case 4: {
                if(puzzleImageType5 == null) puzzleImageType5 = image;
                break;
            }
        }
    }

    public int getPuzzleNum() {
        return puzzleNum;
    }

    public void setPuzzleNum(int puzzleNum) {
        this.puzzleNum = puzzleNum;
    }

    public Puzzle getDownNeighbor() {
        return downNeighbor;
    }

    public void setDownNeighbor(Puzzle downNeighbor) {
        this.downNeighbor = downNeighbor;
    }

    public Puzzle getUpNeighbor() {
        return upNeighbor;
    }

    public void setUpNeighbor(Puzzle upNeighbor) {
        this.upNeighbor = upNeighbor;
    }

    public Puzzle getLeftNeighbor() {
        return leftNeighbor;
    }

    public void setLeftNeighbor(Puzzle leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    public Puzzle getRightNeighbor() {
        return rightNeighbor;
    }

    public void setRightNeighbor(Puzzle rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }

    public void dispose () {
        puzzleImageType1.dispose();
        puzzleImageType2.dispose();
        puzzleImageType3.dispose();
        puzzleImageType4.dispose();
        puzzleImageType5.dispose();
    }

    public void takeNeighbors(int touchedType, Array<Puzzle> insertedItems){
        Array<Puzzle> temp = new Array<Puzzle>();
        if (this.getLeftNeighbor() != null) {
            if(!insertedItems.contains(this.getLeftNeighbor(), true))
            if (this.getLeftNeighbor().getPuzzleType() == touchedType) {
                insertedItems.add(this.getLeftNeighbor());
                this.getLeftNeighbor().takeNeighbors(touchedType, insertedItems);
            }
        }
        if (this.getRightNeighbor() != null) {
            if(!insertedItems.contains(this.getRightNeighbor(), true))
            if (this.getRightNeighbor().getPuzzleType() == touchedType) {
                insertedItems.add(this.getRightNeighbor());
                this.getRightNeighbor().takeNeighbors(touchedType, insertedItems);
            }
        }
        if (this.getUpNeighbor() != null) {
            if(!insertedItems.contains(this.getUpNeighbor(), true))
            if (this.getUpNeighbor().getPuzzleType() == touchedType) {
                insertedItems.add(this.getUpNeighbor());
                this.getUpNeighbor().takeNeighbors(touchedType, insertedItems);
            }
        }
        if (this.getDownNeighbor() != null) {
            if(!insertedItems.contains(this.getDownNeighbor(), true))
            if (this.getDownNeighbor().getPuzzleType() == touchedType) {
                insertedItems.add(this.getDownNeighbor());
                this.getDownNeighbor().takeNeighbors(touchedType, insertedItems);

            }
        }
    }
}
