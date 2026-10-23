package com.example.randomfilms

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var movies: Array<String>
    private val remainingMovies = mutableListOf<Int>()
    private var currentMovie = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        movies = resources.getStringArray(R.array.movies)
        val savedRemaining = savedInstanceState?.getIntArray("remainingMovies")
        if (savedRemaining != null) {
            remainingMovies.addAll(savedRemaining.toList())
            currentMovie = savedInstanceState?.getInt("currentMovie", -1) ?: -1
        } else {
            remainingMovies.addAll(movies.indices)
        }

        findViewById<Button>(R.id.nextButton).setOnClickListener { showNextMovie() }
        findViewById<Button>(R.id.resetButton).setOnClickListener { resetMovies() }
        updateScreen()
    }

    private fun showNextMovie() {
        if (remainingMovies.isEmpty()) return

        currentMovie = remainingMovies.removeAt(Random.nextInt(remainingMovies.size))
        updateScreen()
    }

    private fun resetMovies() {
        remainingMovies.clear()
        remainingMovies.addAll(movies.indices)
        currentMovie = -1
        updateScreen()
    }

    private fun updateScreen() {
        findViewById<TextView>(R.id.movieTitle).text =
            movies.getOrNull(currentMovie) ?: getString(R.string.movie_placeholder)

        findViewById<TextView>(R.id.status).text = if (remainingMovies.isEmpty()) {
            getString(R.string.no_more_movies)
        } else {
            getString(R.string.movies_remaining, remainingMovies.size, movies.size)
        }
        findViewById<Button>(R.id.nextButton).isEnabled = remainingMovies.isNotEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("remainingMovies", remainingMovies.toIntArray())
        outState.putInt("currentMovie", currentMovie)
    }
}
