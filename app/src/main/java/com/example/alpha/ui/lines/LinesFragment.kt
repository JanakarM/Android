package com.example.alpha.ui.lines

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alpha.LineFormActivity
import com.example.alpha.R
import com.example.alpha.db.entity.Line
import com.example.alpha.ui.users.UsersFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class LinesFragment : Fragment() {

    private lateinit var linesViewModel: LinesViewModel
    private lateinit var noLinesView: TextView
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val a = activity as AppCompatActivity
        a.supportActionBar?.title = getString(R.string.menu_lines)
        linesViewModel =
                ViewModelProvider(this).get(LinesViewModel::class.java)
        val root = inflater.inflate(R.layout.recycler_view_list, container, false)

        recyclerView = root.findViewById(R.id.recycler_view)
        val userAdapter= LinesAdapter { line ->
            val snackBar = Snackbar.make(root, "Line -> " + line.line_name, Snackbar.LENGTH_LONG)
            snackBar.setAction("View Parties", View.OnClickListener {
                view ->


                val navController = a.findNavController(R.id.nav_host_fragment)
                val bundle = Bundle()
                bundle.putInt("line_id", line.line_id!!)
                navController.navigate(R.id.nav_users, bundle)
//                val fragment2 = UsersFragment()
//                fragment2.lineId = line.line_id
//                val fragmentTransaction: FragmentTransaction? = a.supportFragmentManager?.beginTransaction()
//                fragmentTransaction?.replace(container!!.id, fragment2, "line_parties")
//                fragmentTransaction?.addToBackStack(null)
//                fragmentTransaction?.commit()
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
        noLinesView = root.findViewById(R.id.txt_empty_notes_view)
        noLinesView.text = getString(R.string.no_lines)

        linesViewModel.usersLiveData.observe(viewLifecycleOwner, {
            it?.let {
                userAdapter.submitList(it as MutableList<Line>)
                toggleEmptyNotes(it.size)
            }
        })
        val addUserButton : Button = root.findViewById(R.id.button)
        addUserButton.setOnClickListener { view ->
            startAddLineForm(view)
        }
        val deleteUserButton : Button = root.findViewById(R.id.button2)
        deleteUserButton.setOnClickListener { view ->
            deleteAll()
        }
        addUserButton.text = getString(R.string.add_lines)
        deleteUserButton.text = getString(R.string.delete_all_lines)
        return root
    }
    private fun toggleEmptyNotes(size: Int) {
        if (size > 0) {
            noLinesView.visibility = View.GONE
        } else {
            noLinesView.visibility = View.VISIBLE
        }
    }
    private fun startAddLineForm(view: View)
    {
        val intent = Intent(context, LineFormActivity::class.java)
        startActivity(intent)
    }
    private fun deleteAll()
    {
        viewLifecycleOwner.lifecycleScope.launch {
            linesViewModel.deleteAll()
        }
    }
}