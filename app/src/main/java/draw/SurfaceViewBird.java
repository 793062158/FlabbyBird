package draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dali.flabbybird.MainActivity;
import dali.flabbybird.R;
import tool.MusicUtils;
import tool.SharedPreferencesUtils;

/**
 * Created by 康力 on 2015/10/20.
 */
public class SurfaceViewBird extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = "SurfaceViewBird";
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Context context;

    /**
     * 用于绘制的子线程
     */
    private Thread t;
    /**
     * 控制主线程的开关
     */
    private boolean isRunning;
    /**
     * 绘制的画笔
     */
    private Paint mPaint;
    /**
     * 背景图片
     */
    private Bitmap mBg;
    /**
     * 当前背景的宽高和范围
     */
    private RectF rectBg = new RectF();
    private int mWidth;
    private int mHeight;

    /**
     * 绘制鸟的相关类
     */
    private Bird mBird;
    private Bitmap mBirdBitmap;

    /**
     * 绘制地板的相关类
     */
    private Floor mFloor;
    private Bitmap mFloorBitmap;
    private int mSpeed;//地板移动的速度
    private int tempMspeed = 2;//设置地板的速度

    /**
     * 绘制管道的相关类
     */
    private Pipe mPipe;
    private Bitmap mTopBitmap;
    private Bitmap mBottomBitmap;
    RectF rectF = new RectF();
    private static final int PIPE_WIDTH = 60;
    private int mPipeWidth;
    private static List<Pipe> pipes;
    /**
     * 绘制分数相关类
     */
    private final int[] mNums = new int[]{R.drawable.n0, R.drawable.n1,
            R.drawable.n2, R.drawable.n3, R.drawable.n4, R.drawable.n5,
            R.drawable.n6, R.drawable.n7, R.drawable.n8, R.drawable.n9};
    private Bitmap[] mNumBitmap;
    private Score mScore;
    private int score;
    private TextView tv_newscore;
    private TextView tv_oldscore;

    /**
     * 游戏状态相关
     */
    private enum mGameStatus {
        WATTING, RUNNING, STOP;
    }


    private mGameStatus mStatus = mGameStatus.WATTING;
    private static final int BIRD_UP_SIZE = -16;
    private static final int BIRD_DOWN_SPEED = 2;
    private int birdUpSize;
    private int tempbirdUpSize;
    private int birdDownSpeed;
    private int tempBirDownSpeed;
    /**
     * 管道生成相关
     */
    private static final int MOVE_PIPE_DISTANCE = 250;
    private int move_distance_pipe;//管道移动到一定的距离产生一个管道
    private int tem_move_distance_pipe;

    private List<Pipe> removePipes;

    private MusicUtils musicUtils;

    public SurfaceViewBird(Context context, int tempMspeed) {
        this(context, null);
        mSpeed = Utils.dp2px(context, tempMspeed);
    }

    public SurfaceViewBird(Context context, AttributeSet attrs) {
        super(context, attrs);
        //实例化对象
        //this.context = context;

        mHolder = getHolder();
        mHolder.addCallback(this);
        setZOrderOnTop(true);//设置画布背景透明
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        setFocusable(true);//设置可获得焦点
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);//设置常亮
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//设置锯齿样式,会消耗大量的资源，使绘制变慢
        mPaint.setDither(true);//设置抖动效果，会让画面更加饱满
        initBitmaps();
        mPipeWidth = Utils.dp2px(context, PIPE_WIDTH);
        birdUpSize = Utils.dp2px(context, BIRD_UP_SIZE);
        birdDownSpeed = Utils.dp2px(context, BIRD_DOWN_SPEED);//鸟下降的速度
        move_distance_pipe = Utils.dp2px(context, MOVE_PIPE_DISTANCE);
        pipes = new ArrayList<Pipe>();//管道的集合
        removePipes = new ArrayList<Pipe>();//移除的管道集合
        initPopWindow();
        initReadyPopWindow();
        musicUtils = new MusicUtils(getContext());
        musicUtils.initSoundpool();
    }

    /**
     * 初始化一些图片
     */
    private void initBitmaps() {
        //Log.i(TAG, "initBitmaps");
        int index = (int) (Math.random() * 2) + 1;
        Log.i("TAG", "index" + index);
        if (index == 1) {
            mBg = loadImageByResId(R.drawable.bg3);
        } else {
            mBg = loadImageByResId(R.drawable.bg_night);
        }
        mBirdBitmap = loadImageByResId(R.drawable.bird0_0);
        mFloorBitmap = loadImageByResId(R.drawable.tile1);
        mTopBitmap = loadImageByResId(R.drawable.g2);
        mBottomBitmap = loadImageByResId(R.drawable.g1);
        mNumBitmap = new Bitmap[mNums.length];
        for (int i = 0; i < mNumBitmap.length; i++) {
            mNumBitmap[i] = loadImageByResId(mNums[i]);
        }
    }

    /**
     * 根据resId加载图片
     *
     * @param resId
     * @return
     */
    private Bitmap loadImageByResId(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //开启线程，不断的绘制
        //Log.i(TAG,"surfaceCreated");
        isRunning = true;
        t = new Thread(this);
        t.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;//通知关闭线程
    }

    @Override
    public void run() {
        //不断的进行绘制
        while (isRunning) {
            long start = System.currentTimeMillis();
            logic();
            if (isSendReadyMessage) {
                mHandler.sendEmptyMessage(1);
                isSendReadyMessage = false;
            }
            draw();

            long end = System.currentTimeMillis();

            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start));//保证了50秒才刷新一次页面
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSendReadyMessage = true;

    /**
     * 逻辑相关，用于管理游戏的状态和动作
     */
    private int tempScore;
    private void logic() {
        switch (mStatus) {
            case RUNNING:
                score = 0;
                logicPipe();//管道的逻辑相关
                tempbirdUpSize += birdDownSpeed;//加上鸟下降的速度
                mBird.setY(mBird.getY() + tempbirdUpSize);//让鸟自由掉下来
                mFloor.setX(mFloor.getX() - mSpeed);//让地板移动起来
                checkGameOver();//检查游戏是否结束
                score += removePipes.size();//每次分数等于管道移除的数量+鸟已经通过的数量
                for (Pipe pipe : pipes) {
                    if (pipe.getX() + mPipeWidth < mBird.getX()) {
                        score++;
                    }
                }
                if (score > tempScore){//每次分数变动的时候响一次
                    musicUtils.playSound(8,0);
                }
                tempScore = score;
                mScore.setIndex(score);//把分数传入，并且计算
                break;
            case WATTING:
                break;
            case STOP:
                if (isStartMusic) {
                    //播放结束游戏音乐
                    musicUtils.playSound(2, 0);
                    Log.i("dali", "play gamee over music");
                    isStartMusic = false;
                }
                //如果游戏失败，还在空中，就让他掉下地
                if (mBird.getY() < mFloor.getY() - mBird.getHeight()) {

                    tempbirdUpSize += birdDownSpeed;
                    mBird.setY(mBird.getY() + tempbirdUpSize);

                } else {
                    if (isSendMessage || !popupWindow.isShowing() && !popupWindow2.isShowing()) {
                        mHandler.sendEmptyMessage(0);
                        isSendMessage = false;

                    }

                }
                break;

        }
    }

    private boolean isStartMusic = true;

    /**
     * 管道的逻辑相关
     */
    private void logicPipe() {
        for (Pipe pipe : pipes) {

            if (pipe.getX() < -mPipeWidth) {
                removePipes.add(pipe);
                //Log.i(TAG, "当前管道的数量为:" + pipes.size());
                //Log.i(TAG, "当前移除管道的数量为:" + removePipes.size());
                continue;
            }
        }
        pipes.removeAll(removePipes);

        tem_move_distance_pipe += mSpeed;
        if (tem_move_distance_pipe >= move_distance_pipe) {
            Pipe pipe = new Pipe(getContext(), mWidth, mHeight, mTopBitmap, mBottomBitmap);
            pipes.add(pipe);
            tem_move_distance_pipe = 0;
        }
        for (Pipe pipe : pipes) {
            pipe.setX(pipe.getX() - mSpeed);
        }
    }

    /**
     * 重写用户的点击事件，用于交互
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            switch (mStatus) {
                case WATTING:
                    mStatus = mGameStatus.RUNNING;
                    break;
                case RUNNING:
                    //让鸟飞起来
                    musicUtils.playSound(1, 0);
                    tempbirdUpSize = birdUpSize;
                    break;
                case STOP:
                    //让鸟下落
                    break;
            }
        }
        return true;
    }

    private void draw() {
        mCanvas = mHolder.lockCanvas();//拿到图纸
        try {
            if (mCanvas != null) {
                //draw something
                //绘制背景
                drawBg();
                //绘制鸟
                drawBird();
                //绘制管道
                drawPipe();

                //绘制地板
                drawFloor();
                //绘制分数
                drawScore();


            }
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);//释放掉
            }
        }
    }

    /**
     * 绘制背景
     */
    private void drawBg() {
        mCanvas.drawBitmap(mBg, null, rectBg, null);
    }

    /**
     * 绘制鸟
     */
    private void drawBird() {
        mBird.draw(mCanvas);
    }

    /**
     * 绘制地板
     */
    private void drawFloor() {
        mFloor.draw(mCanvas, mPaint);
    }

    /**
     * 绘制管道
     */
    private void drawPipe() {
        for (Pipe pipe : pipes) {
            pipe.draw(mCanvas, rectF);
        }
    }

    /**
     * 绘制分数
     */
    private void drawScore() {
        mScore.draw(mCanvas);
    }

    /**
     * 初始化尺寸
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        rectBg.set(0, 0, w, h);
        mBird = new Bird(getContext(), mWidth, mHeight, mBirdBitmap);
        mFloor = new Floor(mWidth, mHeight, mFloorBitmap);
        mPipe = new Pipe(getContext(), mWidth, mHeight, mTopBitmap, mBottomBitmap);
        pipes.add(mPipe);
        rectF.set(0, 0, mPipeWidth, mHeight);
        mScore = new Score(mWidth, mHeight, mNumBitmap);

    }


    /**
     * 检查游戏是否结束
     */
    private void checkGameOver() {
        if (mBird.getY() > mFloor.getY() + mBird.getHeight()) {
            mStatus = mGameStatus.STOP;
        }
        //通过管道
        for (Pipe pipe : pipes) {
            if (pipe.getX() + mPipeWidth < mBird.getX()) {

                continue;
            }
            if (mBird.getX() + mBird.getWidth() > pipe.getX() && (mBird.getY() < pipe.getHeight() || (mBird.getY() + mBird.getHeight()) > (pipe.getHeight() + pipe.getMargin()))) {
                mStatus = mGameStatus.STOP;
                break;
            }
        }
    }

    private int oldScore;
    /**
     * handler用于控制显示popWindow
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (popupWindow2.isShowing()) return;
                showPopWindow();
            } else if (msg.what == 1) {
                showReadyPopWindow();
            }

        }
    };

    /**
     * 显示popWindow
     */
    private boolean isSendMessage = true;

    public void showPopWindow() {
        WindowManager wm = (WindowManager) getContext().
                getSystemService(Context.WINDOW_SERVICE);
        Log.i("dali", "popWindow show");
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        //设置popwindow显示位置
        //popupWindow.showAtLocation(view, Gravity.CENTER, width/2, height/2);
        popupWindow.showAtLocation(this, Gravity.CENTER, 0, 0);
        //获取popwindow焦点
        popupWindow.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        tv_newscore = (TextView) popView.findViewById(R.id.tv_score);
        tv_oldscore = (TextView) popView.findViewById(R.id.tv_score_history);
        oldScore = (int) SharedPreferencesUtils.getParam(getContext(), "score", 0);
        if (score > oldScore) {
            SharedPreferencesUtils.setParam(getContext(), "score", score);
            tv_newscore.setText(score + "分,打破记录!!");
            tv_oldscore.setText(oldScore + "分");
        } else {
            tv_oldscore.setText(oldScore + "分");
            tv_newscore.setText(score + "分!!");
        }

        ImageView iv = (ImageView) popView.findViewById(R.id.iv_medal);
        int index = 0;
        //奖牌
        if (score < 10) {
            index = R.drawable.medals_0;
        } else if (score < 20) {
            index = R.drawable.medals_1;
        } else if (score < 30) {
            index = R.drawable.medals_2;
        } else {
            index = R.drawable.medals_3;
        }
        iv.setImageResource(index);
        popView.findViewById(R.id.btn_play).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUtils.playSound(4, 0);
                isSendMessage = true;
                isSendReadyMessage = true;
                isStartMusic = true;
                mStatus = mGameStatus.WATTING;
                //清除管道
                pipes.clear();
                removePipes.clear();
                //重置鸟的位置
                mBird.setY(mHeight * 3 / 5);
                //重置鸟下落的速度
                tempbirdUpSize = 0;
                //设置产生管道距离为0
                tem_move_distance_pipe = 0;
                //设置分数为0
                score = 0;
                mScore.setIndex(score);
                mPipe = new Pipe(getContext(), mWidth, mHeight, mTopBitmap, mBottomBitmap);
                pipes.add(mPipe);
                popupWindow.dismiss();

            }
        });
        popView.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUtils.playSound(4, 0);
                isRunning = false;//关闭线程
                MainActivity.instance.finish();//finish activity
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 显示ReadypopWindow
     */
    public void showReadyPopWindow() {
        //设置popwindow显示位置
        //popupWindow.showAtLocation(view, Gravity.CENTER, width/2, height/2);
        popupWindow2.showAtLocation(this, Gravity.CENTER, 0, -100);
        //获取popwindow焦点
        popupWindow2.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.update();

    }

    /**
     * 初始化popWindow
     */
    private View popView;
    private PopupWindow popupWindow;

    public void initPopWindow() {
        popView = LayoutInflater.from(getContext()).inflate(R.layout.pop_gameover, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置popwindow出现和消失动画
        popupWindow.setAnimationStyle(R.style.PopMenuAnimation);

    }

    /**
     * 初始化pop ready
     */

    private View popView2;
    private PopupWindow popupWindow2;

    public void initReadyPopWindow() {
        popView2 = LayoutInflater.from(getContext()).inflate(R.layout.pop_ready, null);
        popupWindow2 = new PopupWindow(popView2, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow2.setBackgroundDrawable(new ColorDrawable(0));
        //设置popwindow出现和消失动画
        popupWindow2.setAnimationStyle(R.style.PopMenuAnimation2);

    }


}
