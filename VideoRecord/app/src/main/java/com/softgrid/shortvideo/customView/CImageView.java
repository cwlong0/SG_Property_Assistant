package com.softgrid.shortvideo.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.softgrid.shortvideo.R;

public class CImageView extends ImageView {

    private Context context;

    public CImageView(Context context) {
        super(context);
        this.context = context;
        updateDrawable();
    }

    public CImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        updateDrawable();
    }

    public CImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        updateDrawable();
    }

    @SuppressWarnings("deprecation")
	private void updateDrawable(){
        if (getDrawable() == null){
            if (getBackground() != null){
                setBackgroundDrawable(selector(getBackground()));
            }
        }
        else{
            setImageDrawable(selector(getDrawable()));
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        updateDrawable();
    }


    /** 设置Selector。 */
    private StateListDrawable selector(Drawable drawable) {
        StateListDrawable selector = new StateListDrawable();
        Drawable normal = drawable;
        Drawable pressed = newDrawble(drawable);
        setFilter(pressed);

        // View.PRESSED_ENABLED_STATE_SET
        selector.addState(new int[] {android.R.attr.state_pressed}, pressed);
        // View.EMPTY_STATE_SET
        selector.addState(new int[]{}, normal);
        return selector;
    }


    private Drawable newDrawble(Drawable drawable){
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return new BitmapDrawable(getContext().getResources(), bitmap);
    }

    /**
     *   设置滤镜
     */
    private void setFilter(Drawable drawable) {
        //设置滤镜
        drawable.setColorFilter(context.getResources().getColor(R.color.transparent_clicked),
                PorterDuff.Mode.MULTIPLY);
    }
}
