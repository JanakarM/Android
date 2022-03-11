package com.example.alpha.ui.transactions

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alpha.R
import com.example.alpha.db.AppRepository
import com.example.alpha.db.entity.PartyTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter (private val onClick: (PartyTransaction) -> Unit) : ListAdapter<PartyTransaction, TransactionAdapter.UsersViewHolder>(UserDiffCallback) {
    class UsersViewHolder(itemView: View, val onClick: (PartyTransaction) -> Unit, context: Context) :
            RecyclerView.ViewHolder(itemView) {
        private val primaryText: TextView = itemView.findViewById(R.id.txt_view_name)
        private val secondaryText : TextView = itemView.findViewById(R.id.txt_view_contact)
        private val editButton : FloatingActionButton = itemView.findViewById(R.id.edit_row)
        private var currentUser: PartyTransaction? = null
        var context: Context = context
        init {
            itemView.setOnClickListener {
                currentUser?.let {
                    onClick(it)
                }
            }
        }

        /* Bind user name. */
        fun bind(user: PartyTransaction) {
            currentUser = user
            val appRepository = AppRepository.getAppRepositoryInstance()
            primaryText.text = appRepository!!.getParty(user.party_id).party_name
            val date = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(user.time_in_millis)
            secondaryText.text = context.getString(R.string.user_txt, user.amount_paid.toString(),date)
            editButton.visibility = View.GONE
        }
    }

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_list_row, parent, false)
        view.setBackgroundColor(Color.parseColor("#AFA1A1"))
        return UsersViewHolder(view, onClick, parent.context)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = getItem(position)
        var color : Int
        if(position % 2 == 0)
        {
            color = R.color.light_grey
        }
        else
        {
            color = R.color.grey
        }

        holder.itemView.setBackgroundColor(holder.context.getColor(color))
        holder.bind(user)

    }
}
object UserDiffCallback : DiffUtil.ItemCallback<PartyTransaction>() {
    override fun areItemsTheSame(oldItem: PartyTransaction, newItem: PartyTransaction): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PartyTransaction, newItem: PartyTransaction): Boolean {
        return oldItem.party_id == newItem.party_id
    }
}