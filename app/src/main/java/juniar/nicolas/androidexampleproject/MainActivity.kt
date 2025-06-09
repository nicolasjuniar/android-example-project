package juniar.nicolas.androidexampleproject

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import juniar.nicolas.androidexampleproject.databinding.ActivityMainBinding
import juniar.nicolas.androidskeletoncore.BaseViewBindingActivity
import juniar.nicolas.androidskeletoncore.CommonUtil.loadImage
import juniar.nicolas.androidskeletoncore.CommonUtil.showToast
import juniar.nicolas.androidskeletoncore.GenericDataStoreHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var genericDataStoreHelper: GenericDataStoreHelper

    private val viewModel: MainViewModel by viewModels()

    private val randomImageUrl =
        "https://plus.unsplash.com/premium_photo-1666900440561-94dcb6865554?q=80&w=3027&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"

    private val randomFact by lazy {
        genericDataStoreHelper.createKey<String>("random_fact")
    }

    override fun getContentView() = ActivityMainBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        viewBinding.ivRandom.loadImage(randomImageUrl, this@MainActivity)

        lifecycleScope.launch {
            genericDataStoreHelper.load(randomFact).collectLatest {
                viewBinding.tvFact.text = it
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
                genericDataStoreHelper.save(randomFact, it.fact)
            }
        }
    }
}