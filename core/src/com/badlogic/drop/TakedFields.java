package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class TakedFields {
    private Array<Puzzle> takedLine;
    private int fieldstype;
    private int restrictedtype;
    private Array<Texture> puzzleImages;

    public TakedFields(Array<Texture> images) {
        this.takedLine = new Array<Puzzle>();
        this.fieldstype = -1;
        this.restrictedtype = -1;
        this.puzzleImages = images;
    }

    public void insertTouchedFields(Puzzle touchedItem, TakedFields opponentFields){
        int touchedType = 0;
        Array<Puzzle> temp = new Array<Puzzle>();
        if(touchedItem != null){
            touchedType = touchedItem.getPuzzleType();
            // invert taked

            //check that it doesn't cross with another player selected type
            if(touchedType == restrictedtype) return;

            invertType(touchedType);
            fieldstype = touchedType;
            opponentFields.setRestrictedtype(touchedType);

            for(Puzzle takedItem: takedLine) {

                takedItem.takeNeighbors(touchedType, temp);
            }
        }

        if(!temp.isEmpty()) {
            for (Puzzle elem : temp) {
                if (!inTakedLine(elem)) {
                    takedLine.add(elem);
                }
            }
        }
        else{
            //block only for addition of first corner elements for each player
            if(touchedItem != null)
                if(takedLine.size == 0)
                    takedLine.add(touchedItem);
        }
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

    public int getFieldstype() {
        return fieldstype;
    }

    public void setFieldstype(int fieldstype) {
        this.fieldstype = fieldstype;
    }

    public int getRestrictedtype() {
        return restrictedtype;
    }

    public void setRestrictedtype(int restrictedtype) {
        this.restrictedtype = restrictedtype;
    }

    public void drawAll(SpriteBatch batch) {
        for(Puzzle puzz: takedLine) {
            puzz.drawPuzzle(batch);
        }
    }

    public int getTouchedCount(){
        return takedLine.size;
    }
}
