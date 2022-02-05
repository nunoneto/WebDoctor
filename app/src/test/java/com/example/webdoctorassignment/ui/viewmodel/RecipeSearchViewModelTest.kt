package com.example.webdoctorassignment.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.webdoctorassignment.domain.RecipeDomain
import com.example.webdoctorassignment.repository.IRecipeRepository
import com.example.webdoctorassignment.ui.mapper.RecipeUiMapper
import com.example.webdoctorassignment.ui.models.RecipeUiModel
import com.example.webdoctorassignment.util.UiState
import com.example.webdoctorassignment.util.UniTestSchedulerProvider
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.inOrder
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RecipeSearchViewModelTest {

    @Mock
    private lateinit var repository: IRecipeRepository

    @Mock
    private lateinit var uiMapper: RecipeUiMapper

    @Mock
    private lateinit var observer: Observer<UiState<RecipeUiModel>>

    private lateinit var underTest: RecipeSearchViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        underTest = RecipeSearchViewModel(repository, uiMapper, UniTestSchedulerProvider())
    }

    @Test
    fun shouldSearchRecipes() {
        val query = "potato"

        val domain = listOf(mock(RecipeDomain::class.java))
        given(repository.search(query)).willReturn(Single.just(domain))

        val uiModels = listOf(mock(RecipeUiModel::class.java))
        given(uiMapper.map(domain)).willReturn(uiModels)

        underTest.uiState.observeForever(observer)

        underTest.searchRecipe(query)

        inOrder(repository, uiMapper, observer).apply {
            verify(repository).search(query)
            verify(observer).onChanged(UiState.Loading(true))
            verify(uiMapper).map(domain)
            verify(observer).onChanged(UiState.Loading(false))
            verify(observer).onChanged(UiState.Success(uiModels))
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun shouldShowErrorIfMapperError() {
        val query = "potato"

        val domain = listOf(mock(RecipeDomain::class.java))
        given(repository.search(query)).willReturn(Single.just(domain))

        given(uiMapper.map(domain)).willThrow(IllegalArgumentException())

        underTest.uiState.observeForever(observer)

        underTest.searchRecipe(query)

        inOrder(repository, uiMapper, observer).apply {
            verify(repository).search(query)
            verify(observer).onChanged(UiState.Loading(true))
            verify(uiMapper).map(domain)
            verify(observer).onChanged(UiState.Loading(false))
            verify(observer).onChanged(UiState.Error())
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun shouldShowErrorIfRepoError() {
        val query = "potato"

        given(repository.search(query)).willReturn(Single.error(NullPointerException()))

        underTest.uiState.observeForever(observer)

        underTest.searchRecipe(query)

        inOrder(repository, uiMapper, observer).apply {
            verify(repository).search(query)
            verify(observer).onChanged(UiState.Loading(true))
            verify(observer).onChanged(UiState.Loading(false))
            verify(observer).onChanged(UiState.Error())
            verifyNoMoreInteractions()
        }
    }
}