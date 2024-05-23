import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kmp_shopping_sharedui.composeapp.generated.resources.Res
import kmp_shopping_sharedui.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch
import util.ProductApiState

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        AppContent(homeViewModel = HomeViewModel())
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(
     homeViewModel: HomeViewModel
){
    val products = homeViewModel.products.collectAsState()

    val productState = homeViewModel.productResponse

    BoxWithConstraints {
        val scope = this
        val maxWidth = scope.maxWidth

        var cols = 2
        var modifier = Modifier.fillMaxWidth()
        var scrollState = rememberLazyGridState()
        var searchQuery by remember { mutableStateOf("") }
        var searchedProducts by remember { mutableStateOf(products.value) }


        if(maxWidth > 1080.dp){
            cols = 3
            modifier  = Modifier.widthIn(max = 1080.dp)
        }

        val coroutineScope = rememberCoroutineScope()

        when(productState.value){
            ProductApiState.Empty -> {

            }
            is ProductApiState.Failure -> {
                Text(text = "Fail")
            }
            ProductApiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }

            }
            is ProductApiState.Success -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(cols),
                        state = scrollState,
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.draggable(orientation = Orientation.Vertical, state = rememberDraggableState {delta->
                            coroutineScope.launch {
                                scrollState.scrollBy(-delta)
                            }

                        }) // to add dragable scroll in desktop
                    ){

                        item(
                            span = { GridItemSpan(cols) }
                        ) {
                            Column {
                                SearchBar(
                                    leadingIcon = {
                                        Icon(imageVector = Icons.Default.Search , contentDescription = null)
                                    },
                                    query = searchQuery,
                                    active = false,
                                    onActiveChange = {},
                                    onQueryChange = {
                                        searchQuery = it
                                    },
                                    onSearch = {
                                        searchedProducts = products.value.filter {product ->
                                            product.title.toString().contains(searchQuery, ignoreCase = true) == true
                                        }
                                    },
                                    placeholder = {
                                        Text(text = "Search Product")
                                    }
                                ){

                                }
                                Spacer(modifier = Modifier.height(15.dp))
                            }

                        }

                        items(
                            items = products.value ,
                            key = {product -> product.id.toString()}
                        ){product ->


                            Card (
                                shape = RoundedCornerShape(15.dp),
                                modifier = Modifier.padding(8.dp)
                                    .fillMaxWidth(),
                                elevation = 2.dp
                            ){
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    val painter = rememberImagePainter(product.image.toString())
                                    Image(
                                        painter = painter,
                                        modifier = Modifier.height(130.dp).padding(8.dp),
                                        contentDescription = product.title.toString()
                                    )

                                    Text(
                                        text = product.title.toString(),
                                        maxLines =  2,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(start=16.dp, end = 16.dp)
                                    )
                                    // Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "${product.price} INR",
                                        textAlign = TextAlign.Start,
                                        maxLines =  2,
                                        fontWeight = FontWeight.Bold,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}