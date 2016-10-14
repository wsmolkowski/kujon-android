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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Usos;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.SimpleDividerItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class UsosesActivity extends BaseActivity {

    public static final int NUMBER_OF_CLICKS = 5;
    public static final int CLICKS_TIME = 2 * 1000;
    public static final int USOS_LOGIN_REQUEST_CODE = 1;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    List<Long> toolbarClickTimestamps = new ArrayList<>();
    @Bind(R.id.toolbar_title) TextView toolbarTitle;

    @BindColor(R.color.dark) int colorDark;
    @BindColor(R.color.opacityBlack) int colorOpacityBlack;

    private UsosesAdapter adapter;

    private boolean demoEnabled = false;
    private AlertDialog alertDialog;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usoses);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Wybierz uczelnię");

        requestUsoses();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsosesAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
    }

    private void requestUsoses() {
        showProgress(true);
        kujonBackendApi.usosesAll().enqueue(new Callback<KujonResponse<List<Usos>>>() {
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
            holder.name.setTextColor(usos.enabled ? colorDark : colorOpacityBlack);
            picasso.load(usos.logo).into(holder.logo);

            holder.rootView.setOnClickListener(v -> {
                if (usos.enabled) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(UsosesActivity.this);
                    dlgAlert.setMessage("Zostaniesz teraz przekierowany do strony " + usos.name + ", aby zalogować się na Twoje konto w USOS");
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton("OK", (dialog, which) -> {
                        Intent intent = new Intent(UsosesActivity.this, UsoswebLoginActivity.class);
                        intent.putExtra(UsoswebLoginActivity.USOS_POJO, gson.toJson(usos));
                        startActivityForResult(intent, USOS_LOGIN_REQUEST_CODE);
                    });
                    dlgAlert.setNegativeButton("Cofnij", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    alertDialog = dlgAlert.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(UsosesActivity.this, !isEmpty(usos.comment) ? usos.comment : "Ta uczelnia jest aktualnie niedostępna", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override public int getItemCount() {
            return filteredUsoses().size();
        }

        public void setItems(List<Usos> items) {
            if (items != null) {
                this.items = $.sortBy(items, item -> item.name);
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
        View rootView;

        public UsosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rootView = itemView;
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
            String usosPojo = data.getStringExtra(UsoswebLoginActivity.USOS_POJO);
            intent.putExtra(UsoswebLoginActivity.USOS_POJO, usosPojo);
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
