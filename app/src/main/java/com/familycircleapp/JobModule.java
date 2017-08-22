package com.familycircleapp;

import android.content.Context;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;
import com.familycircleapp.battery.UpdateBatteryInfoJob;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.repository.UserRepository;

import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public final class JobModule {

  @Provides
  @Singleton
  JobManager provideJobManager(final Context context, final JobCreator jobCreator) {
    JobManager.create(context).addJobCreator(jobCreator);
    return JobManager.instance();
  }

  @Provides
  @Singleton
  JobCreator provideJobCreator(final Map<String, Provider<Job>> jobs) {
    return new AppJobCreator(jobs);
  }

  @Provides
  @IntoMap
  @StringKey(UpdateBatteryInfoJob.JOB_TAG)
  Job provideUpdateBatteryInfoJob(
      final Context context,
      final CurrentUser currentUser,
      final UserRepository userRepository
  ) {
    return new UpdateBatteryInfoJob(context, currentUser, userRepository);
  }

}
