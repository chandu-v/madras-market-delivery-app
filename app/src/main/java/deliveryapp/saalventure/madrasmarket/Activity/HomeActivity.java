package deliveryapp.saalventure.madrasmarket.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import deliveryapp.saalventure.madrasmarket.R;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout1;
    ViewPager2 viewPager1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }
}
