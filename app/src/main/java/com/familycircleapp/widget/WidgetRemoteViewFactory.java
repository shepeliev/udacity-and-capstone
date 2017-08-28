package com.familycircleapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.amulyakhare.textdrawable.TextDrawable;
import com.familycircleapp.R;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.ui.BindingAdapters;
import com.familycircleapp.ui.details.UserDetailsActivity;
import com.familycircleapp.utils.Ctx;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import timber.log.Timber;

public class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

  private final Context mContext;
  private final UserRepository mUserRepository;
  private final Intent mIntent;

  private String[] mUserIds;
  private List<UserWidgetModel> mUsers = Collections.emptyList();

  public WidgetRemoteViewFactory(
      final Context context, final UserRepository userRepository, final Intent intent
  ) {
    mContext = context;
    mUserRepository = userRepository;
    mIntent = intent;
  }

  @Override
  public void onCreate() {
    final int widgetId = mIntent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
    );
    mUserIds = mIntent.getStringArrayExtra(WidgetProvider.USER_IDS);

    Observable.fromArray(mUserIds)
        .flatMap(userId -> mUserRepository.getUser(userId).toObservable())
        .map(UserWidgetModel::fromUser)
        .toList()
        .subscribe(
            users -> {
              this.mUsers = users;
              AppWidgetManager
                  .getInstance(mContext)
                  .notifyAppWidgetViewDataChanged(widgetId, R.id.lv_users);
            },
            Timber::e
        );
  }

  @Override
  public void onDataSetChanged() {
    // do nothing
  }

  @Override
  public void onDestroy() {
    // do nothing;
  }

  @Override
  public int getCount() {
    return mUsers.size();
  }

  @Override
  public RemoteViews getViewAt(final int position) {
    final RemoteViews views =
        new RemoteViews(mContext.getPackageName(), R.layout.user_widget_list_item);
    final UserWidgetModel model = mUsers.get(position);

    views.setTextViewText(R.id.tw_user_displayed_name, model.getDisplayName());
    views.setTextViewText(
        R.id.tw_user_status,
        mContext.getString(R.string.user_status_near, model.getStatusText())
    );
    views.setTextViewText(
        R.id.tw_battery_level,
        mContext.getString(R.string.battery_level, model.getBatteryLevel())
    );
    final TextDrawable avatarDdrawable = BindingAdapters.drawableAvatar(model.getDisplayName());
    final Bitmap avatarBitmap = toBitmap(avatarDdrawable);
    views.setImageViewBitmap(R.id.iw_avatar, avatarBitmap);
    views.setImageViewResource(
        R.id.tw_battery_status,
        BindingAdapters.batteryImageResource(model.getBatteryStatus(), model.getBatteryLevel())
    );

    final Bundle extras = new Bundle();
    extras.putString(UserDetailsActivity.EXTRA_USER_ID, model.getId());
    final Intent intent = new Intent();
    intent.putExtras(extras);
    views.setOnClickFillInIntent(R.id.user_widget_list_item, intent);

    return views;
  }

  private Bitmap toBitmap(final Drawable drawable) {
    final int sideSize = Ctx.dip(mContext, 40);
    final Bitmap bitmap = Bitmap.createBitmap(sideSize, sideSize, Bitmap.Config.ARGB_8888);
    final Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);

    return bitmap;
  }

  @Override
  public RemoteViews getLoadingView() {
    return null;
  }

  @Override
  public int getViewTypeCount() {
    return 1;
  }

  @Override
  public long getItemId(final int position) {
    return mUsers.get(position).hashCode();
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }
}
