package com.example.alpha.ui.users

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alpha.PartyFormActivity
import com.example.alpha.R
import com.example.alpha.db.entity.Party
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class UsersFragment : Fragment(){

    private lateinit var dataViewModel: UsersViewModel
    private lateinit var noUsersView: TextView
    lateinit var recyclerView: RecyclerView
    var lineId : Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val a = activity as AppCompatActivity
        a.supportActionBar?.title = getString(R.string.users)
        dataViewModel =
                ViewModelProvider(this).get(UsersViewModel::class.java)
        val line_id = requireArguments().getInt("line_id")
        dataViewModel.lineId = line_id
        dataViewModel.start()
        val root = inflater.inflate(R.layout.recycler_view_list, container, false)
        dataViewModel.lineId = lineId
        recyclerView = root.findViewById(R.id.recycler_view)
        val userAdapter= UsersAdapter { user ->
            val snackBar = Snackbar.make(root, "Party -> " + user.party_name, Snackbar.LENGTH_LONG)
            snackBar.setAction("Make transaction", View.OnClickListener {
                    view ->

                val navController = a.findNavController(R.id.nav_host_fragment)

                val bundle = Bundle()
                bundle.putSerializable("party", user)
                navController.navigate(R.id.nav_transaction, bundle)
            }).show()
        }
        val layoutManager = LinearLayoutManager(context);
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = layoutManager
        noUsersView = root.findViewById(R.id.txt_empty_notes_view)

        dataViewModel.usersLiveData.observe(viewLifecycleOwner, {
            it?.let {
                userAdapter.submitList(it as MutableList<Party>)
                toggleEmptyNotes(it.size)
            }
        })
        val addUserButton : View = root.findViewById(R.id.button)
        addUserButton.setOnClickListener { view ->
            startAddUserForm(view)
        }
        val deleteUserButton : View = root.findViewById(R.id.button2)
        deleteUserButton.setOnClickListener { view ->
            deleteAll()
        }
        return root
    }
    private fun toggleEmptyNotes(size: Int) {
        if (size > 0) {
            noUsersView.visibility = View.GONE
        } else {
            noUsersView.visibility = View.VISIBLE
        }
    }
    private fun startAddUserForm(view: View)
    {
        val intent = Intent(context, PartyFormActivity::class.java)
        startActivity(intent)
    }
    private fun deleteAll()
    {
        viewLifecycleOwner.lifecycleScope.launch {
            dataViewModel.deleteAllParties()
        }
    }
}