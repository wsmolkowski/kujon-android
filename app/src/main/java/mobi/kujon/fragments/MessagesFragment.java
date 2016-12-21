package mobi.kujon.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.underscore.$;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Message;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesFragment extends ListFragment {

    private Adapter adapter;

    @Override protected String getRequestUrl() {
        return backendApi.getMessages().request().url().toString();
    }

    @Override protected void loadData(boolean refresh) {
        Call<KujonResponse<List<Message>>> messages = refresh ? backendApi.getMessagesRefresh() : backendApi.getMessages();
        messages.enqueue(new Callback<KujonResponse<List<Message>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<Message>>> call, Response<KujonResponse<List<Message>>> response) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                if(ErrorHandlerUtil.handleResponse(response)) {
                    List<Message> messageList = response.body().data;
                    adapter.setData(messageList);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<List<Message>>> call, Throwable t) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new MessagesFragment.Adapter();
        recyclerView.setAdapter(adapter);
        activity.showProgress(true);

        loadData(false);
    }

    @Override public void onStart() {
        super.onStart();
        activity.setToolbarTitle(R.string.messages);
    }

    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Message> messages = new LinkedList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_message, parent, false);
            return new ViewHolder(view);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Message message = messages.get(position);
            holder.messageSender.setText(message.from);
            holder.messageSendDate.setText(message.createdTime);
        }

        @Override public int getItemCount() {
            return messages.size();
        }

        public void setData(List<Message> data) {
            this.messages = $.sortBy(data, item -> item.createdTime);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.message_sender) TextView messageSender;
        @Bind(R.id.message_send_date) TextView messageSendDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {

            });
        }
    }

}
