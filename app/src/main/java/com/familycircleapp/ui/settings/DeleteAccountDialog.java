package com.familycircleapp.ui.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.familycircleapp.App;
import com.familycircleapp.R;
import com.familycircleapp.repository.CurrentUser;
import com.familycircleapp.utils.Ctx;
import com.firebase.ui.auth.AuthUI;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class DeleteAccountDialog extends DialogFragment {

  @Inject CurrentUser mCurrentUser;
  @BindView(R.id.et_password) EditText mPassword;

  private Disposable mDisposable;

  public DeleteAccountDialog() {
    App.getComponent().inject(this);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(final Bundle savedInstanceState) {
    final View view = LayoutInflater
        .from(getActivity())
        .inflate(R.layout.dialog_delete_account, null);
    ButterKnife.bind(this, view);

    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setView(view)
        .setPositiveButton(android.R.string.ok, null)
        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
          // dismiss;
        });

    final AlertDialog dialog = builder.create();

    dialog.setOnShowListener(dialogInterface -> {
      final Button button = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
      mDisposable = RxTextView
          .afterTextChangeEvents(mPassword)
          .map(event -> !TextUtils.isEmpty(event.view().getText()))
          .startWith(false)
          .subscribe(button::setEnabled);

      button.setOnClickListener(this::deleteAccount);
    });

    return dialog;
  }

  @Override
  public void onDismiss(final DialogInterface dialog) {
    if (mDisposable != null) {
      mDisposable.dispose();
      mDisposable = null;
    }
    super.onDismiss(dialog);
  }

  @SuppressWarnings("unused")
  private void deleteAccount(final View view) {
    mCurrentUser
        .deleteAccount(mPassword.getText().toString())
        .subscribe(
            aVoid -> AuthUI.getInstance().delete(getActivity())
                .addOnSuccessListener(aVoid1 -> NavUtils.navigateUpFromSameTask(getActivity()))
                .addOnFailureListener(error -> {
                  Timber.e(error);
                  Ctx.toast(getActivity(), error.getLocalizedMessage());
                }),
            error -> {
              Timber.e(error);
              Ctx.toast(getActivity(), R.string.error_deleting_account_incorrect_password);
            }
        );
  }
}
