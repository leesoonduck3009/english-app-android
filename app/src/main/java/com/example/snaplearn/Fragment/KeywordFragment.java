package com.example.snaplearn.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.snaplearn.Adapter.KeywordAdapter;
import com.example.snaplearn.Model.Keyword;
import com.example.snaplearn.R;
import com.example.snaplearn.databinding.FragmentKeywordBinding;
import com.example.snaplearn.utils.Listener.IOnKeywordClick;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class KeywordFragment extends Fragment implements IOnKeywordClick {

    private FragmentKeywordBinding binding;
    private KeywordAdapter keywordAdapter;
    private List<Keyword> keywordList = new ArrayList<>();
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    public KeywordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generateSampleData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentKeywordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recycleViewKeyword.setLayoutManager(new LinearLayoutManager(getContext()));
        keywordAdapter = new KeywordAdapter(keywordList, this);
        binding.recycleViewKeyword.setAdapter(keywordAdapter);

        tabLayout = binding.tabLayout;
        viewPager = binding.viewPager;
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

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

        return view;
    }

    private void generateSampleData() {
        Keyword keyword1 = new Keyword.Builder()
                .setKeyWordId("KW001")
                .setKeyWordText("apple")
                .setEngMeaning("A fruit that is typically red, green, or yellow.")
                .setVietMeaning("Một loại trái cây thường có màu đỏ, xanh lá cây hoặc vàng.")
                .setEngSentence("She ate an apple for breakfast.")
                .setVietSentence("Cô ấy đã ăn một quả táo cho bữa sáng.")
                .setUserId("U001")
                .build();

        Keyword keyword2 = new Keyword.Builder()
                .setKeyWordId("KW002")
                .setKeyWordText("banana")
                .setEngMeaning("A long curved fruit that grows in clusters and has soft pulpy flesh and yellow skin when ripe.")
                .setVietMeaning("Một loại trái cây dài cong mọc thành chùm và có thịt mềm, da màu vàng khi chín.")
                .setEngSentence("Monkeys love to eat bananas.")
                .setVietSentence("Khỉ rất thích ăn chuối.")
                .setUserId("U002")
                .build();

        Keyword keyword3 = new Keyword.Builder()
                .setKeyWordId("KW003")
                .setKeyWordText("cherry")
                .setEngMeaning("A small, round fruit that is typically bright or dark red.")
                .setVietMeaning("Một loại trái cây nhỏ, tròn thường có màu đỏ tươi hoặc đậm.")
                .setEngSentence("She bought a bag of cherries at the market.")
                .setVietSentence("Cô ấy đã mua một túi anh đào ở chợ.")
                .setUserId("U001")
                .build();

        keywordList.add(keyword1);
        keywordList.add(keyword2);
        keywordList.add(keyword3);
    }

    @Override
    public void onKeywordClick(Keyword keyword) {
        binding.resultHistoryKw.setVisibility(View.VISIBLE);

        // Truyền dữ liệu sang fragment
        viewPagerAdapter.setKeyword(keyword);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        private Keyword keyword = new Keyword.Builder().setEngMeaning("h").setEngSentence("h").setVietSentence("v").setVietMeaning("v").build();

        public ViewPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        public void setKeyword(Keyword keyword) {
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
