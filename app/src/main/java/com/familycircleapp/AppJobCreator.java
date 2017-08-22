package com.familycircleapp;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import java.util.Map;

import javax.inject.Provider;

public final class AppJobCreator implements JobCreator {

  private final Map<String, Provider<Job>> mJobs;

  public AppJobCreator(final Map<String, Provider<Job>> jobs) {
    mJobs = jobs;
  }

  @Override
  public Job create(final String tag) {
    final Provider<Job> jobProvider = mJobs.get(tag);
    return jobProvider != null ? jobProvider.get() : null;
  }
}
