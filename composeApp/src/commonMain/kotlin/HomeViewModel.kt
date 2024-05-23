import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.HomeRepository
import data.model.Product
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import util.ProductApiState

class HomeViewModel: ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products  = _products.asStateFlow()

    val homeRepository  = HomeRepository()

    init {
        viewModelScope.launch {
             homeRepository.getProducts().collect{ products->
                _products.update { it+products }
            }
        }

        // for getting API STATE
        getProduct()
    }

    val productResponse : MutableState<ProductApiState> = mutableStateOf(ProductApiState.Empty)

    fun getProduct() = viewModelScope.launch {
        homeRepository.getProducts()
            .onStart {
                productResponse.value = ProductApiState.Loading
            }.catch {
                productResponse.value = ProductApiState.Failure(it)
            }.collect{
                productResponse.value  = ProductApiState.Success(it)
            }
    }




}