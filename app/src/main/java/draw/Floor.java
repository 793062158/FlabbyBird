package draw;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

/**
 * 绘制地板类
 * Created by 康力 on 2015/10/22.
 */
public class Floor {
    /**
     * 地板是总画面高度的4/5
     */
    private static final float FLOOR_Y_HEIGHT = 4/5F;
    /**
     * 屏幕的宽、高
     */
    private int mWidth;
    private int mHeight;
    /**
     * 地板的x、y轴
     */
    private int x;
    private int y;
    /**
     * BitmapShader类 为了重复、拉伸图片的类
     */
    private BitmapShader mFloorShader;

    /**
     * 初始化地板
     * @param width
     * @param height
     * @param bitmap
     */
    public Floor(int width,int height,Bitmap bitmap){
        mWidth = width;
        mHeight = height;
        y = (int) (height*FLOOR_Y_HEIGHT);
        //横向重复、纵向拉伸
        mFloorShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
    }
    /**
     * 绘制地板的方法
     */
    public void draw(Canvas mCanvas,Paint mPaint){
        /**
         * 为了让x轴不断的递减，就可以让地板动起来，但是不能无穷的递减
         */
        if (-x > mWidth){
            x = x % mWidth;
        }
        mCanvas.save(Canvas.MATRIX_SAVE_FLAG);
        mCanvas.translate(x,y);//移动到当前位置
        mPaint.setShader(mFloorShader);
        mCanvas.drawRect(x,0,-x+mWidth,mHeight-y,mPaint);
        mCanvas.restore();//释放当初保存的状态
        mPaint.setShader(null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
