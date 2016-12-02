package draw;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by 康力 on 2015/10/22.
 */
public class Utils {
    /**
     * dip转化为px
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context,float dp){
        int px = Math.round(TypedValue.applyDimension(//round 四舍五入
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                        .getDisplayMetrics()));
        return px;
    }
}
