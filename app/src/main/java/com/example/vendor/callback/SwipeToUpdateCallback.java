package com.example.vendor.callback;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.R;

public class SwipeToUpdateCallback   extends ItemTouchHelper.Callback{
    Context mContext;
    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable upDateDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;
    public SwipeToUpdateCallback(Context context) {
        mContext = context;
        mBackground = new ColorDrawable();
        backgroundColor = Color.parseColor("#98FF98");
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        upDateDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_update);
        intrinsicWidth = upDateDrawable.getIntrinsicWidth();
        intrinsicHeight = upDateDrawable.getIntrinsicHeight();
    }
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();
        int backgroundCornerOffset = 0;
        boolean isCancelled = dX > 0 && !isCurrentlyActive;



        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }mBackground.setColor(backgroundColor);
        mBackground.setBounds(itemView.getLeft(),itemView.getTop(),
                itemView.getLeft()+(int) dX+ backgroundCornerOffset, itemView.getBottom());

        int iconMargin = (itemHeight- intrinsicHeight) / 2;
        int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int iconBottom = iconTop + intrinsicHeight;
        int iconLeft = itemView.getLeft() + iconMargin - intrinsicWidth;
        int iconRight = itemView.getLeft() + iconMargin;
        //int iconLeft = itemView.getRight() - iconMargin - intrinsicWidth;
        //int iconRight = itemView.getRight() - iconMargin;

        upDateDrawable.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        //upDateDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        mBackground.draw(c);
        upDateDrawable.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


    }
    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
