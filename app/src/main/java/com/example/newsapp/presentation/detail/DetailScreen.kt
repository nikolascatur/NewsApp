package com.example.newsapp.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsapp.R
import com.example.newsapp.domain.model.Article
import com.example.newsapp.presentation.Dimens.ArticleImageHeight
import com.example.newsapp.presentation.Dimens.MediumPadding1
import com.example.newsapp.presentation.detail.component.DetailTopBar

@Composable
fun DetailScreen(
    article: Article,
    event: (DetailEvent) -> Unit,
    navigatorUp: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        DetailTopBar(
            onBrowsing = {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(article.url)
                    if (resolveActivity(context.packageManager) != null) {
                        context.startActivity(this)
                    }
                }
            },
            onShareClick = {
                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, article.url)
                    type = "text/plain"
                    if (resolveActivity(context.packageManager) != null) {
                        context.startActivity(this)
                    }
                }
            },
            onBookmarkClick = {
                event(DetailEvent.SaveArticles)
            },
            onBackClick = navigatorUp
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                top = MediumPadding1,
                start = MediumPadding1,
                end = MediumPadding1
            )
        ) {

            item {
                AsyncImage(
                    model = ImageRequest.Builder(context = context).data(article.urlToImage)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ArticleImageHeight)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MediumPadding1)
                )
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.displaySmall,
                    color = colorResource(
                        id = R.color.text_title
                    )
                )
                Text(
                    text = article.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(
                        id = R.color.text_title
                    )
                )
            }
        }

    }

}