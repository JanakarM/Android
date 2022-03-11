package com.example.alpha.ui.lines

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alpha.LineFormActivity
import com.example.alpha.R
import com.example.alpha.db.AppRepository
import com.example.alpha.db.entity.Line
import com.example.alpha.db.entity.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LinesAdapter (private val onClick: (Line) -> Unit) : ListAdapter<Line, LinesAdapter.UsersViewHolder>(UserDiffCallback) {
    class UsersViewHolder(itemView: View, val onClick: (Line) -> Unit, context: Context) :
            RecyclerView.ViewHolder(itemView) {
        private val primaryText: TextView = itemView.findViewById(R.id.txt_view_name)
        private val secondaryText : TextView = itemView.findViewById(R.id.txt_view_contact)
        private val editButton : FloatingActionButton = itemView.findViewById(R.id.edit_row)
        private var currentUser: Line? = null
        var context: Context = context
        init {
            itemView.setOnClickListener {
                currentUser?.let {
                    if(currentUser!!.active)
                    {
                        onClick(it)
                    }
                }
            }
        }


        private fun startEditLineForm(line: Line)
        {
            val intent = Intent(context, LineFormActivity::class.java)
            val line = AppRepository.getAppRepositoryInstance()!!.getLine(line.line_id!!)
            intent.putExtra("line", line)
            intent.putExtra("edit", true)
            context.startActivity(intent)
        }
        /* Bind user name. */
        fun bind(line: Line) {
            currentUser = line

//            userTextView.text = user.id.toString() + " - " + user.name
//            userTextView.text = context.getString(R.string.user_txt, user.id, user.name)
            primaryText.text = line.line_name
//            secondaryText.text = context.getString(R.string.user_txt, user.email,user.mobile)
            var temp:String

            secondaryText.text = " | ${line.place}"

            editButton.setOnClickListener { view ->
                startEditLineForm(currentUser!!)
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
object UserDiffCallback : DiffUtil.ItemCallback<Line>() {
    override fun areItemsTheSame(oldItem: Line, newItem: Line): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Line, newItem: Line): Boolean {
        return oldItem.line_id == newItem.line_id
    }
}