package draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by 康力 on 2015/10/22.
 */
public class Score {
    /**
     * 单个分数的高度
     */
    private static final float SINGLE_SCORE_HEIGHT = 1/15F;
    /**
     * 单个分数的宽度
     */
    private int singleScoreWidth;
    /**
     * 单个分数的高度
     */
    private int singleScoreHeight;
    /**
     * 装分数图片的数组
     */
    private Bitmap[] mBitmaps;
    private RectF rectF = new RectF();
    private int gameWidth;
    private int gameHeight;
    private int index;
    public Score(int gameWidth,int gameHeight,Bitmap[] bitmaps){
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        mBitmaps = bitmaps;
        singleScoreHeight = (int) (gameHeight * SINGLE_SCORE_HEIGHT);
        singleScoreWidth = singleScoreHeight / bitmaps[0].getHeight() * bitmaps[0].getWidth();
        rectF.set(0,0,singleScoreWidth,singleScoreHeight);
    }



    public void draw(Canvas mCanvas){

        int ge = index % 10;
        int shi = index /10;
        mCanvas.save(Canvas.MATRIX_SAVE_FLAG);
        mCanvas.translate(gameWidth / 2 - singleScoreWidth / 2,
                1f / 8 * gameHeight);
        if (shi != 0){
            mCanvas.drawBitmap(mBitmaps[shi],null,rectF,null);
            mCanvas.translate(singleScoreWidth, 0);
        }

        mCanvas.drawBitmap(mBitmaps[ge],null,rectF,null);
        mCanvas.restore();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
