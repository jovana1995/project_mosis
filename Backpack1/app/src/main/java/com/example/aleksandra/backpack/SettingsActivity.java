package com.example.aleksandra.backpack;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.aleksandra.backpack.Models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<String> usersString;
    private ArrayList<User> users;

    private Map<String, String> friends;
    private Collection<String> friendsUsername;

    private AcceptThread serverThread;
    private ConnectThread clientThread;
    private ArrayList<BluetoothDevice> devices;
    private BluetoothDevice bluetoothDevice;
    private String userRequest;
    private String myID;
    //FirebaseAuth mAuth;
    private Intent i;
    private int REQUEST_ENABLE_BT = 1;
    private int REQUEST_CHANGE_BT = 2;
    //private ValueEventListener friendsListener;
    //private DatabaseReference friendRefrence;
    private String username;
    private User user;


    @SuppressLint("HandlerLeak")
    public android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Log.i("tag", "server hendler");
            switch (msg.what) {
                case ConnectThread.MessageConstants.MESSAGE_READ:
                    byte[] b = (byte[]) msg.obj;
                    String s = new String(b, 0, msg.arg1);
                    Log.i("Podaci what : ", s);
                    Toast.makeText(getApplicationContext(), "Handler se pokrece", Toast.LENGTH_SHORT).show();
                    ShowMessage(s);
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    public android.os.Handler cHandler = new android.os.Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Log.i("tag", "klijent hendler");
            byte[] b;
            String s;
            boolean vOut;
            switch (msg.what) {
                case ConnectThread.MessageConstants.MESSAGE_WRITE:
                    b = (byte[]) msg.obj;
                    s = new String(b, 0, msg.arg1);
                    if (s.compareTo("0000000000000000000000000000") != 0) {
                        ShowUserNotification(s);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void ShowUserNotification(String userID) {

        //FirebaseDatabase db = FirebaseDatabase.getInstance();
        //final DatabaseReference dr = db.getReference().child("User");//.child(userID);
        //final Query query = dr.orderByKey().equalTo(userID).limitToFirst(1);

        //final ChildEventListener cel = query.addChildEventListener(new ChildEventListener() {
        //  @Override
        //public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        //  User user = dataSnapshot.getValue(User.class);
        Toast.makeText(SettingsActivity.this, "You are now friends with " + "jovana.s@gmail.com" /*user.username*/ + ".", Toast.LENGTH_LONG).show();

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_ID")
                        .setSmallIcon(R.drawable.ic_search)
                        .setContentTitle(/*user.username*/"jovana.s@gmail.com")
                        .setSound(uri)
                        .setContentText(/*user.name*/"Jovana" + " has accepted your friend request.");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
        //query.removeEventListener(this);
        //}

        //@Override
        //public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

        //@Override
        //public void onChildRemoved(DataSnapshot dataSnapshot) { }

        //@Override
        //public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

        //@Override
        //public void onCancelled(DatabaseError databaseError) { }
        //});
    }


    private final BroadcastReceiver mReceiver;
    {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                Switch s;
                Toast.makeText(getApplicationContext(), "U broadcast receiveru smo!", Toast.LENGTH_SHORT).show();
                assert action != null;
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR);
                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            s = findViewById(R.id.switch_bluetooth);
                            s.setChecked(false);
                            if (clientThread != null)
                                clientThread.cancel();
                            if (serverThread != null)
                                serverThread.cancel();
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            break;
                        case BluetoothAdapter.STATE_ON:
                            mBluetoothAdapter.setName(username);
                            s = findViewById(R.id.switch_bluetooth);
                            s.setChecked(true);
                            serverThread = new AcceptThread(mHandler, myID);
                            serverThread.mSettingsActivity = SettingsActivity.this;
                            serverThread.start();
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            break;
                    }
                }
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    Toast.makeText(getApplicationContext(), "Pronadjen uredjaj", Toast.LENGTH_SHORT).show();
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    showDevices(deviceName, device);
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //mAuth = FirebaseAuth.getInstance();
        //myID = mAuth.getCurrentUser().getUid();
        friends = new HashMap<>();
        friendsUsername = new ArrayList<>();
        getAllFriends();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.cancelDiscovery();

        final SharedPreferences sharedPref = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        sharedPref.edit().clear().apply();
        username = sharedPref.getString("username", "");
        boolean friends = sharedPref.getBoolean("showFriends", false);
        boolean challenges = sharedPref.getBoolean("showChallenges", true);

        //Switch sw = findViewById(R.id.switchService);
        //if (isMyServiceRunning(MyService.class)) TODO ovo pojma nemam sta je to je neka njihova klasa koju sam izkomentarisala
        //sw.setChecked(true);
        //else
        //    sw.setChecked(false);

        if (mBluetoothAdapter == null) {
            Toast.makeText(SettingsActivity.this, "Device doesn't support Bluetooth.", Toast.LENGTH_LONG).show();
        }

        if (mBluetoothAdapter.isEnabled()) {
            Switch s = findViewById(R.id.switch_bluetooth);
            s.setChecked(true);
        }

        users = new ArrayList<>();
        devices = new ArrayList<>();

        //Switch s1 = findViewById(R.id.switch_friends);
        //s1.setChecked(friends);
        //Switch s2 = findViewById(R.id.switch_challenges);
        //s2.setChecked(challenges);

        /*findViewById(R.id.switch_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch s = findViewById(R.id.switch_friends);
                SharedPreferences.Editor editor = sharedPref.edit();

                if (s.isChecked())
                    editor.putBoolean("showFriends", true);
                else
                    editor.putBoolean("showFriends", false);
                editor.apply();
            }
        });*/

        /*findViewById(R.id.switch_challenges).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch s = findViewById(R.id.switch_challenges);
                SharedPreferences.Editor editor = sharedPref.edit();

                if (s.isChecked())
                    editor.putBoolean("showChallenges", true);
                else
                    editor.putBoolean("showChallenges", false);
                editor.apply();
            }
        });*/


        findViewById(R.id.backToSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //findViewById(R.id.friendrequest_container).setVisibility(View.INVISIBLE);
                //findViewById(R.id.settings_container).setVisibility(View.VISIBLE);

                ListView usersList = findViewById(R.id.listViewDevices);
                usersList.setAdapter(null);
                users = new ArrayList<>();
                mBluetoothAdapter.cancelDiscovery();

                if (clientThread != null)
                    clientThread.cancel();

                Toast.makeText(getApplicationContext(), "startovana je serverthread", Toast.LENGTH_SHORT).show();
                serverThread = new AcceptThread(mHandler, myID);
                serverThread.mSettingsActivity = SettingsActivity.this;
                serverThread.start();

                //friendRefrence.addValueEventListener(friendsListener);
            }
        });

        //i = new Intent(getApplicationContext(), MyService.class);
        //findViewById(R.id.switchService).setOnClickListener(new View.OnClickListener() {

            //Switch s = findViewById(R.id.switchService);

          //  @Override
          //  public void onClick(View v) {
                //if (s.isChecked()) {
                //Intent firebase = new Intent(SettingsActivity.this, FirebaseVisibleActivity.class);
                //startActivity(firebase);
                //    startService(i);
                //} else {
                //FirebaseDatabase db = FirebaseDatabase.getInstance();
                //DatabaseReference dr = db.getReference("User").child(myID);
                //dr.child("visible").setValue(false);
                //    stopService(i);
                //}
          //  }
        //});

        findViewById(R.id.switch_bluetooth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch s = findViewById(R.id.switch_bluetooth);
                if (s.isChecked()) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(enableBtIntent);

                        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                        startActivity(discoverableIntent);
                    }
                } else {
                    mBluetoothAdapter.disable();
                    if (serverThread != null)
                        serverThread.cancel();
                }
            }
        });

        ListView usersList = findViewById(R.id.listViewDevices);
        usersList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
          //  if(users.size()!=0) {
                User user = users.get(info.position);
           // }

                menu.setHeaderTitle(/*user.username*/"jovana.s@gmail.com");
                menu.add(0, 1, 1, "Send Friend Request");
            }
        });


        findViewById(R.id.searchFriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("Found users:");
                if (serverThread != null) {
                    serverThread.cancel();
                }

                //findViewById(R.id.friendrequest_container).setVisibility(View.VISIBLE);
                //findViewById(R.id.settings_container).setVisibility(View.INVISIBLE);
                users = new ArrayList<>();

                if (!mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.startDiscovery();
                }
            }
        });

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter1);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Bundle positionBundle = new Bundle();
        positionBundle.putInt("position", info.position);
        Intent i;

        if (item.getItemId() == 1) {
            clientThread = new ConnectThread(devices.get(info.position), cHandler);
            clientThread.start();
      //      Toast.makeText(getApplicationContext(), "client thread stated", Toast.LENGTH_SHORT).show();
            //friendRefrence.removeEventListener(friendsListener);
        }
        return super.onContextItemSelected(item);
    }

    private void showDevices(final String username, final BluetoothDevice device) {
        //FirebaseDatabase db = FirebaseDatabase.getInstance();
        //DatabaseReference dr = db.getReference("User");
        //final Query query = dr.orderByChild("username").equalTo(username);

        //final ChildEventListener el = query.addChildEventListener(new ChildEventListener() {
        //  @Override
        //public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) { //
        //  User user = dataSnapshot.getValue(User.class);
        User user = new User("Jovana","S","jovana.s@gmail.com", "jovana.s@gmail.com", 12); //TODO dodat konstruktor
        if (!friendsUsername.contains(/*user.username*/"jovana.s@gmail.com")) {
            boolean isti = false;
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device1 : pairedDevices) {

                    if (device.getAddress().compareTo(device1.getAddress()) == 0) {
                        isti = true;
                    }
                }
                if (!isti) {
                    users.add(user);
                    devices.add(device);
                    ListView usersList = findViewById(R.id.listViewDevices);
                    usersList.setAdapter(new ArrayAdapter<User>(SettingsActivity.this, android.R.layout.simple_list_item_1, users));
                    usersList.setVisibility(View.VISIBLE);
                }
            }
        }
        //query.removeEventListener(this);
        //}

        //@Override
        //public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        //}

        //@Override
        //public void onChildRemoved(DataSnapshot dataSnapshot) {
        //}

        //@Override
        //public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        //}

        //@Override
        //public void onCancelled(DatabaseError databaseError) {
        //}
        //});

        //query.addValueEventListener(new ValueEventListener() {
        //  @Override
        //public void onDataChange(DataSnapshot dataSnapshot) {
        //  if (dataSnapshot == null) {
        //    query.removeEventListener(el);
        //}
        //query.removeEventListener(this);
        //}
        //@Override
        //public void onCancelled(DatabaseError databaseError) {
        //}
        //});
    }

    private void getAllFriends() {
        //friendRefrence = FirebaseDatabase.getInstance().getReference().child("Friends").child(myID);
        //friendsListener = friendRefrence.addValueEventListener(new ValueEventListener() {
        //  @Override
        //public void onDataChange(DataSnapshot dataSnapshot) {
        //friends = (Map<String, String>) dataSnapshot.getValue();
        if (friends != null)
            friendsUsername = friends.values();
        else
            friendsUsername = new ArrayList<>();
        //}

        //@Override
        //public void onCancelled(DatabaseError databaseError) {
        //}
        //});
    }

    public void ShowMessage(String id) {
 //       Toast.makeText(this, "Pokrecemo FriendRequestActivity", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(SettingsActivity.this, FriendRequestActivity.class);
        i.putExtra("userID", id);
        startActivityForResult(i, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, "Friend request accepted", Toast.LENGTH_SHORT).show();
            serverThread.accept = true;
            //serverThread.semaphore.release();
        }
        if (resultCode == RESULT_FIRST_USER) {
            serverThread.accept = false;
            //serverThread.semaphore.release();
        }

        //ListView friendslist = findViewById(R.id.listFriends);
        //getAllFriends();
        //friendslist.setAdapter(new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_list_item_1, (List<String>) friendsUsername));
    }

    /*private void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            Toast.makeText(SettingsActivity.this, "Unpairing ... ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Remove bond", e.getMessage());
        }
    }*/

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

   /* private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }*/
}
