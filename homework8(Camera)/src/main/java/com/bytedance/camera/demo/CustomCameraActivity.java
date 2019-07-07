package com.bytedance.camera.demo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Trace;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_IMAGE;
import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_VIDEO;
import static com.bytedance.camera.demo.utils.Utils.getOutputMediaFile;

public class CustomCameraActivity extends AppCompatActivity {

    private static final String TAG = CustomCameraActivity.class.getSimpleName();
    private SurfaceView mSurfaceView;
    private Camera mCamera;

    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;

    private boolean isRecording = false;

    private int rotationDegree = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_camera);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO},0);
        }


        releaseCameraAndPreview();
        Log.d(TAG, "onCreate: camera number " + Camera.getNumberOfCameras());
        mCamera = getCamera(CAMERA_TYPE);


        mSurfaceView = findViewById(R.id.img);
        //todo 给SurfaceHolder添加Callback
        startPreview(mSurfaceView.getHolder());


        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            //todo 拍一张照片
            mCamera.takePicture(null, null, mPicture);
            //scanDirAsync(this,new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraDemo"));

        });

        findViewById(R.id.btn_record).setOnClickListener(v -> {
            //todo 录制，第一次点击是start，第二次点击是stop
            if (isRecording) {
                //todo 停止录制
                isRecording = false;
                releaseMediaRecorder();
                scanDirAsync(this,new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraDemo"));


            } else {
                //todo 录制
                if( prepareVideoRecorder()){
                    isRecording = true;
                    try{
                        mMediaRecorder.prepare();
                        mMediaRecorder.start();
                    }catch (Exception e){

                        releaseMediaRecorder();
                        e.printStackTrace();
                    }
                }
            }
        });

        findViewById(R.id.btn_facing).setOnClickListener(v -> {
            //todo 切换前后摄像头
            if(CAMERA_TYPE == Camera.CameraInfo.CAMERA_FACING_FRONT){
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
            }else if(CAMERA_TYPE == Camera.CameraInfo.CAMERA_FACING_BACK){
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
            try {
                mCamera.setPreviewDisplay(mSurfaceView.getHolder());
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //startPreview(mSurfaceView.getHolder());

        });




        findViewById(R.id.btn_zoom).setOnClickListener(v -> {
            //todo 调焦，需要判断手机是否支持
            mCamera.autoFocus(autoFocusCallback);
            if (mCamera.getParameters().isSmoothZoomSupported()) {
                try {
                    Camera.Parameters params = mCamera.getParameters();
                    final int MAX = params.getMaxZoom();
                    if(MAX!=0) {
                        int zoomValue = params.getZoom();
                        //Trace.Log("-----------------MAX:"+MAX+"   params : "+zoomValue);
                        zoomValue += 5;
                        params.setZoom(zoomValue);
                        mCamera.setParameters(params);
                        //Trace.Log("Is support Zoom " + params.isZoomSupported());
                    }
                } catch (Exception e) {
                    //Trace.Log("--------exception zoom");
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(this, "不支持调焦", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //success表示对焦成功
            if (success){
                Log.i("TAG", "myAutoFocusCallback:success...");
                //myCamera.setOneShotPreviewCallback(null);
            } else {
                //未对焦成功
                Log.i("TAG", "myAutoFocusCallback: 失败了...");
            }
        }
    };


    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
    public void scanDirAsync(Context ctx, File dir) {

        Log.d(TAG, "scanDirAsync: dir = ["+dir.getAbsolutePath()+"]" );
        if (dir.isDirectory()) {
            //测试f这个路径表示的文件是否是一个目录
            File[] files = dir.listFiles();//返回一个抽象（绝对）路径名数组，这些路径名表示此抽象路径名表示的目录中的文件
            if (files != null) {
                //初始化数组长度
                for (File file : files) {
                    if (file.isFile()) {
                        String[] paths = new String[2];
                        paths[0] = dir.getAbsolutePath() + "/" + file.getName();
                        Log.d(TAG, "scanDirAsync: paths = " + paths[0]);

                        MediaScannerConnection.scanFile(ctx, paths, new String[]{"image/*", "vedio/*"}, (path, uri) -> {
                            //扫描完成时逻辑
                            Log.d(TAG, "scanDirAsync: finished with path = " + path + ", uri = " + uri.toString());
                        });
                        Toast.makeText(ctx, paths[0], Toast.LENGTH_SHORT).show();
                    }
                }
            }
            Log.d(TAG, "scanDirAsync: ready to asyn ");


            //这里就可以直接用了，第三个这里用文件的后缀名，为空o

            /*MediaScannerConnection.scanFile(ctx, totalPath, new String[]{"image/*","vedio/*"}, (path, uri) -> {
                //扫描完成时逻辑
                Log.d(TAG, "scanDirAsync: finished with path = "+path+", uri = "+uri.toString());
            });*/


            Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
            scanIntent.setData(FileProvider.getUriForFile(this,"com.bytedance.camera.demo",dir));
            ctx.sendBroadcast(scanIntent);

            Log.d(TAG, "scanDirAsync() called with: ctx = [" + ctx + "], dir = [" + dir.getAbsolutePath()+ "]");
        }

    }


    public Camera getCamera(int position) {
        CAMERA_TYPE = position;
        if (mCamera != null) {
            releaseCameraAndPreview();
        }
        Camera cam = Camera.open(position);

        //todo 摄像头添加属性，例是否自动对焦，设置旋转方向等

        rotationDegree = getCameraDisplayOrientation(position);
        cam.setDisplayOrientation(rotationDegree);
//        mCamera.autoFocus(new Camera.AutoFocusCallback() {
//            @Override
//            public void onAutoFocus(boolean success, Camera camera) {
//
//            }
//        });
        return cam;
    }


    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }


    private void releaseCameraAndPreview() {
        //todo 释放camera资源
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }




    Camera.Size size;

    private void startPreview(SurfaceHolder holder) {
        //todo 开始预览
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCamera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCameraAndPreview();
            }
        });
    }


    private MediaRecorder mMediaRecorder;

    private boolean prepareVideoRecorder() {
        //todo 准备MediaRecorder
        mMediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
        mMediaRecorder.setOrientationHint(rotationDegree);


        return true;
    }


    private void releaseMediaRecorder() {
        //todo 释放MediaRecorder、
        if(mMediaRecorder!=null){
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try{
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                mCamera.lock();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }catch (RuntimeException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    private Camera.PictureCallback mPicture = (data, camera) -> {
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            Log.d(TAG, "pic data = "+data.toString());
            fos.close();
        } catch (IOException e) {
            Log.d("mPicture", "Error accessing file: " + e.getMessage());
        }
        mCamera.startPreview();

    };


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = Math.min(w, h);

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
