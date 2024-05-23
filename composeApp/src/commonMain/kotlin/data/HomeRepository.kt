package data

import data.apiClient.httpClient
import data.model.Product
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepository {

    suspend fun getProductApi() : List<Product>{
        val response = httpClient.get("https://fakestoreapi.com/products")
        return response.body()
    }

    fun getProducts():Flow<List<Product>> = flow {
        emit(getProductApi())
    }

}