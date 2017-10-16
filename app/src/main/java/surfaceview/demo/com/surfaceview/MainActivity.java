package surfaceview.demo.com.surfaceview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import surfaceview.demo.com.surfaceview.views.CustomView;

public class MainActivity extends AppCompatActivity {
    Paint paint = new Paint();

    SurfaceView cameraView;
    BarcodeDetector barcode;
    CameraSource cameraSource;
    SurfaceHolder holder;
    private Boolean doOnce = true;
    public static final int REQUEST_CODE = 1;
    public static final int PERMISSION_REQUEST = 200;
    public static double screenInches;
    public static int widthPx;
    public static int heightPx;
    private TextView tv;
    private DisplayMetrics dm;
    public static float multiplier;
    public static double cameraViewWidth;
    public static double camerViewHeight;
    public static double cameraViewLeft;
    public static double cameraViewTop;
    private CustomView customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.textview);

        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        widthPx =dm.widthPixels;
        heightPx =dm.heightPixels;
        double wi=(double)widthPx/(double)dm.xdpi;
        double hi =(double)heightPx/(double)dm.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        screenInches = Math.sqrt(x+y);
        multiplier = (float) (dm.widthPixels/1440.0);
        Log.d("dimens", Integer.toString(widthPx) + " " + Integer.toString(heightPx) + " " + Double.toString(screenInches));

//        tv.setText("DensityDpi: " + dm.densityDpi + "\n" + "Display Density: " + dm.density + "\n" +
//        "xdpi: " + dm.xdpi + "\n" + "pixels width: " + dm.widthPixels + "\n" + "ydpi: " + dm.ydpi +"\n" +
//                "pixels height: " + dm.heightPixels + "\nmultiplier: " + multiplier);

        Log.d("dimens", "densityDpi: " + dm.densityDpi);
        Log.d("dimens", "display density: " + dm.density);
        Log.d("dimens", "xdpi: " + dm.xdpi);
        Log.d("dimens", "width: " + dm.widthPixels);
        Log.d("dimens", "ydpi: " + dm.ydpi);
        Log.d("dimens", "height: " + dm.heightPixels);

        customView = (CustomView)findViewById(R.id.customeView1);
        customView.drawBrackets();
        //customView.setY(0);

//        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT); //WRAP_CONTENT param can be FILL_PARENT
//        params.leftMargin = 500; //XCOORD
//        params.topMargin = 500; //YCOORD
//        customView.setLayoutParams(params);





        //barcodeTextView = (TextView)findViewById(R.id.barcodeTextView);
        cameraView = (SurfaceView)findViewById(R.id.cameraView);
        cameraView.setZOrderMediaOverlay(true);
        holder = cameraView.getHolder();
        barcode = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);

        }

        if(!barcode.isOperational()){
            Toast.makeText(getApplicationContext(), "Sorry could not setup detector!", Toast.LENGTH_LONG).show();
            this.finish();
        }
        cameraSource = new CameraSource.Builder(this, barcode)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1687, 900)
                .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(cameraView.getHolder());
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                android.view.ViewGroup.LayoutParams lp = cameraView.getLayoutParams();
//                lp.width = (int) (widthPx * 0.6); // required width
//                lp.height = (int) (heightPx * 0.6); // required height
                double ratio = screenInches/10;
                lp.width = (int) (widthPx * 0.7); // required width
                lp.height = (int) (heightPx * ratio); // required height
                cameraView.setLayoutParams(lp);
                cameraViewLeft = cameraView.getLeft();
                cameraViewTop = cameraView.getTop();
                cameraViewWidth = lp.width;
                camerViewHeight = lp.height;
                displayData();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        barcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                //Log.d("Detection", "Detector1");
//                if (barcodes.size() > 0) {
//                    if(doOnce) {
//                        Barcode thisCode = barcodes.valueAt(0);
//
//
//                        Log.v("BarcodeData2", thisCode.rawValue);
//                        //Log.v("BarcodeData2", barcode.displayValue);
//                        //barcodeTextView.setText(thisCode.rawValue);
//                        //Intent intent = new Intent();
//                        //Toast.makeText(getApplicationContext(), thisCode.rawValue, Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
//                        intent.putExtra("barcode", thisCode.rawValue);
//                        //intent.putExtra("barcode", "data");
//                        startActivityForResult(intent, REQUEST_CODE);
//                        //setResult(RESULT_OK, intent);
//                        //finish();
//                        doOnce = false;
//                    }
//                }
            }

        });


    }

    private void displayData(){
        tv.setText("DensityDpi: " + dm.densityDpi + "\n" + "Display Density: " + dm.density + "\n" +
                "xdpi: " + dm.xdpi + "\n" + "pixels width: " + dm.widthPixels + "\n" + "ydpi: " + dm.ydpi +"\n" +
                "pixels height: " + dm.heightPixels + "\nmultiplier: " + multiplier + "\ncameraViewWidth: " + cameraViewWidth +
        "\ncameraViewHeigth: " + camerViewHeight + "\ncameraViewTop: " + cameraViewTop + "\ncameraViewLeft: " + cameraViewLeft +
        "\ncustomViewHeigth: " + customView.getHeight() + "\ncustomViewWidth: " + customView.getWidth());
    }



}

