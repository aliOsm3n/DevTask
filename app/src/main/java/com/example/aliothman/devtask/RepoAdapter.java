package com.example.aliothman.devtask;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.MyViewHolder> {
    private List<Model_Repo> modelRepos;
    Context context;
    onClicklong onClicklong;

    public RepoAdapter(List<Model_Repo> modelRepos, Context context, onClicklong onClicklong) {
        this.modelRepos = modelRepos;
        this.context = context;
        this.onClicklong = onClicklong;
    }

    @Override
    public RepoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepoAdapter.MyViewHolder holder, int position) {
        Model_Repo model_repo = modelRepos.get(position);
        holder.repo_name.setText(model_repo.getRepo_name());
        holder.descreption.setText(model_repo.getDescreptoion());
        holder.owner_name.setText(model_repo.getOwner_name());
        if (modelRepos.get(position).getFork() == "false") {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.success_toast_color));
        } else {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return modelRepos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView repo_name, descreption, owner_name;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            repo_name = view.findViewById(R.id.repo_name);
            descreption = view.findViewById(R.id.description);
            owner_name = view.findViewById(R.id.repo_owner);
            relativeLayout = view.findViewById(R.id.layout_item);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int p = getLayoutPosition();
                    onClicklong.data("openDialog", modelRepos.get(p).getUrl_owner(), modelRepos.get(p).getUrl_repo());
                    Log.e("position", p + "");
                    return true;
                }
            });
        }
    }


}
