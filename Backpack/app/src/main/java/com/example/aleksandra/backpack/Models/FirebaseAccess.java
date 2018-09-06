package com.example.aleksandra.backpack.Models;

import android.app.Application;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.EventLog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseAccess {

    private ArrayList<User> Users;
    private ArrayList<PlaceModel> Places;
    private ArrayList<EventModel> Events;

    private HashMap<String,Integer> userKeyIndexMapping;
    private HashMap<String, Integer> placeKeyIndexMapping;
    private HashMap<String, Integer> eventKeyIndexMapping;

    private DatabaseReference database;

    private static final String FIREBASE_USER_CHILD="user";
    private static final String FIREBASE_PLACE_CHILD="place";
    private static final String FIREBASE_EVENT_CHILD="event";


    private FirebaseAccess()
    {
        Users=new ArrayList<User>();
        Places=new ArrayList<PlaceModel>();
        Events=new ArrayList<EventModel>();


        placeKeyIndexMapping=new HashMap<String, Integer>();
        userKeyIndexMapping=new HashMap<String,Integer>();
        eventKeyIndexMapping=new HashMap<String,Integer>();


        database= FirebaseDatabase.getInstance().getReference();

        database.child(FIREBASE_USER_CHILD).addChildEventListener(userChildEventListener);
        database.child(FIREBASE_USER_CHILD).addListenerForSingleValueEvent(userParentEventListener);
        database.child(FIREBASE_PLACE_CHILD).addChildEventListener(placeChildEventListener);
        database.child(FIREBASE_PLACE_CHILD).addListenerForSingleValueEvent(placeParentEventListener);
        database.child(FIREBASE_EVENT_CHILD).addChildEventListener(eventChildEventListener);
        database.child(FIREBASE_EVENT_CHILD).addListenerForSingleValueEvent(eventParentEventListener);
    }

    //samo jednom se izvrsava
    ValueEventListener userParentEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(userUpdateEventListener != null){
                userUpdateEventListener.onUserListUpdate();
            }
            ArrayList<User> notes = new ArrayList<>();
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                User note = noteDataSnapshot.getValue(User.class);
                note.UserKey=noteDataSnapshot.getKey();
                notes.add(note);
            }
            Users=notes;

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ValueEventListener placeParentEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(placeUpdateEventListener != null){
                placeUpdateEventListener.onPlaceListUpdate();
            }
            ArrayList<PlaceModel> notes = new ArrayList<>();
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                PlaceModel note = noteDataSnapshot.getValue(PlaceModel.class);
                note.placeKey=noteDataSnapshot.getKey();
                notes.add(note);
            }
            Places=notes;

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener eventParentEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(eventUpdateEventListener != null){
                eventUpdateEventListener.onEventListUpdate();
            }
            ArrayList<EventModel> notes = new ArrayList<>();
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                EventModel note = noteDataSnapshot.getValue(EventModel.class);
                note.eventKey=noteDataSnapshot.getKey();
                notes.add(note);
            }
            Events=notes;

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    //na svaku promenu child
    ChildEventListener userChildEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            if(!userKeyIndexMapping.containsKey(ukey))
            {
                User user=dataSnapshot.getValue(User.class);
                user.UserKey=ukey;
                Users.add(user);
                userKeyIndexMapping.put(ukey,Users.size()-1);
                if(userUpdateEventListener != null) {
                    userUpdateEventListener.onUserListUpdate();
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            com.example.aleksandra.backpack.Models.User user=dataSnapshot.getValue(com.example.aleksandra.backpack.Models.User.class);
            user.UserKey=ukey;
            if(userKeyIndexMapping.containsKey(ukey))
            {
                int index=userKeyIndexMapping.get(ukey);
                Users.set(index,user);
            }
            else
            {
                Users.add(user);
                userKeyIndexMapping.put(ukey,Users.size()-1);
            }
            if(userUpdateEventListener != null){
                userUpdateEventListener.onUserListUpdate();
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String ukey=dataSnapshot.getKey();
            if(userKeyIndexMapping.containsKey(ukey))
            {
                int index=userKeyIndexMapping.get(ukey);
                Users.remove(index);
                recreateUserKeyIndexMapping();
            }
            if(userUpdateEventListener != null){
                userUpdateEventListener.onUserListUpdate();
            }

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ChildEventListener placeChildEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            if(!userKeyIndexMapping.containsKey(ukey))
            {
                PlaceModel user=dataSnapshot.getValue(PlaceModel.class);
                user.placeKey=ukey;
                Places.add(user);
                placeKeyIndexMapping.put(ukey,Places.size()-1);
                if(placeUpdateEventListener != null) {
                    placeUpdateEventListener.onPlaceListUpdate();
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            PlaceModel user=dataSnapshot.getValue(PlaceModel.class);
            user.placeKey=ukey;
            if(userKeyIndexMapping.containsKey(ukey))
            {
                int index=placeKeyIndexMapping.get(ukey);
                Places.set(index,user);
            }
            else
            {
                Places.add(user);
                placeKeyIndexMapping.put(ukey,Places.size()-1);
            }
            if(placeUpdateEventListener != null) {
                placeUpdateEventListener.onPlaceListUpdate();
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String ukey=dataSnapshot.getKey();
            if(userKeyIndexMapping.containsKey(ukey))
            {
                int index=userKeyIndexMapping.get(ukey);
                Places.remove(index);
                recreatePlaceKeyIndexMapping();
            }
            if(placeUpdateEventListener != null) {
                placeUpdateEventListener.onPlaceListUpdate();
            }

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ChildEventListener eventChildEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            if(!eventKeyIndexMapping.containsKey(ukey))
            {
                EventModel user=dataSnapshot.getValue(EventModel.class);
                user.eventKey=ukey;
                Events.add(user);
                eventKeyIndexMapping.put(ukey,Events.size()-1);
                if(eventUpdateEventListener != null) {
                    eventUpdateEventListener.onEventListUpdate();
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            EventModel user=dataSnapshot.getValue(EventModel.class);
            user.eventKey=ukey;
            if(eventKeyIndexMapping.containsKey(ukey))
            {
                int index=eventKeyIndexMapping.get(ukey);
                Events.set(index,user);
            }
            else
            {
                Events.add(user);
                userKeyIndexMapping.put(ukey,Users.size()-1);
            }
            if(eventUpdateEventListener != null) {
                eventUpdateEventListener.onEventListUpdate();
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String ukey=dataSnapshot.getKey();
            if(eventKeyIndexMapping.containsKey(ukey))
            {
                int index=eventKeyIndexMapping.get(ukey);
                Events.remove(index);
                recreatePlaceKeyIndexMapping();
            }
            if(eventUpdateEventListener != null) {
                eventUpdateEventListener.onEventListUpdate();
            }

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private static class  SingltonHolder
    {
        public  static  final FirebaseAccess instance=new FirebaseAccess();
    }
    public static FirebaseAccess getInstance()
    {
        return SingltonHolder.instance;
    }

    public ArrayList<User> getUsers()
    {
        return  Users;
    }
    public ArrayList<PlaceModel> getPlaces()
    {
        return  Places;
    }
    public ArrayList<EventModel> getEvents()
    {
        return  Events;
    }

    public void addNewUser(User user)
    {
        //DA LI OVDE TREBA DA SE POVUKU SVI PODACI?
        //PRILIKOM PROVOG PRIJAVLJIBANJA , TREBA DA SE POVUKU SVI OSTALI KORISNICI?

            String key=database.push().getKey();
            Users.add(user);
            userKeyIndexMapping.put(key,Users.size()-1);
            database.child(FIREBASE_USER_CHILD).child(key).setValue(user);
            user.UserKey=key;
    }
    public  User getUser(int index)
    {
     return Users.get(index);
    }
    public  void deleteUser(int index)
    {
        database.child(FIREBASE_USER_CHILD).child(Users.get(index).UserKey).removeValue();
        Users.remove(index);
        recreateUserKeyIndexMapping();

    }

    public void addNewPlace(PlaceModel user)
    {
        //DA LI OVDE TREBA DA SE POVUKU SVI PODACI?
        //PRILIKOM PROVOG PRIJAVLJIBANJA , TREBA DA SE POVUKU SVI OSTALI KORISNICI?

        String key=database.push().getKey();
        Places.add(user);
        placeKeyIndexMapping.put(key,Places.size()-1);
        database.child(FIREBASE_PLACE_CHILD).child(key).setValue(user);
        user.placeKey=key;
    }
    public  PlaceModel getPlace(int index)
    {
        return Places.get(index);
    }
    public  void deletePlace(int index)
    {
        database.child(FIREBASE_PLACE_CHILD).child(Places.get(index).placeKey).removeValue();
        Places.remove(index);
        recreatePlaceKeyIndexMapping();

    }
    public void addNewEvent(EventModel user)
    {
        //DA LI OVDE TREBA DA SE POVUKU SVI PODACI?
        //PRILIKOM PROVOG PRIJAVLJIBANJA , TREBA DA SE POVUKU SVI OSTALI KORISNICI?

        String key=database.push().getKey();
        Events.add(user);
        eventKeyIndexMapping.put(key,Events.size()-1);
        database.child(FIREBASE_EVENT_CHILD).child(key).setValue(user);
        user.eventKey=key;
    }
    public  EventModel getEvent(int index)
    {
        return Events.get(index);
    }
    public  void deleteEvent(int index)
    {
        database.child(FIREBASE_EVENT_CHILD).child(Events.get(index).eventKey).removeValue();
        Events.remove(index);
        recreateEventKeyIndexMapping();

    }
    public void uploadImage(Uri filePath, String name) {
      /*  storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if(filePath != null)
        {

            StorageReference ref = storageReference.child("images/"+ name);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    })
                    ;
        }*/
    }
  /*  public Uri getImage(String name)
    {
        Uri downloadURI = storageReference.child("circleako.png").getDownloadUrl().getResult();
        return  downloadURI;
    }*/
    public  void updateUser(int index, String nme, String srnme, String e, String pass, String g,String u)
    {

        User user=Users.get(index);
        user.setFirst_name(nme);
        user.setLast_name(srnme);
        user.setEmail(e);
        user.setPassword(pass);
        user.setGender(g);
        user.setUsername(u);
        database.child(FIREBASE_USER_CHILD).child(user.UserKey).setValue(user);
    }
    public  void updatePlace(int index, String comment, String placeName, String placeLocation, String placeState, String placePhone,String placeImage,String timePassed, String lon, String lot, String u)
    {

        PlaceModel pm=Places.get(index);
        pm.comment = comment;
        pm.placeName = placeName;
        pm.placeLocation = placeLocation;
        pm.placeState = placeState;
        pm.placePhone = placePhone;
        pm.placeImage = placeImage;
        pm.timePassed = timePassed;
        pm.longitute=lot;
        pm.longitute=lon;
        pm.userId=u;
        database.child(FIREBASE_PLACE_CHILD).child(pm.placeKey).setValue(pm);
    }
    public  void updateEvents(int index,String when, String where, String personImage, String comment, String name, String state, String placeName, String placeLocation, String timePassed, String pname)
    {

        EventModel pm=Events.get(index);
        pm.when = when;
        pm.where = where;
        pm.personImage = personImage;
        pm.comment = comment;
        pm.name = name;
        pm.state = state;
        pm.placeName = placeName;
        pm.placeLocation = placeLocation;
        pm.timePassed = timePassed;
        pm.userID=pname;
        database.child(FIREBASE_EVENT_CHILD).child(pm.eventKey).setValue(pm);
    }
    private  void recreateUserKeyIndexMapping() {
        userKeyIndexMapping.clear();
        for (int i = 0; i < Users.size(); i++)
            userKeyIndexMapping.put(Users.get(i).UserKey, i);
    }
    private  void recreatePlaceKeyIndexMapping() {
        placeKeyIndexMapping.clear();
        for(int i=0;i<Places.size();i++)
            userKeyIndexMapping.put(Places.get(i).placeKey,i);

    }
    private  void recreateEventKeyIndexMapping() {
        eventKeyIndexMapping.clear();
        for(int i=0;i<Events.size();i++)
            eventKeyIndexMapping.put(Events.get(i).eventKey,i);

    }

    userListUpdateEventListener userUpdateEventListener;
    public void setUserEventListener(userListUpdateEventListener listener)
    {
        userUpdateEventListener = listener;
    }

    public interface userListUpdateEventListener {
        void onUserListUpdate();
    }

    placeListUpdateEventListener placeUpdateEventListener;
    public void setPlaceEventListener(placeListUpdateEventListener listener)
    {
        placeUpdateEventListener = listener;
    }

    public interface placeListUpdateEventListener {
        void onPlaceListUpdate();
    }

    eventListUpdateEventListener eventUpdateEventListener;
    public void setEventListener(eventListUpdateEventListener listener)
    {
        eventUpdateEventListener = listener;
    }

    public interface eventListUpdateEventListener {
        void onEventListUpdate();
    }


}

