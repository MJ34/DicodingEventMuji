package com.rsjd.dicodingeventmuji.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.databinding.ActivitySearchBinding
import com.rsjd.dicodingeventmuji.ui.adapter.EventAdapter
import com.rsjd.dicodingeventmuji.ui.detail.DetailEventActivity
import com.rsjd.dicodingeventmuji.utils.EventResult
import com.rsjd.dicodingeventmuji.utils.ViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSearchView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter { event ->
            navigateToDetail(event)
        }
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = this@SearchActivity.adapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        if (it.isNotEmpty()) {
                            viewModel.searchEvents(it)
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })

            // Set initial focus
            requestFocus()
        }
    }

    private fun setupClickListeners() {
        binding.viewError.btnRetry.setOnClickListener {
            val query = binding.searchView.query.toString()
            if (query.isNotEmpty()) {
                viewModel.searchEvents(query)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.searchResults.observe(this) { result ->
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
                        binding.rvSearchResults.visibility = View.GONE
                    } else {
                        binding.viewEmpty.root.visibility = View.GONE
                        binding.rvSearchResults.visibility = View.VISIBLE
                        adapter.submitList(result.data)
                    }
                }
                is EventResult.Error -> {
                    showLoading(false)
                    binding.viewError.root.visibility = View.VISIBLE
                    binding.viewEmpty.root.visibility = View.GONE
                    binding.rvSearchResults.visibility = View.GONE
                    binding.viewError.tvErrorMessage.text = result.error
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.rvSearchResults.visibility = View.GONE
            binding.shimmerLayout.startShimmer()
        } else {
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
        }
    }

    private fun navigateToDetail(event: Event) {
        val intent = Intent(this, DetailEventActivity::class.java).apply {
            putExtra(DetailEventActivity.EXTRA_EVENT_ID, event.id)
        }
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}