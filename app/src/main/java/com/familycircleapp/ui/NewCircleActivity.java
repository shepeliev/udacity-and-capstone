package com.familycircleapp.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.ui.common.CreateCircleViewModel;
import com.familycircleapp.utils.Ctx;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class NewCircleActivity extends AppCompatLifecycleActivity {

  @Inject ViewModelProvider.Factory mViewModelFactory;

  @BindView(R.id.loader_screen) FrameLayout mLoaderScreen;
  @BindView(R.id.btn_create_new_circle) Button mCreateCircleButton;
  @BindView(R.id.et_circle_name) EditText mCircleNameEditText;

  private CompositeDisposable mCompositeDisposable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_circle);
    ButterKnife.bind(this);

    App.getComponent().inject(this);

    final CreateCircleViewModel createCircleViewModel = ViewModelProviders
        .of(this, mViewModelFactory)
        .get(CreateCircleViewModel.class);

    createCircleViewModel.getRunningState().observe(this, this::handleRunningState);
    createCircleViewModel.getResult().observe(this, this::handleSuccessResult);
    createCircleViewModel.getError().observe(this, this::handleFailResult);

    mCompositeDisposable = new CompositeDisposable(
        RxView.clicks(mCreateCircleButton).subscribe(o -> createCircleViewModel.start()),

        circleNameChanges()
            .map(code -> !TextUtils.isEmpty(code))
            .subscribe(mCreateCircleButton::setEnabled),

        circleNameChanges().subscribe(createCircleViewModel::setCircleName)
    );
  }

  @Override
  protected void onDestroy() {
    if (mCompositeDisposable != null) {
      mCompositeDisposable.dispose();
      mCompositeDisposable = null;
    }

    super.onDestroy();
  }

  private Observable<String> circleNameChanges() {
    return RxTextView
        .afterTextChangeEvents(mCircleNameEditText)
        .map(event -> event.view().getText().toString());
  }

  private void handleRunningState(final boolean isRunning) {
    mLoaderScreen.setVisibility(isRunning ? View.VISIBLE : View.GONE);
    mCreateCircleButton.setEnabled(!isRunning && !TextUtils.isEmpty(mCircleNameEditText.getText()));
  }

  private void handleFailResult(final String errorText) {
    if (!TextUtils.isEmpty(errorText)) {
      Ctx.toast(this, errorText);
    }
  }

  private void handleSuccessResult(@SuppressWarnings("unused") final String result) {
    NavUtils.navigateUpFromSameTask(this);
  }
}
