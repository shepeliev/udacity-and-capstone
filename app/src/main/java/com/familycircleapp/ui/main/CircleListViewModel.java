package com.familycircleapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.familycircleapp.repository.Circle;
import com.familycircleapp.repository.CircleRepository;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.utils.F;
import com.familycircleapp.utils.Rx;

import java.util.List;

import io.reactivex.Observable;

public class CircleListViewModel extends ViewModel {

  private final CurrentUser mCurrentUser;
  private final CircleRepository mCircleRepository;

  private LiveData<List<Circle>> mCircles;

  public CircleListViewModel(
      @NonNull final CurrentUser currentUser,
      @NonNull final CircleRepository circleRepository
  ) {
    mCurrentUser = currentUser;
    mCircleRepository = circleRepository;
  }

  public LiveData<List<Circle>> getCircles() {
    if (mCircles == null) {
      mCircles = loadCircles();
    }

    return mCircles;
  }

  private LiveData<List<Circle>> loadCircles() {
    final String userId = mCurrentUser.getId();
    if (userId == null) {
      throw new IllegalStateException("user must be authenticated");
    }

    final Observable<List<Circle>> listObservable = mCurrentUser
        .observeCirclesList()
        .map(ids -> F.map(ids, mCircleRepository::observeCircle))
        .flatMap(observables -> Observable.combineLatest(
            observables,
            circles -> F.map(circles, circle -> (Circle) circle)
        ));

    return Rx.liveData(listObservable);
  }
}
