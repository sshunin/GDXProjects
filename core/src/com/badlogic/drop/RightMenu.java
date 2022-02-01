package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class RightMenu {
    private Array<Puzzle> menuItems ;
    private int ScreenWidth = 1200;
    private int ScreenHeight = 800;

    public RightMenu(Array<Texture> menuImages){
        this.menuItems = new Array<Puzzle>();
        initMenu(menuImages);
    }

    public void drawMenu(SpriteBatch batch){
        for(Puzzle item: menuItems){
            item.drawPuzzle(batch);
        }
    }

    private void initMenu(Array<Texture> menuImages){
        int iter = 0;
        for(Texture item: menuImages){
            Puzzle puzzle = new Puzzle();

            puzzle.height = item.getHeight();
            puzzle.width = item.getWidth();

            puzzle.x = ScreenWidth - puzzle.getWidth() - 70;
            puzzle.y = iter* puzzle.getHeight() + 150;

            puzzle.setPuzzleType(iter);
            puzzle.setTexture(item, iter);
            menuItems.add(puzzle);
            iter++;
        }
    }

    public Puzzle findTouchedMenu(int x, int y) {
        for(Puzzle menuElem: menuItems) {
            if(x >= menuElem.x &&  x <= menuElem.x + menuElem.getWidth())
            {
                if(y >= menuElem.y && y <= menuElem.y + menuElem.getHeight())
                {
                    return menuElem;
                }
            }
        }

        return null;
    }

    public void setResolution(int width, int height){
        ScreenWidth = width;
        ScreenHeight = height;
    }
}
