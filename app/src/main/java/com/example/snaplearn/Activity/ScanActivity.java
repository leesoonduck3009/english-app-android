package com.example.snaplearn.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.snaplearn.R;
import com.example.snaplearn.databinding.ActivityScanBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScanActivity extends AppCompatActivity {

    private ActivityScanBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private ActivityResultLauncher<Intent> takePictureLauncher;
    private Uri photoUri;
    private static final int REQUEST_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityScanBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        binding.btnShowSelect.setOnClickListener(v -> {
            binding.frameSelect.setVisibility(View.VISIBLE);
        });

        binding.btnClose.setOnClickListener(v -> {
            binding.frameSelect.setVisibility(View.GONE);
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Lấy URI của ảnh được chọn
                        photoUri = result.getData().getData();
                        // Thực hiện việc xử lý ảnh đã chọn, ví dụ hiển thị ảnh trong ImageView
                        binding.btnShowSelect.setImageURI(photoUri);
                        if (photoUri != null) {
                            binding.btnStartLearn.setEnabled(true);
                            binding.btnStartLearn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightlightgreen));
                        }
                    }
                });

        // Đặt OnClickListener cho btnSelectPic
        binding.btnSelectPic.setOnClickListener(v -> {
            // Tạo một Intent để mở thư viện ảnh
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Khởi động Activity để chọn ảnh
            activityResultLauncher.launch(intent);
        });

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Hiển thị ảnh chụp trong ImageView
                        binding.btnShowSelect.setImageURI(photoUri);
                        if (photoUri != null) {
                            binding.btnStartLearn.setEnabled(true);
                            binding.btnStartLearn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.lightlightgreen));
                        }

                    }
                });

        // Đặt OnClickListener cho btnTakePic
        binding.btnTakePic.setOnClickListener(v -> {
            if (checkPermissions()) {
                // Tạo một Intent để mở máy ảnh
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Đảm bảo rằng có một Activity có thể xử lý Intent này
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Tạo một file để lưu ảnh
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Xử lý lỗi khi tạo file
                        ex.printStackTrace();
                    }
                    // Tiếp tục chỉ khi file đã được tạo thành công
                    if (photoFile != null) {
                        photoUri = FileProvider.getUriForFile(this, "com.example.snaplearn.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        takePictureLauncher.launch(intent);
                    }
                }
            } else {
                requestPermissions();
            }
        });

        binding.btnStartLearn.setOnClickListener(v-> {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("PHOTO_URI",photoUri.toString());
            startActivity(intent);
        });

        binding.btnBack.setOnClickListener(v-> {
            finish();
        });
    }

    private File createImageFile() throws IOException {
        // Tạo tên file ảnh duy nhất
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, thực hiện lại thao tác
                binding.btnTakePic.performClick();
            }
        }
    }
}
