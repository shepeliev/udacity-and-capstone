package com.familycircleapp;


import com.familycircleapp.ui.main.MainActivity;

public interface Component {

  void inject(final EntryPointActivity target);

  void inject(final MainActivity target);
}
