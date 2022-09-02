package io.moneyflow.server.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.moneyflow.server.dto.CategoryDTO
import io.moneyflow.server.entity.Category
import io.moneyflow.server.entity.Household
import io.moneyflow.server.entity.User
import io.moneyflow.server.mapper.CategoryMapper
import io.moneyflow.server.response.ListApiResponse
import io.moneyflow.server.service.AccessControlService
import io.moneyflow.server.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/categories")
class CategoryController(
    val categoryService: CategoryService,
    val categoryMapper: CategoryMapper,
    val objectMapper: ObjectMapper,
    val validator: SmartValidator,
    val accessControlService: AccessControlService
) {
    @PostMapping("")
    fun create(@Valid @RequestBody categoryDTO: CategoryDTO, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        val category = categoryMapper.map(categoryDTO)

        if (!accessControlService.canManageHousehold(user, category.household)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        categoryService.save(category)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getList(@RequestParam(required = true, name = "household") household: Household): ListApiResponse<CategoryDTO> {
        val categories = categoryService.getByHousehold(household)

        return if (categories.isNotEmpty()) {
            ListApiResponse(categories.toList().map(categoryMapper::map), categories.size.toLong())
        } else {
            ListApiResponse(Collections.emptyList(), 0)
        }
    }

    @PatchMapping(path = ["{id}"], consumes = ["application/merge-patch+json"])
    fun update(@PathVariable("id") category: Category, @RequestBody patch: JsonNode, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        if (!accessControlService.canManageHousehold(user, category.household)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        val categoryDTO = categoryMapper.map(category)
        val original: JsonNode = objectMapper.valueToTree(categoryDTO)
        val patched: JsonNode = JsonMergePatch.fromJson(patch).apply(original)
        val patchedCategoryDTO: CategoryDTO = objectMapper.treeToValue(patched, CategoryDTO::class.java) ?: throw RuntimeException("Couldn't convert CategoryDTO JSON back to CategoryDTO")

        val validationErrors = BeanPropertyBindingResult(patchedCategoryDTO, "category")

        validator.validate(patchedCategoryDTO, validationErrors)

        if (validationErrors.hasErrors()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val patchedCategory = categoryMapper.merge(patchedCategoryDTO, category)

        categoryService.save(patchedCategory)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping(path = ["{id}"])
    fun delete(@PathVariable("id") category: Category, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        if (!accessControlService.canManageHousehold(user, category.household)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        categoryService.delete(category.id!!)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}