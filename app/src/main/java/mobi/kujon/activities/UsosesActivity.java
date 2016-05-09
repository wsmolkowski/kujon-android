package mobi.kujon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.underscore.$;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Usos;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsosesActivity extends BaseActivity {

    public static final int NUMBER_OF_CLICKS = 5;
    public static final int CLICKS_TIME = 2 * 1000;
    public static final int USOS_LOGIN_REQUEST_CODE = 1;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    List<Long> toolbarClickTimestamps = new ArrayList<>();
    private UsosesAdapter adapter;

    private boolean demoEnabled = false;
    private AlertDialog alertDialog;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usoses);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Wybierz uczelnię");

        requestUsoses();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsosesAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void requestUsoses() {
        showProgress(true);
        kujonBackendApi.usoses().enqueue(new Callback<KujonResponse<List<Usos>>>() {
            @Override public void onResponse(Call<KujonResponse<List<Usos>>> call, Response<KujonResponse<List<Usos>>> response) {
                showProgress(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    adapter.setItems(response.body().data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<List<Usos>>> call, Throwable t) {
                showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;

            case R.id.help:
                help();
                return true;

            default:
                return true;
        }
    }

    private void help() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setTitle("Wybierz swoją uczelnię");
        dlgAlert.setMessage("Jeżeli Twojej Uczelni nie ma na liście to znacza że nie korzysta z wspieranego przez Kujona systemu USOS. Jeżeli masz uwagi - napisz do nas :)");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        dlgAlert.setNeutralButton("Prześlij opinię", (dialog, which) -> {
            contactUs();
        });
        alertDialog = dlgAlert.create();
        alertDialog.show();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    private class UsosesAdapter extends RecyclerView.Adapter<UsosViewHolder> {

        private List<Usos> items = new ArrayList<>();

        @Override public UsosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.usos_row, parent, false);
            return new UsosViewHolder(v);
        }

        @Override public void onBindViewHolder(UsosViewHolder holder, int position) {
            Usos usos = filteredUsoses().get(position);
            holder.name.setText(usos.name);
            holder.usosId = usos.usosId;
            holder.usosName = usos.name;
            Picasso.with(KujonApplication.getApplication()).load(usos.logo).into(holder.logo);
        }

        @Override public int getItemCount() {
            return filteredUsoses().size();
        }

        public void setItems(List<Usos> items) {
            if (items != null) {
                this.items = items;
                notifyDataSetChanged();
            }
        }

        private List<Usos> filteredUsoses() {
            if (demoEnabled) {
                return items;
            } else {
                return $.filter(items, it -> !"DEMO".equals(it.usosId));
            }
        }
    }

    class UsosViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.usos_name) TextView name;
        @Bind(R.id.usos_logo) ImageView logo;
        String usosId;
        String usosName;

        public UsosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(UsosesActivity.this);
                dlgAlert.setMessage("Zostaniesz teraz przekierowany do strony " + usosName + ", aby zalogować się na Twoje konto w USOS");
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(UsosesActivity.this, UsoswebLoginActivity.class);
                    intent.putExtra(UsoswebLoginActivity.USOS_ID, usosId);
                    intent.putExtra(UsoswebLoginActivity.USOS_NAME, usosName);
                    startActivityForResult(intent, USOS_LOGIN_REQUEST_CODE);
                });
                dlgAlert.setNegativeButton("Cofnij", (dialog, which) -> {
                    dialog.dismiss();
                });
                alertDialog = dlgAlert.create();
                alertDialog.show();
            });
        }
    }

    @Override protected void onStart() {
        super.onStart();
        Toast.makeText(UsosesActivity.this, "Wybierz swoją uczelnię", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == USOS_LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, CongratulationsActivity.class);
            String usosName = data.getStringExtra(UsoswebLoginActivity.USOS_NAME);
            intent.putExtra(UsoswebLoginActivity.USOS_NAME, usosName);
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.toolbar)
    public void toolbarClick() {
        if (!demoEnabled) {
            System.out.println(toolbarClickTimestamps);

            Long timestamp = new Date().getTime();
            if (toolbarClickTimestamps.size() >= NUMBER_OF_CLICKS && timestamp - toolbarClickTimestamps.get(0) < CLICKS_TIME) {
                // 5 subsequent clicks within 2 seconds detected
                toolbarClickTimestamps.clear();
                Toast.makeText(UsosesActivity.this, "Dodatkowa uczelnia dodana do listy", Toast.LENGTH_SHORT).show();
                demoEnabled = true;
                adapter.notifyDataSetChanged();
                return;
            }

            if (toolbarClickTimestamps.size() > 5) {
                toolbarClickTimestamps.remove(0);
            }

            toolbarClickTimestamps.add(timestamp);
        }
    }

    @Override public void onBackPressed() {
        logout();
    }
}
