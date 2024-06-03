package com.example.text_detection;

import static androidx.camera.video.internal.compat.Api23Impl.build;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.text_detection.service.ApiService;
import com.example.text_detection.utils.CameraXViewModel;
import com.example.text_detection.utils.ImageUploader;
import com.example.text_detection.utils.ResponseInfo;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity  {
    private ProcessCameraProvider cameraProvider;
    private CameraSelector cameraSelector;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private Preview previewUseCase;
    private ImageAnalysis analysisUseCase;
    private PreviewView previewView;
    private Button imageCaptureButton;
    private ImageCapture imageCapture;
    private ImageView imgView;
    private LinearLayout btnPutImage;
    private ApiService apiService;
    java.util.concurrent.Executor cameraExecutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        previewView = findViewById(R.id.preview_view);
        imageCaptureButton = findViewById(R.id.image_capture_button);
        imgView = findViewById(R.id.imgView);
        btnPutImage = findViewById(R.id.btnPutImage);
//        previewView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                setupCamera();
//                // Loại bỏ listener để tránh gọi nhiều lần
//                previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });
        apiService = new ApiService();
        apiService.getTest(new ApiService.OnFinishGetDataListener() {
            @Override
            public void FinishGetData(ResponseInfo responseInfo, Exception e) {
                if(e!=null)
                    Toast.makeText(getApplicationContext(),"Hello", Toast.LENGTH_SHORT).show();
            }
        });
        setListener();
    }
    private void setListener(){
        imageCaptureButton.setOnClickListener(v->{
            //MoveToDetectWordActivity();
        });
        btnPutImage.setOnClickListener(v->{
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }
    public Bitmap imageProxyToBitmap(ImageProxy imageProxy) {
        @OptIn(markerClass = ExperimentalGetImage.class) Image image = imageProxy.getImage();
        if (image == null) {
            return null;
        }

        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    private void captureImage(){
        imageCapture.takePicture(cameraExecutor, new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                super.onCaptureSuccess(image);
                Bitmap bitmap = imageProxyToBitmap(image);
                ImageUploader imageUploader = new ImageUploader();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                super.onError(exception);
            }
        });
    }

    private void MoveToDetectWordActivity(){
        Intent intent = new Intent(this, DetectWordActivity.class);
        startActivity(intent);
    }
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new
                    ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    try {
                        imgView.setImageURI(uri);
                        apiService.uploadImageFromUri(getApplicationContext(), uri, new ApiService.OnFinishGetDataListener() {
                            @Override
                            public void FinishGetData(ResponseInfo responseInfo, Exception e) {
                                if(e!=null){
                                    Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.e("RuntimeException", e.getMessage());
                        throw new RuntimeException(e);

                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });
    //region setup camera for cameraX scan
    private void setupCamera() {
        cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        CameraXViewModel cameraXViewModel = new ViewModelProvider(this, factory).get(CameraXViewModel.class);
        cameraXViewModel.getProcessCameraProvider().observe(this, provider -> {
            cameraProvider = provider;
            if (isCameraPermissionGranted()) {
                bindCameraUseCases();
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.CAMERA},
                        PERMISSION_CAMERA_REQUEST
                );
            }
        });
    }

    private void bindCameraUseCases() {
        bindPreviewUseCase();
        bindAnalyseUseCase();
    }

    private void bindPreviewUseCase() {
        if (cameraProvider == null) {
            return;
        }
        if (previewUseCase != null) {
            cameraProvider.unbind(previewUseCase);
        }
        if(previewView.getDisplay()==null)
        {
            return;
        }
        previewUseCase = new Preview.Builder()
                .setTargetRotation(previewView.getDisplay().getRotation())
                .build();
        previewUseCase.setSurfaceProvider(previewView.getSurfaceProvider());

        try {
            cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    previewUseCase
            );
        } catch (IllegalStateException | IllegalArgumentException e) {
            Log.e(TAG, e.getMessage() != null ? e.getMessage() : e.toString());
        }
    }

    private void bindAnalyseUseCase() {


        if (cameraProvider == null) {
            return;
        }
        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase);
        }
        analysisUseCase = new ImageAnalysis.Builder()
                .build();

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor();

        analysisUseCase.setAnalyzer(
                cameraExecutor,
                this::processImageProxy
        );

        try {
            cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    analysisUseCase
            );
        } catch (IllegalStateException | IllegalArgumentException e) {
            Log.e(TAG, e.getMessage() != null ? e.getMessage() : e.toString());
        }
        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY).build();
        cameraProvider.bindToLifecycle((LifecycleOwner) this,cameraSelector,previewUseCase,imageCapture);
    }

    @OptIn(markerClass = ExperimentalGetImage.class) private void processImageProxy(
            ImageProxy imageProxy) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST) {
            if (isCameraPermissionGranted()) {
                bindCameraUseCases();
            } else {
                Log.e(TAG, "no camera permission");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(
                getBaseContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_CAMERA_REQUEST = 1;

//endregion
}