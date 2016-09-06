package pe.hgs.truler.tools;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/** 경로로부터 이미지를 읽는다. 읽어 들이는 이미지는 다운 사이징하여 읽어들이며 크기가 큰 이미지의 경우 처리하는 시간이 길어 질 수 있다.
 * Created by ysb06 on 2016-08-08.
 */
public class ImageLoader {

	/**	이미지 처리가 이루이지는 이미지 최소 크기 */
	public static final int IMAGE_MAX_SIZE = 1024;		//만약 표시되는 이미지의 화질을 높이고 싶을 경우 값을 증가시킨다.

	private ContentResolver resolver;
	private Uri uriPath;
	private Bitmap bitImage;

	/** ImageLoader 객체 초기화, 경로로부터 이미지를 로드.
	 *
	 * @param resolver URI 처리를 위한 Context.ContentResolver
	 * @param path URI 경로
	 */
	public ImageLoader(ContentResolver resolver, Uri path) {
		this.resolver = resolver;
		this.uriPath = path;

		bitImage = loadImage();
	}

	/** 처리되어 메모리에 저장된 Bitmap 반환
	 *
	 * @return Bitmap 이미지
	 */
	public Bitmap getImage() {
		if(bitImage.isRecycled()) {
			Logger.warn("Image Loader -> Bitmap was recycled. Returning null.");
			return null;
		} else {
			return bitImage;
		}
	}

	/** 리소스 해 메서드.,Bitmap 이미지에 대한 Reference를 가지고 있고 이에 대해 이미 Recycle한 경우에는 부를 필요는 없다.
	될* 그렇지 않을  경에는우 반드시 불러주어야 Memory Leak을 방지할 수 있다. */
	public void dispose() {
		bitImage.recycle();
	}

	private Bitmap loadImage() {

		InputStream stream = getImageInputStream();
		if(stream == null)
			Logger.warn("Image Loader -> Null stream");

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(stream, null, onlyBoundsOptions);
		try {
			if (stream != null) {
				stream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
			return null;

		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

		double ratio = (originalSize > IMAGE_MAX_SIZE) ? (originalSize / IMAGE_MAX_SIZE) : 1.0;

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		stream = getImageInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(stream, null, bitmapOptions);

		int orientation = getImageOrientation(getPathFromUri(uriPath));
		Logger.debug("Image Size -> (" + onlyBoundsOptions.outWidth + ", " + onlyBoundsOptions.outHeight + ")\tOrientation -> " + orientation);
		bitmap = rotateBitmap(bitmap, orientation);

		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private InputStream getImageInputStream() {
		InputStream stream = null;
		try {
			stream = resolver.openInputStream(uriPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return stream;
	}

	private int getPowerOfTwoForSampleRatio(double ratio){
		int k = Integer.highestOneBit((int)Math.floor(ratio));
		if(k==0) return 1;
		else return k;
	}

	private Bitmap rotateBitmap(Bitmap origin, float degrees) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);

		return Bitmap.createBitmap(origin , 0, 0, origin.getWidth(), origin.getHeight(), matrix, true);
	}

	public String getPathFromUri(Uri uri){
		Cursor cursor = resolver.query(uri, null, null, null, null );
		String path = "";
		if (cursor != null) {
			cursor.moveToNext();
			path = cursor.getString( cursor.getColumnIndex( "_data" ) );
			cursor.close();
		}

		return path;
	}

	public static int getImageOrientation(String path){

		int rotation =0;
		try {
			ExifInterface exif = new ExifInterface(path);
			int rot= exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			if(rot == ExifInterface.ORIENTATION_ROTATE_90){
				rotation = 90;
			}else if(rot == ExifInterface.ORIENTATION_ROTATE_180){
				rotation = 180;
			}else if(rot == ExifInterface.ORIENTATION_ROTATE_270){
				rotation = 270;
			}else{
				rotation = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rotation;
	}
}
