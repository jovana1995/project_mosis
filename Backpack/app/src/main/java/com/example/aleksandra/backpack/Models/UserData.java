package com.example.aleksandra.backpack.models;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.UUID;

public class UserData {
    private ArrayList<User> Users;
    private HashMap<String,Integer> userKeyIndexMapping;
    private DatabaseReference database;
    private static final String FIREBASE_CHILD="user";


    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference   ;
    private UserData()
    {
        Users=new ArrayList<User>();
        userKeyIndexMapping=new HashMap<String,Integer>();
        database= FirebaseDatabase.getInstance().getReference();
        database.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        database.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);
    }
    ValueEventListener parentEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(updateEventListener != null){
                updateEventListener.onListUpdate();
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
    ChildEventListener childEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            if(!userKeyIndexMapping.containsKey(ukey))
            {
                User user=dataSnapshot.getValue(User.class);
                user.UserKey=ukey;
                Users.add(user);
                userKeyIndexMapping.put(ukey,Users.size()-1);
                if(updateEventListener != null) {
                    updateEventListener.onListUpdate();
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String ukey=dataSnapshot.getKey();
            User user=dataSnapshot.getValue(User.class);
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
            if(updateEventListener != null){
                updateEventListener.onListUpdate();
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String ukey=dataSnapshot.getKey();
            if(userKeyIndexMapping.containsKey(ukey))
            {
                int index=userKeyIndexMapping.get(ukey);
                Users.remove(index);
                recreateKeyIndexMapping();
            }
            if(updateEventListener != null){
                updateEventListener.onListUpdate();
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
        public  static  final UserData instance=new UserData();
    }
    public static UserData getInstance()
    {
        return SingltonHolder.instance;
    }
    public ArrayList<User> getUsers()
    {
        return  Users;
    }
    public void addNewUser(User user)
    {
        //DA LI OVDE TREBA DA SE POVUKU SVI PODACI?
        //PRILIKOM PROVOG PRIJAVLJIBANJA , TREBA DA SE POVUKU SVI OSTALI KORISNICI?

            String key=database.push().getKey();
            Users.add(user);
            userKeyIndexMapping.put(key,Users.size()-1);
            database.child(FIREBASE_CHILD).child(key).setValue(user);
            user.UserKey=key;
    }
    public  User getUser(int index)
    {
     return Users.get(index);
    }
    public  void deleteUser(int index)
    {
        database.child(FIREBASE_CHILD).child(Users.get(index).UserKey).removeValue();
        Users.remove(index);
        recreateKeyIndexMapping();

    }
    public void uploadImage(Uri filePath, String name) {
        storage = FirebaseStorage.getInstance();
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
        }
    }
    public Uri getImage(String name)
    {
        Uri downloadURI = storageReference.child("circleako.png").getDownloadUrl().getResult();
        return  downloadURI;
    }
    public  void updateUser(int index, String nme, String srnme, String e, String pass, String g,String u)
    {

        User user=Users.get(index);
        user.setFirst_name(nme);
        user.setLast_name(srnme);
        user.setEmail(e);
        user.setPassword(pass);
        user.setGender(g);
        user.setUsername(u);
        database.child(FIREBASE_CHILD).child(user.UserKey).setValue(user);
    }
    public  void updateUser(int index, String nme, String srnme, String e, String pass, String g,String u,String lat, String lang)
    {

        User user=Users.get(index);
        user.setFirst_name(nme);
        user.setLast_name(srnme);
        user.setEmail(e);
        user.setPassword(pass);
        user.setGender(g);
        user.setUsername(u);
        user.setLongitude(lang);
        user.setLatitude(lat);
        database.child(FIREBASE_CHILD).child(user.UserKey).setValue(user);
    }
    private  void recreateKeyIndexMapping()
    {
        userKeyIndexMapping.clear();
        for(int i=0;i<Users.size();i++)
            userKeyIndexMapping.put(Users.get(i).UserKey,i);

    }
    ListUpdateEventListener updateEventListener;
    public void setEventListener(ListUpdateEventListener listener)
    {
        updateEventListener = listener;
    }

    public interface ListUpdateEventListener {
        void onListUpdate();
    }


}
