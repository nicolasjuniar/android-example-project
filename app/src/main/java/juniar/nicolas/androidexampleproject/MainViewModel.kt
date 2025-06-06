package juniar.nicolas.androidexampleproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val catFactResponse = MutableLiveData<CatFactResponse>()
    private val infoMessage = MutableLiveData<String>()
    fun observeCatFactResponse() = catFactResponse
    fun observeInfoMessage() = infoMessage

    fun getRandomCatFact() {
        viewModelScope.launch {
            try {
                val response = apiService.getRandomCatFact()
                catFactResponse.postValue(response)
            } catch (e: Exception) {
                infoMessage.postValue(e.localizedMessage)
            }
        }
    }
}