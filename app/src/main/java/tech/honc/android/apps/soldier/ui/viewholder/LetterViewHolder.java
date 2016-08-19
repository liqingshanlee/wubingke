package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.utils.data.Letter;

/**
 * Created by MrJiang
 * on 4/22/2016.
 */
public class LetterViewHolder extends EasyViewHolder<Letter> {
  @Bind(R.id.letter_city) TextView mPopularCity;

  public LetterViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_letter);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, Letter value) {
    if (value != null) {
      mPopularCity.setText(value.letter);
    }
  }
}
