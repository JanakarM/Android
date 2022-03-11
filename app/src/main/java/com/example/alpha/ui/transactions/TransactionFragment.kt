package com.example.alpha.ui.transactions

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alpha.R
import com.example.alpha.TransactionActivityForm
import com.example.alpha.db.entity.Party
import com.example.alpha.db.entity.PartyTransaction
import kotlinx.coroutines.launch

class TransactionFragment : Fragment() {

    private lateinit var linesViewModel: TransactionViewModel
    private lateinit var noLinesView: TextView
    lateinit var recyclerView: RecyclerView
    var party:Party? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val a = activity as AppCompatActivity
        a.supportActionBar?.title = getString(R.string.transactions)
        linesViewModel =
                ViewModelProvider(this).get(TransactionViewModel::class.java)
        val root = inflater.inflate(R.layout.recycler_view_list, container, false)
        party = requireArguments().getSerializable("party") as Party
        linesViewModel.partyId = party!!.party_id
        linesViewModel.start()

        recyclerView = root.findViewById(R.id.recycler_view)
        val userAdapter= TransactionAdapter {
        }
        val layoutManager = LinearLayoutManager(context);
        val dividerItemDecoration = DividerItemDecoration(
                recyclerView.context,
                layoutManager.orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = layoutManager
        noLinesView = root.findViewById(R.id.txt_empty_notes_view)
        noLinesView.text = getString(R.string.no_transaction)

        linesViewModel.usersLiveData.observe(viewLifecycleOwner, {
            it?.let {
                userAdapter.submitList(it as MutableList<PartyTransaction>)
                toggleEmptyNotes(it.size)
            }
        })
        val addUserButton : Button = root.findViewById(R.id.button)
        addUserButton.setOnClickListener { view ->
            startAddTransactionForm(view)
        }
        val deleteUserButton : Button = root.findViewById(R.id.button2)
        deleteUserButton.setOnClickListener { view ->
            deleteAll()
        }
        addUserButton.text = getString(R.string.add_transaction)
        deleteUserButton.text = getString(R.string.delete_all_transaction)
        return root
    }
    private fun toggleEmptyNotes(size: Int) {
        if (size > 0) {
            noLinesView.visibility = View.GONE
        } else {
            noLinesView.visibility = View.VISIBLE
        }
    }
    private fun startAddTransactionForm(view: View)
    {
        val intent = Intent(context, TransactionActivityForm::class.java)
        intent.putExtra("party", party)
        startActivity(intent)
    }
    private fun deleteAll()
    {
        viewLifecycleOwner.lifecycleScope.launch {
            linesViewModel.deleteAllTransactions()
        }
    }
}