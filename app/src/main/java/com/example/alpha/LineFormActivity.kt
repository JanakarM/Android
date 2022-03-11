package com.example.alpha

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.FileUtils
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.alpha.db.AppDatabase
import com.example.alpha.db.AppRepository
import com.example.alpha.db.entity.Line
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.File
import java.time.DayOfWeek

class LineFormActivity : AppCompatActivity() {
    var currentLine : Line? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.line_activity_form)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(getColor(R.color.dark_grey)))

        val spinner: Spinner = findViewById(R.id.day_of_week_spinner)
        ArrayAdapter(this, android.R.layout.simple_spinner_item, DayOfWeek.values()).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            if(intent.getBooleanExtra("edit", false))
            {
                currentLine = intent.getSerializableExtra("line") as Line
                findViewById<EditText>(R.id.line_name).setText(currentLine!!.line_name)
                findViewById<EditText>(R.id.line_place).setText(currentLine!!.place)
                findViewById<Spinner>(R.id.day_of_week_spinner).setSelection(adapter.getPosition(currentLine!!.dayOfWeek))
                findViewById<Button>(R.id.btn_add_line).text = getString(R.string.update_line)
            }
        }
    }
    fun addLine(view: View)
    {
        val appRepository = AppRepository.getAppRepositoryInstance()
        val lineName = findViewById<EditText>(R.id.line_name)
        val linePlace = findViewById<EditText>(R.id.line_place)
        val dayOfWeek = findViewById<Spinner>(R.id.day_of_week_spinner)
        var error : Boolean = false
        if(TextUtils.isEmpty(lineName.text))
        {
            lineName.error = getString(R.string.invalid_input_error, "Name")
            error = true
        }
        if(TextUtils.isEmpty(linePlace.text))
        {
            linePlace.error = getString(R.string.invalid_input_error, "Place")
            error = true
        }
        if(error)
        {
            return
        }

        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
//        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val code : String?
            val line = Line(line_name = lineName.text.toString(), place = linePlace.text.toString(), dayOfWeek = dayOfWeek.selectedItem as DayOfWeek)
            if(intent.getBooleanExtra("edit", false))
            {
                line.line_id = currentLine!!.line_id
                code = appRepository!!.updateLine(line)
            }
            else
            {
                code = appRepository!!.addLine(line)
            }
            if(code == "SUCCESS")
            {
                val snackBar = Snackbar.make(view, "Done.", Snackbar.LENGTH_LONG)
                snackBar.setAction("View Lines", View.OnClickListener { _ ->
                    onBackPressed()
                }).show()
            }
            else
            {
                if(code == "DUPLICATE")
                {
                    lineName.error = getString(R.string.duplicate_line)
                }
                else
                {
                    val snackBar = Snackbar.make(view, code!!, Snackbar.LENGTH_LONG)
                    snackBar.setAction("", null).show()
                }
            }
        }

        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        progressBar.visibility = View.GONE

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}