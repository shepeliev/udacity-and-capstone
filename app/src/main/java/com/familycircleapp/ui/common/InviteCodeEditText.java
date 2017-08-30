package com.familycircleapp.ui.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.familycircleapp.R;

public class InviteCodeEditText extends android.support.v7.widget.AppCompatEditText
    implements TextWatcher {

  private boolean mSelfChange = false;

  public InviteCodeEditText(final Context context) {
    super(context);
    init();
  }

  public InviteCodeEditText(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public InviteCodeEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    addTextChangedListener(this);
  }

  @Override
  public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
    // not implemented
  }

  @Override
  public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
    // not implemented
  }

  @Override
  public void afterTextChanged(final Editable s) {
    if (s.length() == 0) {
      return;
    }

    if (!mSelfChange) {
      mSelfChange = true;
      final String splitter = getContext().getString(R.string.invite_code_splitter);
      final int inviteCodeLength =
          getContext().getResources().getInteger(R.integer.invite_code_length);
      final int segmentLength = inviteCodeLength / 2;
      final String normalized = split(
          s.toString().replace(splitter, ""), splitter, segmentLength
      ).toUpperCase();
      s.replace(0, s.length(), normalized);
    } else {
      mSelfChange = false;
    }
  }

  private String split(final String text, final String splitter, final int segmentLength) {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < text.length(); i++) {
      if (i > 0 && i % segmentLength == 0) {
        sb.append(splitter);
      }
      sb.append(text.charAt(i));
    }

    return sb.toString();
  }
}
