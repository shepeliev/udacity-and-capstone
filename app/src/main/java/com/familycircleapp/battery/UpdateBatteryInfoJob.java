package com.familycircleapp.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;
import com.familycircleapp.utils.F;

import timber.log.Timber;

public final class UpdateBatteryInfoJob extends Job {

  public static final String JOB_TAG = "update_battery_injo_job";

  private final Context mContext;
  private final CurrentUser mCurrentUser;
  private final UserRepository mUserRepository;

  public UpdateBatteryInfoJob(
      @NonNull final Context context,
      @NonNull final CurrentUser currentUser,
      @NonNull final UserRepository userRepository
  ) {
    mContext = context;
    mCurrentUser = currentUser;
    mUserRepository = userRepository;
  }

  public static void startJob(final long intervalMs) {
    if (JobManager.instance().getAllJobRequestsForTag(JOB_TAG).isEmpty()) {
      buildJobRequest(intervalMs).schedule();
    }
  }

  public static void cancelJob() {
    F.foreach(JobManager.instance().getAllJobRequestsForTag(JOB_TAG), JobRequest::cancelAndEdit);
  }

  private static JobRequest buildJobRequest(final long intervalMs) {
    return new JobRequest.Builder(JOB_TAG).setPeriodic(intervalMs).build();
  }

  @NonNull
  @Override
  protected Result onRunJob(final Params params) {
    final String userId = mCurrentUser.getId();
    if (userId == null) {
      Timber.e("User must be authenticated");
      return Result.FAILURE;
    }

    final Intent intent =
        mContext.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    if (intent == null) {
      Timber.e("Getting battery info failed.");
      return Result.FAILURE;
    }

    final BatteryInfo batteryInfo = BatteryInfo.fromIntent(intent);
    Timber.d("Battery info: %s", batteryInfo);
    mUserRepository.saveBatteryInfo(userId, batteryInfo);

    return Result.SUCCESS;
  }
}
