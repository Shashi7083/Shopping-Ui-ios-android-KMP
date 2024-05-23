package util

import data.model.Product

sealed class ProductApiState{

    class Success(val data : List<Product>) : ProductApiState()

    class Failure(val msg : Throwable) : ProductApiState()

    object Loading : ProductApiState()

    object Empty : ProductApiState()
}