package littiere.jeremie.tp_runtimepermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_RUNTIME_PERMISSION = "TAG_RUNTIME_PERMISSION";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int MULTIPLE_PERMISSION_REQUEST_CODE = 5;
    static final int PICK_CONTACT_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TP Runtime Permission");

        // Ouverture d'une pop up afin d'accèder aux paramètres de l'application depuis le package manager.
        Button settingButton = (Button) findViewById(R.id.btn_settings);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsDialog();
            }
        });

        //Liste les permissions autorisées sur l'application
        Button listGrantedPermissionButton = (Button) findViewById(R.id.bt_perm_granted);
        listGrantedPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupération du nom du Package
                String packageName = getPackageName();
                // récupération de l'ensemble des permissions sur le package
                List<String> permissionList = getAllPermissions(packageName);
                StringBuffer grantedPermissionBuf = new StringBuffer();
                grantedPermissionBuf.append("Permissions accordées pour notre application: \r\n\n");

                // Pour chaque permission, vérification si autorisée au Runtime
                int size = permissionList.size();
                for (int i = 0; i < size; i++) {
                    String permission = permissionList.get(i);
                    // Affichage de la permission si accordée
                    if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                            == PackageManager.PERMISSION_GRANTED) {
                        grantedPermissionBuf.append(permissionList.get(i));
                        if (i < size - 1) {
                            grantedPermissionBuf.append(",\r\n");
                        }
                    }
                }
                // Affichage de l'ensemble des permissions autorisés dans une alertDialog.
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage(grantedPermissionBuf.toString());
                alertDialog.show();
            }
        });

        // Ouverture d'un browser WEB.
        Button accountsPermissionButton = (Button) findViewById(R.id.btn_internet);
        accountsPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                        == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,
                            "Permission INTERNET autorisée", Toast.LENGTH_LONG).show();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                    startActivity(browserIntent);
                }
            }
        });

        // **** STEP 2 – CAMERA *******************
        // Vérifie si app Photo est accordée dans les Runtime Permissions et le lance si oui.
        Button appPhotoButton = (Button) findViewById(R.id.btn_camera);
        appPhotoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // Vérification depuis le Package manager: la permission est elle accordée ?
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // shouldShowRequestPermissionRationale permet de savoir si la permission à déja était
                    // demandée par l’utilisateur.
                    // Si oui, mieux vaux ne pas la redemander ... Nous pouvons prévoir un message d'information par exemple
                    // Sinon, vous pouvez demander la permission à l’aide de la méthode ActivityCompat.requestPermissions.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.CAMERA)) {
                        informationDialog();
                    }

                    //Demande à l'utilisateur d'autoriser la runtime permission: Ouverture d'une boîte de dialogue: Autoriser ou non
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CODE);

                } else {
                    Toast.makeText(MainActivity.this,
                            "Manifest.permission.CAMERA permission déjà accordée", Toast.LENGTH_LONG).show();
                    // Ouverture de l'appareil photo
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivity(intent);
                }
            }
        });

        // **** STEP 3 – CONTACTS - Permission Group *******************
        // Vérifie si la lecture des contacts est accordée dans les Runtime Permissions et le lance si oui.
        Button readContactPermissionButton = (Button) findViewById(R.id.btn_read_contact);
        readContactPermissionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // Vérification depuis le Package manager: la permission est elle accordée ?
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // shouldShowRequestPermissionRationale permet de savoir si la permission à déja était
                    // demandée par l’utilisateur.
                    // Si oui, mieux vaux ne pas la redemander ... Nous pouvons prévoir un message d'information par exemple
                    // Sinon, vous pouvez demander la permission à l’aide de la méthode ActivityCompat.requestPermissions.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.CAMERA)) {
                        informationDialog();
                    }


                    /* Demande à l'utilisateur d'autoriser la runtime permission:
                            Ouverture d'une boîte de dialogue: Autoriser ou non */
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CODE);
                } else {
                    Toast.makeText(MainActivity.this,
                            "Manifest.permission.READ_CONTACTS permission déjà accordée", Toast.LENGTH_LONG).show();
                    // Ouverture des contacts
                    Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
                }
            }
        });

        // Création d'un contact
        Button contactCreatePermissionButton = (Button) findViewById(R.id.btn_create_contact);
        contactCreatePermissionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // Vérification depuis le Package manager: la permission est elle accordée ?
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // shouldShowRequestPermissionRationale permet de savoir si la permission à déja était
                    // demandée par l’utilisateur.
                    // Si oui, mieux vaux ne pas la redemander ... Nous pouvons prévoir un message d'information par exemple
                    // Sinon, vous pouvez demander la permission à l’aide de la méthode ActivityCompat.requestPermissions.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.CAMERA)) {
                        informationDialog();
                    }


                    /* Demande à l'utilisateur d'autoriser la runtime permission:
                            Ouverture d'une boîte de dialogue: Autoriser ou non */
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_CONTACTS},
                            PERMISSION_REQUEST_CODE);

                } else {
                    Toast.makeText(MainActivity.this,
                            "Manifest.permission.WRITE_CONTACTS permission déjà accordée", Toast.LENGTH_LONG).show();
                    // Création d'un contact
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, "John");
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, "0123");
                    int PICK_CONTACT = 100;
                    startActivityForResult(intent, PICK_CONTACT);
                }
            }
        });

        // **** STEP 4 – MULTIPLE PERMISSIONS*********************

        // Demande de trois permissions
        Button multipleRequestedPermissionButton = (Button) findViewById(R.id.btn_plusieurs_permissions);
        multipleRequestedPermissionButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                // Liste de permissions à demander
                List<String> listPermissionsNeeded = new ArrayList<>();

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
                }
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
                }

                // Si il y a des permissions à demander:
                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSION_REQUEST_CODE);
                    //Deuxième paramètre [] permission pourrait être le suivant par exemple:
                    // new String[] { Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION }

                } else {
                    Toast.makeText(MainActivity.this, "Permissions CALL_PHONE / ACCESS_COARSE_LOCATION / RECORD_AUDIO autorisées ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    // Affichage de l'ensemble des permissions accordées pour cette application TP_runtimepermission
    // affichage des permissions "normales" (manifest) & "dangereuses" qui sont autorisées
    private List<String> getAllPermissions(String packageName) {
        List<String> permissionsList = new ArrayList<String>();
        try {

            // Récupération du nom du Package.
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

            // Ajout de chaque permissions
            int length = packageInfo.requestedPermissions.length;
            for (int i = 0; i < length; i++) {
                String requestPermission = packageInfo.requestedPermissions[i];
                permissionsList.add(requestPermission);
            }
        } catch (PackageManager.NameNotFoundException ex) {
            Log.e(TAG_RUNTIME_PERMISSION, "getAllGrantedPermissions: " + ex.getMessage(), ex);
        } finally {
            return permissionsList;
        }
    }

    private void openSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Permissions requises");
        builder.setMessage("Pour pouvoir bénéficier de toutes les fonctionnalités de cette application, des permissions sont requises");
        builder.setPositiveButton("Paramètres de l'App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void informationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Permissions requises");
        builder.setMessage("Vous avez refusé la permission pour cette fonctionnalité. Veuillez l'autoriser si vous souhaitez l'utiliser");
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // Prise en compte de la réponse de  ActivityCompat.requestPermissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Retouur de notre demande de permission (PERMISSION_REQUEST_CODE)

            if (grantResults.length > 0) {
                // Construction de l'affichage du résultat de la demande de runtime Permission
                StringBuffer msgBuf = new StringBuffer();
                int grantResult = grantResults[0];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    msgBuf.append("Permission accordée pour : ");
                } else {
                    msgBuf.append("Permission refusée pour : ");
                }

                // Ajout des permissions autorisées dans le message:
                if (permissions != null) {
                    int length = permissions.length;
                    for (int i = 0; i < length; i++) {
                        String permission = permissions[i];
                        msgBuf.append(permission);
                        if (i < length - 1) {
                            msgBuf.append(",");
                        }
                    }
                }

                // Affichage du message dans le Toaster
                Toast.makeText(getApplicationContext(), msgBuf.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}