package com.example.webdoctorassignment.repository

import com.example.webdoctorassignment.domain.RecipeDomain
import com.example.webdoctorassignment.domain.RecipeDomainMapper
import com.example.webdoctorassignment.network.api.EdamanApi
import com.example.webdoctorassignment.network.api.response.RecipeSearchResponse
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.lang.IllegalArgumentException

@RunWith(MockitoJUnitRunner::class)
class RecipeRepositoryTest {

    @Mock
    private lateinit var edamanApi: EdamanApi

    @Mock
    private lateinit var recipeDomainMapper: RecipeDomainMapper

    private lateinit var underTest: RecipeRepository

    @Before
    fun setup() {
        underTest = RecipeRepository(edamanApi, recipeDomainMapper)
    }

    @Test
    fun shouldReturnListOfRecipes() {
        val query = "potatos"

        val networkEntity = mock(RecipeSearchResponse::class.java)
        given(edamanApi.searchRecipes(query)).willReturn(Single.just(networkEntity))

        val domain = listOf(mock(RecipeDomain::class.java))
        given(recipeDomainMapper.map(networkEntity)).willReturn(domain)

        underTest.search(query).test()
            .assertNoErrors()
            .assertValue(domain)

        inOrder(edamanApi, recipeDomainMapper).apply {
            verify(edamanApi).searchRecipes(query)
            verify(recipeDomainMapper).map(networkEntity)
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun shouldPropagateErrorIfDomainMapperException() {
        val query = "potatos"

        val networkEntity = mock(RecipeSearchResponse::class.java)
        given(edamanApi.searchRecipes(query)).willReturn(Single.just(networkEntity))

        val error = IllegalArgumentException()
        given(recipeDomainMapper.map(networkEntity)).willThrow(error)

        underTest.search(query).test()
            .assertError(error)

        inOrder(edamanApi, recipeDomainMapper).apply {
            verify(edamanApi).searchRecipes(query)
            verify(recipeDomainMapper).map(networkEntity)
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun shouldPropagateExceptionIfApiError() {
        val query = "potatos"

        val error = Throwable()
        given(edamanApi.searchRecipes(query)).willReturn(Single.error(error))

        underTest.search(query).test()
            .assertError(error)

        inOrder(edamanApi, recipeDomainMapper).apply {
            verify(edamanApi).searchRecipes(query)
            verifyNoMoreInteractions()
        }
    }
}