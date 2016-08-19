package tech.honc.android.apps.soldier.ui.widget;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.smartydroid.android.starter.kit.utilities.RecyclerViewUtils;
import java.util.ArrayList;
import support.ui.adapters.BaseEasyViewHolderFactory;
import support.ui.adapters.EasyRecyclerAdapter;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.BeanCertification;
import tech.honc.android.apps.soldier.ui.viewholder.CertificationViewHolder;

/**
 * Created by Administrator on 2016/6/23.
 * 认证dialog
 */
public class CertificationDialog extends android.support.v4.app.DialogFragment {

  @Bind(R.id.ui_dialog_certification) TextView mUiDialogCertification;
  @Bind(R.id.ui_dialog_recycler) RecyclerView mUiDialogRecycler;
  private EasyRecyclerAdapter mEasyRecyclerAdapter;
  private static ArrayList<BeanCertification> mCertifications;

  public static CertificationDialog newInstance(ArrayList<BeanCertification> user) {
    mCertifications = user;
    return new CertificationDialog();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mEasyRecyclerAdapter = new EasyRecyclerAdapter(getContext());
    mEasyRecyclerAdapter.viewHolderFactory(new BaseEasyViewHolderFactory(getContext()));
    mEasyRecyclerAdapter.bind(BeanCertification.class, CertificationViewHolder.class);
  }

  @TargetApi(Build.VERSION_CODES.M) @Nullable @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_dialog_certification, container, false);
    ButterKnife.bind(this, view);
    LinearLayoutManager manager =
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    mUiDialogRecycler.setLayoutManager(manager);
    mUiDialogRecycler.addItemDecoration(RecyclerViewUtils.buildItemDecoration(getContext()));
    mUiDialogRecycler.setAdapter(mEasyRecyclerAdapter);
    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    mEasyRecyclerAdapter.appendAll(mCertifications);
    return view;
  }

  @TargetApi(Build.VERSION_CODES.M) @Override public void onResume() {
    super.onResume();
    mEasyRecyclerAdapter.notifyDataSetChanged();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
