package simple.example.daftarmasjid;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedPreferenceUtility {


    private static final String PREF_FILE = "simple.example.catatankas.DATA";
    // PREF_FILE -> Nama file utk penyimpanan,
    // biasanya menyertakan app.id agar tidak bentrok dgn app lain
    private static final String FAVOR_KEY = "TRANS"; // KEY utk daftar transaksi
    private static final String USER_NAME_KEY = "USERNAME"; // KEY untuk nama user

    private static SharedPreferences getSharedPref(Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(
                PREF_FILE, Context.MODE_PRIVATE);
        return sharedPref;
    }
    /*
        Ambil data username dari Shared Preference
     */
    public static String getUserName(Context ctx) {
        return getSharedPref(ctx).getString(USER_NAME_KEY,"NO NAME");
    }
    /*
        Simpan data username ke Shared Preference
     */
    public static void saveUserName(Context ctx, String userName) {
        Log.d("SH PREF","Change user name to"+userName);
        getSharedPref(ctx).edit().putString(USER_NAME_KEY,userName).apply();
    }
    /*
        Ambil data transaksi dari Shared Preference
        Perhatikan bahwa data disimpan dalam format JSON String
     */
    public static List<DaftarMasjid> getAllMenuRestoran(Context ctx) {
        String jsonString = getSharedPref(ctx).getString(FAVOR_KEY, null);
        List<DaftarMasjid> trs = new ArrayList<DaftarMasjid> ();
        if (jsonString != null) {
            Log.d("SH PREF","json string is:"+jsonString);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    trs.add(DaftarMasjid.fromJSONObject(obj));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(trs,(menuRestoran, t1) -> {
            return menuRestoran.getTanggal().compareTo(t1.getTanggal());
        }); // urutkan transaksi berdasarkan tanggal
        return trs;
    }
    /*
        Simpan data transaksi ke Shared Preference
        Perhatikan bahwa data disimpan dalam format JSON String
     */
    private static void saveAllMenuRestoran(Context ctx, List<DaftarMasjid> trs) {
        List<JSONObject> jsonTrs = new ArrayList<JSONObject>();
        JSONArray jsonArr = new JSONArray();
        for (DaftarMasjid tr : trs) {
            jsonArr.put(tr.toJSONObject());
        }
        getSharedPref(ctx).edit().putString(FAVOR_KEY,jsonArr.toString()).apply();
    }

    /*
        Tambah data transaksi baru ke Shared Preference
     */
    public static void addMenuRestoran(Context ctx, DaftarMasjid tr) {
        List<DaftarMasjid> trs = getAllMenuRestoran(ctx);
        trs.add(tr);
        saveAllMenuRestoran(ctx,trs);
    }

    public static void editFavorite(Context ctx, DaftarMasjid tr) {
        List<DaftarMasjid> trs = getAllMenuRestoran(ctx);
        for (DaftarMasjid tre:trs) {
            if (tr.getId().equals(tre.getId())) {
                trs.remove(tre);
                trs.add(tr);
            }
        }
        saveAllMenuRestoran(ctx,trs);
    }

    public static void deleteFavorite(Context ctx, String id) {
        List<DaftarMasjid> trs = getAllMenuRestoran(ctx);
        for (DaftarMasjid tr:trs) {
            if (tr.getId().equals(id)) {
                trs.remove(tr);
            }
        }
        saveAllMenuRestoran(ctx,trs);
    }

}
