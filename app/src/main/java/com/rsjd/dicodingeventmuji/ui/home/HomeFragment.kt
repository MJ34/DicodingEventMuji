package com.rsjd.dicodingeventmuji.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.databinding.FragmentHomeBinding
import com.rsjd.dicodingeventmuji.ui.MainActivity
import com.rsjd.dicodingeventmuji.ui.adapter.EventAdapter
import com.rsjd.dicodingeventmuji.ui.adapter.EventHorizontalAdapter
import com.rsjd.dicodingeventmuji.ui.detail.DetailEventActivity
import com.rsjd.dicodingeventmuji.ui.search.SearchActivity
import com.rsjd.dicodingeventmuji.utils.EventResult
import com.rsjd.dicodingeventmuji.utils.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var upcomingAdapter: EventHorizontalAdapter
    private lateinit var finishedAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        // Setup upcoming events horizontal recyclerview
        upcomingAdapter = EventHorizontalAdapter { event ->
            navigateToDetail(event)
        }
        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = upcomingAdapter
        }

        // Setup finished events vertical recyclerview
        finishedAdapter = EventAdapter { event ->
            navigateToDetail(event)
        }
        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = finishedAdapter
        }
    }

    private fun setupClickListeners() {
        binding.ivSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        binding.tvSeeAllActive.setOnClickListener {
            // Navigate to Active Events fragment
            (requireActivity() as MainActivity).navigateToTab(1)
        }

        binding.tvSeeAllFinished.setOnClickListener {
            // Navigate to Finished Events fragment
            (requireActivity() as MainActivity).navigateToTab(2)
        }

        binding.viewErrorUpcoming.btnRetry.setOnClickListener {
            viewModel.getActiveEvents()
        }

        binding.viewErrorFinished.btnRetry.setOnClickListener {
            viewModel.getFinishedEvents()
        }
    }

    private fun observeViewModel() {
        viewModel.activeEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EventResult.Loading -> {
                    binding.shimmerUpcoming.visibility = View.VISIBLE
                    binding.rvUpcomingEvents.visibility = View.GONE
                    binding.viewErrorUpcoming.root.visibility = View.GONE
                    binding.shimmerUpcoming.startShimmer()
                }
                is EventResult.Success -> {
                    binding.shimmerUpcoming.stopShimmer()
                    binding.shimmerUpcoming.visibility = View.GONE
                    binding.rvUpcomingEvents.visibility = View.VISIBLE
                    binding.viewErrorUpcoming.root.visibility = View.GONE
                    upcomingAdapter.submitList(result.data.take(5))
                }
                is EventResult.Error -> {
                    binding.shimmerUpcoming.stopShimmer()
                    binding.shimmerUpcoming.visibility = View.GONE
                    binding.rvUpcomingEvents.visibility = View.GONE
                    binding.viewErrorUpcoming.root.visibility = View.VISIBLE
                    binding.viewErrorUpcoming.tvErrorMessage.text = result.error
                }
            }
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EventResult.Loading -> {
                    binding.shimmerFinished.visibility = View.VISIBLE
                    binding.rvFinishedEvents.visibility = View.GONE
                    binding.viewErrorFinished.root.visibility = View.GONE
                    binding.shimmerFinished.startShimmer()
                }
                is EventResult.Success -> {
                    binding.shimmerFinished.stopShimmer()
                    binding.shimmerFinished.visibility = View.GONE
                    binding.rvFinishedEvents.visibility = View.VISIBLE
                    binding.viewErrorFinished.root.visibility = View.GONE
                    finishedAdapter.submitList(result.data.take(5))
                }
                is EventResult.Error -> {
                    binding.shimmerFinished.stopShimmer()
                    binding.shimmerFinished.visibility = View.GONE
                    binding.rvFinishedEvents.visibility = View.GONE
                    binding.viewErrorFinished.root.visibility = View.VISIBLE
                    binding.viewErrorFinished.tvErrorMessage.text = result.error
                }
            }
        }

        // Load the data
        viewModel.getActiveEvents()
        viewModel.getFinishedEvents()
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