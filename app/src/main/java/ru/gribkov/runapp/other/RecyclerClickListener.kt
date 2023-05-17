package ru.gribkov.runapp.other

import ru.gribkov.runapp.adapter.InfoAdapter

interface RecyclerClickListener {
    fun onItemRemoveClick(position: Int)
    fun onItemInfoClick(holder: InfoAdapter.InfoViewHolder)
}