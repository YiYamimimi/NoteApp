package com.example.noteapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.bean.Note;
import com.example.noteapp.EditActivity;
import com.example.noteapp.NoteDbOpenHelper;
import com.example.noteapp.R;
import com.example.noteapp.bean.Note;
import com.example.noteapp.util.ToastUtil;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Note> mBeanList; //数据实体
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private NoteDbOpenHelper mNoteDbOpenHelper;

    private int viewType;

    public static int TYPE_LINEAR_LAYOUT = 0;
    public static int TYPE_GRID_LAYOUT = 1;

    public MyAdapter(Context context, List<Note> mBeanList){
        this.mBeanList = mBeanList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mNoteDbOpenHelper = new NoteDbOpenHelper(mContext);
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @NonNull
    @Override
    //viewholder创建
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_LINEAR_LAYOUT){
            View view = mLayoutInflater.inflate(R.layout.list_item_layout, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }else if(viewType == TYPE_GRID_LAYOUT){
            View view = mLayoutInflater.inflate(R.layout.list_item_grid_layout, parent, false);
            MyGridViewHolder myGridViewHolder = new MyGridViewHolder(view);
            return myGridViewHolder;
        }

        return null;
    }
    //viewholder绑定
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        if(holder instanceof MyViewHolder){
            bindMyViewHolder((MyViewHolder) holder, position);
        } else if (holder instanceof MyGridViewHolder) {
            bindGridViewHolder((MyGridViewHolder) holder, position);
        }
    }

    private void bindMyViewHolder(MyViewHolder holder, int position) {
        Note note = mBeanList.get(position);
        holder.mTvTitle.setText(note.getTitle()); //标题
        holder.mTvContent.setText(note.getContent());  //内容
        holder.mTvTime.setText(note.getCreatedTime());  //时间
        holder.rlContainer.setOnClickListener(v -> {

            Intent intent = new Intent(mContext, EditActivity.class);
            intent.putExtra("note", note);
            mContext.startActivity(intent);
        });

        holder.rlContainer.setOnLongClickListener(v -> {
            // 弹出弹窗
            Dialog dialog = new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
            View dialogView = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);
            TextView tvDelete = dialogView.findViewById(R.id.tv_delete);


            tvDelete.setOnClickListener(v1 -> {
                int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());
                if (row > 0) {
                    removeData(position);
                }
                dialog.dismiss();
            });


            dialog.setContentView(dialogView);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            return true;
        });
    }


    private void bindGridViewHolder(MyGridViewHolder holder, int position) {
        Note note = mBeanList.get(position);
        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(note.getContent());
        holder.mTvTime.setText(note.getCreatedTime());
        holder.rlContainer.setOnClickListener(v -> {
            //跳转到编辑记事，恢复信息
            Intent intent = new Intent(mContext, EditActivity.class);
            intent.putExtra("note", note);
            mContext.startActivity(intent);
        });

        holder.rlContainer.setOnLongClickListener(v -> {
            // 弹出弹窗，长按删除
            Dialog dialog = new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
            View dialogView = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);
            TextView tvDelete = dialogView.findViewById(R.id.tv_delete);

            tvDelete.setOnClickListener(v1 -> {
                int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());
                if (row > 0) {
                    removeData(position);
                    ToastUtil.toastShort(mContext,"删除成功！");
                }else{
                    ToastUtil.toastShort(mContext,"删除失败！");
                }
                dialog.dismiss(); //对话框消失

            });

            dialog.setContentView(dialogView);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    } //数量

    public void refreshData(List<Note> notes) {
        this.mBeanList = notes;
        notifyDataSetChanged(); //通知数据
    }
    //删除
    public void removeData(int pos) {
        mBeanList.remove(pos);
        notifyItemRemoved(pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.mTvContent = itemView.findViewById(R.id.tv_content);
            this.mTvTime = itemView.findViewById(R.id.tv_time);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }

    class MyGridViewHolder extends RecyclerView.ViewHolder{

        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;

        public MyGridViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.mTvContent = itemView.findViewById(R.id.tv_content);
            this.mTvTime = itemView.findViewById(R.id.tv_time);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }
}
