package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import butterknife.ButterKnife;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.enums.RoleType;
import tech.honc.android.apps.soldier.ui.fragment.FilterAgainFemaleFragment;
import tech.honc.android.apps.soldier.ui.fragment.FilterAgainMaleFragment;
import tech.honc.android.apps.soldier.ui.fragment.FilterTypeFragment;
import tech.honc.android.apps.soldier.utils.data.SearchData;

/**
 * Created by MrJiang on 4/16/2016.
 * 再次筛选
 */
public class FilterAgainActivity extends BaseActivity
    implements FilterTypeFragment.NoticeFragmentChange {

  private NoticeContentChange mNoticeContentChange;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_filter_again);
    ButterKnife.bind(this);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    init();
  }

  private void init() {
    Fragment typeFragment = new FilterTypeFragment();
    Bundle bundle = new Bundle();
    bundle.putString("identity", getIntentType());
    bundle.putParcelable("data", getIntentSearchData());
    typeFragment.setArguments(bundle);
    getSupportFragmentManager().beginTransaction().add(R.id.type_list, typeFragment).commit();
    if (getIntentType().equals(RoleType.JUNSAO.toString())) {
      Fragment filterAgainFemaleFragment = FilterAgainFemaleFragment.create();
      Bundle bundleFemale = new Bundle();
      bundleFemale.putParcelable("data", getIntentSearchData());
      bundleFemale.putString("name", getSearchNickName());
      filterAgainFemaleFragment.setArguments(bundleFemale);
      SetNoticeContentChange((NoticeContentChange) filterAgainFemaleFragment);
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, filterAgainFemaleFragment)
          .commit();
    } else {
      Bundle bundleMale = new Bundle();
      bundleMale.putParcelable("data", getIntentSearchData());
      bundleMale.putString("name", getSearchNickName());
      Fragment filterAgainMaleFragment = new FilterAgainMaleFragment();
      filterAgainMaleFragment.setArguments(bundleMale);
      SetNoticeContentChange((NoticeContentChange) filterAgainMaleFragment);
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, filterAgainMaleFragment)
          .commit();
    }
  }

  public String getSearchNickName() {
    return getIntent().getStringExtra("name");
  }

  public String getIntentType() {
    String male = getIntent().getStringExtra("identity");
    if (male != null) {
      if (male.equals(RoleType.JUNSAO.toString())) {
        return RoleType.JUNSAO.toString();
      }
      return RoleType.SOLDIER.toString();
    }
    return null;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId()==android.R.id.home){
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  public SearchData getIntentSearchData() {
    return getIntent().getParcelableExtra("data");
  }

  @Override public void noticeChange(SearchData searchData) {
    mNoticeContentChange.sendContent(searchData);
  }

  public interface NoticeContentChange {
    void sendContent(SearchData searchData);
  }

  public void SetNoticeContentChange(NoticeContentChange change) {
    mNoticeContentChange = change;
  }
}
