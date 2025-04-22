package com.rsjd.dicodingeventmuji.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
    private var currentEvent: Event? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, 0)
        viewModel.getEventDetail(eventId)

        observeEventDetail()
        observeFavoriteStatus(eventId)
        setupFavoriteButton()
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
                        currentEvent = event
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

    private fun observeFavoriteStatus(eventId: Int) {
        viewModel.isFavorite(eventId).observe(this) { isFav ->
            isFavorite = isFav
            updateFavoriteButton(isFav)
        }
    }

    private fun setupFavoriteButton() {
        binding.fabFavorite.setOnClickListener {
            currentEvent?.let { event ->
                Log.d("FavoriteToggle", "Current favorite status: $isFavorite, Event ID: ${event.id}")
                if (isFavorite) {
                    viewModel.removeFromFavorite(event.id)
                } else {
                    viewModel.addToFavorite(event)
                }
            }
        }

        viewModel.favoriteStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    isFavorite = resource.data == true
                    updateFavoriteButton(isFavorite)

                    val message = if (isFavorite) "Ditambahkan ke favorit" else "Dihapus dari favorit"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                    Log.d("FavoriteToggle", "Favorite status updated: $isFavorite")
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                    Log.e("FavoriteToggle", "Error: ${resource.message}")
                }
                else -> {}
            }
        }
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_filled)
            binding.fabFavorite.contentDescription = getString(R.string.remove_from_favorite)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
            binding.fabFavorite.contentDescription = getString(R.string.add_to_favorite)
        }
    }

    private fun populateUI(event: Event) {
        binding.apply {
            tvEventName.text = event.name
            tvOrganizer.text = event.ownerName
            tvDate.text = DateFormatter.formatDate(event.beginTime)
            tvQuota.text = String.format(getString(R.string.quota_format),
                event.quota - event.registrants, event.quota)
            tvDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

            try {
                Glide.with(this@DetailActivity)
                    .load(event.mediaCover)
                    .into(ivEventCover)
            } catch (e: Exception) {
                // Handle error loading image
            }

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
            binding.fabFavorite.visibility = View.GONE
        } else {
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
            binding.contentLayout.visibility = View.VISIBLE
            binding.fabFavorite.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String) {
        binding.viewError.root.visibility = View.VISIBLE
        binding.viewError.tvError.text = message
        binding.contentLayout.visibility = View.GONE
        binding.fabFavorite.visibility = View.GONE
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