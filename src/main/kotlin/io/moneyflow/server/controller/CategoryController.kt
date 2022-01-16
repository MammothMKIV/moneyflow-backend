package io.moneyflow.server.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.moneyflow.server.dto.CategoryDTO
import io.moneyflow.server.entity.Category
import io.moneyflow.server.mapper.CategoryMapper
import io.moneyflow.server.response.ListApiResponse
import io.moneyflow.server.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    val validator: SmartValidator
) {
    @PostMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun create(@Valid @RequestBody categoryDTO: CategoryDTO) {
        val category = categoryMapper.map(categoryDTO)

        categoryService.save(category)
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getList(@RequestParam(required = false, defaultValue = "0") page: Int, @RequestParam(required = false, defaultValue = "20") perPage: Int): ListApiResponse<CategoryDTO> {
        val categories = categoryService.getAll(page, perPage)

        return if (!categories.isEmpty) {
            ListApiResponse(categories.toList().map(categoryMapper::map), categories.totalElements)
        } else {
            ListApiResponse(Collections.emptyList(), 0)
        }
    }

    @PatchMapping(path = ["{id}"], consumes = ["application/merge-patch+json"])
    fun update(@PathVariable id: Long, @RequestBody patch: JsonNode): ResponseEntity<Any> {
        val category = categoryService.get(id)

        if (patch.has("id")) {
            (patch as ObjectNode).remove("id")
        }

        if (category == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val original: JsonNode = objectMapper.valueToTree(category)
        val patched: JsonNode = JsonMergePatch.fromJson(patch).apply(original)
        val patchedCategory: Category = objectMapper.treeToValue(patched, Category::class.java) ?: throw RuntimeException("Couldn't convert Category JSON back to Category")

        val validationErrors = BeanPropertyBindingResult(patchedCategory, "category")

        validator.validate(patchedCategory, validationErrors)

        if (validationErrors.hasErrors()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        categoryService.save(patchedCategory)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping(path = ["{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        val category = categoryService.get(id)

        if (category == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        categoryService.delete(category.id!!)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}