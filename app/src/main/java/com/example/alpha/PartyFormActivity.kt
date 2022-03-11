package com.example.alpha

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.room.util.FileUtil
import com.example.alpha.db.AppDatabase
import com.example.alpha.db.AppRepository
import com.example.alpha.db.entity.Line
import com.example.alpha.db.entity.Party
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.File

class PartyFormActivity : AppCompatActivity() {
    var currentLine : Party? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.party_activity_form)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(getColor(R.color.dark_grey)))

        val lineSpinner: Spinner = findViewById(R.id.lines_spinner)
        val appRepository = AppRepository.getAppRepositoryInstance()
        val adapter = SpinAdapter(this, android.R.layout.simple_spinner_item, appRepository!!.getAllLinesAsList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        lineSpinner.adapter = adapter

        val partyTypeSpinner: Spinner = findViewById(R.id.party_type_spinner)
        val partyTypeSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOf("Weekly","Monthly")).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            partyTypeSpinner.adapter = adapter
        }

        partyTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                if(parent.getItemAtPosition(pos) == "Weekly"){
                    findViewById<EditText>(R.id.interest).visibility = View.GONE
                }
                else
                {
                    findViewById<EditText>(R.id.interest).visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do Nothing
            }
        }

        if(intent.getBooleanExtra("edit", false))
        {
            currentLine = intent.getSerializableExtra("party") as Party
            findViewById<EditText>(R.id.party_name).setText(currentLine!!.party_name)
            findViewById<EditText>(R.id.principal).setText(currentLine!!.total_amount.toString())
            findViewById<EditText>(R.id.interest).setText(currentLine!!.interest.toString())
            lineSpinner.setSelection(adapter.getPosition(appRepository!!.getLine(currentLine!!.line_id)))
            partyTypeSpinner.setSelection(partyTypeSpinnerAdapter.getPosition(currentLine!!.party_type))
            findViewById<Button>(R.id.btn_add_party).text = getString(R.string.update_party)
        }
    }
    fun addLine(view: View)
    {
        val appRepository = AppRepository.getAppRepositoryInstance()
        val lineName = findViewById<EditText>(R.id.party_name)
        val interest = findViewById<EditText>(R.id.interest)
        val principal = findViewById<EditText>(R.id.principal)
        val linesSpinner = findViewById<Spinner>(R.id.lines_spinner)
        val partyTypeSpinner = findViewById<Spinner>(R.id.party_type_spinner)
        var error : Boolean = false
        if(TextUtils.isEmpty(lineName.text))
        {
            lineName.error = getString(R.string.invalid_input_error, getString(R.string.party_name))
            error = true
        }
        if(interest.visibility == View.VISIBLE && TextUtils.isEmpty(interest.text))
        {
            interest.error = getString(R.string.invalid_input_error, getString(R.string.interest))
            error = true
        }
        if(TextUtils.isEmpty(principal.text))
        {
            principal.error = getString(R.string.invalid_input_error, getString(R.string.principal_amt))
            error = true
        }
        if(error)
        {
            return
        }

        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        val totalAmt = principal.text.toString().toLong()
        lifecycleScope.launch {
            val code : String?
            val l = (linesSpinner.selectedItem as Line)
            val line = Party(party_name = lineName.text.toString(), line_id = l.line_id, interest =  if(interest.visibility == View.VISIBLE){interest.text.toString().toLong()}else{null}, total_amount = principal.text.toString().toLong(), party_id = null, amount_to_be_paid = totalAmt, party_type = partyTypeSpinner.selectedItem.toString())
            if(intent.getBooleanExtra("edit", false))
            {
                line.party_id = currentLine!!.party_id
                code = appRepository!!.updateParty(line)
            }
            else
            {
                code = appRepository!!.addParty(line)
            }
            if(code == "SUCCESS")
            {
                val snackBar = Snackbar.make(view, "Done.", Snackbar.LENGTH_LONG)
                snackBar.setAction("View Parties", View.OnClickListener {
                    _ ->
                    onBackPressed()
                }).show()
            }
            else
            {
                if(code == "DUPLICATE")
                {
                    lineName.error = getString(R.string.duplicate_party)
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
class SpinAdapter(context: Context, var textViewResourceId: Int, var users: List<Line>) : ArrayAdapter<Line>(context, textViewResourceId, users) {
    override fun getItem(position: Int): Line {
        return users[position]
    }
    override fun getPosition(item: Line?): Int {
        var i = 0
        while (i < users.size)
        {
            val user = users[i]
            if(user.line_id == item!!.line_id)
            {
                return i
            }
            i++
        }
        return -1
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        val label = super.getView(position, convertView, parent) as TextView
//        label.setTextColor(Color.BLACK)
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.text = users[position].line_name

        // And finally return your dynamic (or custom) view for each spinner item
        return label
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    override fun getDropDownView(position: Int, convertView: View?,
                                 parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
//        label.setTextColor(Color.BLACK)
        label.text = users[position].line_name
        return label
    }
}