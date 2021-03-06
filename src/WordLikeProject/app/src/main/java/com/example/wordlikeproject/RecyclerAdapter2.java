package com.example.wordlikeproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<Word> listData = new ArrayList<>();
    private Word data;
    private Context context;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.words_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Word data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView spellingView;
        private TextView rankView;
        private TextView meaningView;
        private TextView sentenceView;
        private Word data;

        ItemViewHolder(View itemView) {
            super(itemView);
            spellingView = itemView.findViewById(R.id.spellingView);
            rankView = itemView.findViewById(R.id.rankView);
            meaningView = itemView.findViewById(R.id.meaningView);
            sentenceView = itemView.findViewById(R.id.sentenceView);

        }

        void onBind(Word data) {
            this.data = data;
            spellingView.setText(data.getWordSpelling());
            rankView.setText(String.valueOf(data.getRank()));
            meaningView.setText(data.getWordMeaning());
            sentenceView.setText(data.getWordSentence());
        }
    }
}