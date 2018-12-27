package com.andro.whitelist.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andro.whitelist.R
import com.andro.whitelist.db.dao.WhitelistDao
import com.andro.whitelist.db.handler.WhitelistDatabase
import com.andro.whitelist.db.model.Contact
import kotlinx.android.synthetic.main.contact_list_item.view.*

class ContactAdapter(private val context: Context, private val checkBoxVisibility: Boolean = false) :
    ListAdapter<Contact, ContactAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(p0: Contact, p1: Contact): Boolean = p0.id == p1.id

        override fun areContentsTheSame(p0: Contact, p1: Contact): Boolean = p0 == p1
    }) {

    var previousSelectedList: List<Contact>? = null
    private var whitelistDao: WhitelistDao = WhitelistDatabase.getInstance(context)!!.whitelistDao()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {
        val classListView = LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false)
        return ViewHolder(classListView)
    }

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bindData(contact, position)
        holder.itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                addContact(contact)
            } else {
                removeContact(contact)
            }
        }
        holder.itemView.remove_contact.setOnClickListener {
            removeContact(contact)
            notifyDataSetChanged()
        }
    }

    private fun removeContact(contact: Contact) {
        whitelistDao.delete(contact)
    }

    private fun addContact(contact: Contact) {
        whitelistDao.insertContact(contact)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(contact: Contact, position: Int) {
            itemView.contact_name.text = contact.name
            itemView.contact_number.text = contact.phoneNumber
            itemView.checkbox.isChecked = previousSelectedList?.contains(contact) ?: false
            if (checkBoxVisibility) {
                itemView.checkbox.visibility = View.VISIBLE
            } else {
                itemView.remove_contact.visibility = View.VISIBLE
            }
        }
    }
}