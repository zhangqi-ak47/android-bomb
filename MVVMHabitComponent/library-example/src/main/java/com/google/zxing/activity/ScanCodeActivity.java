package com.google.zxing.activity;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.goldze.mvvmhabit.R;
import com.goldze.mvvmhabit.databinding.ActivityScanCodeBinding;
import com.goldze.mvvmhabit.game.GM;
import com.goldze.mvvmhabit.game.NPC;
import com.goldze.mvvmhabit.game.boss.BossVm;
import com.goldze.mvvmhabit.game.role.BaseA;
import com.goldze.mvvmhabit.game.store.CommonResponse;
import com.goldze.mvvmhabit.game.store.CommonUI;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.decoding.CaptureActivityHandler;
import com.google.zxing.decoding.RGBLuminanceSource;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.util.BitmapUtil;
import com.google.zxing.util.Constant;
import com.google.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class ScanCodeActivity extends BaseA<ActivityScanCodeBinding, BossVm> implements SurfaceHolder.Callback {

    private static final int REQUEST_CODE_SCAN_GALLERY = 100;
    private boolean hasSurface=false;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private boolean playBeep;
    private boolean vibrate;
    private MediaPlayer mediaPlayer;
    private ViewfinderView viewfinderView;
    private static final float BEEP_VOLUME = 0.10f;
    private ProgressDialog mProgress;
    private Bitmap scanBitmap;
    private CaptureActivityHandler handler;

    private ImageButton btnFlash;
    private boolean isFlashOn = false;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, ScanCodeActivity.class);
        return intent;
    }

    @Override
    public int initLayout(Bundle savedInstanceState) {
        return R.layout.activity_scan_code;
    }

    @Override
    public int initArgument() {
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = binding.surfaceView;
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
        initAnim();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public int initFlow() {
        setNaviTitle(-1, "boss");

        CameraManager.init(getApplication());
        viewfinderView=binding.viewfinderView;
        return 0;
    }

    @Override
    public int initLayoutUpdate(CommonUI common) {
        if (NPC.LAYOUT_UPDATE_TAG.equals(common._tag)) {
            //更新界面UI
        }
        return 0;
    }

    private void initAnim(){
        binding.ascLine.post(new Runnable() {
            @Override
            public void run() {
                int height=ScreenUtils.getScreenHeight();
                int h=height/4;

                ObjectAnimator translationY = ObjectAnimator.ofFloat(binding.ascLine, "translationY", h, h*3, h);
                translationY.setRepeatCount(Animation.INFINITE);
                translationY.setInterpolator(new LinearInterpolator());
                translationY.setDuration(5000);
                translationY.start();
            }
        });
    }

    private void openLight(){
        try {
            boolean isSuccess = CameraManager.get().setFlashLight(!isFlashOn);
            if(!isSuccess){
                Toast.makeText(ScanCodeActivity.this, "暂时无法开启闪光灯", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isFlashOn) {
                // 关闭闪光灯
                btnFlash.setImageResource(R.drawable.flash_off);
                isFlashOn = false;
            } else {
                // 开启闪光灯
                btnFlash.setImageResource(R.drawable.flash_on);
                isFlashOn = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void openAlbum(){
        //打开手机中的相册
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
        innerIntent.setType("image/*");
        startActivityForResult(innerIntent, REQUEST_CODE_SCAN_GALLERY);
    }

    private void handleAlbumPic(Intent data) {
        //获取选中图片的路径
        final Uri uri = data.getData();

        mProgress = new ProgressDialog(ScanCodeActivity.this);
        mProgress.setMessage("正在扫描...");
        mProgress.setCancelable(false);
        mProgress.show();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Result result = scanningImage(uri);
                mProgress.dismiss();
                if (result != null) {
                    Toast.makeText(ScanCodeActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.INTENT_EXTRA_KEY_QR_SCAN ,result.getText());

                    resultIntent.putExtras(bundle);
                    ScanCodeActivity.this.setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(ScanCodeActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Result scanningImage(Uri uri) {
        if (uri == null) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        scanBitmap = BitmapUtil.decodeUri(this, uri, 500, 500);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    handleAlbumPic(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    public void handleDecode(Result result, Bitmap barcode) {
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //FIXME
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(ScanCodeActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ScanCodeActivity.this, resultString, Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.INTENT_EXTRA_KEY_QR_SCAN, resultString);
            System.out.println("sssssssssssssssss scan 0 = " + resultString);
            // 不能使用Intent传递大于40kb的bitmap，可以使用一个单例对象存储这个bitmap
//            bundle.putParcelable("bitmap", barcode);
//            Logger.d("saomiao",resultString);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
        }
        ScanCodeActivity.this.finish();
    }

    @Override
    public int initServerResponse(CommonResponse common) {
        if (common.success()) {
            switch (common._type) {
                case 1:
                    break;
            }
        } else if (common.getCode() > 0) {
            //服务器返回的问题
        } else {
            //自身的问题
        }
        return 0;
    }

    @Override
    public Class<BossVm> initVM() {
        return BossVm.class;
    }

    @Override
    public void onClickView(View view) {
        if (view.getId() == R.id.asc_close) {
            finish();
        }else if (view.getId()==R.id.asc_album){
            openAlbum();
        }
    }

    @Override
    public String onCommon(String tag, int type, Object obj) {
        super.onCommon(tag, type, obj);
        if (NPC.FRAGMENT_CALL_ACTIVITY_TAG.equals(tag)){
            switch (type){
                case 1:
                    break;
            }
        }

        return tag;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }
}
