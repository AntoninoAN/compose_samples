package com.cvscodechallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cvscodechallenge.ui.screen.DetailScreen
import com.cvscodechallenge.ui.screen.SearchScreen
import com.cvscodechallenge.ui.theme.CVSCodeChallengeTheme
import com.cvscodechallenge.utils.Navigation
import com.cvscodechallenge.utils.di.SearchApplication
import com.cvscodechallenge.utils.di.SearchInjector
import com.cvscodechallenge.viewmodel.SearchViewModel

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val viewModel: SearchViewModel by lazy {
        (application as SearchApplication).searchInjector.viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                val navController = rememberNavController()

                NavHost(navController = navController,
                    startDestination = Navigation.SEARCH.route){
                    composable(Navigation.DETAILS.route,
                    arguments = listOf(navArgument("pos"){
                        type = NavType.IntType })){
                        DetailScreen(viewModel = viewModel, position = it.arguments?.getInt("pos") ?: 0)
                    }
                    composable(Navigation.SEARCH.route){
                        SearchScreen(viewModel = viewModel, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    CVSCodeChallengeTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}
