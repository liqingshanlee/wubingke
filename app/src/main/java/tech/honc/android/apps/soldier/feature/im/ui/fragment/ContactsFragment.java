package tech.honc.android.apps.soldier.feature.im.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContactCacheUpdateListener;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWDBContact;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FriendService;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.model.Contact;
import tech.honc.android.apps.soldier.feature.im.model.ContactsDummy;
import tech.honc.android.apps.soldier.feature.im.model.ContactsTotal;
import tech.honc.android.apps.soldier.feature.im.ui.activity.SystemAddContactActivity;
import tech.honc.android.apps.soldier.feature.im.ui.activity.TribeActivity;
import tech.honc.android.apps.soldier.feature.im.ui.adapter.ContactsAdapter;
import tech.honc.android.apps.soldier.feature.im.ui.viewholder.ContactsDummyViewHolder;
import tech.honc.android.apps.soldier.feature.im.ui.viewholder.ContactsViewHolder;
import tech.honc.android.apps.soldier.feature.im.utils.CharacterParser;
import tech.honc.android.apps.soldier.feature.im.utils.ContactsComparator;
import tech.honc.android.apps.soldier.feature.im.widget.SideBar;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.ui.fragment.BaseFragment;

/**
 * Author:　Create on 2016-5-23-0023 19:28 by Doublemine
 * Email:
 * Summary:　TODO
 * Update Date: 2016-5-23-0023 19:28 modify by Doublemine
 */
public class ContactsFragment extends BaseFragment
    implements EasyViewHolder.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
    EasyViewHolder.OnItemLongClickListener, IYWContactCacheUpdateListener {
  @Bind(R.id.im_ui_sidebar) SideBar mSideBar;
  @Bind(R.id.im_ui_view_bubble) TextView mBubble;
  @Bind(R.id.im_ui_recycler_view) RecyclerView mRecyclerView;
  @Bind(R.id.im_ui_swf) SwipeRefreshLayout mSwipeRefreshLayout;

  private IYWContactService mIYWContactService;
  private YWIMKit mYWIMKit;
  private ContactsAdapter mContactsAdapter;
  private List<IYWDBContact> mIYWDBContactList;
  private static ContactsFragment instance;
  private List<Contact> mContacts;
  private ContactsDummy mNewContacts;
  private ContactsDummy mGroup;
  private FriendService mFriendService;

  @Override public void onFriendCacheUpdate(String s, String s1) {
    Log.d("ContactsFragment", "onFriendCacheUpdate-->s=" + s + "-->s1=" + s1);
    updateContactsFromCache();
  }

  @Override public void onRefresh() {
    syncContactsFormServer();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFriendService = ApiService.createFriendService();
    initIM();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    getContactFromCache();
    initRecyclerView();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.im_fragment_contacts;
  }

  @Override public void onItemClick(int position, View view) {
    Object o = mContactsAdapter.get(position);
    if (o instanceof ContactsDummy) {
      ContactsDummy contactsDummy = (ContactsDummy) o;
      switch (contactsDummy.mTag) {
        case ContactsDummy.NEW_CONTACTS:
          startActivity(new Intent(getContext(), SystemAddContactActivity.class));
          break;
        case ContactsDummy.GROUP:
          TribeActivity.startGroup(getActivity());
          break;
      }
    } else if (o instanceof Contact) {
      Contact contact = (Contact) o;
      Intent intent = mYWIMKit.getChattingActivityIntent(contact.userId, contact.appKey);
      intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
      startActivity(intent);
    }
  }

  @Override public boolean onLongItemClicked(int position, View view) {
    final Object o = mContactsAdapter.get(position);
    if (o instanceof Contact) {
      final Contact contact = (Contact) o;
      new MaterialDialog.Builder(getActivity()).items(R.array.im_contact_menu)
          .itemsCallback(new MaterialDialog.ListCallback() {
            @Override public void onSelection(MaterialDialog dialog, View itemView, int which,
                CharSequence text) {
              if (which == 0) {//删除好友
                delContact(contact);
              } else if (which == 1) {//修改备注
                changeRemarkName(contact);
              }
            }
          })
          .build()
          .show();
      return true;
    }
    return false;
  }

  @Override public void onResume() {
    super.onResume();
    updateContactsFromCache();
  }

  private void changeRemarkName(final Contact contact) {
    new MaterialDialog.Builder(getActivity()).input("请输入新的备注名", null, false,
        new MaterialDialog.InputCallback() {
          @Override public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
            editNickName(contact, input.toString());
          }
        }).build().show();
  }

  private void editNickName(Contact contact, String newName) {
    if (mIYWContactService != null) {
      mIYWContactService.chgContactRemark(contact.userId, contact.appKey, newName,
          new IWxCallback() {
            @Override public void onSuccess(Object... objects) {
              Toast.makeText(getActivity(), "修改备注完成", Toast.LENGTH_SHORT).show();
            }

            @Override public void onError(int i, String s) {
              Toast.makeText(getActivity(), "修改备注失败", Toast.LENGTH_SHORT).show();
            }

            @Override public void onProgress(int i) {

            }
          });
    }
  }

  private void delContact(final Contact contact) {
    if (mIYWContactService != null) {
      mIYWContactService.delContact(contact.userId, contact.appKey, new IWxCallback() {
        @Override public void onSuccess(Object... objects) {
          Call<Status> call = mFriendService.delFriends(contact.userId);
          call.enqueue(new Callback<Status>() {
            @Override public void onResponse(Call<Status> call, Response<Status> response) {
              if (response.isSuccessful()) {
                Toast.makeText(getActivity(), "删除好友成功", Toast.LENGTH_SHORT).show();
              }
            }

            @Override public void onFailure(Call<Status> call, Throwable t) {
              Toast.makeText(getActivity(), "删除好友失败", Toast.LENGTH_SHORT).show();
            }
          });
        }

        @Override public void onError(int i, String s) {
          Toast.makeText(getActivity(), "删除好友失败", Toast.LENGTH_SHORT).show();
        }

        @Override public void onProgress(int i) {

        }
      });
    }
  }

  private void initIM() {
    mYWIMKit = LoginHelper.getInstance().getYWIMKit();
    mIYWContactService = mYWIMKit.getContactService();
    mIYWContactService.addContactCacheUpdateListener(this);
  }

  private void getContactFromCache() {
    initView();
    mIYWDBContactList = mIYWContactService.getContactsFromCache();
    mContacts = new ArrayList<>();
    for (IYWDBContact contact : mIYWDBContactList) {
      Contact item = new Contact();
      item.appKey = contact.getAppKey();
      item.avatar = contact.getAvatarPath();
      item.nickName = contact.getShowName();
      item.userId = contact.getUserId();
      item.letterChar =
          CharacterParser.getInstance().getSelling(contact.getShowName()).toUpperCase().charAt(0);
      mContacts.add(item);
    }
    Collections.sort(mContacts,new ContactsComparator());
    mContactsAdapter.addAll(mContacts);
    mContactsAdapter.add(mNewContacts, 0);
    mContactsAdapter.add(mGroup, 1);
    mContactsAdapter.add(new ContactsTotal(mContacts.size()));
    mSideBar.setUpCharList(mContactsAdapter.getSortLetter());
  }

  private void updateContactsFromCache() {
    mIYWDBContactList = mIYWContactService.getContactsFromCache();
    mContacts.clear();
    for (IYWDBContact contact : mIYWDBContactList) {
      Contact item = new Contact();
      item.appKey = contact.getAppKey();
      item.avatar = contact.getAvatarPath();
      item.nickName = contact.getShowName();
      item.userId = contact.getUserId();
      while (item.nickName == null || TextUtils.isEmpty(item.nickName) || item.nickName.equals(
          item.userId)) {
        item.nickName = LoginHelper.getInstance()
            .getYWIMKit()
            .getContactService()
            .getContactProfileInfo(item.userId, item.appKey)
            .getShowName();
      }
      item.letterChar =
          CharacterParser.getInstance().getSelling(contact.getShowName()).toUpperCase().charAt(0);
      mContacts.add(item);
    }
    Collections.sort(mContacts,new ContactsComparator());
    mContactsAdapter.addAll(mContacts);
    mContactsAdapter.add(mNewContacts, 0);
    mContactsAdapter.add(mGroup, 1);
    mContactsAdapter.add(new ContactsTotal(mContacts.size()));
    if (mContactsAdapter.getSortLetter() != null && mSideBar != null) {
      mSideBar.setUpCharList(mContactsAdapter.getSortLetter());
    }
    mContactsAdapter.notifyDataSetChanged();
  }

  private void initView() {
    initSwipeRefresh();
    mContactsAdapter = new ContactsAdapter(getActivity());

    mNewContacts = ContactsDummy.newContacts();
    mGroup = ContactsDummy.group();
    mContactsAdapter.bind(ContactsDummy.class, ContactsDummyViewHolder.class);
    mContactsAdapter.bind(Contact.class, ContactsViewHolder.class);
    mContactsAdapter.bind(ContactsTotal.class, ContactsTotalViewHolder.class);
    mContactsAdapter.add(mNewContacts, 0);
    mContactsAdapter.add(mGroup, 1);
  }

  private void initRecyclerView() {
    initSidebar();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mRecyclerView.setLayoutManager(linearLayoutManager);
    mRecyclerView.setAdapter(mContactsAdapter);
    mContactsAdapter.setOnClickListener(this);
    mContactsAdapter.setOnLongClickListener(this);
    mRecyclerView.addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(getContext()).size(1).build());
    final StickyRecyclerHeadersDecoration headersDecor =
        new StickyRecyclerHeadersDecoration(mContactsAdapter);
    mRecyclerView.addItemDecoration(headersDecor);
    mContactsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override public void onChanged() {
        headersDecor.invalidateHeaders();
      }
    });
  }

  private void initSwipeRefresh() {
    mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
        android.R.color.holo_green_light, android.R.color.holo_orange_light,
        android.R.color.holo_red_light);
    mSwipeRefreshLayout.setOnRefreshListener(this);
  }

  private void initSidebar() {
    mSideBar.setBubble(mBubble);
    mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

      @Override public void onTouchingLetterChanged(String s) {
        int position = mContactsAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
          mRecyclerView.scrollToPosition(position);
        }
      }
    });
  }

  private void syncContactsFormServer() {
    mSwipeRefreshLayout.setRefreshing(true);
    mIYWContactService.syncContacts(new IWxCallback() {
      @Override public void onSuccess(Object... objects) {
        updateContactsFromCache();
        mSwipeRefreshLayout.setRefreshing(false);
        mContactsAdapter.notifyDataSetChanged();
      }

      @Override public void onError(int i, String s) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.e("ContactsFragment", "同步联系人错误:" + s + "错误码:" + i);
      }

      @Override public void onProgress(int i) {

      }
    });
  }
}
