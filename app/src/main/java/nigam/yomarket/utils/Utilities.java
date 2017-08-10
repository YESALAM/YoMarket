package nigam.yomarket.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public abstract class Utilities {
	public static final boolean D = true;
	private static String TAG = Utilities.class.getSimpleName();
	public static Thread updateThread;

	public static  String getStringImage(Bitmap bmp){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		return encodedImage;
	}
	//--------------------------------FIXME: Following conneciton to be work-----------------
	public static String readJson(Context ctx, String RequestMethod, String getURL){
				HttpURLConnection mConnection;
				try {
					if(Utilities.D) Log.v(TAG,"f_url[0]=" + getURL);
//--------------------part2---------------------------
//					SSLContext sc = SSLContext.getInstance("TLS");
//					sc.init(null, null, new java.security.SecureRandom());

					URL url = new URL(getURL);
					mConnection = (HttpURLConnection) url.openConnection();
//					mConnection.setSSLSocketFactory(sc.getSocketFactory());
					//mConnection.setRequestProperty("Accept","application/json,text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
					//mConnection.setRequestProperty("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/59.0.3071.109 Chrome/59.0.3071.109 Safari/537.36");
					mConnection.setReadTimeout(30000 /* milliseconds */);
					mConnection.setConnectTimeout(30000 /* milliseconds */);
					mConnection.setRequestMethod(RequestMethod);
					mConnection.setDoInput(true);
					mConnection.connect();
					int statusCode = mConnection.getResponseCode();
					if (statusCode != HttpURLConnection.HTTP_OK) {
						if(Utilities.D) Log.d(TAG,"unSuccessfullReading=" + statusCode);
						return "";
					}else{
						if(Utilities.D) Log.d(TAG,"ReadSuccessfull=" + statusCode);
						return readFromServer(mConnection);
					}
				} catch (UnsupportedEncodingException e1) { //e1.printStackTrace();
					if(Utilities.D) Log.e(TAG,"UnsupportedEncodingException=" + e1.getMessage());
					return "";
				} catch (IOException e) {//e.printStackTrace();
					if(Utilities.D) Log.e(TAG,"IOException=" + e.getMessage());
					return "";
				} catch (Exception e) {//e.printStackTrace();
					if(Utilities.D) Log.e(TAG,"NoConnectionError=" + e.getMessage());
					return "";
				}
			}

	public static boolean NetworkCheck(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			if(Utilities.D) Log.v(TAG, "NETWORK = Wifi");
			return true;
		}

		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			if(Utilities.D) Log.v(TAG, "NETWORK = Mobile");
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			System.out.println("NETWORK = Active network");
			return true;
		}
		if(Utilities.D) Log.v(TAG, "NETWORK = No Network");
		return false;
	}

	public static boolean isConnectingToInternet(Context ctx) {
		ConnectivityManager connectivity = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public static class WakeLocker {
		private static PowerManager.WakeLock wakeLock;

		public static void acquire(Context context) {
			if (wakeLock != null)
				wakeLock.release();

			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP
					| PowerManager.ON_AFTER_RELEASE, "WakeLock");
			wakeLock.acquire();
		}

		public static void release() {
			if (wakeLock != null)
				wakeLock.release();
			wakeLock = null;
		}
	}

	public static String readFromServer(HttpURLConnection connection) throws IOException {
    InputStreamReader stream = null;
    try {
        stream = new InputStreamReader(connection.getInputStream());
        BufferedReader br = new BufferedReader(stream);
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }
        if(Utilities.D) Log.d(TAG,"Response=" + sb.toString());
        return sb.toString();
    } finally {
        if (stream != null) {
            stream.close();
        }
    }
}
	public static String sendData(String UrlBase, String method, String urlParameters){
		HttpURLConnection httpConn = null;
		int statusCode = -1;
		try {
			if(Utilities.D) Log.v(TAG,"UrlBase=" + UrlBase);
			if(Utilities.D) Log.v(TAG,"urlParameters=" + urlParameters);

			URL url = new URL(UrlBase);
			URLConnection urlConn = url.openConnection();
			if (!(urlConn instanceof HttpURLConnection)) {
				throw new IOException("URL is not an Http URL!");
			}
			httpConn = (HttpURLConnection) urlConn;
			httpConn.setReadTimeout(30000);
			httpConn.setConnectTimeout(30000);
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
//	         httpConn.setChunkedStreamingMode(1024);
			httpConn.setRequestMethod(method);
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			httpConn.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			httpConn.setRequestProperty("Content-Language", "en-US");

			httpConn.setUseCaches (false);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
//		    httpConn.connect();

// 			Send request
			DataOutputStream wr = new DataOutputStream(httpConn.getOutputStream ());
			wr.writeBytes (urlParameters);
			wr.flush ();
			wr.close ();
			statusCode = httpConn.getResponseCode();
			Log.v(TAG,"hghvghvhgvhgvgh"+ HttpURLConnection.HTTP_OK+statusCode);
			if (statusCode == HttpURLConnection.HTTP_OK) {
				InputStreamReader stream = null;
				StringBuffer sb = null, sb_print = null;
				try {
					stream = new InputStreamReader(httpConn.getInputStream());
					BufferedReader br = new BufferedReader(stream);
					String line;
					sb = new StringBuffer();
					sb_print = new StringBuffer();
					while((line = br.readLine()) != null) {
						sb.append(line);
						if(Utilities.D){
							sb_print.append(line);
							sb_print.append("\n");
						}
					}
					br.close();
				} catch (IOException e) { e.printStackTrace();
				} finally {
					if (stream != null) {
						stream.close();
					}
				}
				if(Utilities.D) Log.i(TAG,"ReadSuccessfull=" + statusCode);
				if(Utilities.D) Log.i(TAG,"Response=" + sb_print.toString());
				return sb.toString();
			} else {
				if(Utilities.D) Log.e(TAG,"unSuccessfullReading=" + statusCode);
				return null;
			}
		}catch (MalformedURLException e) { //e.printStackTrace();
			if(Utilities.D) Log.e(TAG,"MalformedURLException=" + e.getMessage());
			return null;
		} catch (UnsupportedEncodingException e) {//e.printStackTrace();
			if(Utilities.D) Log.e(TAG,"UnsupportedEncodingError=" + e.getMessage());
			return null;
		} catch (Exception e) { //e.printStackTrace();
			if(Utilities.D) Log.e(TAG,"NoConnectionError=" + e.getMessage());
			return null;
		} finally {
			if(httpConn != null) {
				httpConn.disconnect();
			}
		}
	}

	public static String sendDataRaw(String UrlBase, String method, String urlParameters){
		HttpURLConnection httpConn = null;
		int statusCode = -1;
		try {
			if(Utilities.D) Log.v(TAG,"UrlBase=" + UrlBase);
			if(Utilities.D) Log.v(TAG,"urlParameters=" + urlParameters);

			URL url = new URL(UrlBase);
			URLConnection urlConn = url.openConnection();
			if (!(urlConn instanceof HttpURLConnection)) {
				throw new IOException("URL is not an Http URL!");
			}
			httpConn = (HttpURLConnection) urlConn;
			httpConn.setReadTimeout(30000);
			httpConn.setConnectTimeout(30000);
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
//	         httpConn.setChunkedStreamingMode(1024);
			httpConn.setRequestMethod(method);
			httpConn.setRequestProperty("Content-Type", "application/json");

			httpConn.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			httpConn.setRequestProperty("Content-Language", "en-US");

			httpConn.setUseCaches (false);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
//		    httpConn.connect();

// 			Send request
			DataOutputStream wr = new DataOutputStream(httpConn.getOutputStream ());
			wr.writeBytes (urlParameters);
			wr.flush ();
			wr.close ();
			statusCode = httpConn.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_OK) {
				InputStreamReader stream = null;
				StringBuffer sb = null, sb_print = null;
				try {
					stream = new InputStreamReader(httpConn.getInputStream());
					BufferedReader br = new BufferedReader(stream);
					String line;
					sb = new StringBuffer();
					sb_print = new StringBuffer();
					while((line = br.readLine()) != null) {
						sb.append(line);
						if(Utilities.D){
							sb_print.append(line);
							sb_print.append("\n");
						}
					}
					br.close();
				} catch (IOException e) { e.printStackTrace();
				} finally {
					if (stream != null) {
						stream.close();
					}
				}
				if(Utilities.D) Log.i(TAG,"ReadSuccessfull=" + statusCode);
				if(Utilities.D) Log.i(TAG,"Response=" + sb_print.toString());
				return sb.toString();
			} else {
				if(Utilities.D) Log.e(TAG,"unSuccessfullReading=" + statusCode);
				return null;
			}
		}catch (MalformedURLException e) { //e.printStackTrace();
			if(Utilities.D) Log.e(TAG,"MalformedURLException=" + e.getMessage());
			return null;
		} catch (UnsupportedEncodingException e) {//e.printStackTrace();
			if(Utilities.D) Log.e(TAG,"UnsupportedEncodingError=" + e.getMessage());
			return null;
		} catch (Exception e) { //e.printStackTrace();
			if(Utilities.D) Log.e(TAG,"NoConnectionError=" + e.getMessage());
			return null;
		} finally {
			if(httpConn != null) {
				httpConn.disconnect();
			}
		}
	}

	//	//---------------------------------UploadImage and Bitmap----------------------------------------
public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

	private int spanCount;
	private int spacing;
	private boolean includeEdge;

	public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
		this.spanCount = spanCount;
		this.spacing = spacing;
		this.includeEdge = includeEdge;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		int position = parent.getChildAdapterPosition(view); // item position
		int column = position % spanCount; // item column

		if (includeEdge) {
			outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
			outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

			if (position < spanCount) { // top edge
				outRect.top = spacing;
			}
			outRect.bottom = spacing; // item bottom
		} else {
			outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
			outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
			if (position >= spanCount) {
				outRect.top = spacing; // item top
			}
		}
	}
}
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){ex.printStackTrace();}
    }
	private Bitmap decodeFile(File file){
	        try {
	            BitmapFactory.Options opt = new BitmapFactory.Options();
	            opt.inJustDecodeBounds = true;
	            BitmapFactory.decodeStream(new FileInputStream(file),null,opt);
	            final int REQUIRED_SIZE=70;
	            int width_tmp=opt.outWidth, height_tmp=opt.outHeight;
	            int scale=1;
	            while(true){
	                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	                    break;
	                width_tmp/=2;
	                height_tmp/=2;
	                scale*=2;
	            }
	            BitmapFactory.Options opte = new BitmapFactory.Options();
	            opte.inSampleSize=scale;
	            return BitmapFactory.decodeStream(new FileInputStream(file), null, opte);
	        } catch (FileNotFoundException e) {e.printStackTrace();}
	        return null;
	    }
    public static String replaceSpaceInString(String s){
        int i;
        for (i=0;i<s.length();i++){
            System.out.println("i is "+i);
            if (s.charAt(i)==(int)32){
                s=s.substring(0, i)+"%20"+s.substring(i+1, s.length());
                i=i+2;
            }
        }
        return s;
    }
	public Bitmap getBitmapFromURL(String src) {
		try {
			java.net.URL url = new java.net.URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


		public static Bitmap compressImage(Bitmap bitmap) {
			if (bitmap != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
				int length = baos.size();
				Log.d("compressImg", "before compress  size:" + (length / 1024) + "KB");

				final int MIN_BYTE = 100 * 1024;
				// Less than 200K let it

				if (length > MIN_BYTE) {
					Log.d("compressImg", "large than 200KB begin compress.");
					// Compression ratio formula, can be self definition
					int quality = (int) (((length - MIN_BYTE) / 10.0 + MIN_BYTE) * 100 / length);
					Log.d("compressImg", "compress rate:" + quality + "%");
					baos.reset();

					if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)) {
						byte[] bs = baos.toByteArray();
						length = bs.length;
						Log.d("compressImage", "compress success new size:" + (length / 1024) + "KB");
						ByteArrayInputStream bais = new ByteArrayInputStream(bs);

						return BitmapFactory.decodeStream(bais);
					}
				} else {
					Log.d("compressImage", "Not compress less than 200K size:" + (length / 1024) + "KB");
				}
			} else {
				Log.d("compressImage", "decode bitmap error");
			}
			return bitmap;
		}
//
////-----------------------------Keyboard status--------------------------------
	public void showSoftKeyboard(Context ctx, View view){
	    if(view.requestFocus()){
	        InputMethodManager imm =(InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	    }
	}
	public void hideSoftKeyboard(Activity ctx, View view){
	    if(view.requestFocus()){
	    	ctx.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    }
	}
	public static void hideKeyboard(Activity activity) {
		View view = activity.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
//
////-----------------------------NetworkCheck--------------------------------

	public static String loadJSON(Context ctx, String fileName) {
		String json = null;
		try {
			InputStream is = ctx.getAssets().open(fileName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}public  boolean isInternetConnection(Context context)
    {

        ConnectivityManager connectivityManager =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    public static final boolean isInternetOn(Context ctx) {

		ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

			// Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
			return true;

		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

			Toast.makeText(ctx, "Please check Internet Connection", Toast.LENGTH_SHORT).show();
			return false;
		}
		return false;
	}

////-------------------------------STRING BUILDER EMAIL CHECK-------------------------------
	public static boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}
	public static Typeface getFont(Context ctx, String typeface) {
		return Typeface.createFromAsset(ctx.getAssets(), typeface);
	}
	public static boolean isNull(String str) {
		str = str.trim();
		if(str.isEmpty() || str.length() == 0 || str.equals(null) || str.equals("null") || str.equals("Null") || str.equals(" ") || str.equals("  "))
			return true;
		else
			return false;
	}
	public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");


	public static void updateToken(Context context,String id,String token){
		try {
			if(id == null) return ;
			String baseURL = apis.BASE_API+apis.TOKEN_UPDATE+"?r_id="+ id+"&d_id="+token;//+Statics.notificationcounterid;
			Log.i("Token Updateion","updating token --> "+Statics.id+"  --  "+token);

			Utilities.readJson(context, "GET", baseURL);


		} catch (Exception e) {
			e.printStackTrace();
		}
		return ;
	}
}

