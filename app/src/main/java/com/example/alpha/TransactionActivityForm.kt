package com.example.alpha

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.alpha.db.AppRepository
import com.example.alpha.db.entity.Line
import com.example.alpha.db.entity.Party
import com.example.alpha.db.entity.PartyTransaction
import com.example.alpha.ui.users.UsersAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class TransactionActivityForm : AppCompatActivity() {
    lateinit var party : Party
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_form)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(getColor(R.color.dark_grey)))
        party = intent.getSerializableExtra("party") as Party
    }
    fun addLine(view: View)
    {
        val appRepository = AppRepository.getAppRepositoryInstance()
        val amtPaid = findViewById<EditText>(R.id.amt_paid)
        var error : Boolean = false
        if(TextUtils.isEmpty(amtPaid.text))
        {
            amtPaid.error = getString(R.string.invalid_input_error, getString(R.string.amt))
            error = true
        }
        if(error)
        {
            return
        }

        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        val amt = amtPaid.text.toString().toLong()
        val amountToBePaid = party.amount_to_be_paid
        party.amount_to_be_paid = if("Weekly" == party.party_type){amountToBePaid - amt}else{UsersAdapter.UsersViewHolder.getAmt(party) - amt}

        lifecycleScope.launch {
            val code : String?
            val line = PartyTransaction(party_id = party.party_id!!, time_in_millis = System.currentTimeMillis(), amount_paid = amt, remaining_amount = party.amount_to_be_paid, transaction_id = null)
            code = appRepository!!.addPartyTransaction(line,party)
            if(code == "SUCCESS")
            {
                val snackBar = Snackbar.make(view, "Done.", Snackbar.LENGTH_LONG)
                snackBar.setAction("View Transactions", View.OnClickListener {
                    _ ->
                    onBackPressed()
                }).show()
            }
            else
            {
                val snackBar = Snackbar.make(view, code!!, Snackbar.LENGTH_LONG)
                snackBar.setAction("", null).show()
            }
        }

        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}