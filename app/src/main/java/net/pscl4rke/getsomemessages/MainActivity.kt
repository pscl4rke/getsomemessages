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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Log.i("GetSomeMessages", "Main Activity Created")

        fab.setOnClickListener myClickHandler@ { view ->
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
                val count = cursor.count
                Snackbar.make(view, "There are " + count + " messages", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                val tv = findViewById<TextView>(R.id.middletext)
                val pb = findViewById<ProgressBar>(R.id.progressBar)
                var i = 0
                var status = ""
                var text = "" + cursor.columnCount + "\n\n"
                val t = Thread() {
                    while (cursor.moveToNext()) {
                        status = "Message " + i + " of " + count
                        i = i + 1
                        //tv.setText(status)
                        //tv.draw(getCanvas())
                        //tv.text
                        pb.max = count
                        pb.progress = i
                        //text = text + cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(4) + " " + cursor.getString(5)
                        for (x in 0..33) {
                            text = text + " " + cursor.getString(x)
                        }
                        text = text + "\n\n"
                    }

                    cursor.close()
                    //tv.setText("Saving...")
                    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val fileobj = File(dir, "FooFile")
                    fileobj.printWriter().use { out ->
                        out.println(text)
                    }
                    runOnUiThread {
                        tv.setText("Done")
                    }
                }
                t.start()
            }
        }
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
