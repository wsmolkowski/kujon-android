package mobi.kujon.google_drive.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */

public class GoogleDriveVirtualFile {


    public static InputStream getInputStream(Activity activity ,Uri uriToFile) throws IOException {
        InputStream inputStream;
        if (isVirtualFile(activity,uriToFile)) {
            inputStream = getInputStreamForVirtualFile(activity,uriToFile);
        } else {
            inputStream = activity.getContentResolver().openInputStream(uriToFile);
        }
        return inputStream;
    }


    public static boolean isVirtualFile(Activity activity, Uri uri) {
        if (!DocumentsContract.isDocumentUri(activity,uri)) {
            return false;
        }
        Cursor cursor = activity.getContentResolver().query(
                uri,
                new String[]{DocumentsContract.Document.COLUMN_FLAGS},
                null, null, null);

        int flags = 0;
        if (cursor.moveToFirst()) {
            flags = cursor.getInt(0);
        }
        cursor.close();

        return (flags & DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT) != 0;
    }

    private static InputStream getInputStreamForVirtualFile(Activity activity, Uri uri)
            throws IOException {
        ContentResolver resolver = activity.getContentResolver();
        String[] openableMimeTypes = resolver.getStreamTypes(uri, "application/pdf");
        if (openableMimeTypes == null ||
                openableMimeTypes.length < 1) {
            throw new FileNotFoundException();
        }
        return resolver
                .openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null)
                .createInputStream();
    }
}
