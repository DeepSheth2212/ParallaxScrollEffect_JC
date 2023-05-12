package com.example.parallaxscrolleffect_jc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val moonspeed = 0.08f
            val midbgspeed = 0.03f
            val imageHeight = (LocalConfiguration.current.screenWidthDp * (2f / 3f)).dp

            var moonOffset by remember {
                mutableStateOf(0f)
            }

            var midbgOffset by remember {
                mutableStateOf(0f)
            }

            val lazyListState = rememberLazyListState() //to know exactly at which place we are scrolling at because we don't want to implement the parallax effect in every item

            //it is the interface that gives us functions to get information about the current scroll strength
            val nestedScrollConnection = remember {
                object : NestedScrollConnection{
                    override fun onPreScroll(
                        available: Offset, // that contains the information regarding how much we move finger on screen
                        source: NestedScrollSource,
                    ): Offset {
                        val delta = available.y // movement of touch in y direction
                        val layoutInfo = lazyListState.layoutInfo
                        if(lazyListState.firstVisibleItemIndex==0){
                            return Offset.Zero
                        }
                        if(layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1){
                            return Offset.Zero
                        }
                        moonOffset += delta * moonspeed
                        midbgOffset += delta * midbgspeed
                        return Offset.Zero // determines how much we actually scrolled the element compare to movement of touch
                    }
                }
            }


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .nestedScroll(nestedScrollConnection),
                state = lazyListState
            ){
                items(10){
                    Text(text = "sample item" ,  modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp))
                }
                item { 
                    Box(modifier = Modifier
                        .clipToBounds() // to make sure image doesn't overlap on any other item
                        .fillMaxWidth()
                        .height(imageHeight)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFf36b21),
                                    Color(0xFFf9a521)
                                )
                            )
                        )
                    )
                    {
                        Image(painter = painterResource(id = R.drawable.ic_moonbg),
                            contentDescription = "moon",
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .matchParentSize()
                                .graphicsLayer {
                                    translationY = moonOffset
                                }//to just transform the content of this image without affecting others
                        )
                        Image(painter = painterResource(id = R.drawable.ic_midbg),
                            contentDescription = "mid_bg",
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .matchParentSize()
                                .graphicsLayer {
                                    translationY = midbgOffset
                                }
                        )
                        Image(painter = painterResource(id = R.drawable.ic_outerbg),
                            contentDescription = "outer_bg",
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.BottomCenter,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                }
                items(20){
                    Text(text = "sample item" ,  modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp))
                }
            }
        }
    }
}

