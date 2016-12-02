package draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * 鸟的类
 * Created by 康力 on 2015/10/22.
 */
public class Bird {
    /**
     * 鸟在屏幕的2/3高度
     */
    private static final float BIRD_HEIGHT = 3/5F;
    /**
     * 鸟的宽度30dp
     */
    private static final int BIRD_SIZE = 30;
    /**
     * 鸟的X坐标
     */
    private int x;
    /**
     * 鸟的Y坐标
     */
    private int y;
    /**
     * 鸟的宽度
     */
    private int mWidth;
    /**
     * 鸟的高度
     */
    private int mHeight;
    /**
     * 鸟的图片
     */
    private Bitmap bitmap;
    /**
     * 鸟绘制的范围
     */
    private RectF rect = new RectF();
    public Bird(Context context,int gameWidth,int gameHeight,Bitmap bitmap){
        this.bitmap = bitmap;
        //鸟的位置
        x = gameWidth/2 - bitmap.getWidth()/2;
        y = (int) (gameHeight * BIRD_HEIGHT);
        //计算鸟的宽度和高度
        mWidth = Utils.dp2px(context,BIRD_SIZE);
        mHeight = (int) (mWidth * 1.0f / bitmap.getWidth() * bitmap.getHeight());

    }
    /**
     *  绘制自己
     */
    public void draw(Canvas canvas){
        rect.set(x,y,x+mWidth,y+mHeight);
        canvas.drawBitmap(bitmap,null,rect,null);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
