package com.example.snaplearn.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.snaplearn.Adapter.KeywordAdapter;
import com.example.snaplearn.Contract.IResultActivityContract;
import com.example.snaplearn.Fragment.EnglishFragment;
import com.example.snaplearn.Fragment.KeywordFragment;
import com.example.snaplearn.Fragment.ParagraphFragment;
import com.example.snaplearn.Fragment.VietnameseFragment;
import com.example.snaplearn.Model.Keyword;
import com.example.snaplearn.Model.Paragraph;
import com.example.snaplearn.Presenter.ResultActivityPresenter;
import com.example.snaplearn.R;
import com.example.snaplearn.databinding.ActivityMainBinding;
import com.example.snaplearn.databinding.ActivityResultBinding;
import com.example.snaplearn.utils.ExistedItemException;
import com.example.snaplearn.utils.Listener.IOnKeywordClick;
import com.example.snaplearn.utils.UriToByte;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity implements IResultActivityContract.View, IOnKeywordClick {
    private ActivityResultBinding binding;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ResultActivity.ViewPagerAdapter viewPagerAdapter;
    private IResultActivityContract.Presenter presenter;
    private KeywordAdapter keywordAdapter;
    private List<Keyword> listKeyword;
    private Paragraph paragraph;
    private Keyword keyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityResultBinding.inflate(getLayoutInflater());
        presenter = new ResultActivityPresenter(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnBack.setOnClickListener(v->{
            finish();
        });
        binding.btnBackLoading.setOnClickListener(v->{
            finish();
        });

        binding.btnClose.setOnClickListener(v->{
            binding.overlay.setVisibility(View.GONE);
            binding.resultNotice.setVisibility(View.GONE);
        });
        binding.btnSave.setOnClickListener(v->{
            presenter.saveKeyword(keyword);
        });
        tabLayout = binding.tabLayout;
        viewPager = binding.viewPager;
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        listKeyword = new ArrayList<>();
        keywordAdapter = new KeywordAdapter(listKeyword,this);
        binding.recycleViewKeyword.setAdapter(keywordAdapter);
        binding.recycleViewKeyword.setLayoutManager(new LinearLayoutManager(this));
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("English");
                            break;
                        case 1:
                            tab.setText("Vietnamese");
                            break;
                    }
                }
        ).attach();

        if (getIntent().hasExtra("PHOTO_URI")) {
            String photoUriString = getIntent().getStringExtra("PHOTO_URI");
            Uri photoUri = Uri.parse(photoUriString);
            byte[] image = UriToByte.convertUriToByteArray(getContentResolver(),photoUri);
            // Sử dụng photoUri để hiển thị ảnh trong imageView hoặc thực hiện xử lý khác
            loading();
            binding.ivResult.setImageURI(photoUri);
            presenter.detectImage(image);
        }
    }

    @Override
    public void onDetectImageSuccess(Paragraph paragraph) {
        this.paragraph = paragraph;
        listKeyword.addAll(paragraph.getKeywordInParagraph());
        keywordAdapter.notifyDataSetChanged();
        loadingSuccess();
    }

    @Override
    public void onSaveKeywordSuccess(Keyword keyword) {
        Toast.makeText(getApplicationContext(),"Save keyword success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveKeywordFailed(Exception e) {
        if(e instanceof ExistedItemException){
            Toast.makeText(getApplicationContext(),"Keyword already save", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getApplicationContext(),"Save keyword failed", Toast.LENGTH_SHORT).show();
        Log.e("save_keyword",e.getMessage());
    }

    @Override
    public void onDectectImageFail(Exception e) {
        Log.e("result_error", e.getMessage());
        Toast.makeText(getApplicationContext(), "Detect Image failed", Toast.LENGTH_SHORT).show();
        finish();
    }

        @Override
    public void onKeywordClick(Keyword keyword) {
            this.keyword = keyword;
            binding.overlay.setVisibility(View.VISIBLE);
            binding.resultNotice.setVisibility(View.VISIBLE);
            viewPagerAdapter.setKeyword(keyword);
    }
    private void loading(){
        binding.loadingLayout.setVisibility(View.VISIBLE);
        binding.mainContentLayout.setVisibility(View.GONE);
    }
    private void loadingSuccess(){
        binding.loadingLayout.setVisibility(View.GONE);
        binding.mainContentLayout.setVisibility(View.VISIBLE);
    }
    private static class ViewPagerAdapter extends FragmentStateAdapter {
        private Keyword keyword;
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        public void setKeyword(Keyword keyword){
            this.keyword = keyword;
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = EnglishFragment.newInstance(keyword.getEngMeaning(), keyword.getEngSentence());
                    break;
                case 1:
                    fragment = VietnameseFragment.newInstance(keyword.getVietMeaning(), keyword.getVietSentence());
                    break;
            }
            return fragment;
        }
        @Override
        public int getItemCount() {
            return 2;
        }
    }

}