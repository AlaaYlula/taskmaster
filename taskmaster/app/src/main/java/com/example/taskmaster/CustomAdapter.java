package com.example.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.taskmaster.data.Task;
// Change the Task class To be Amplify :
import com.amplifyframework.datastore.generated.model.Task;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    List<Task> tasksList;
    CustomClickListener listener;

    public CustomAdapter(List<Task> tasksList, CustomClickListener listener) {
        this.tasksList = tasksList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // task Layout has the three elements
        View listItemView = layoutInflater.inflate(R.layout.task_layout, parent, false);
        return new CustomViewHolder(listItemView, listener);
    }

    // will be called multiple times to inject the data into the view holder object
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.title.setText(tasksList.get(position).getTitle());
//        holder.body.setText(tasksList.get(position).getBody());
//        holder.state.setText(tasksList.get(position).getState().toString());
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public interface CustomClickListener {
        void onTaskClicked(int position);
    }

    // CustomViewHolder //
    static class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView body;
        TextView state;

        CustomClickListener listener;

        public CustomViewHolder(@NonNull View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            title = itemView.findViewById(R.id.title);
//            body = itemView.findViewById(R.id.description);
//            state = itemView.findViewById(R.id.state);

            itemView.setOnClickListener(view -> listener.onTaskClicked(getAdapterPosition()));
        }
    }


}
