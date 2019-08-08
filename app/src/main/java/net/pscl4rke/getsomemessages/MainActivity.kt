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

import java.io.File

import android.os.Bundle
import android.os.Environment
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*

import android.net.Uri
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            //var hello = "hell" + "o"
            var cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null)
            if (cursor != null) {
                val count = cursor.count
                Snackbar.make(view, "There are " + count + " messages", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                var text = "" + cursor.columnCount + "\n\n"
                while (cursor.moveToNext()) {
                    //text = text + cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(4) + " " + cursor.getString(5)
                    for (x in 0 .. 33) {
                        text = text + " " + cursor.getString(x)
                    }
                    text = text + "\n\n"
                }
                cursor.close()
                val tv = findViewById<TextView>(R.id.middletext)
                //tv.setText("Woo " + count + " there")
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val fileobj = File(dir, "FooFile")
                fileobj.printWriter().use {out ->
                    //out.println("hello world")
                    out.println(text)
                }
                tv.setText(text)
            }
        }
    }

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
