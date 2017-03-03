package mobi.kujon.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.underscore.$;
import com.github.underscore.Predicate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.activities.MessageDetailsActivity;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Message;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.predicates.MessagePredicate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mobi.kujon.KujonApplication.FROM_NOTIFICATION;

public class MessagesFragment extends AbstractFragmentSearchWidget<Message> {

    private Adapter adapter;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override protected String getRequestUrl() {
        return backendApi.getMessages().request().url().toString();
    }

    @Override protected void loadData(boolean refresh) {
        Call<KujonResponse<List<Message>>> messages = refresh ? backendApi.getMessagesRefresh() : backendApi.getMessages();
        messages.enqueue(new Callback<KujonResponse<List<Message>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<Message>>> call, Response<KujonResponse<List<Message>>> response) {
                showSpinner(false);
                if(ErrorHandlerUtil.handleResponse(response)) {
                    List<Message> messageList = response.body().data;
                    dataFromApi = messageList;
                    adapter.setData(messageList);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<List<Message>>> call, Throwable t) {
                showSpinner(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new MessagesFragment.Adapter();
        recyclerView.setAdapter(adapter);
        showSpinner(true);
        loadData(getActivity().getIntent().getBooleanExtra(FROM_NOTIFICATION, false));
    }

    @Override
    protected void setDataToAdapter(List<Message> filter) {
        adapter.setData(filter);
    }

    @Override
    protected Predicate<Message> createPredicate(String query) {
        return new MessagePredicate(query);
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
            holder.message = message;
            holder.messageSender.setText(message.from);
            holder.messageSendDate.setText(dateFormat.format(message.createdTime));
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

        @BindView(R.id.message_sender) TextView messageSender;
        @BindView(R.id.message_send_date) TextView messageSendDate;
        Message message;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                MessageDetailsActivity.showMessageDetails(getActivity(), message.from,
                        dateFormat.format(message.createdTime), message.message);
            });
        }
    }

}
