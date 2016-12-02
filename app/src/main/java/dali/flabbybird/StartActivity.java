package dali.flabbybird;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import tool.MusicUtils;
import tool.SharedPreferencesUtils;

public class StartActivity extends AppCompatActivity {
    private MusicUtils musicUtils;
    private PopupWindow sound_popupWindow;
    private PopupWindow say_popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initPopWindow();
        initRecordPopWindow();
        initgame_sound();
        initSayPop();
        musicUtils = new MusicUtils(this);
        musicUtils.initSoundpool();
    }

    /**
     * 显示popWindow
     * @param view
     */
    public void button_rate(View view) {
        musicUtils.playSound(4,0);
        /*WindowManager wm = (WindowManager)
                getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();*/
        //设置popwindow显示位置
        //popupWindow.showAtLocation(view, Gravity.CENTER, width/2, height/2);
        popupWindow.showAsDropDown(view, -140, 0);
        //获取popwindow焦点
        popupWindow.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popView.findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUtils.playSound(7,0);//游戏音效
                Toast.makeText(StartActivity.this, "设置游戏速度为:拖拉机", Toast.LENGTH_SHORT).show();
                speed = 4;
                popupWindow.dismiss();
            }
        });
        popView.findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUtils.playSound(7,0);//游戏音效
                Toast.makeText(StartActivity.this, "设置游戏速度为:摩托车", Toast.LENGTH_SHORT).show();
                speed = 8;
                popupWindow.dismiss();
            }
        });
        popView.findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUtils.playSound(7,0);//游戏音效
                Toast.makeText(StartActivity.this, "设置游戏速度为:火箭", Toast.LENGTH_SHORT).show();
                speed = 12;
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 初始化popWindow
     */
    private int speed = 4;
    private View popView;
    private View popView2;
    private PopupWindow popupWindow;
    private PopupWindow popupWindow2;
    public void initPopWindow(){
        popView = LayoutInflater.from(StartActivity.this).inflate(R.layout.pop_rate, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置popwindow出现和消失动画
        popupWindow.setAnimationStyle(R.style.PopMenuAnimation);

    }
    public void initRecordPopWindow(){
        popView2 = LayoutInflater.from(StartActivity.this).inflate(R.layout.pop_record, null);
        popupWindow2 = new PopupWindow(popView2, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow2.setBackgroundDrawable(new ColorDrawable(0));
        //设置popwindow出现和消失动画
        popupWindow2.setAnimationStyle(R.style.PopMenuAnimation);

    }

    public void button_play(View view) {
        musicUtils.playSound(4,0);//音效
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.putExtra("speed",speed);
        startActivity(intent);
    }

    public void button_score(View view) {
        musicUtils.playSound(4,0);//音效
        int score = 0;
        score = (int) SharedPreferencesUtils.getParam(this, "score", 0);
        //设置popwindow显示位置
        //popupWindow.showAtLocation(view, Gravity.CENTER, width/2, height/2);
        popupWindow2.showAsDropDown(findViewById(R.id.button_rate), -140, 0);
        //获取popwindow焦点
        popupWindow2.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.update();
        TextView tv = (TextView) popView2.findViewById(R.id.tv_score);
        tv.setText(score+"分");
    }

    /**
     * 退出程序相关
     */
    private long preTime;
    public static final long TWO_SECOND = 2 * 1000;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 截获后退键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = new Date().getTime();

            // 如果时间间隔大于2秒, 不处理
            if ((currentTime - preTime) > TWO_SECOND) {
                // 显示消息
                Toast.makeText(this, "再按一次退出",
                        Toast.LENGTH_SHORT).show();

                // 更新时间
                preTime = currentTime;

                // 截获事件,不再处理
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 声音设置

     */
    public void sound_setting(View view) {
        musicUtils.playSound(4,0);//音效
        sound_popupWindow.showAtLocation(this.findViewById(R.id.main),Gravity.CENTER,0,0);


    }

    private void initgame_sound(){
        View view1 = LayoutInflater.from(StartActivity.this).inflate(R.layout.sound_setting, null);
        sound_popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sound_popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //设置popwindow出现和消失动画
        //获取popwindow焦点
        sound_popupWindow.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        sound_popupWindow.setOutsideTouchable(true);
        sound_popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
        Button open = (Button) view1.findViewById(R.id.open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.SOUND = true;
                sound_popupWindow.dismiss();
            }
        });

        Button close = (Button) view1.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.SOUND = false;
                sound_popupWindow.dismiss();
            }
        });
    }



    /**
     * 游戏说明
     * @param view
     */
    public void game_say(View view) {
        musicUtils.playSound(4,0);//音效
        say_popupWindow.showAtLocation(this.findViewById(R.id.main),Gravity.CENTER,0,0);
    }

    private void initSayPop(){
        View view1 = LayoutInflater.from(StartActivity.this).inflate(R.layout.game_say, null);
        say_popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        say_popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        //获取popwindow焦点
        say_popupWindow.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        say_popupWindow.setOutsideTouchable(true);
        //设置popwindow出现和消失动画
        say_popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
    }

    /**
     * 结束游戏
     * @param view
     */
    public void over_game(View view) {
        musicUtils.playSound(4,0);//音效
        finish();
    }
}
