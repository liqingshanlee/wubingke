package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import com.github.paolorotolo.appintro.AppIntro2;
import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.ui.fragment.AppIntroFragment;

/**
 * Created by MrJiang on 2016/5/9.
 */
public class SoldierAppIntro extends AppIntro2
{
  @Override public void init(@Nullable Bundle savedInstanceState) {
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    addSlide(AppIntroFragment.newInstance(R.layout.fragment_app_intro, R.drawable.intro_1));
    addSlide(AppIntroFragment.newInstance(R.layout.fragment_app_intro, R.drawable.intro_2));
    addSlide(AppIntroFragment.newInstance(R.layout.fragment_app_intro, R.drawable.intro_3));
    addSlide(AppIntroFragment.newInstance(R.layout.fragment_app_intro, R.drawable.intro_4));
  }

  @Override public void onNextPressed() {

  }

  @Override public void onDonePressed() {
    SupportApp.enterApp();
    Navigator.startMainActivity(this);
  }

  @Override public void onSlideChanged() {

  }
}
