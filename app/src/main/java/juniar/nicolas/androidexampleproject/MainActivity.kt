package juniar.nicolas.androidexampleproject

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import juniar.nicolas.androidskeletoncore.GenericDataStoreHelper
import juniar.nicolas.androidskeletoncore.Util.loadImage
import juniar.nicolas.androidskeletoncore.Util.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var genericDataStoreHelper: GenericDataStoreHelper

    private val viewModel: MainViewModel by viewModels()

    private val randomImageUrl =
        "https://plus.unsplash.com/premium_photo-1666900440561-94dcb6865554?q=80&w=3027&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    private val imageView: ImageView by lazy {
        findViewById(R.id.iv_random)
    }

    private val tvFact: TextView by lazy {
        findViewById(R.id.tv_fact)
    }

    private val randomFact by lazy {
        genericDataStoreHelper.createKey<String>("random_fact")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageView.loadImage(randomImageUrl, this@MainActivity)

        lifecycleScope.launch {
            genericDataStoreHelper.load(randomFact).collectLatest {
                tvFact.text = it
            }
        }
        observeViewModel()
        viewModel.getRandomCatFact()
    }

    private fun observeViewModel() {
        viewModel.observeInfoMessage().observe(this@MainActivity) {
            showToast(it)
        }
        viewModel.observeCatFactResponse().observe(this@MainActivity) {
            lifecycleScope.launch {
                genericDataStoreHelper.save(randomFact, it.fact.orEmpty())
            }
        }
    }
}