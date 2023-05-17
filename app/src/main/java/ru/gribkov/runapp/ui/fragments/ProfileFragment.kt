package ru.gribkov.runapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_info.view.*
import kotlinx.android.synthetic.main.profile_fragment.*
import ru.gribkov.runapp.R
import ru.gribkov.runapp.adapter.InfoAdapter
import ru.gribkov.runapp.db.Run
import ru.gribkov.runapp.other.Constants.KEY_NAME
import ru.gribkov.runapp.other.Constants.KEY_WEIGHT
import ru.gribkov.runapp.other.RecyclerClickListener
import ru.gribkov.runapp.ui.viewmodels.MainViewModel
import ru.gribkov.runapp.ui.viewmodels.ProfileViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var infAdapter: InfoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProfileNameWeightSharedPref()
        setupRecyclerView()

        tvEdit.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            infAdapter.submitList(it)
        })
    }

    private fun loadProfileNameWeightSharedPref(){
        val name = sharedPreferences.getString(KEY_NAME, "")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT, 80f)
        tvNameProf.text = name
        tvWeightProf.text = weight.toString()
    }

    private fun setupRecyclerView() = rvInfo.apply {
        var isVisible = false

        infAdapter = InfoAdapter()
        adapter = infAdapter
        layoutManager = LinearLayoutManager(requireContext())

        infAdapter.setItemListener(object : RecyclerClickListener{
            override fun onItemRemoveClick(position: Int) {
                val run = infAdapter.differ.currentList.toMutableList()
                val id = run[position].id
                run.removeAt(position)
                infAdapter.submitList(run)
                id?.let { viewModel.deleteById(it) }
            }

            override fun onItemInfoClick(holder: InfoAdapter.InfoViewHolder) {
                holder.itemView.apply {
                    if (!isVisible) {
                        ivTrackingImg.visibility = View.VISIBLE
                        isVisible = true
                    } else {
                        ivTrackingImg.visibility = View.GONE
                        isVisible = false
                    }
                }
            }
        })

    }
}