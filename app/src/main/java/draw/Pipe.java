package draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by 康力 on 2015/10/22.
 */
public class Pipe {
    /**
     * 上下管道之间的距离
     */
    private static final float PIPE_BETWEEN_HEIGHT = 1/7F;
    /**
     * 上管道的最大高度
     */
    private static final float PIPE_UP_MAX_HEIGHT = 2/5F;
    /**
     * 上管道的最小高度
     */
    private static final float PIPE_UP_MIN_HEIGHT = 1/5F;

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public int getHeight() {

        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * 管道的横坐标
     */
    private int x;
    /**
     * 上管道的高度
     */
    private int height;
    /**
     * 高度之间的高度
     */
    private int margin;
    /**
     * 上管道的图片
     */
    private Bitmap mTop;
    /**
     * 下管道的图片
     */
    private Bitmap mBottom;
    /**
     * 随机数,用于产生管道高度的
     */

    private static Random r = new Random();
    public Pipe(Context context,int gameWidth,int gameHeight,Bitmap top,Bitmap bottom){
        mTop = top;
        mBottom = bottom;
        margin = (int) (gameHeight * PIPE_BETWEEN_HEIGHT);//管道的距离
        x = gameWidth;//管道从坐标默认出来
        ramdomHeight(gameHeight);


    }

    /**
     * 随机产生高度
     * @param gameHeight
     */
    private void ramdomHeight(int gameHeight) {
        height = r.nextInt((int) (gameHeight * (PIPE_UP_MAX_HEIGHT - PIPE_UP_MIN_HEIGHT)));
        height = (int) (height + gameHeight * PIPE_UP_MIN_HEIGHT);
    }

    /**
     * 绘制地板自己
     */
    public void draw(Canvas mCanvas,RectF rectF){
        mCanvas.save(Canvas.MATRIX_SAVE_FLAG);
        mCanvas.translate(x, -(rectF.bottom - height));
        mCanvas.drawBitmap(mTop, null, rectF, null);
        mCanvas.translate(0, rectF.bottom + margin);
        mCanvas.drawBitmap(mBottom, null, rectF, null);
        mCanvas.restore();
    }

    public  int getX() {
        return x;
    }

    public  void setX(int x) {
        this.x = x;
    }
}
