package com.familycircleapp.repository;

import com.google.firebase.auth.UserInfo;

import java.util.function.Consumer;

class CurrentUserImpl implements CurrentUser {

  @Override
  public boolean isAuthenticated() {
    return false;
  }
}
