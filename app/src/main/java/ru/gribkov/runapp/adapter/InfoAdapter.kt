package ru.gribkov.runapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_info.view.*
import ru.gribkov.runapp.R
import ru.gribkov.runapp.db.Run
import ru.gribkov.runapp.other.RecyclerClickListener
import ru.gribkov.runapp.other.TrackingUtility
import ru.gribkov.runapp.ui.fragments.ProfileFragment
import java.text.SimpleDateFormat
import java.util.*

class InfoAdapter : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    inner class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var listener: RecyclerClickListener

    fun setItemListener(listener: RecyclerClickListener) {
        this.listener = listener
    }

    val diffCallBack = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)

    fun submitList(list: List<Run>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.item_info,
                parent,
                false
            )
        val infoViewHolder = InfoViewHolder(v)
        val runDelete = infoViewHolder.itemView.findViewById<TextView>(R.id.tvDelRun)
        runDelete.setOnClickListener {
            listener.onItemRemoveClick(infoViewHolder.adapterPosition)
        }
        val runInfo = infoViewHolder.itemView.findViewById<TextView>(R.id.tvMoreInf)
        runInfo.setOnClickListener {
            listener.onItemInfoClick(infoViewHolder)
        }

        return infoViewHolder
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val inf = differ.currentList[position]
        holder.itemView.apply {

            Glide.with(this).load(inf.img).into(ivTrackingImg)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = inf.timestamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDateInf.text = dateFormat.format(calendar.time)

            val avgSpeed = "${inf.avgSpeedInKMH}km/h"
            tvAvgSpeedInf.text = avgSpeed

            val distanceInKm = "${inf.distanceInMeters / 1000f}km"
            tvDistanceInf.text = distanceInKm

            val timeInMil = TrackingUtility.getFormattedStopWatchTime(inf.timeInMillis)
            tvTimeInf.text = timeInMil

            val caloriesBurned = "${inf.caloriesBurned}kcal"
            tvCaloriesInf.text = caloriesBurned
        }
    }
}