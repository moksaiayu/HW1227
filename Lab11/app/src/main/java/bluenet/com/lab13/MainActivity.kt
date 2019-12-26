package bluenet.com.lab11

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bluenet.com.lab13.MySQLiteOpenHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var dbrw: SQLiteDatabase
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter
        dbrw = MySQLiteOpenHelper(this).writableDatabase
        btn_query.setOnClickListener {
            val c = dbrw.rawQuery(if(ed_book.length()<1) "SELECT * FROM myTable"
            else "SELECT * FROM myTable WHERE book LIKE '${ed_book.text}'",null)
            c.moveToFirst()
            items.clear()
            showToast("共有${c.count}筆資料")
            for (i in 0 until c.count) {
                items.add("書名:${c.getString(0)}\t\t\t\t價格:${c.getInt(1)}")
                c.moveToNext()}
            adapter.notifyDataSetChanged()
            c.close()}
        btn_insert.setOnClickListener {
            if (ed_book.length()<1 || ed_price.length()<1)
                showToast("欄位請勿留空")
            else
                try{
                    dbrw.execSQL("INSERT INTO myTable(book, price) VALUES(?,?)", arrayOf<Any?>(ed_book.text.toString(), ed_price.text.toString()))
                    showToast("新增書名${ed_book.text}    價格${ed_price.text}")
                    cleanEditText()
                }catch (e: Exception){
                    showToast("新增失敗:$e")}}
        btn_update.setOnClickListener {
            if (ed_book.length()<1 || ed_price.length()<1)
                showToast("欄位請勿留空")
            else
                try{
                    dbrw.execSQL("UPDATE myTable SET price = ${ed_price.text} WHERE book LIKE '${ed_book.text}'")
                    showToast("更新書名${ed_book.text}    價格${ed_price.text}")
                    cleanEditText()}catch (e: Exception){showToast("更新失敗:$e")}}
        btn_delete.setOnClickListener {
            if (ed_book.length()<1)
                showToast("書名請勿留空")
            else
                try{
                    dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '${ed_book.text}'")
                    showToast("刪除書名${ed_book.text}")
                    cleanEditText()}catch (e: Exception){showToast("刪除失敗:$e")}}}
    private fun showToast(text: String) = Toast.makeText(this,text, Toast.LENGTH_LONG).show()
    private fun cleanEditText(){
        ed_book.setText("")
        ed_price.setText("")}
    override fun onDestroy() {
        super.onDestroy()
        dbrw.close()}}