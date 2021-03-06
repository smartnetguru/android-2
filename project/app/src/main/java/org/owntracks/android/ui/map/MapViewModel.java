package org.owntracks.android.ui.map;

import android.content.Context;
import android.databinding.ObservableMap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;

import org.owntracks.android.data.model.Contact;
import org.owntracks.android.data.repos.ContactsRepo;
import org.owntracks.android.injection.qualifier.AppContext;
import org.owntracks.android.injection.scopes.PerActivity;
import org.owntracks.android.model.FusedContact;
import org.owntracks.android.ui.contacts.ContactsViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import timber.log.Timber;


/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */

@PerActivity
public class MapViewModel extends ContactsViewModel<MapMvvm.View> implements MapMvvm.ViewModel {



    private final Context ctx;
    private final ContactsRepo contactsRepo;

    private Contact activeContact;
    private int mBottomSheetState = BottomSheetBehavior.STATE_HIDDEN;
    private ObservableMap<String, FusedContact> contacts;


    @Inject
    public MapViewModel(@AppContext Context context, ContactsRepo contactsRepo) {
        super(context);
        Timber.v("onCreate");

        this.ctx = context.getApplicationContext();
        this.contactsRepo = contactsRepo;
    }

    @Override
    public void saveInstanceState(@NonNull Bundle outState) {
    }

    @Override
    public void restoreInstanceState(@NonNull Bundle savedInstanceState) {
    }

    public void attachView(@NonNull MapMvvm.View view, @Nullable Bundle savedInstanceState) {
        super.attachView(view, savedInstanceState);
    }


        @Override
    public void detachView() {
        super.detachView();
    }


    @Override
    public int getBottomSheetState() {
        return mBottomSheetState;
    }


    @Override
    public String getFusedAccuracy() {
        return "acc";
    }

    @Override
    public String getFusedTimestamp() {
        return "tst";
    }

    @Override
    public String getFusedContactId() {
        return "id";
    }

    @Override
    public void onMapReady() {
        Timber.v("onMapReady");
    }

    private void subscribeToContactChanges() {
         contacts = contactsRepo.getAll();
        contacts.addOnMapChangedCallback(new ContactsRepoSubscriber());
    }

    private class ContactsRepoSubscriber extends ObservableMap.OnMapChangedCallback<ObservableMap<String, FusedContact>, String, FusedContact>{
        @Override
        public void onMapChanged(ObservableMap<String, FusedContact> map, String key) {
            FusedContact c = map.get(key);
            Timber.v("changedKey:%s", key);
            if(c == null)
                getView().removeMarker(key);
            else
                getView().updateMarker(c);
        }
    }


    @Override
    public void onMapMarkerClick(@NonNull Contact c) {

    }

    @Override
    public void onMapClick() {
        hideBottomSheet();
    }

    private void hideBottomSheet() {
        setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void expandBottomSheet() {
        setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void collapseBottomSheet() {
        setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    public void setBottomSheetState(int bottomSheetState) {
        this.mBottomSheetState = bottomSheetState;
        notifyChange();
    }
}
