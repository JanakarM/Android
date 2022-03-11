package com.example.alpha.ui.users

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alpha.PartyFormActivity
import com.example.alpha.R
import com.example.alpha.db.AppRepository
import com.example.alpha.db.entity.Party
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UsersAdapter (private val onClick: (Party) -> Unit) : ListAdapter<Party, UsersAdapter.UsersViewHolder>(UserDiffCallback) {
    class UsersViewHolder(itemView: View, val onClick: (Party) -> Unit, context: Context) :
            RecyclerView.ViewHolder(itemView) {
        private val primaryText: TextView = itemView.findViewById(R.id.txt_view_name)
        private val secondaryText : TextView = itemView.findViewById(R.id.txt_view_contact)
        private val editButton : FloatingActionButton = itemView.findViewById(R.id.edit_row)
        private var currentUser: Party? = null
        var context: Context = context
        init {
            itemView.setOnClickListener {
                currentUser?.let {
                    onClick(it)
                }
            }
        }

        private fun startEditLineForm(party: Party)
        {
            val intent = Intent(context, PartyFormActivity::class.java)
            intent.putExtra("party", party)
            intent.putExtra("edit", true)
            context.startActivity(intent)
        }

        /* Bind user name. */
        fun bind(user: Party) {
            currentUser = user

            primaryText.text = user.party_name

            val t = if("Weekly" == user.party_type)
            {
                user.amount_to_be_paid
            }
            else
            {
                //calculate monthly interest
                getAmt(user)
            }

            secondaryText.text = context.getString(R.string.user_txt, user.total_amount.toString(),t.toString())
            editButton.setOnClickListener { view ->
                startEditLineForm(currentUser!!)
            }
        }
        companion object{
            fun getAmt(user: Party) : Long
            {
                val appRepository = AppRepository.getAppRepositoryInstance()
                val latestTransaction = appRepository!!.getLatestPartyTransactions(user.party_id!!)
                val interest = user.interest
                var amountToBePaid = user.amount_to_be_paid
                var noOfMonths = if(latestTransaction == null){
                    (System.currentTimeMillis() - user.date_in_millis)
                }
                else
                {
                    (System.currentTimeMillis() - latestTransaction.time_in_millis)

                }
                noOfMonths /= 1000
                noOfMonths /= 60
//                noOfMonths /= 60
//                noOfMonths /= 24
//                noOfMonths /= 7*4
                amountToBePaid += (noOfMonths * amountToBePaid * interest!!) / 100
                return amountToBePaid
            }
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
        var t = System.currentTimeMillis() - user.date_in_millis
//        t /= (1000 * 60 * 60 * 24 * 7)
        t /= (1000 * 60)
        color = if("Weekly" == user.party_type && t > 2)
        {
            R.color.red
        }
        else if(position % 2 == 0)
        {
            R.color.light_grey
        }
        else
        {
            R.color.grey
        }

        holder.itemView.setBackgroundColor(holder.context.getColor(color))
        holder.bind(user)

    }
}
object UserDiffCallback : DiffUtil.ItemCallback<Party>() {
    override fun areItemsTheSame(oldItem: Party, newItem: Party): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Party, newItem: Party): Boolean {
        return oldItem.party_id == newItem.party_id
    }
}