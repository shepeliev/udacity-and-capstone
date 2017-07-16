package com.familycircleapp.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrentUserImplTest {

  @Mock private FirebaseAuth mockFirebaseAuth;
  @Mock private FirebaseDatabase mockFirebaseDatabase;
  @InjectMocks private CurrentUserImpl mCurrentUser;

  @Test
  public void isAuthenticated_returnsFalse_ifUserNotAuthenticated() throws Exception {
    assertFalse(mCurrentUser.isAuthenticated());
  }

  @Test
  public void isAuthenticated_returnsTrue_ifUserAuthenticated() throws Exception {
    when(mockFirebaseAuth.getCurrentUser()).thenReturn(mock(FirebaseUser.class));
    assertTrue(mCurrentUser.isAuthenticated());
  }

  @Test
  public void getId_returnsString_ifUserAuthenticated() throws Exception {
    final FirebaseUser mockFirebaseUser = mock(FirebaseUser.class);
    when(mockFirebaseUser.getUid()).thenReturn("user_1");
    when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);

    assertEquals("user_1", mCurrentUser.getId());
  }

  @Test
  public void getId_returnsNull_ifUserNotAuthenticated() throws Exception {
    assertNull(mCurrentUser.getId());
  }

  @Test
  public void getDisplayName_returnsString_ifUserAuthenticated() throws Exception {
    final FirebaseUser mockFirebaseUser = mock(FirebaseUser.class);
    when(mockFirebaseUser.getDisplayName()).thenReturn("John");
    when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);

    assertEquals("John", mCurrentUser.getDisplayName());
  }

  @Test
  public void getDisplayName_returnsNull_ifUserNotAuthenticated() throws Exception {
    assertNull(mCurrentUser.getId());
  }

  @Test
  public void joinCircle_updatesCurrentCircleKey_andMembersKey() throws Exception {
    final FirebaseUser mockFirebaseUser = mock(FirebaseUser.class);
    when(mockFirebaseUser.getUid()).thenReturn("user_1");
    when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
    final DatabaseReference mockDatabaseRef = mock(DatabaseReference.class);
    when(mockFirebaseDatabase.getReference()).thenReturn(mockDatabaseRef);

    mCurrentUser.joinCircle("circle_1", null);

    final Map<String, Object> expectedUpdate = new HashMap<String, Object>() {{
      put("/circles/circle_1/members/user_1", true);
      put("/users/user_1/currentCircle", "circle_1");
    }};
    verify(mockDatabaseRef).updateChildren(eq(expectedUpdate), any());
  }
}
