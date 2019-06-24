package com.example.wordlikeproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector detector;
    private OnItemClickListener listener;
    //private activity_words_list_window.ClickListener clickListener;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, final OnItemClickListener listener) {
        this.listener = listener;//
        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && listener != null) {
                    Log.d("long","press");
                    listener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
//            출처: https://liveonthekeyboard.tistory.com/entry/안드로이드-RecyclerView-2-OnItemClick-리스너-구현 [키위남]
        });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onLongItemClick(View child, int childAdapterPosition);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) {
        View childView = view.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (childView != null && listener != null && detector.onTouchEvent(motionEvent)) {
            try {
                listener.onItemClick(childView, view.getChildAdapterPosition(childView));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {
    }
}
