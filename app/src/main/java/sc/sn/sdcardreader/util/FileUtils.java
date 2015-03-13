package sc.sn.sdcardreader.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Class helper about {@code File}.
 *
 * @author S. Grimault
 */
public class FileUtils {

    public void startIntent(Context context, @NonNull final File file) {
        if (file.isFile()) {
            final Intent intent = new Intent(Intent.ACTION_VIEW);

            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).getPath());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Nullable
    public static String getMimeType(@NonNull final File file) {
        if (file.isFile()) {
            final String fileExtension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).getPath());

            if (!TextUtils.isEmpty(fileExtension)) {
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
            }
        }

        return null;
    }
}
