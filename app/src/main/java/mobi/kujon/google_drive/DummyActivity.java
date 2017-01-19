package mobi.kujon.google_drive;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;

import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.google_drive.model.UploadedFile;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.ErrorHandlerUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DummyActivity extends BaseActivity {

    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        KujonApplication.getComponent().inject(this);
        getPhoto();
    }


    public String getPath(Context context, Uri uri) {

//        if (uri.toString().startsWith(GOOGLE_PHOTO_PROVIDER)) {
//            return uri.toString();
//        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void getPhoto() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        startActivityForResult(galleryIntent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MultipartBody.Builder builder = new MultipartBody.Builder("Boundary-KujoniOSApp-8232343678423491230621");
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(this, selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                RequestBody rbFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteArray);
                RequestBody cid = RequestBody.create(null, "1000-2D97SR");
                RequestBody tid = RequestBody.create(null, "2015");
                RequestBody share = RequestBody.create(null, "*");
                builder.addFormDataPart("files", "test_img.png", RequestBody.create(MediaType.parse("multipart/form-data"), byteArray));
                builder.addFormDataPart("course_id", "1000-2D97SR");
                builder.addFormDataPart("term_id", "2015");
                builder.addFormDataPart("file_shared_with", "*");
                MultipartBody body = builder.build();
                kujonFilesharingApi.uploadFile(cid, tid, share, rbFile).enqueue(new Callback<KujonResponse<List<UploadedFile>>>() {
                    @Override
                    public void onResponse(Call<KujonResponse<List<UploadedFile>>> call, Response<KujonResponse<List<UploadedFile>>> response) {
                        if(ErrorHandlerUtil.handleResponse(response)){
                            Toast.makeText(DummyActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DummyActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<KujonResponse<List<UploadedFile>>> call, Throwable t) {
                        Toast.makeText(DummyActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
}
