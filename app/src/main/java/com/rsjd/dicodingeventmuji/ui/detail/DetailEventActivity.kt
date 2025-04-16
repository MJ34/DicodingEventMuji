package com.rsjd.dicodingeventmuji.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.bumptech.glide.Glide
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.databinding.ActivityDetailEventBinding
import com.rsjd.dicodingeventmuji.utils.DateFormatter
import com.rsjd.dicodingeventmuji.utils.EventResult
import com.rsjd.dicodingeventmuji.utils.ViewModelFactory

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding

    private val viewModel: DetailEventViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupClickListeners()
        loadEventDetail()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupClickListeners() {
        binding.fabRegister.setOnClickListener {
            val event = viewModel.event.value
            if (event is EventResult.Success) {
                openEventLink(event.data.link)
            }
        }

        binding.viewError.btnRetry.setOnClickListener {
            loadEventDetail()
        }
    }

    private fun loadEventDetail() {
        val eventId = intent.getStringExtra(EXTRA_EVENT_ID)

        if (eventId != null) {
            viewModel.getEventDetail(eventId)
            observeViewModel()
        } else {
            showError("Invalid event ID")
        }
    }

    private fun observeViewModel() {
        viewModel.event.observe(this) { result ->
            when (result) {
                is EventResult.Loading -> {
                    showLoading(true)
                    binding.contentLayout.visibility = View.GONE
                    binding.viewError.root.visibility = View.GONE
                }
                is EventResult.Success -> {
                    showLoading(false)
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.viewError.root.visibility = View.GONE

                    populateEventDetail(result.data)
                }
                is EventResult.Error -> {
                    showLoading(false)
                    binding.contentLayout.visibility = View.GONE
                    binding.viewError.root.visibility = View.VISIBLE
                    binding.viewError.tvErrorMessage.text = result.error
                }
            }
        }
    }

    private fun populateEventDetail(event: Event) {
        with(binding) {
            // Set event name
            tvEventName.text = event.name

            // Set event organizer
            tvOrganizer.text = event.ownerName

            // Set event date
            tvDate.text = DateFormatter.formatDate(event.beginTime)

            // Set event quota
            val quotaText = "${event.quota - event.registrant} / ${event.quota}"
            tvQuota.text = quotaText

            // Set event description
            tvDescription.text = event.description

            // Set event cover image
            val imageUrl = event.mediaCover ?: event.imageLogo
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(this@DetailEventActivity)
                    .load(imageUrl)
                    .into(ivEventCover)
            }

            // Set collapsing toolbar title
            collapsingToolbar.title = event.name
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.shimmerLayout.startShimmer()
        } else {
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
        }
    }

    private fun showError(message: String) {
        binding.shimmerLayout.visibility = View.GONE
        binding.contentLayout.visibility = View.GONE
        binding.viewError.root.visibility = View.VISIBLE
        binding.viewError.tvErrorMessage.text = message
    }

    private fun openEventLink(url: String) {
        val uri = Uri.parse(url)
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        customTabsIntent.launchUrl(this, uri)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}