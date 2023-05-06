package io.github.salil.jokes.presentation

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.myweatherapp.helper.SharedPreferenceUtils
import dagger.hilt.android.AndroidEntryPoint
import io.github.salil.jokes.R
import io.github.salil.jokes.databinding.ActivityMainBinding
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: JokesViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val jokesAdapter: JokesAdapter by lazy { JokesAdapter() }
    private var mSharedPreferenceUtils: SharedPreferenceUtils? = null
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 60000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

        setContentView(binding.root)
        mSharedPreferenceUtils = applicationContext?.let {
            SharedPreferenceUtils.getInstance(
                it
            )
        }
        setupAppBar()
        setupViews()
        setupObservers()
    }


    override fun onResume() {
        //start handler as activity become visible
        handler.postDelayed(Runnable {
            //do something
            viewModel.fetchJokes()
            runnable?.let { handler.postDelayed(it, delay) }
        }.also { runnable = it }, delay)
        super.onResume()
    }

    override fun onPause() {
        runnable?.let { handler.removeCallbacks(it) } //stop handler when activity not visible
        super.onPause()
    }
    private fun setupAppBar() {
        with(binding) {
            setSupportActionBar(toolbar)
        }
    }

    private fun setupViews() {
        with(binding) {
            recycler.apply {
                setHasFixedSize(true)
                adapter = jokesAdapter
                itemAnimator = FadeInDownAnimator()
            }

            swipe.apply {
                setColorSchemeColors(ContextCompat.getColor(context, R.color.white))
                setProgressBackgroundColorSchemeColor(
                    ContextCompat.getColor(
                        context,
                        R.color.background
                    )
                )
            }

            swipe.setOnRefreshListener {
                viewModel.fetchJokes()
                swipe.isRefreshing = false
            }
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            jokes.observe(this@MainActivity, { joke ->
                joke?.let {
                    updateJokes(it)
                }

            })

            loading.observe(this@MainActivity, {
                updateProgress(it)
            })

            error.observe(this@MainActivity, {
                showError(it)
            })

            fetchJokes()
        }
    }

    private fun updateJokes(jokes: String) {
        val list = mutableListOf<String>()

        if (mSharedPreferenceUtils?.containsKey("jokesList") == true && mSharedPreferenceUtils?.getSharedPref(
                "jokesList"
            ).isNullOrEmpty().not()
        ) {
            mSharedPreferenceUtils?.getSharedPref("jokesList")?.let { list.addAll(it) }
        }
        if (list.size >= 10) {
            list.removeAt(0)
            list.add(jokes)
        } else {
            list.add(jokes)
        }

        mSharedPreferenceUtils?.putSharedPref("jokesList", list)
        jokesAdapter.submitList(list)
        binding.recycler.isVisible = true
    }

    private fun updateProgress(flag: Boolean) {
        when (flag) {
            true -> {
                binding.error.isVisible = false
                binding.progress.isVisible = true
            }
            false -> binding.progress.isVisible = false
        }
    }

    private fun showError(message: String) {
        with(binding){
            error.text = message
            recycler.isVisible = false
            error.isVisible = true
        }

    }
}