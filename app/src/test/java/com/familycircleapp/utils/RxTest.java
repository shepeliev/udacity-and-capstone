package com.familycircleapp.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.support.annotation.NonNull;

import com.familycircleapp.repository.HasId;
import com.familycircleapp.repository.NotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RxTest {

  @Mock private DatabaseReference mockReference;
  @Mock private DataSnapshot mockDataSnapshot;

  @Before
  public void setUp() throws Exception {
    when(mockDataSnapshot.getRef()).thenReturn(mockReference);
  }

  @Test
  public void observable_subscribeOnSuccess() throws Exception {
    mockDataSnapshot = mock(DataSnapshot.class);
    when(mockDataSnapshot.exists()).thenReturn(true);
    when(mockDataSnapshot.getKey()).thenReturn("foo_1");
    when(mockDataSnapshot.getValue(Foo.class)).thenReturn(new Foo());

    final Foo[] value = {null};
    Rx.observable(mockReference, Foo.class).subscribe(v -> value[0] = v);

    final ArgumentCaptor<ValueEventListener> listenerCaptor =
        ArgumentCaptor.forClass(ValueEventListener.class);
    verify(mockReference).addValueEventListener(listenerCaptor.capture());
    final ValueEventListener listener = listenerCaptor.getValue();
    listener.onDataChange(mockDataSnapshot);

    assertEquals("foo_1", value[0].getId());
  }

  @Test
  public void observable_subscribeOnError_noReference() throws Exception {
    when(mockDataSnapshot.exists()).thenReturn(false);

    final Throwable[] error = {null};
    Rx.observable(mockReference, Foo.class)
        .subscribe(v -> fail("onSuccess should not be invoked"), err -> error[0] = err);

    final ArgumentCaptor<ValueEventListener> listenerCaptor =
        ArgumentCaptor.forClass(ValueEventListener.class);
    verify(mockReference).addValueEventListener(listenerCaptor.capture());
    final ValueEventListener listener = listenerCaptor.getValue();
    listener.onDataChange(mockDataSnapshot);

    assertTrue(error[0] instanceof NotFoundException);
  }

  @Test
  public void observable_subscribeOnError_databaseError() throws Exception {
    final Throwable[] error = {null};
    Rx.observable(mockReference, Foo.class)
        .subscribe(v -> fail("onSuccess should not be invoked"), err -> error[0] = err);

    final ArgumentCaptor<ValueEventListener> listenerCaptor =
        ArgumentCaptor.forClass(ValueEventListener.class);
    verify(mockReference).addValueEventListener(listenerCaptor.capture());
    final ValueEventListener listener = listenerCaptor.getValue();
    listener.onCancelled(DatabaseError.zzqv(DatabaseError.PERMISSION_DENIED));

    assertTrue(error[0] instanceof DatabaseException);
  }

  @Test
  public void observable_dispose_shouldRemove_valueEventListener() throws Exception {
    final ValueEventListener mockListener = mock(ValueEventListener.class);
    when(mockReference.addValueEventListener(any())).thenReturn(mockListener);

    Rx.observable(mockReference, Foo.class)
        .subscribe().dispose();

    verify(mockReference).removeEventListener(mockListener);
  }

  @Test
  public void single_subscribeOnSuccess() throws Exception {
    when(mockDataSnapshot.exists()).thenReturn(true);
    when(mockDataSnapshot.getKey()).thenReturn("foo_1");
    when(mockDataSnapshot.getValue(Foo.class)).thenReturn(new Foo());

    final Foo[] value = {null};
    Rx.single(mockReference, Foo.class).subscribe(v -> value[0] = v);

    final ArgumentCaptor<ValueEventListener> listenerCaptor =
        ArgumentCaptor.forClass(ValueEventListener.class);
    verify(mockReference).addListenerForSingleValueEvent(listenerCaptor.capture());
    final ValueEventListener listener = listenerCaptor.getValue();
    listener.onDataChange(mockDataSnapshot);

    assertEquals("foo_1", value[0].getId());
  }

  @Test
  public void single_subscribeOnError_noReference() throws Exception {
    when(mockDataSnapshot.exists()).thenReturn(false);

    final Throwable[] error = {null};
    Rx.single(mockReference, Foo.class)
        .subscribe(v -> fail("onSuccess should not be invoked"), err -> error[0] = err);

    final ArgumentCaptor<ValueEventListener> listenerCaptor =
        ArgumentCaptor.forClass(ValueEventListener.class);
    verify(mockReference).addListenerForSingleValueEvent(listenerCaptor.capture());
    final ValueEventListener listener = listenerCaptor.getValue();
    listener.onDataChange(mockDataSnapshot);

    assertTrue(error[0] instanceof NotFoundException);
  }

  @Test
  public void single_subscribeOnError_databaseError() throws Exception {
    final Throwable[] error = {null};
    Rx.single(mockReference, Foo.class)
        .subscribe(v -> fail("onSuccess should not be invoked"), err -> error[0] = err);

    final ArgumentCaptor<ValueEventListener> listenerCaptor =
        ArgumentCaptor.forClass(ValueEventListener.class);
    verify(mockReference).addListenerForSingleValueEvent(listenerCaptor.capture());
    final ValueEventListener listener = listenerCaptor.getValue();
    listener.onCancelled(DatabaseError.zzqv(DatabaseError.PERMISSION_DENIED));

    assertTrue(error[0] instanceof DatabaseException);
  }

  private class Foo implements HasId {
    private String id;

    @Override
    public String getId() {
      return id;
    }

    @Override
    public void setId(@NonNull final String id) {
      this.id = id;
    }
  }
}
