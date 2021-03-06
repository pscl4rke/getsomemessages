package net.pscl4rke.getsomemessages

/*
long messageId = cursor.getLong(0);
long threadId = cursor.getLong(1);
String address = cursor.getString(2);
long contactId = cursor.getLong(3);
String contactId_string = String.valueOf(contactId);
long timestamp = cursor.getLong(4);
String body = cursor.getString(5);
*/

import android.content.pm.PackageManager
import java.io.File

import android.os.Bundle
import android.os.Environment
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.util.Log

import kotlinx.android.synthetic.main.activity_main.*

import android.net.Uri
import android.os.Build
import android.widget.TextView
import android.widget.ProgressBar
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Log.i("GetSomeMessages", "Main Activity Created")

        fab.setOnClickListener myClickHandler@ { _view ->
            Log.i("GetSomeMessages","The button has been clicked")
            if (!haveAllNecessaryPermissions()) {
                return@myClickHandler
            }
            Log.i("GetSomeMessages", "Permissions must be good")
            //var cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null)
            //var cursor = getContentResolver().query(Uri.parse("content://sms/all"), null, null, null, null)
            //var cursor = getContentResolver().query(Uri.parse("content://sms/outbox"), null, null, null, null)
            //var cursor = getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null)
            var cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null)
            if (cursor != null) {
                val tv = findViewById<TextView>(R.id.middletext)
                tv.setText("There are ${cursor.count} messages")
                val pb = findViewById<ProgressBar>(R.id.progressBar)
                pb.max = cursor.count
                var i = 0
                val meta = JSONObject()
                val records = JSONArray()
                meta.put("count", cursor.count)
                val t = Thread() {
                    while (cursor.moveToNext()) {
                        i = i + 1
                        val record = JSONObject()
                        if (i % 20 == 0) {
                            pb.progress = i
                        }
                        for (columnIndex in 0..(cursor.columnCount - 1)) {
                            record.put(cursor.getColumnName(columnIndex), cursor.getString(columnIndex))
                        }
                        records.put(record)
                    }
                    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val fileobj = File(dir, outputFileName())
                    val doc = JSONObject()
                    doc.put("metadata", meta)
                    doc.put("records", records)
                    fileobj.printWriter().use { out ->
                        out.println(doc.toString(4))
                    }
                    val count = cursor.count // ensure other thread below doesn't access after .close() called
                    runOnUiThread {
                        tv.setText("Exported ${count} messages")
                    }
                    cursor.close()
                }
                t.start()
            }
        }
    }

    fun outputFileName(): String {
        // I could add the date, which would stop it from overwriting each time, but would make
        // it harder to automate the downloading of the right file...
        //return "TextMessages.0000-00-00.000000.json"
        return "TextMessages.json"
    }

    fun haveAllNecessaryPermissions(): Boolean {
        if (Build.VERSION.SDK_INT < 23) {
            return true
        }
        if (this.checkSelfPermission(android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
            Log.i("GetSomeMessages", "READ_SMS permission is currently denied")
            this.requestPermissions(arrayOf(android.Manifest.permission.READ_SMS), 1)
            //MainActivity.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS))
            Log.i("GetSomeMessages", "Requested, and returning for now")
            return false
        }
        if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            Log.i("GetSomeMessages", "WRITE_EXTERNAL_STORAGE permission is currently denied")
            this.requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            Log.i("GetSomeMessages", "Requested, and returning for now")
            return false
        }
        return  true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.i("GetSomeMessages", "The code " + requestCode + " came back")
        when (requestCode) {
            1 -> {
            }
            else -> {
            }
        }
    }

    // not using...
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
