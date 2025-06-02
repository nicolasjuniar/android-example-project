package juniar.nicolas.androidexampleproject

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import juniar.nicolas.androidskeletoncore.GenericDataStoreHelper
import juniar.nicolas.androidskeletoncore.Util.loadImage
import juniar.nicolas.androidskeletoncore.Util.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var genericDataStoreHelper: GenericDataStoreHelper

    private val randomImageUrl =
        "https://plus.unsplash.com/premium_photo-1666900440561-94dcb6865554?q=80&w=3027&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    private val imageView: ImageView by lazy {
        findViewById(R.id.iv_random)
    }

    private val tvFact: TextView by lazy {
        findViewById(R.id.tv_fact)
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
        val randomFact = genericDataStoreHelper.createKey<String>("random_fact")
        apiService.getRandomCatFact().enqueue(object : Callback<CatFactResponse> {
            override fun onResponse(p0: Call<CatFactResponse>, p1: Response<CatFactResponse>) {
                lifecycleScope.launch {
                    showToast("success get random fact")
                    genericDataStoreHelper.save(randomFact, p1.body()?.fact.orEmpty())
                }
            }

            override fun onFailure(p0: Call<CatFactResponse>, p1: Throwable) {
                showToast("error " + p1.message.orEmpty())
            }

        })

        lifecycleScope.launch {
            genericDataStoreHelper.load(randomFact).collectLatest {
                tvFact.text = it
            }
        }
    }
}