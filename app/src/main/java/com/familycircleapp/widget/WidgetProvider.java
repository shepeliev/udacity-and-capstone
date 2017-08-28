package com.familycircleapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.familycircleapp.App;
import com.familycircleapp.BuildConfig;
import com.familycircleapp.R;
import com.familycircleapp.repository.Circle;
import com.familycircleapp.repository.CurrentCircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.ui.details.UserDetailsActivity;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import timber.log.Timber;

public class WidgetProvider extends AppWidgetProvider {

  static final String USER_IDS = BuildConfig.APPLICATION_ID + ".USER_IDS";

  @Inject CurrentUser mCurrentUser;
  @Inject CurrentCircleRepository mCurrentCircleRepository;

  @Override
  public void onUpdate(
      final Context context,
      final AppWidgetManager appWidgetManager,
      final int[] appWidgetIds
  ) {
    if (appWidgetIds == null) {
      return;
    }

    App.getComponent().inject(this);

    for (final int widgetId : appWidgetIds) {
      if (mCurrentUser.isAuthenticated()) {
        mCurrentCircleRepository.getCurrentCircle()
            .map(Circle::getMembers)
            .map(Map::keySet)
            .subscribe(
                userIds -> appWidgetManager.updateAppWidget(
                    widgetId, getRemoteViews(context, widgetId, userIds)
                ),
                Timber::e
            );
      }
    }
  }

  private RemoteViews getRemoteViews(
      final Context context,
      final int widgetId,
      final Set<String> userIds
  ) {
    final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
    views.setRemoteAdapter(R.id.lv_users, getIntent(context, widgetId, userIds));
    views.setEmptyView(R.id.lv_users, R.id.empty_view);

    final Intent detailsIntent = new Intent(context, UserDetailsActivity.class);
    final PendingIntent detailsPendingIntent = PendingIntent.getActivity(
        context, 0, detailsIntent, PendingIntent.FLAG_UPDATE_CURRENT
    );
    views.setPendingIntentTemplate(R.id.lv_users, detailsPendingIntent);

    return views;

  }

  private Intent getIntent(
      final Context context,
      final int widgetId,
      final Set<String> userIds
  ) {
    final Intent intent = new Intent(context, WidgetService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
    intent.putExtra(USER_IDS, userIds.toArray(new String[userIds.size()]));

    return intent;
  }
}
