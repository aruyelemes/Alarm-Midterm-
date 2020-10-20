package kz.iitu.midterm

import android.app.TimePickerDialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kz.iitu.midterm.db.Alarm
import kz.iitu.midterm.db.AppDatabase
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AsyncTask.execute {
            val items = AppDatabase.getInstance(applicationContext)?.getItemDao()?.getAllItems()
            runOnUiThread {
                items?.let {
                    adapter = Adapter(ArrayList(it), onClickItem = { item ->
                        val myDialogFragment = DeltailsFragment(item)
                        val manager = supportFragmentManager
                        myDialogFragment.show(manager, "myDialog")
                    },
                        onLongClickItem = { item ->
                            val myDialogFragment = DeleteDialog(this,item)
                            val manager = supportFragmentManager
                            myDialogFragment.show(manager, "myDialog")
                        }
                    )
                    recyclerview.layoutManager = LinearLayoutManager(this)
                    recyclerview.adapter = adapter
                }}}

        btnMewAlarm.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timepicker, hour, minute ->
                cal.set(Calendar.HOUR, hour)
                cal.set(Calendar.MINUTE, minute)
                AsyncTask.execute {
                    AppDatabase.getInstance(context = applicationContext)?.getItemDao()?.insertItem(
                        Alarm( name = "Good Morning",hour = hour,minute = minute,isActive = true))
                    AsyncTask.execute {
                        val items = AppDatabase.getInstance(applicationContext)?.getItemDao()?.getAllItems()
                        runOnUiThread {

                            items?.let {
                                adapter = Adapter(ArrayList(it), onClickItem = { item ->
                                    val myDialogFragment = DeltailsFragment(item)
                                    val manager = supportFragmentManager
                                    myDialogFragment.show(manager, "myDialog")
                                },
                                    onLongClickItem = { item ->
                                        val myDialogFragment = DeleteDialog(this,item)
                                        val manager = supportFragmentManager
                                        myDialogFragment.show(manager, "myDialog")
                                    }
                                )
                                recyclerview.layoutManager = LinearLayoutManager(this)
                                recyclerview.adapter = adapter
                            }}}
                }
            }
            TimePickerDialog(this,
                timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)
                ,true).show()
        }
    }
}