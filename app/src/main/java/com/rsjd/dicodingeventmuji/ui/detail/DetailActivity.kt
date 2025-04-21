package com.rsjd.dicodingeventmuji.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.rsjd.dicodingeventmuji.R
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.databinding.ActivityDetailBinding
import com.rsjd.dicodingeventmuji.utils.DateFormatter
import com.rsjd.dicodingeventmuji.utils.Resource
import com.rsjd.dicodingeventmuji.utils.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, 0)
        viewModel.getEventDetail(eventId)

        observeEventDetail()
    }

    private fun observeEventDetail() {
        viewModel.event.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                    hideError()
                }
                is Resource.Success -> {
                    showLoading(false)
                    hideError()
                    resource.data?.let { event ->
                        populateUI(event)
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    showError(resource.message ?: getString(R.string.error_message))
                }
            }
        }
    }

    private fun populateUI(event: Event) {
        binding.apply {
            // Pastikan nullcheck untuk semua field
            tvEventName.text = event.name
            tvOrganizer.text = event.ownerName
            tvDate.text = DateFormatter.formatDate(event.beginTime)

            // String format dengan format yang benar
            // Perbaiki format string di strings.xml
            tvQuota.text = String.format(getString(R.string.quota_format),
                event.quota - event.registrants, event.quota)

            // Set HTML description dengan safety check
            tvDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)


            // Load cover image dengan safety check
            try {
                Glide.with(this@DetailActivity)
                    .load(event.mediaCover)
                    .into(ivEventCover)
            } catch (e: Exception) {
                // Handle error loading image
            }

            // Set register button dengan safety check
            btnRegister.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity,
                        "Tidak dapat membuka link: ${e.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.shimmerLayout.startShimmer()
            binding.contentLayout.visibility = View.GONE
            binding.viewError.root.visibility = View.GONE
        } else {
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
            binding.contentLayout.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String) {
        binding.viewError.root.visibility = View.VISIBLE
        binding.viewError.tvError.text = message
        binding.contentLayout.visibility = View.GONE
        binding.viewError.btnRetry.setOnClickListener {
            val eventId = intent.getIntExtra(EXTRA_EVENT_ID, 0)
            viewModel.getEventDetail(eventId)
        }
    }

    private fun hideError() {
        binding.viewError.root.visibility = View.GONE
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.event_detail)
        }
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