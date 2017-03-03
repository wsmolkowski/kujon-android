package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;

public class MessageDetailsActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.message_sender_details) TextView sender;
    @BindView(R.id.message_send_date_details) TextView sendDate;
    @BindView(R.id.message_text) TextView message;

    public static final String SENDER = "SENDER";
    public static final String DATE_SENT = "DATE_SENT";
    public static final String MESSAGE = "MESSAGE";

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        ButterKnife.bind(this);
        KujonApplication.getComponent().inject(this);
        toolbarTitle.setText(R.string.details);
        setMessageViews();
    }

    private void setMessageViews() {
        Bundle bundle = getIntent().getExtras();
        sender.setText(bundle.getString(SENDER));
        sendDate.setText(bundle.getString(DATE_SENT));
        String messageToSet = "".equals(bundle.getString(MESSAGE)) ? (String) getText(R.string.empty_message) : bundle.getString(MESSAGE);
        message.setText(messageToSet);
    }

    public static void showMessageDetails(Activity activity, String from, String date, String message) {
        Intent intent = new Intent(activity, MessageDetailsActivity.class);
        intent.putExtra(SENDER, from);
        intent.putExtra(DATE_SENT, date);
        intent.putExtra(MESSAGE, message);
        activity.startActivity(intent);
    }

}
