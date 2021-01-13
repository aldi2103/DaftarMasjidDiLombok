package simple.example.daftarmasjid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btnTambahMenuRestoran;
    ImageButton btnChangeUserName;
    ListView lvDaftarMenuRestoran;
    TextView txNoData, txUsername;
    DaftarMasjidAdapter adapter;
    List<DaftarMasjid> daftarMasjidAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        inisialisasiView ();
        loadDataMenuRestoran ();
        setupListview ();
    }

    private void inisialisasiView () {
        btnTambahMenuRestoran = findViewById (R.id.btn_add_favorite);
        btnTambahMenuRestoran.setOnClickListener (view -> bukaFormTambahMenuRestoran ());
        btnChangeUserName = findViewById (R.id.btn_change_username);
        btnChangeUserName.setOnClickListener (view -> changeUserName ());
        lvDaftarMenuRestoran = findViewById (R.id.lv_kataloglaptop);
        txNoData = findViewById (R.id.tx_nodata);
        txUsername = findViewById (R.id.tx_user_name);
        txUsername.setText (SharedPreferenceUtility.getUserName (this));

    }

    private void setupListview () {
        adapter = new DaftarMasjidAdapter (this, daftarMasjidAdapter);
        lvDaftarMenuRestoran.setAdapter (adapter);
    }

    private void loadDataMenuRestoran () {
        daftarMasjidAdapter = SharedPreferenceUtility.getAllMenuRestoran (this);
    }

    private void refreshListView () {
        adapter.clear ();
        loadDataMenuRestoran ();
        adapter.addAll (daftarMasjidAdapter);
    }

    private void bukaFormTambahMenuRestoran () {
        Intent intent = new Intent (this, FromDaftarMasjidActivity.class);
        startActivity (intent);
    }

    private void changeUserName () {
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setTitle ("Ganti nama");

        final EditText input = new EditText (this);
        builder.setView (input);

        builder.setPositiveButton ("Simpan", new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                SharedPreferenceUtility.saveUserName (getApplicationContext (), input.getText ().toString ());
                Toast.makeText (getApplicationContext (), "Nama user berhasil disimpan", Toast.LENGTH_SHORT).show ();
                txUsername.setText (SharedPreferenceUtility.getUserName (getApplicationContext ()));
            }
        });
        builder.setNegativeButton ("Batal", new DialogInterface.OnClickListener () {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                dialog.cancel ();
            }
        });
        builder.show ();

    }

    @Override
    protected void onResume () {
        super.onResume ();
        refreshListView ();
    }
}