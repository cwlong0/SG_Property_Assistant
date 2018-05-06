package com.softgrid.shortvideo.utils;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.softgrid.shortvideo.upload.VideoListPersistence;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Raymond Pen on 2016/7/25.
 */
public class CameraFunc {
    private Camera mCamera;
    //private PhotoUploadServer puServer;
    private SurfaceView mPhotoView;
    private Application app;
    private boolean isCapture = false;
    private int mCameraNum;
    private final static int INVALID_CAMERA_ID = -1;
    private int mFrontCameraId = INVALID_CAMERA_ID;
    private int mBackCameraId = INVALID_CAMERA_ID;
    private int mCurCameraId = INVALID_CAMERA_ID;
    public int PREVIEW_WIDTH = 1920;
    public int PREVIEW_HEIGHT = 1080;
    public int RECORD_WIDTH = 1280;
    public int RECORD_HEIGHT = 720;
    public final static int PHOTO_WIDTH = 1280;
    public final static int PHOTO_HEIGHT = 960;
    private int rotation;
    private MediaRecorder mRecorder = null;
    private boolean mIsRecording = false;
    private String lastRecodingFile = null;
    private Camera.PreviewCallback mPreviewCallBack;
    private String flashMode = Camera.Parameters.FLASH_MODE_OFF;
    private VideoListPersistence vlp;

    public CameraFunc(Application app, SurfaceView photo) {
        this.app = app;
        vlp = new VideoListPersistence(app);
        //puServer = server;
        mPhotoView = photo;
        WindowManager mWindowManager = (WindowManager) app.getSystemService(Application.WINDOW_SERVICE);
        rotation = mWindowManager.getDefaultDisplay().getRotation();

        mCameraNum = Camera.getNumberOfCameras();
        for (int i = 0; i < mCameraNum; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mFrontCameraId = i;
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mBackCameraId = i;
            }
        }
//        if (mBackCameraId != INVALID_CAMERA_ID) {
//            mCurCameraId = mBackCameraId;
//        } else if (mFrontCameraId != INVALID_CAMERA_ID) {
//            mCurCameraId = mFrontCameraId;
//        }
        if (mBackCameraId != INVALID_CAMERA_ID) {
            mCurCameraId = mBackCameraId;
        } else if (mFrontCameraId != INVALID_CAMERA_ID) {
            mCurCameraId = mFrontCameraId;
        }
//        SurfaceView preview = (SurfaceView) findViewById(R.id.preview);
//        mHolder = preview.getHolder();
//        mHolder.addCallback(new PreviewCallback());
//        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public boolean isCameraStart() {
        if (mCamera != null) return true;
        return false;
    }

    public boolean cameraStart(SurfaceHolder holder) {
        try {
            if (isCameraStart()) return true;
            mPreviewCallBack = new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] bytes, Camera camera) {
                }
            };

            mCamera = Camera.open(mCurCameraId);
            mCamera.enableShutterSound(false);
            Log.d("ScheduleCenter", " camer open1");
            initCarmeraSize();
            setCameraDisplayOrientationAndSize(holder);
            if (holder != null) {
                mCamera.setPreviewDisplay(holder);
            }

            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void cameraStop() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                Log.d("ScheduleCenter", " camer realse1");
                mCamera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isFlashModeOn() {
        if (mCamera != null) {
            if (flashMode.equals(Camera.Parameters.FLASH_MODE_OFF)) {
                return false;
            } else if (flashMode.equals(Camera.Parameters.FLASH_MODE_ON)) {
                return true;
            }
        }
        return false;
    }

    public String switchFlashMode() {
        if (mCamera != null) {
            if (flashMode.equals(Camera.Parameters.FLASH_MODE_OFF)) {
                flashMode = Camera.Parameters.FLASH_MODE_ON;
            } else if (flashMode.equals(Camera.Parameters.FLASH_MODE_ON)) {
                flashMode = Camera.Parameters.FLASH_MODE_OFF;
            }
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(flashMode);
            mCamera.setParameters(params);
        }

        return flashMode;
    }

    private void setCameraDisplayOrientationAndSize(SurfaceHolder holder) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCurCameraId, info);

        //int rotation = app.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = rotation * 90;

		/*
         * // the above is just a shorter way of doing this, but could break if
		 * the values change switch (rotation) { case Surface.ROTATION_0:
		 * degrees = 0; break; case Surface.ROTATION_90: degrees = 90; break;
		 * case Surface.ROTATION_180: degrees = 180; break; case
		 * Surface.ROTATION_270: degrees = 270; break; }
		 */

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);

        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        if ((result == 90 || result == 270) && holder != null) {
            // swap - the physical camera itself doesn't rotate in relation
            // to the screen ;)
            holder.setFixedSize(previewSize.height, previewSize.width);
        } else {
            holder.setFixedSize(previewSize.width, previewSize.height);
        }
    }

    private void initCarmeraSize() {
        final Camera.Parameters params = mCamera.getParameters();

        List<Integer> formats = params.getSupportedPreviewFormats();
        List<Camera.Size> videoSize = params.getSupportedVideoSizes();
        List<Camera.Size> previewSize = params.getSupportedPreviewSizes();
        List<Camera.Size> sizeList = params.getSupportedPictureSizes();
        //params.setFlashMode(flashMode);
        if (previewSize != null && previewSize.size() > 0) {
            //params.setPictureSize(PHOTO_WIDTH, PHOTO_HEIGHT);
            setPreviewSize(previewSize);
            params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
        }

        if (videoSize != null && videoSize.size() > 0) {
            setVideoSize(videoSize);
        } else {
            RECORD_WIDTH = PREVIEW_WIDTH;
            RECORD_HEIGHT = PREVIEW_HEIGHT;
        }
        mCamera.setParameters(params);
    }

    private void setPreviewSize(List<Camera.Size> previewSize) {
        int screenWidth = ScreenSize.getScreenWidth(app);
        int screenHeight = ScreenSize.getRealHeight(app);
        Camera.Size size = getOptimalPreviewSize(previewSize, screenHeight, screenWidth);
        PREVIEW_WIDTH = size.width;
        PREVIEW_HEIGHT = size.height;
        Log.e("raymond", size.width + "  " + size.height);
    }

    private void setVideoSize(List<Camera.Size> videoSize) {
        Camera.Size size = getOptimalPreviewSize(videoSize, RECORD_WIDTH, RECORD_HEIGHT);
        RECORD_WIDTH = size.width;
        RECORD_HEIGHT = size.height;
        Log.e("raymond", size.width + "  " + size.height);
    }
/*
        private void uploadPhoto(int kind, String path) {
            PhotoEntity photoData = new PhotoEntity();
            photoData.kind = kind;
            photoData.time = System.currentTimeMillis();
            photoData.url = path;
            photoData.uploaded = 0;
            Log.e("raymond", "before uploadPhoto called");
            puServer.uploadPhoto(photoData);
            Log.e("raymond", "uploadPhoto called");
            //生成视频预览图
            if (photoData.kind == 2) {
                Bitmap bitmap = null;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                    retriever.setDataSource(photoData.url);
                    bitmap = retriever.getFrameAtTime();
                    saveBitmap(bitmap, photoData.url.replace(".mp4", ".jpg"));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        retriever.release();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
*/
    public Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.2;
        double targetRatio = (double) w / h;
        if (sizes == null) {
            return null;
        }
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
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

    public interface BitmapPictureCallBack {
        public void onPictureTaken(String path);
    }

    public void takePicture(final BitmapPictureCallBack cb) {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    Log.e("raymond", "taken time");
                    Bitmap bitmap = null;
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    // 让位图工厂真正的去解析图片
                    opts.inJustDecodeBounds = false;
                    opts.inPreferredConfig = Bitmap.Config.RGB_565;
                    opts.inPurgeable = true;
                    opts.inInputShareable = true;
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
//                    Bitmap temp = Bitmap.createBitmap(bitmap, 0, (PHOTO_HEIGHT - 860) / 2, 1280, 860, null, false);
//                    bitmap.recycle();
//                    bitmap = temp;
                    String imgName = FileOperateUtil.createFileName(".jpg");
                    String imagePath = FileOperateUtil.getFolderPath(app, FileOperateUtil.TYPE_IMAGE) + File.separator + imgName;

                    saveBitmap(bitmap, imagePath);
                    cb.onPictureTaken(imagePath);
                    //uploadPhoto(0, imagePath);
                    mCamera.stopPreview();
                    //mCamera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveBitmap(Bitmap bitmap, String imagePath) {
        File photo = new File(imagePath);
        if (photo.exists()) {
            photo.delete();
        }
        if (!photo.getParentFile().exists()) {
            photo.getParentFile().mkdirs();
        }

        Log.e("raymond", "图片路径" + photo.getAbsolutePath());
        if (photo.exists()) {
            photo.delete();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(photo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean startRecord() {
        if (mIsRecording) return mIsRecording;
        mRecorder = new MediaRecorder();//实例化
        mCamera.unlock();
        //给Recorder设置Camera对象，保证录像跟预览的方向保持一致
        mRecorder.setCamera(mCamera);
        //mRecorder.setOrientationHint(90);  //改变保存后的视频文件播放时是否横屏(不加这句，视频文件播放的时候角度是反的)
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置从麦克风采集声音
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 设置从摄像头采集图像
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  // 设置视频的输出格式 为MP4
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); // 设置音频的编码格式
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264); // 设置视频的编码格式
        mRecorder.setVideoSize(RECORD_WIDTH, RECORD_HEIGHT);
        mRecorder.setVideoEncodingBitRate(1 * 1024 * 1024);
        //mRecorder.setVideoFrameRate(20); // 设置帧率
        mRecorder.setOrientationHint(90);
        //mRecorder.setMaxDuration(30000); //设置最大录像时间为30s
        mRecorder.setPreviewDisplay(mPhotoView.getHolder().getSurface());

        String imgName = FileOperateUtil.createFileName(".mp4");
        lastRecodingFile = FileOperateUtil.getFolderPath(app, FileOperateUtil.TYPE_VIDEO) + File.separator + imgName;

        File photo = new File(lastRecodingFile);
        if (photo.exists()) {
            photo.delete();
        }
        if (!photo.getParentFile().exists()) {
            photo.getParentFile().mkdirs();
        }

        Log.e("raymond", "视频路径" + photo.getAbsolutePath());
        if (photo.exists()) {
            photo.delete();
        }
        mRecorder.setOutputFile(photo.getPath());
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            failRecording();
            mIsRecording = false;
            return mIsRecording;
        }
        mIsRecording = true;
        return mIsRecording;
    }

    public boolean isRecording() {
        return mIsRecording;
    }

    public void failRecording() {
        if (mCamera != null) {
            mCamera.lock();
        }
        try {
            if (mRecorder != null) {
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void stopRecording() {
        if (!mIsRecording) return;
        if (mCamera != null) {
            mCamera.lock();
        }
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //uploadPhoto(2, lastRecodingFile);
        vlp.saveVideo(lastRecodingFile);
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(lastRecodingFile);
            bitmap = retriever.getFrameAtTime();
            saveBitmap(bitmap, lastRecodingFile.replace(".mp4", ".jpg"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        mIsRecording = false;
    }

    public void interruptRecording() {
        if (!mIsRecording) return;
        if (mCamera != null) {
            mCamera.lock();
        }
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        mIsRecording = false;
    }
}
