package com.sachinshinde.theweatherapp.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utilities {
	public static int getStatusBarHeight(Context mContext) {
		int result = 0;
		int resourceId = mContext.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = mContext.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static void createShareIntent(Context context, String appName,
			String packageName) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");

		intent.putExtra(Intent.EXTRA_TEXT, "Checkout the app " + appName
				+ " : " + "http://play.google.com/store/apps/details?id="
				+ packageName);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(Intent.createChooser(intent, "Share").addFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK));

	}

	

	public static void launchMarket(Context context, String packageName) {
		Uri uri = Uri.parse("market://details?id=" + packageName);
		Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
		myAppLinkToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		try {
			context.startActivity(myAppLinkToMarket);
		} catch (ActivityNotFoundException e) {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("http://play.google.com/store/apps/details?id="
							+ packageName))
					.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}
	}

	/**
	 * Convert Uri into File.
	 * 
	 * @param uri
	 * @return file
	 */
	public static File getFile(Uri uri) {
		if (uri != null) {
			String filepath = uri.getPath();
			if (filepath != null) {
				return new File(filepath);
			}
		}
		return null;
	}

	/**
	 * Copy all files from one folder to another
	 * 
	 * @param sourceLocation
	 * @param targetLocation
	 * @throws java.io.IOException
	 */
	public static void copyFiles(File sourceLocation, File targetLocation)
			throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}
			File[] files = sourceLocation.listFiles();
			for (File file : files) {
				InputStream in = new FileInputStream(file);
				OutputStream out = new FileOutputStream(targetLocation + "/"
						+ file.getName());

				// Copy the bits from input stream to output stream
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		}
	}

	/**
	 * Copy specified file to target folder
	 * 
	 * @param File
	 *            sourceFile
	 * @param File
	 *            targetLocation
	 * @throws java.io.IOException
	 */

	public static void copyFile(File sourceFile, File targetLocation)
			throws IOException {

		if (!targetLocation.exists()) {
			targetLocation.mkdir();
		}
		// File[] files = sourceLocation.listFiles();
		// for (File file : files) {
		InputStream in = new FileInputStream(sourceFile);
		OutputStream out = new FileOutputStream(targetLocation + "/"
				+ sourceFile.getName());

		// Copy the bits from input stream to output stream
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		// }

	}

	/**
	 * Convert dp to px
	 * 
	 * @author Sachin
	 * @param i
	 * @param mContext
	 * @return
	 */

	public static int dpToPx(int i, Context mContext) {

		DisplayMetrics displayMetrics = mContext.getResources()
				.getDisplayMetrics();
		return (int) ((i * displayMetrics.density) + 0.5);

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	public static void setDrawable(View ll, Drawable drawable) {
		int sdk = Build.VERSION.SDK_INT;
		if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
			ll.setBackgroundDrawable(drawable);
		} else {
			ll.setBackground(drawable);
		}
	}

	@SuppressLint("NewApi")
	public static Drawable generatePatternBitmap(Context mContext, int width,
			int height) {

		Bitmap mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(mBitmap);

		int mRectangleSize = (int) (5 * mContext.getResources()
				.getDisplayMetrics().density);
		int numRectanglesHorizontal = (int) Math.ceil((width / mRectangleSize));
		int numRectanglesVertical = (int) Math.ceil(height / mRectangleSize);

		Rect r = new Rect();
		boolean verticalStartWhite = true;
		for (int i = 0; i <= numRectanglesVertical; i++) {

			boolean isWhite = verticalStartWhite;
			for (int j = 0; j <= numRectanglesHorizontal; j++) {

				r.top = i * mRectangleSize;
				r.left = j * mRectangleSize;
				r.bottom = r.top + mRectangleSize;
				r.right = r.left + mRectangleSize;

				Paint mPaintWhite = new Paint(Paint.FILTER_BITMAP_FLAG);
				mPaintWhite.setColor(0xffffffff);
				Paint mPaintGray = new Paint(Paint.FILTER_BITMAP_FLAG);
				mPaintGray.setColor(0xffcbcbcb);

				canvas.drawRect(r, isWhite ? mPaintWhite : mPaintGray);

				isWhite = !isWhite;
			}

			verticalStartWhite = !verticalStartWhite;

		}

		Drawable d = new BitmapDrawable(mContext.getResources(), mBitmap);

		return d;

	}

	public static Drawable generatePatternBitmap(Context mContext) {

		int width = dpToPx(50, mContext);
		int height = dpToPx(50, mContext);

		Bitmap mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(mBitmap);

		int mRectangleSize = (int) (5 * mContext.getResources()
				.getDisplayMetrics().density);
		int numRectanglesHorizontal = (int) Math.ceil((width / mRectangleSize));
		int numRectanglesVertical = (int) Math.ceil(height / mRectangleSize);

		Rect r = new Rect();
		boolean verticalStartWhite = true;
		for (int i = 0; i <= numRectanglesVertical; i++) {

			boolean isWhite = verticalStartWhite;
			for (int j = 0; j <= numRectanglesHorizontal; j++) {

				r.top = i * mRectangleSize;
				r.left = j * mRectangleSize;
				r.bottom = r.top + mRectangleSize;
				r.right = r.left + mRectangleSize;

				Paint mPaintWhite = new Paint(Paint.FILTER_BITMAP_FLAG);
				mPaintWhite.setColor(0xffffffff);
				Paint mPaintGray = new Paint(Paint.FILTER_BITMAP_FLAG);
				mPaintGray.setColor(0xffcbcbcb);

				canvas.drawRect(r, isWhite ? mPaintWhite : mPaintGray);

				isWhite = !isWhite;
			}

			verticalStartWhite = !verticalStartWhite;

		}

		Drawable d = new BitmapDrawable(mContext.getResources(), mBitmap);

		return d;

	}

	/**
	 * Creates a Bitmap with specified color and of 50dp size
	 * 
	 * @param color
	 * @param context
	 * @return Colored Bitmap
	 */
	public static Bitmap RetBitCol(int color, Context mContext) {

		Bitmap mBitmap = Bitmap.createBitmap(dpToPx(50, mContext),
				dpToPx(50, mContext), Config.ARGB_8888);
		Canvas c = new Canvas(mBitmap);
		c.drawColor(color);
		return mBitmap;
	}

	public static void CallNumber(String cNumber, Context mContext) throws Exception{
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + cNumber));
		mContext.startActivity(callIntent
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	public static void SendSmS(String cNumber, Context mContext) throws Exception{
		mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts(
				"sms", cNumber, null)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

	}

	public static Bitmap convtocircle(int id, int color, Context mContext) {
		Options options = new Options();
		options.inScaled = false;
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
				id, options);
		Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),

		bitmap.getHeight(), Config.ARGB_8888);
		BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		paint.setShader(shader);
		paint.setAntiAlias(true);

		Canvas c = new Canvas(circleBitmap);
		c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		Paint mypaint = new Paint(Paint.FILTER_BITMAP_FLAG);
		mypaint.setAntiAlias(true);
		mypaint.setColor(color);
		mypaint.setStyle(Style.STROKE);
		mypaint.setStrokeWidth(2.5f);
		c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				(float) (bitmap.getWidth() / 2 - 2.5), mypaint);
		return circleBitmap;
	}

	public static Bitmap convtocircle(Bitmap bitmap, int color) {

		Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),

		bitmap.getHeight(), Config.ARGB_8888);
		BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		paint.setShader(shader);
		paint.setAntiAlias(true);

		Canvas c = new Canvas(circleBitmap);
		c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		Paint mypaint = new Paint(Paint.FILTER_BITMAP_FLAG);
		mypaint.setAntiAlias(true);
		mypaint.setColor(color);
		mypaint.setStyle(Style.STROKE);
		mypaint.setStrokeWidth(2.5f);
		c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				(float) (bitmap.getWidth() / 2 - 1), mypaint);
		return circleBitmap;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	public static Bitmap patchBarDrawable(int mHeight, int mWidth, int mColor) {
		Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		c.drawRoundRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()),
				7f, 7f, mPaint);
		return bitmap;
	}

	public static Bitmap patchBarnBlankDrawable(int mHeight, int mWidth,
			int mColor, int totalHeight, int mGravity) {
		Bitmap bitmap = Bitmap.createBitmap(mWidth, totalHeight,
				Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		int top = 0;
		switch (mGravity) {
		case Gravity.TOP:
			top = 0;
			break;
		case Gravity.CENTER:
			top = (totalHeight / 2) - (mHeight / 2);
			break;
		case Gravity.BOTTOM:
			top = totalHeight - mHeight;
			break;
		}
		c.drawRoundRect(new RectF(0, top, bitmap.getWidth(), mHeight), 7f, 7f,
				mPaint);
		return bitmap;
	}

	public static char enCode(char s) {
		switch (s) {
		case '1':
			return 'p';
		case '2':
			return 'z';
		case '3':
			return 'a';
		case '4':
			return 'c';
		case '5':
			return 't';
		case '6':
			return 'l';
		case '7':
			return 'r';
		case '8':
			return 's';
		case '9':
			return 'n';
		case '0':
			return 'b';
		case 'p':
			return '1';
		case 'z':
			return '2';
		case 'a':
			return '3';
		case 'c':
			return '4';
		case 't':
			return '5';
		case 'l':
			return '6';
		case 'r':
			return '7';
		case 's':
			return '8';
		case 'n':
			return '9';
		case 'b':
			return '0';
		case '-':
			return '+';
		case '+':
			return '-';
		default:
			return '0';

		}
	}

	

	public static Drawable BitmapToDrawable(Bitmap bitmap, Context mContext) {

		Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
		return drawable;
	}

	/**
	 * @author Sachin Convert dp to px
	 */
	public static int ReturnLength(int i, Context mContext) {

		DisplayMetrics displayMetrics = mContext.getResources()
				.getDisplayMetrics();
		return (int) ((i * displayMetrics.density) + 0.5);

	}

	



	public static Long appInstalled(Context mContext) {
		try {
			long first;

			File dir = new File(Environment.getExternalStorageDirectory()
					+ "/Android/data/.android_secure/.everything/");
			dir.mkdirs();

			File lastModified;
			if (dir.listFiles().length > 0
					&& dir.listFiles()[0].getName().contains(".io")) {
				lastModified = dir.listFiles()[0];
			} else {
				String string = System.currentTimeMillis() + "";
				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 0; i < string.length(); i++) {
					stringBuilder.append(Utilities.enCode(string.charAt(i)));
				}
				lastModified = new File(
						Environment.getExternalStorageDirectory()
								+ "/Android/data/.android_secure/.everything/"
								+ stringBuilder.toString() + ".io");
			}
			if (!lastModified.exists()) {
				lastModified.createNewFile();
			}
			String myString = (lastModified.getName()).substring(0,
					lastModified.getName().indexOf('.'));
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < myString.length(); i++) {
				stringBuilder.append(Utilities.enCode(myString.charAt(i)));
			}
			first = Long.parseLong(stringBuilder.toString());

			return first;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static boolean checkIfTrialExpired(Context mContext) {
		long date = appInstalled(mContext);
		long now = System.currentTimeMillis();

		long diff = now - date;

		if (diff < 0) {
			return false;

		} else if (diff >= 864000000) {
			return true;
		} else {
			return false;
		}

	}

	public static String generatePayLoad(Context mContext) {

		StringBuilder build = new StringBuilder();
		String[] arr = getAccountNames(mContext);
		for (int i = 0; i < arr.length; i++)
			build.append(arr[i] + "//");

		// new Logger(getBaseContext()).Toast(build.toString());
		return build.toString();
	}

	public static String[] getAccountNames(Context mContext) {
		AccountManager mAccountManager = AccountManager.get(mContext);
		Account[] accounts = mAccountManager.getAccountsByType("com.google");
		String[] names = new String[accounts.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = accounts[i].name;
		}
		return names;
	}

}
