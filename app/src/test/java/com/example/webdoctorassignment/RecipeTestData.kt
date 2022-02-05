package com.example.webdoctorassignment

import com.example.webdoctorassignment.domain.RecipeDomain
import com.example.webdoctorassignment.network.api.response.Recipe
import com.example.webdoctorassignment.network.api.response.RecipeSearchResponse

object RecipeTestData {

    val recipeNetwork = RecipeSearchResponse.RecipeWrapper(
        recipe = Recipe(
            uri = "uri",
            url = "url",
            label = "label",
            ingredientLines = listOf("ingredient a", "ingredient b"),
            calories = "calories",
            source = "source",
            totalTime = "totalTime",
            cuisineType = listOf("kosher", "american"),
            images = Recipe.Images(
                thumbnail = Recipe.Images.Image(url = "thumbnailImageUrl")
            )
        )
    )

    val recipeDomain = RecipeDomain(
        uri = "uri",
        url = "url",
        label = "label",
        ingredientLines = listOf("ingredient a", "ingredient b"),
        calories = "calories",
        source = "source",
        totalTime = "totalTime",
        cuisineType = listOf("kosher", "american"),
        thumbnailImageUrl = "thumbnailImageUrl"
    )
}