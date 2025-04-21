package com.rsjd.dicodingeventmuji.ui.event.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsjd.dicodingeventmuji.R
import com.rsjd.dicodingeventmuji.databinding.FragmentUpcomingBinding
import com.rsjd.dicodingeventmuji.ui.adapter.EventAdapter
import com.rsjd.dicodingeventmuji.ui.detail.DetailActivity
import com.rsjd.dicodingeventmuji.utils.Resource
import com.rsjd.dicodingeventmuji.utils.ViewModelFactory

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpcomingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val adapter = EventAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UpcomingFragment.adapter
        }

        // Set click listener
        adapter.setOnItemClickListener { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            }
            startActivity(intent)
        }


        setupSwipeRefresh()
        observeEvents()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchUpcomingEvents()
        }

        // Customize warna loading indicator
        binding.swipeRefresh.setColorSchemeResources(
            R.color.purple_500,
            R.color.teal_200,
            R.color.purple_700
        )
    }

    private fun observeEvents() {
        viewModel.events.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                    showError(false)
                }
                is Resource.Success -> {
                    showLoading(false)
                    showError(false)

                    val events = resource.data
                    if (events.isNullOrEmpty()) {
                        showEmptyState(true)
                    } else {
                        showEmptyState(false)
                        adapter.submitList(events)
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    showEmptyState(false)
                    showError(true, resource.message)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading && adapter.itemCount == 0) {
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.shimmerLayout.startShimmer()
        } else {
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
        }

        binding.swipeRefresh.isRefreshing = isLoading && adapter.itemCount > 0
    }

    private fun showError(isError: Boolean, message: String? = null) {
        if (isError) {
            binding.viewError.root.visibility = View.VISIBLE
            binding.viewError.tvError.text = message ?: getString(R.string.error_message)
            binding.viewError.btnRetry.setOnClickListener {
                viewModel.fetchUpcomingEvents()
            }
        } else {
            binding.viewError.root.visibility = View.GONE
        }
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.emptyView.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvEvents.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}