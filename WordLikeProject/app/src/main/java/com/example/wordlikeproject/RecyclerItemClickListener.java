package com.example.wordlikeproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector detector;
    private OnItemClickListener listener;
    //private activity_words_list_window.ClickListener clickListener;

    public RecyclerItemClickListener(Context context, RecyclerView recyclerView, OnItemClickListener listener) {
        this.listener = listener;//
        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
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
