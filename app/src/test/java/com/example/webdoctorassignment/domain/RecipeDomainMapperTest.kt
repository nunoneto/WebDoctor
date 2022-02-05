package com.example.webdoctorassignment.domain

import com.example.webdoctorassignment.RecipeTestData.recipeDomain
import com.example.webdoctorassignment.RecipeTestData.recipeNetwork
import com.example.webdoctorassignment.network.api.response.RecipeSearchResponse
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RecipeDomainMapperTest {

    private lateinit var underTest: RecipeDomainMapper

    @Before
    fun setup() {
        underTest = RecipeDomainMapper()
    }

    @Test
    fun shouldMapNetworkEntity() {
        val actual = underTest.map(RecipeSearchResponse(hits = listOf(recipeNetwork)))
        assertEquals(listOf(recipeDomain), actual)
    }
}