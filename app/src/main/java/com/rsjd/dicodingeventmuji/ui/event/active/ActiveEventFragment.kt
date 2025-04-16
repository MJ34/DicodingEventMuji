package com.rsjd.dicodingeventmuji.ui.event.active

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.databinding.FragmentActiveEventBinding
import com.rsjd.dicodingeventmuji.ui.adapter.EventAdapter
import com.rsjd.dicodingeventmuji.ui.detail.DetailEventActivity
import com.rsjd.dicodingeventmuji.utils.EventResult
import com.rsjd.dicodingeventmuji.utils.ViewModelFactory

class ActiveEventFragment : Fragment() {

    private var _binding: FragmentActiveEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ActiveEventViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter { event ->
            navigateToDetail(event)
        }
        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ActiveEventFragment.adapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        viewModel.searchEvents(it)
                    } else {
                        viewModel.getActiveEvents()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isEmpty()) {
                        viewModel.getActiveEvents()
                    }
                }
                return true
            }
        })
    }

    private fun setupClickListeners() {
        binding.viewError.btnRetry.setOnClickListener {
            viewModel.getActiveEvents()
        }
    }

    private fun observeViewModel() {
        viewModel.events.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EventResult.Loading -> {
                    showLoading(true)
                    binding.viewError.root.visibility = View.GONE
                    binding.viewEmpty.root.visibility = View.GONE
                }
                is EventResult.Success -> {
                    showLoading(false)
                    binding.viewError.root.visibility = View.GONE

                    if (result.data.isEmpty()) {
                        binding.viewEmpty.root.visibility = View.VISIBLE
                        binding.rvEvents.visibility = View.GONE
                    } else {
                        binding.viewEmpty.root.visibility = View.GONE
                        binding.rvEvents.visibility = View.VISIBLE
                        adapter.submitList(result.data)
                    }
                }
                is EventResult.Error -> {
                    showLoading(false)
                    binding.viewError.root.visibility = View.VISIBLE
                    binding.viewEmpty.root.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.viewError.tvErrorMessage.text = result.error
                }
            }
        }

        // Load active events
        viewModel.getActiveEvents()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.rvEvents.visibility = View.GONE
            binding.shimmerLayout.startShimmer()
        } else {
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
        }
    }

    private fun navigateToDetail(event: Event) {
        val intent = Intent(requireContext(), DetailEventActivity::class.java).apply {
            putExtra(DetailEventActivity.EXTRA_EVENT_ID, event.id)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}