package sp2016.cs310.com.hw2furkankemikli.util;

import android.graphics.Bitmap;
import android.util.Base64;

import sp2016.cs310.com.hw2furkankemikli.constants.TakeNotesConstants;

import java.io.ByteArrayOutputStream;

/**
 * Created by furkankemikli on 17.05.2016.
 */
public class ImageUtil {
    public static String encodeString(Bitmap bitmap) {
        StringBuilder encodedImage = new StringBuilder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, TakeNotesConstants.IMAGE_QUALITY, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage.append("data:image/jpeg;base64,");
        encodedImage.append(Base64.encodeToString(imageBytes, Base64.NO_WRAP));
        return encodedImage.toString();
    }
}
