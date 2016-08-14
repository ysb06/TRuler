package pe.hgs.truler.tools;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

enum PathType { URI, FILE }

/** 경로로부터 이미지를 읽는다. 읽어 들이는 이미지는 다운 사이징하여 읽어들이며 크기가 큰 이미지의 경우 처리하는 시간이 길어 질 수 있다.
 * Created by ysb06 on 2016-08-08.
 */
public class ImageLoader {

	/**	이미지 처리가 이루이지는 이미지 최소 크기 */
	public static final int IMAGE_MAX_SIZE = 1024;		//만약 표시되는 이미지의 화질을 높이고 싶을 경우 값을 증가시킨다.

	private PathType ImagePathType;
	private ContentResolver resolver;
	private Uri uriPath;
	private String sFilePath;

	private Bitmap bitImage;

	/** ImageLoader 객체 초기화, 경로로부터 이미지를 로드.
	 *
	 * @param resolver URI 처리를 위한 Context.ContentResolver
	 * @param path URI 경로
	 */
	public ImageLoader(ContentResolver resolver, Uri path) {
		ImagePathType = PathType.URI;
		this.resolver = resolver;
		this.uriPath = path;

		bitImage = loadImage();
	}

	/** ImageLoader 객체 초기화, 경로로부터 이미지를 로드.
	 *
	 * @param path 이미지 파일 경로
	 */
	public ImageLoader(String path) {
		ImagePathType = PathType.FILE;
		this.sFilePath = path;

		bitImage = loadImage();
	}

	/** 처리되어 메모리에 저장된 Bitmap 반환
	 *
	 * @return Bitmap 이미지
	 */
	public Bitmap getImage() {
		return bitImage;
	}

	/** 리소스 해제, 이미지를 사용하지 않게 될 경우 반드시 불러주어야 Memory Leak을 방지할 수 있다. */
	public void dispose() {
		bitImage.recycle();
	}

	private Bitmap loadImage() {

		InputStream stream = getImageInputStream();

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(stream, null, onlyBoundsOptions);
		try {
			stream.close();
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
			switch (ImagePathType) {
				case URI:
					stream = resolver.openInputStream(uriPath);
					break;
				case FILE:
					File fileImage = new File(sFilePath);
					stream = new FileInputStream(fileImage);
					break;
			}
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
}
