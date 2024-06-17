package com.example.snaplearn.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.snaplearn.Fragment.EnglishFragment;
import com.example.snaplearn.Fragment.KeywordFragment;
import com.example.snaplearn.Fragment.ParagraphFragment;
import com.example.snaplearn.Fragment.VietnameseFragment;
import com.example.snaplearn.R;
import com.example.snaplearn.databinding.ActivityMainBinding;
import com.example.snaplearn.databinding.ActivityResultBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ResultActivity extends AppCompatActivity {
    private ActivityResultBinding binding;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ResultActivity.ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityResultBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent().hasExtra("PHOTO_URI")) {
            String photoUriString = getIntent().getStringExtra("PHOTO_URI");
            Uri photoUri = Uri.parse(photoUriString);
            // Sử dụng photoUri để hiển thị ảnh trong imageView hoặc thực hiện xử lý khác
            
            binding.ivResult.setImageURI(photoUri);
        }
        binding.btnBack.setOnClickListener(v->{
            finish();
        });
        binding.btnBackLoading.setOnClickListener(v->{
            finish();
        });
        binding.btnWord1.setOnClickListener(v -> {
            binding.overlay.setVisibility(View.VISIBLE);
            binding.resultNotice.setVisibility(View.VISIBLE);
        });
        binding.btnClose.setOnClickListener(v->{
            binding.overlay.setVisibility(View.GONE);
            binding.resultNotice.setVisibility(View.GONE);
        });

        tabLayout = binding.tabLayout;
        viewPager = binding.viewPager;
        viewPagerAdapter = new ResultActivity.ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("English");
                    break;
                case 1:
                    tab.setText("Vietnamese");
                    break;
            }
        }).attach();
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new EnglishFragment();
                case 1:
                    return new VietnameseFragment();
                default:
                    return new EnglishFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
    private void loading(){
        binding.loadingLayout.setVisibility(View.VISIBLE);
        binding.mainContentLayout.setVisibility(View.GONE);
    }
    private void loadingSuccess(){
        binding.loadingLayout.setVisibility(View.GONE);
        binding.mainContentLayout.setVisibility(View.VISIBLE);
    }
}