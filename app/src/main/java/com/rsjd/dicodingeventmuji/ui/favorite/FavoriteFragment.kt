package com.rsjd.dicodingeventmuji.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsjd.dicodingeventmuji.databinding.FragmentFavoriteBinding
import com.rsjd.dicodingeventmuji.ui.adapter.FavoriteAdapter
import com.rsjd.dicodingeventmuji.ui.detail.DetailActivity
import com.rsjd.dicodingeventmuji.utils.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val adapter = FavoriteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeFavorites()
    }

    private fun setupRecyclerView() {
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteFragment.adapter
        }

        adapter.setOnItemClickListener { favoriteEvent ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_EVENT_ID, favoriteEvent.id)
            }
            startActivity(intent)
        }
    }

    private fun observeFavorites() {
        viewModel.favoriteEvents.observe(viewLifecycleOwner) { favorites ->
            if (favorites.isNullOrEmpty()) {
                showEmptyState(true)
            } else {
                showEmptyState(false)
                adapter.submitList(favorites)
            }
        }
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.emptyView.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvFavorites.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}